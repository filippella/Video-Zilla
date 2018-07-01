package org.dalol.videozilla.model;

/**
 * @author Filippo
 * @version 1.0.0
 * @since Sun, 01/07/2018 at 08:14.
 */
public class VideoRequest {

    private final String videoURL;
    private final VideoSourceType sourceType;

    public VideoRequest(String videoURL, VideoSourceType sourceType) {
        this.videoURL = videoURL;
        this.sourceType = sourceType;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public VideoSourceType getSourceType() {
        return sourceType;
    }
}
