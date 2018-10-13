package io.github.msh91.arch.domain.usecase.base

import com.nhaarman.mockito_kotlin.*
import io.github.msh91.arch.RxSchedulersOverrideRule
import io.github.msh91.arch.domain.mapper.DomainErrorUtil
import io.github.msh91.arch.domain.model.response.ErrorModel
import io.github.msh91.arch.domain.model.response.ErrorStatus
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CompletableUseCaseTest {
    @get:Rule
    val rule: TestRule = RxSchedulersOverrideRule()

    @Mock
    lateinit var compositeDisposable: CompositeDisposable

    @Mock
    lateinit var onResponse: () -> Unit

    @Mock
    lateinit var onFailure: (ErrorModel) -> Unit

    @Mock
    lateinit var onTokenExpire: () -> Unit

    @Mock
    lateinit var errorUtil: DomainErrorUtil

    @Test
    fun `execute useCase should invoke callback with a valid response if everything is ok`() {
        // GIVEN
        val completable = Completable.complete()
        val completableUseCase = mock<CompletableUseCase>(defaultAnswer = Mockito.CALLS_REAL_METHODS)
        whenever(completableUseCase.execute()).thenReturn(completable)

        // WHEN
        val disposable = completableUseCase.execute(compositeDisposable, onResponse, onFailure, onTokenExpire)

        // THEN
        verify(onResponse).invoke()
        verify(onFailure, never()).invoke(any())
        verify(onTokenExpire, never()).invoke()
        verify(compositeDisposable).add(disposable)
    }

    @Test
    fun `execute use case should invoke callback with a ErrorModel if something went wrong`() {
        // GIVEN
        val t = mock<Throwable>()
        val mockedError = mock<ErrorModel>()
        val completable = Completable.error(t)
        val completableUseCase = mock<CompletableUseCase>(defaultAnswer = Mockito.CALLS_REAL_METHODS)
        whenever(errorUtil.getErrorModel(t)).thenReturn(mockedError)
        whenever(completableUseCase.errorUtil).thenReturn(errorUtil)
        whenever(completableUseCase.execute()).thenReturn(completable)

        // WHEN
        val disposable = completableUseCase.execute(compositeDisposable, onResponse, onFailure, onTokenExpire)

        // THEN
        verify(onFailure).invoke(mockedError)
        verify(onResponse, never()).invoke()
        verify(onTokenExpire, never()).invoke()
        verify(compositeDisposable).add(disposable)
    }

    @Test
    fun `execute use case should invoke onTokenExpire callback if an unauthorized error happened`() {
        // GIVEN
        val t = mock<Throwable>()
        val mockedError = mock<ErrorModel>()
        whenever(mockedError.errorStatus).thenReturn(ErrorStatus.UNAUTHORIZED)
        whenever(errorUtil.getErrorModel(t)).thenReturn(mockedError)
        val completable = Completable.error(t)
        val completableUseCase = mock<CompletableUseCase>(defaultAnswer = Mockito.CALLS_REAL_METHODS)
        whenever(completableUseCase.errorUtil).thenReturn(errorUtil)
        whenever(completableUseCase.execute()).thenReturn(completable)

        // WHEN
        val disposable = completableUseCase.execute(compositeDisposable, onResponse, onFailure, onTokenExpire)

        // THEN
        verify(onResponse, never()).invoke()
        verify(onFailure).invoke(mockedError)
        verify(onTokenExpire).invoke()
        verify(compositeDisposable).add(disposable)
    }
}