package io.github.msh91.arcyto.core.data.remote

import com.google.common.truth.Truth.assertThat
import io.github.msh91.arcyto.core.data.local.resource.StringProvider
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class RemoteErrorMapperTest {
    private lateinit var sut: RemoteErrorMapper
    private val stringProvider = mockk<StringProvider>()

    @Before
    fun setUp() {
        sut = RemoteErrorMapper(stringProvider)
    }

    @Test
    fun `should return time out error message`() {
        // GIVEN
        val exception = SocketTimeoutException()
        val message = "time out error"
        every { stringProvider.getString(R.string.error_request_timed_out) } returns message

        // WHEN
        val result = sut.getErrorMessage(exception)

        // THEN
        assertThat(result).isEqualTo(message)
    }

    @Test
    fun `should return no internet error message`() {
        // GIVEN
        val exception = mockk<UnknownHostException>()
        val message = "no internet error"
        every { stringProvider.getString(R.string.error_no_internet_connection) } returns message

        // WHEN
        val result = sut.getErrorMessage(exception)

        // THEN
        assertThat(result).isEqualTo(message)
    }

    @Test
    fun `should return server error message`() {
        // GIVEN
        val exception = mockk<HttpException>().apply {
            every { code() } returns 500
        }
        val message = "server error"
        every { stringProvider.getString(R.string.error_server_error) } returns message

        // WHEN
        val result = sut.getErrorMessage(exception)

        // THEN
        assertThat(result).isEqualTo(message)
    }

    @Test
    fun `should return resource not found error message`() {
        // GIVEN
        val exception = mockk<HttpException>().apply {
            every { code() } returns 404
        }
        val message = "resource not found error"
        every { stringProvider.getString(R.string.error_resource_not_found) } returns message

        // WHEN
        val result = sut.getErrorMessage(exception)

        // THEN
        assertThat(result).isEqualTo(message)
    }

    @Test
    fun `should return server unknown error message`() {
        // GIVEN
        val exception = mockk<HttpException>().apply {
            every { code() } returns 0
        }
        val message = "server unknown error"
        every { stringProvider.getString(R.string.error_server_unknown, exception.code()) } returns message

        // WHEN
        val result = sut.getErrorMessage(exception)

        // THEN
        assertThat(result).isEqualTo(message)
    }

    @Test
    fun `should return null`() {
        // GIVEN
        val exception = Exception()

        // WHEN
        val result = sut.getErrorMessage(exception)

        // THEN
        assertThat(result).isNull()
    }
}