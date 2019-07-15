package io.github.msh91.arch.domain.usecase.base

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.github.msh91.arch.RxSchedulersOverrideRule
import io.github.msh91.arch.domain.mapper.DomainErrorUtil
import io.github.msh91.arch.domain.model.response.*
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class FlowableUseCaseTest {
    @get:Rule
    val rule: TestRule = RxSchedulersOverrideRule()

    @Mock
    lateinit var compositeDisposable: CompositeDisposable

    @Mock
    lateinit var onResponse: (UseCaseResponse<Any>) -> Unit

    @Mock
    lateinit var onTokenExpire: () -> Unit

    @Mock
    lateinit var errorUtil: DomainErrorUtil

    @Test
    fun `execute useCase should invoke callback with a valid response if everything is ok`() {
        // GIVEN
        val mockedResponse = mock<Any>()
        val testFlowable = Flowable.just(mockedResponse)
        val flowableUseCase = mock<FlowableUseCase<Any>>(defaultAnswer = Mockito.CALLS_REAL_METHODS)
        whenever(flowableUseCase.execute()).thenReturn(testFlowable)

        // WHEN
        val disposable = flowableUseCase.execute(compositeDisposable, onResponse, onTokenExpire)

        // THEN
        verify(onResponse).invoke(SuccessResponse(mockedResponse))
        verify(onTokenExpire, never()).invoke()
        verify(compositeDisposable).add(disposable)
    }

    @Test
    fun `execute useCase should invoke callback with a ErrorModel if something went wrong`() {
        // GIVEN
        val t = mock<Throwable>()
        val mockedError = mock<ErrorModel>()
        val testFlowable = Flowable.error<Any>(t)
        val flowableUseCase = mock<FlowableUseCase<Any>>(defaultAnswer = Mockito.CALLS_REAL_METHODS)
        whenever(errorUtil.getErrorModel(t)).thenReturn(mockedError)
        whenever(flowableUseCase.errorUtil).thenReturn(errorUtil)
        whenever(flowableUseCase.execute()).thenReturn(testFlowable)

        // WHEN
        val disposable = flowableUseCase.execute(compositeDisposable, onResponse, onTokenExpire)

        // THEN
        verify(onResponse).invoke(ErrorResponse(mockedError))
        verify(onTokenExpire, never()).invoke()
        verify(compositeDisposable).add(disposable)
    }

    @Test
    fun `execute useCase should invoke onTokenExpire callback if an unauthorized error happened`() {
        // GIVEN
        val t = mock<Throwable>()
        val mockedError = mock<ErrorModel>()
        whenever(mockedError.errorStatus).thenReturn(ErrorStatus.UNAUTHORIZED)
        whenever(errorUtil.getErrorModel(t)).thenReturn(mockedError)
        val testFlowable = Flowable.error<Any>(t)
        val flowableUseCase = mock<FlowableUseCase<Any>>(defaultAnswer = Mockito.CALLS_REAL_METHODS)
        whenever(flowableUseCase.errorUtil).thenReturn(errorUtil)
        whenever(flowableUseCase.execute()).thenReturn(testFlowable)

        // WHEN
        val disposable = flowableUseCase.execute(compositeDisposable, onResponse, onTokenExpire)

        // THEN
        verify(onResponse).invoke(ErrorResponse(mockedError))
        verify(onTokenExpire).invoke()
        verify(compositeDisposable).add(disposable)
    }
}