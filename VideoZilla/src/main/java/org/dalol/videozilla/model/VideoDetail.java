package org.dalol.videozilla.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Filippo
 * @version 1.0.0
 * @since Sun, 01/07/2018 at 08:09.
 */
public class VideoDetail {

    private final String videoTitle;
    private final String videoDescription;
    private final long videoDuration;
    private final List<VideoInfo> videoInfos = new ArrayList<>();

    public VideoDetail(String videoTitle, String videoDescription, long videoDuration, List<VideoInfo> videoInfos) {
        this.videoTitle = videoTitle;
        this.videoDescription = videoDescription;
        this.videoDuration = videoDuration;
        this.videoInfos.addAll(videoInfos);
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public String getVideoDescription() {
        return videoDescription;
    }

    public long getVideoDuration() {
        return videoDuration;
    }

    public List<VideoInfo> getVideoInfos() {
        return videoInfos;
    }

    @Override
    public String toString() {
        return "VideoDetail{" +
                "videoTitle='" + videoTitle + '\'' +
                ", videoDescription='" + videoDescription + '\'' +
                ", videoDuration=" + videoDuration +
                ", videoInfos=" + videoInfos +
                '}';
    }
}
