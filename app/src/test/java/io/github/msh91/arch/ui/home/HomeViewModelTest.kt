package io.github.msh91.arch.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import io.github.msh91.arch.util.connectivity.BaseConnectionManager
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {
    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    @Mock
    lateinit var homeNavigator: HomeNavigator

    @Mock
    lateinit var connectionManager: BaseConnectionManager

    @InjectMocks
    lateinit var homeViewModel: HomeViewModel

    @Test
    fun `list fragment should be opened after start`() {
        // WHEN
        homeViewModel.onStart()

        homeViewModel.activityAction.observeForever { it?.invoke(mock()) }

        // THEN
        verify(homeNavigator).openListFragment(any())
    }
}
