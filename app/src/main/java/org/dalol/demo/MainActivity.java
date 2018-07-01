package org.dalol.demo;

import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.dalol.videozilla.managers.VideoZillaManager;
import org.dalol.videozilla.model.VideoDetail;
import org.dalol.videozilla.model.VideoInfo;
import org.dalol.videozilla.model.VideoRequest;
import org.dalol.videozilla.model.VideoSourceType;
import org.dalol.videozilla.model.callback.OnVideoDetailStatusListener;

import java.io.File;
import java.util.List;

public class MainActivity extends BaseActivity {

    private WebView mWebView;
    private RequestPermissionHelper mRequestPermissionHelper;
    private DownloadManager mDownloadManager;

    private long mRefId;

    @Override
    protected void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mWebView = findViewById(R.id.webView);
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setBuiltInZoomControls(true);
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.loadUrl("https://www.youtube.com/");

        mDownloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        mRequestPermissionHelper = new RequestPermissionHelper();
        VideoZillaManager.get().addVideoDetailStatusListener(videoDetailStatusListener);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionDownload:
                downloadVideo(mWebView.getUrl());
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void downloadVideo(String url) {
        checkDevicePermission(url);
    }

    private void checkDevicePermission(String url) {
        mRequestPermissionHelper.requestPermission(this, new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
        }, 123, url, new RequestPermissionHelper.RequestPermissionListener() {

            @Override
            public void onSuccess(String url) {
                if (Utils.isValidVideoURL(url)) {
                    showProgressDialog("Getting video info...");
                    VideoZillaManager.get().addVideoRequest(new VideoRequest(Uri.parse(url).getQueryParameter("v"), VideoSourceType.YOUTUBE));
                } else {
                    showSimpleDialog("Video cannot be downloaded from the site.");
                }
            }

            @Override
            public void onFailed() {
                showToast("Sorry, request permission failed");
            }

            @Override
            public void onRequestPermission(String[] permissions, int requestCode) {
                ActivityCompat.requestPermissions(MainActivity.this, permissions, requestCode);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mRequestPermissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        VideoZillaManager zillaManager = VideoZillaManager.get();
        zillaManager.clear();
        zillaManager.removeVideoDetailStatusListener(videoDetailStatusListener);
        unregisterReceiver(onComplete);
        super.onDestroy();
    }

    private VideoDetail mVideoDetail;

    private final OnVideoDetailStatusListener videoDetailStatusListener = new OnVideoDetailStatusListener() {
        @Override
        public void onVideoDetailRetrieveSuccess(VideoRequest videoRequest, VideoDetail videoDetail) {

            runOnUiThread(new AbstractRunnable<VideoDetail>(videoDetail) {

                @Override
                protected void run(VideoDetail subject) {
                    dismissDialog();
                    mVideoDetail = subject;
                    List<VideoInfo> videoInfos = mVideoDetail.getVideoInfos();

                    int size = videoInfos.size();

//                    String[] items = new String[size + 1];
                    String[] items = new String[size];

                    for (int i = 0; i < size; i++) {
                        items[i] = videoInfos.get(i).getQuality();
                    }

                    //items[size] = "Extract Audio (.MP3)";

                    System.out.println(videoInfos.get(0));

                    showChoosableItemsDialog("Please select quality",
                            items,
                            1,
                            "Download Video"
                    );
                }
            });
        }

        @Override
        public void onVideoDetailRetrieveFailure(VideoRequest videoRequest, Throwable throwable) {
            System.out.println(throwable.getMessage());
            runOnUiThread(new AbstractRunnable<String>(throwable.getMessage()) {

                @Override
                protected void run(String subject) {
                    dismissDialog();
                    showSimpleDialog(subject);
                }
            });
        }
    };

    @Override
    protected void onItemSelected(int index) {
        super.onItemSelected(index);
        if (mVideoDetail != null) {
            List<VideoInfo> videoInfos = mVideoDetail.getVideoInfos();
            if (index == videoInfos.size()) {
                downloadMp3(videoInfos);

            } else {
                VideoInfo videoInfo = videoInfos.get(index);
                download(mVideoDetail.getVideoTitle(), videoInfo.getUrl(), videoInfo.getType());
            }
        }
    }

    private void downloadMp3(List<VideoInfo> videoInfos) {
        //                File source = new File("source.mp4");

        showToast("Download MP3");


        String url = videoInfos.get(0).getUrl();
        File source = new File(url);
        String outputPath = "/VideoZilla/target.mp3";

        File target = new File(Environment.DIRECTORY_DOWNLOADS, outputPath);


        String sourceLocation = url;
//        String cmd[] = {"-i", sourceLocation, "-vn", "-ar", "44100", "-ac", "2", "-ab", "192", "-f", "mp3", outputPath};
//
//
//        try {
//            FFmpeg.getInstance(getApplicationContext()).execute(cmd, new ExecuteBinaryResponseHandler() {
//
//                @Override
//                public void onProgress(String message) {
//                    super.onProgress(message);
//                }
//
//                @Override
//                public void onSuccess(String message) {
//                    super.onSuccess(message);
//                }
//
//                @Override
//                public void onFailure(String message) {
//                    super.onFailure(message);
//                }
//
//                @Override
//                public void onFinish() {
//                    super.onFinish();
//                }
//
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//            showToast(e.getMessage());
//        }

//                File source = new File("source.avi");
//                File target = new File("target.flv");

//        AudioAttributes audio = new AudioAttributes();
//        audio.setCodec("libmp3lame");
//        audio.setBitRate(new Integer(128000));
//        audio.setChannels(new Integer(2));
//        audio.setSamplingRate(new Integer(44100));
//
//
//        EncodingAttributes attrs = new EncodingAttributes();
//        attrs.setFormat("mp3");
//        attrs.setAudioAttributes(audio);
//
//        Encoder encoder = new Encoder();
//        try {
//            encoder.encode(source, target, attrs);
//        } catch (EncoderException e) {
//            e.printStackTrace();
//            showToast(e.getMessage());
//        }
    }

    private void download(String title, String url, String extension) {

        String ex = getExtension(extension);

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setAllowedOverRoaming(false);
        request.setTitle(title);
        request.setDescription("Downloading Video");
        request.setVisibleInDownloadsUi(true);
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/VideoZilla/" + title + ex);

        mRefId = mDownloadManager.enqueue(request);
    }

    private String getExtension(String extension) {
        String[] split = extension.split(";");
        if (split.length > 0 && split[0].contains("video")) {
            String[] split2 = split[0].split("/");
            return "." + split2[1];
        }
        return ".mp4";
    }


    private final BroadcastReceiver onComplete = new BroadcastReceiver() {

        public void onReceive(Context ctxt, Intent intent) {

            long referenceId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

            if (referenceId == mRefId) {
                showToast("All Download completed");
            }
        }
    };
}
