package io.github.msh91.arch.ui.home.list

import android.util.Log
import io.github.msh91.arch.data.model.response.APIResponse
import io.github.msh91.arch.data.model.response.ErrorResponse
import io.github.msh91.arch.data.model.response.SuccessResponse
import io.github.msh91.arch.data.model.user.TestUserModel
import io.github.msh91.arch.domain.user.CreateUserUseCase
import io.github.msh91.arch.ui.base.BaseViewModel
import io.github.msh91.arch.util.connectivity.BaseConnectionManager
import javax.inject.Inject

class HomeListViewModel @Inject constructor(
        private val createUserUseCase: CreateUserUseCase,
        connectionManager: BaseConnectionManager
) : BaseViewModel(connectionManager) {
    private val TAG = HomeListViewModel::class.java.simpleName
    override fun clearUseCaseDisposables() {}

    override fun onStart() {
        createUserUseCase
                .setParameters(TestUserModel("name", "family"))
                .execute(this::onResponse)
    }

    private fun onResponse(apiResponse: APIResponse<TestUserModel>) {
        Log.d(TAG, "onResponse() called  with: apiResponse = [$apiResponse]")
        when (apiResponse) {
            is SuccessResponse -> {
                Log.d(TAG, "onResponse: response: ${apiResponse.value}")
            }
            is ErrorResponse -> {
            }
        }
    }
}