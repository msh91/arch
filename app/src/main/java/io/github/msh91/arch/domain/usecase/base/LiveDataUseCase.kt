package io.github.msh91.arch.domain.usecase.base

import androidx.lifecycle.LiveData

abstract class LiveDataUseCase<T> : UseCase<LiveData<T>>() {

}