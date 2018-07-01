package org.dalol.videozilla.model.callback;

import org.dalol.videozilla.model.VideoDetail;
import org.dalol.videozilla.model.VideoRequest; /**
 * @author Filippo
 * @version 1.0.0
 * @since Sun, 01/07/2018 at 08:38.
 */
public interface OnVideoDetailStatusListener {

    void onVideoDetailRetrieveSuccess(VideoRequest videoRequest, VideoDetail videoDetail);

    void onVideoDetailRetrieveFailure(VideoRequest videoRequest, Throwable throwable);
}
