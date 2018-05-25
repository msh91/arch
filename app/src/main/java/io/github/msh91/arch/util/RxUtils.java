package io.github.msh91.arch.util;

import android.databinding.Observable;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;

public class RxUtils {

    private RxUtils() {

    }

    public static <T> Flowable<T> toFlowable(@NonNull final ObservableField<T> observableField) {
        return Flowable.create(asyncEmitter -> {

            final Observable.OnPropertyChangedCallback callback = new Observable.OnPropertyChangedCallback() {
                @Override
                public void onPropertyChanged(Observable dataBindingObservable, int propertyId) {
                    if (dataBindingObservable == observableField) {
                        asyncEmitter.onNext(observableField.get());
                    }
                }
            };

            observableField.addOnPropertyChangedCallback(callback);

            asyncEmitter.setCancellable(() -> observableField.removeOnPropertyChangedCallback(callback));

        }, BackpressureStrategy.LATEST);
    }
}
