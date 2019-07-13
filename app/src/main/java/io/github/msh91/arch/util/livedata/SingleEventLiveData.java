package io.github.msh91.arch.util.livedata;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.annotation.NonNull;

import java.util.concurrent.atomic.AtomicBoolean;
/**
 * Custom wrapper for {@link MutableLiveData} that calls observer only one time
 * @param <T>
 */
public class SingleEventLiveData<T> extends MutableLiveData<T> {
    private AtomicBoolean mPending = new AtomicBoolean(false);

    @Override
    public void setValue(T value) {
        mPending.set(true);
        super.setValue(value);
    }



    @Override
    public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer) {
        super.observe(owner, t -> {
            if (mPending.compareAndSet(true, false)) {
                observer.onChanged(t);
            }
        });
    }
}
