package org.dalol.videozilla.managers;

import org.dalol.videozilla.RxCommon.DisposableSingleObserverAdapter;
import org.dalol.videozilla.api.YoutubeVideoApi;
import org.dalol.videozilla.captors.VideoDetailCaptor;
import org.dalol.videozilla.captors.youtube.YoutubeVideoCaptor;
import org.dalol.videozilla.model.Parameter;
import org.dalol.videozilla.model.VideoDetail;
import org.dalol.videozilla.model.VideoRequest;
import org.dalol.videozilla.model.callback.OnVideoDetailStatusListener;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author Filippo
 * @version 1.0.0
 * @since Sun, 01/07/2018 at 07:47.
 */
public final class VideoZillaManager {

    private final Queue<VideoRequest> videoRequests = new LinkedList<>();
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final List<OnVideoDetailStatusListener> videoDetailStatusListeners = new ArrayList<>();
    private VideoDetailCaptor<String, Single<VideoDetail>> youtubeVideoDetailCaptor;
    private boolean isDownloading;

    private static final class InstanceHolder {
        private static final VideoZillaManager INSTANCE = new VideoZillaManager();
    }

    private VideoZillaManager() {
        initializeYoutubeVideoCaptor();
    }

    private void initializeYoutubeVideoCaptor() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.googleapis.com")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        this.youtubeVideoDetailCaptor = new YoutubeVideoCaptor(retrofit.create(YoutubeVideoApi.class));
    }

    public synchronized static VideoZillaManager get() {
        return InstanceHolder.INSTANCE;
    }

    public boolean addVideoDetailStatusListener(OnVideoDetailStatusListener statusListener) {
        return !this.videoDetailStatusListeners.contains(statusListener) && this.videoDetailStatusListeners.add(statusListener);
    }

    public boolean removeVideoDetailStatusListener(OnVideoDetailStatusListener statusListener) {
        return this.videoDetailStatusListeners.remove(statusListener);
    }

    public void addVideoRequest(VideoRequest request) {
        attemptVideoDownload(request);
    }

    private void attemptVideoDownload(VideoRequest request) {
        if (!isDownloading) {
            isDownloading = true;
            getVideoDetails(request);
        } else {
            videoRequests.add(request);
        }
    }

    private void checkForNextVideoRequest() {
        if (!videoRequests.isEmpty()) {
            getVideoDetails(videoRequests.poll());
        } else {
            isDownloading = false;
        }
    }

    private void getVideoDetails(VideoRequest request) {
        switch (request.getSourceType()) {
            case YOUTUBE:
                getYoutubeVideoDetail(request);
                break;
            default:
                checkForNextVideoRequest();
        }
    }

    private void getYoutubeVideoDetail(VideoRequest request) {
        DisposableSingleObserver<VideoDetail> singleObserver =
                this.youtubeVideoDetailCaptor.getVideoDetail(new Parameter<>(request.getVideoURL()))
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.computation())
                        .subscribeWith(new DisposableSingleObserverAdapter<VideoRequest, VideoDetail>(request) {

                            @Override
                            protected void onSuccess(VideoRequest videoRequest, VideoDetail videoDetail) {
                                for (OnVideoDetailStatusListener statusListener : videoDetailStatusListeners) {
                                    statusListener.onVideoDetailRetrieveSuccess(videoRequest, videoDetail);
                                }
                                checkForNextVideoRequest();
                            }

                            @Override
                            protected void onError(VideoRequest videoRequest, Throwable throwable) {
                                for (OnVideoDetailStatusListener statusListener : videoDetailStatusListeners) {
                                    statusListener.onVideoDetailRetrieveFailure(videoRequest, throwable);
                                }
                                checkForNextVideoRequest();
                            }
                        });
        this.compositeDisposable.add(singleObserver);
    }

    public void clear() {
        this.videoRequests.clear();
        this.compositeDisposable.clear();
        this.youtubeVideoDetailCaptor = null;
    }
}
