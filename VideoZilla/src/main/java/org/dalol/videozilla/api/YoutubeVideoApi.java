package org.dalol.videozilla.api;

import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * @author Filippo
 * @version 1.0.0
 * @since Thu, 14/06/2018 at 21:51.
 */
public interface YoutubeVideoApi {

    @Headers({
            "Authority: www.youtube.com",
//            "Accept-Encoding: gzip, deflate, br",
            "Accept: */*",
            "Accept-Encoding: gzip",
            "Host: www.youtube.com"
    })
    @GET("/get_video_info?el=detailpage")
    Single<ResponseBody> getVideoInfo(@Query("video_id") String videoId);
}
