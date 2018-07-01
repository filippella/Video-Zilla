package org.dalol.demo;

/**
 * @author Filippo
 * @version 1.0.0
 * @since Sun, 01/07/2018 at 09:50.
 */
public abstract class AbstractRunnable<S> implements Runnable {

    private final S subject;

    public AbstractRunnable(S subject) {
        this.subject = subject;
    }

    @Override
    public void run() {
        run(subject);
    }

    protected abstract void run(S subject);
}
