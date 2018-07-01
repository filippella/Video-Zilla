package org.dalol.videozilla.RxCommon;

import io.reactivex.observers.DisposableSingleObserver;

/**
 * @author Filippo
 * @version 1.0.0
 * @since Sun, 01/07/2018 at 08:33.
 */
public abstract class DisposableSingleObserverAdapter<S, T> extends DisposableSingleObserver<T> {

    private final S subject;

    public DisposableSingleObserverAdapter(S subject) {
        this.subject = subject;
    }

    @Override
    public void onSuccess(T t) {
        onSuccess(subject, t);
    }

    @Override
    public void onError(Throwable throwable) {
        onError(subject, throwable);
    }

    protected abstract void onSuccess(S subject, T result);

    protected abstract void onError(S subject, Throwable throwable);
}
