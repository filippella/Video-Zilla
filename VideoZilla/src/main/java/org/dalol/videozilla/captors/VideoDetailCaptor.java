package org.dalol.videozilla.captors;

import org.dalol.videozilla.model.Parameter;

/**
 * @author Filippo
 * @version 1.0.0
 * @since Sun, 01/07/2018 at 07:51.
 */
public interface VideoDetailCaptor<P, R> {

    R getVideoDetail(Parameter<P> parameter);
}
