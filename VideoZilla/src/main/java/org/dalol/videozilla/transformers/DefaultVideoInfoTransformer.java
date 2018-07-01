package org.dalol.videozilla.transformers;

import org.dalol.videozilla.model.VideoDetail;
import org.dalol.videozilla.model.VideoInfo;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * @author Filippo
 * @version 1.0.0
 * @since Sat, 16/06/2018 at 15:11.
 */
public class DefaultVideoInfoTransformer implements Transformer<String, VideoDetail> {

    @Override
    public VideoDetail transform(String input) throws Exception {

        List<VideoInfo> infoList = new ArrayList<>();

        Map<String, String> videosInfo = refine(input);

        String tmp = videosInfo.get("url_encoded_fmt_stream_map");
        if (tmp != null) {
            String[] split = tmp.split(",");
            for (String value : split) {
                Map<String, String> videoInfo = refine(value);
                infoList.add(new VideoInfo(videoInfo.get("url"), videoInfo.get("itag"), videoInfo.get("type"), videoInfo.get("quality")));
            }
        }

        String videoTitle = videosInfo.get("title");

        return new VideoDetail(videoTitle, "", 0L, infoList);
    }

    private Map<String, String> refine(String value) throws IOException {
        String parameters[] = value.split("&");
        Map<String, String> map = new Hashtable<>();
        for (String par : parameters) {
            String[] keyValue = par.split("=");
            if (keyValue.length == 1) keyValue = new String[]{keyValue[0], ""};
            map.put(keyValue[0], URLDecoder.decode(keyValue[1], "UTF-8"));
        }
        return map;
    }
}
