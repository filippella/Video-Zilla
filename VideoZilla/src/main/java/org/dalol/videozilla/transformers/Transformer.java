package org.dalol.videozilla.transformers;

/**
 * @author Filippo
 * @version 1.0.0
 * @since Sun, 17/06/2018 at 01:16.
 */
public interface Transformer<I, O> {

    O transform(I input) throws Exception;
}
