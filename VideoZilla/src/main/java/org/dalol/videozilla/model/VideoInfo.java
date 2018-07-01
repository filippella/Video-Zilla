package org.dalol.videozilla.model;

/**
 * @author Filippo
 * @version 1.0.0
 * @since Sun, 01/07/2018 at 08:44.
 */
public class VideoInfo {

    private final String url;
    private final String itag;
    private final String type;
    private final String quality;

    public VideoInfo(String url, String itag, String type, String quality) {
        this.url = url;
        this.itag = itag;
        this.type = type;
        this.quality = quality;
    }

    public String getUrl() {
        return url;
    }

    public String getItag() {
        return itag;
    }

    public String getType() {
        return type;
    }

    public String getQuality() {
        return quality;
    }

    @Override
    public String toString() {
        return "VideoInfo{" +
                "url='" + url + '\'' +
                ", itag='" + itag + '\'' +
                ", type='" + type + '\'' +
                ", quality='" + quality + '\'' +
                '}';
    }
}
