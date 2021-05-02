package io.github.msh91.arch.util.provider

import android.content.Context
import io.github.msh91.arch.R
import io.github.msh91.arch.data.model.Error
import io.github.msh91.arch.data.model.HttpError
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class ResourceProviderTest {
    @MockK
    lateinit var context: Context
    private lateinit var resourceProvider: ResourceProvider

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        resourceProvider = ResourceProvider(context)
        every { context.getString(any()) } returns ""
    }

    @Test
    fun `connection failure error message`() {
        // WHEN
        resourceProvider.getErrorMessage(HttpError.ConnectionFailed)

        // THEN
        verify { context.getString(R.string.error_connection) }
    }

    @Test
    fun `timeout error message`() {
        // WHEN
        resourceProvider.getErrorMessage(HttpError.TimeOut)

        // THEN
        verify { context.getString(R.string.error_timeout) }
    }

    @Test
    fun `unauthorized error message`() {
        // WHEN
        resourceProvider.getErrorMessage(HttpError.UnAuthorized)

        // THEN
        verify { context.getString(R.string.error_unauthorized) }
    }

    @Test
    fun `null error message`() {
        // WHEN
        resourceProvider.getErrorMessage(Error.Null)

        // THEN
        verify { context.getString(R.string.error_null) }
    }

    @Test
    fun `not defined error message`() {
        // WHEN
        resourceProvider.getErrorMessage(mockk<Error.NotDefined>())

        // THEN
        verify { context.getString(R.string.error_not_defined) }
    }

    @Test
    fun `invalid response with message`() {
        // GIVEN
        val mockedError = mockk<HttpError.InvalidResponse>()
        every { mockedError.message } returns "error"
        // WHEN
        val message = resourceProvider.getErrorMessage(mockedError)

        // THEN
        verify(exactly = 0) { context.getString(any()) }
        assert(message == "error")
    }

    @Test
    fun `invalid response without message`() {
        // GIVEN
        val mockedError = mockk<HttpError.InvalidResponse>()
        every { mockedError.message } returns null
        // WHEN
        resourceProvider.getErrorMessage(mockedError)

        // THEN
        verify { context.getString(R.string.error_not_defined) }
    }
}