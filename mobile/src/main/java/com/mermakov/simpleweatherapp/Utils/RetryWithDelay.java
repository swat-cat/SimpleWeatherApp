package com.mermakov.simpleweatherapp.Utils;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Func1;

public class RetryWithDelay
        implements Func1<Observable<? extends Throwable>, Observable<?>> {

    private final int _maxRetries;
    private final int _retryDelayMillis;
    private int _retryCount;

    public RetryWithDelay(final int maxRetries, final int retryDelayMillis) {
        _maxRetries = maxRetries;
        _retryDelayMillis = retryDelayMillis;
        _retryCount = 0;
    }

    // this is a notificationhandler, all that is cared about here
    // is the emission "type" not emission "content"
    // only onNext triggers a re-subscription (onError + onComplete kills it)

    @Override
    public Observable<?> call(Observable<? extends Throwable> inputObservable) {

        // it is critical to use inputObservable in the chain for the result
        // ignoring it and doing your own thing will break the sequence

        return inputObservable.flatMap(new Func1<Throwable, Observable<?>>() {
            @Override
            public Observable<?> call(Throwable throwable) {
                if (++_retryCount < _maxRetries) {

                    // When this Observable calls onNext, the original
                    // Observable will be retried (i.e. re-subscribed)

                    Log.d("Retrying in %d ms", ""+_retryCount * _retryDelayMillis);

                    return Observable.timer(_retryCount * _retryDelayMillis,
                            TimeUnit.MILLISECONDS);
                }
                // Max retries hit. Pass an error so the chain is forcibly completed
                // only onNext triggers a re-subscription (onError + onComplete kills it)
                return Observable.error(throwable);
            }
        });
    }
}
