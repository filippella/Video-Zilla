package org.dalol.videozilla.captors.youtube;

import org.dalol.videozilla.api.YoutubeVideoApi;
import org.dalol.videozilla.captors.VideoDetailCaptor;
import org.dalol.videozilla.model.Parameter;
import org.dalol.videozilla.model.VideoDetail;
import org.dalol.videozilla.transformers.DefaultGzipTransformer;
import org.dalol.videozilla.transformers.DefaultVideoInfoTransformer;

import io.reactivex.Single;
import io.reactivex.functions.Function;
import okhttp3.ResponseBody;

/**
 * @author Filippo
 * @version 1.0.0
 * @since Sun, 01/07/2018 at 07:51.
 */
public class YoutubeVideoCaptor implements VideoDetailCaptor<String, Single<VideoDetail>> {

    private final YoutubeVideoApi mYoutubeVideoApi;

    public YoutubeVideoCaptor(YoutubeVideoApi youtubeVideoApi) {
        mYoutubeVideoApi = youtubeVideoApi;
    }

    @Override
    public Single<VideoDetail> getVideoDetail(Parameter<String> parameter) {
        return mYoutubeVideoApi.getVideoInfo(parameter.getValue())
                .map(new Function<ResponseBody, String>() {
                    @Override
                    public String apply(ResponseBody body) throws Exception {
                        return new DefaultGzipTransformer().transform(body);
                    }
                })
                .map(new Function<String, VideoDetail>() {
                    @Override
                    public VideoDetail apply(String info) throws Exception {
                        return new DefaultVideoInfoTransformer().transform(info);
                    }
                });
    }
}
