package io.github.msh91.arcyto.core.formatter.price

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runners.Parameterized

class FormatPriceUseCaseImplTest {
    private lateinit var sut: FormatPriceUseCaseImpl

    @Before
    fun setUp() {
        sut = FormatPriceUseCaseImpl()
    }

    @Test
    fun testFormat() {
        val prices = testParams()
        prices.forEach {
            val actual = sut.invoke(it.first, it.second)
            assertThat(actual).isEqualTo(it.third)
        }
    }

    @Parameterized.Parameters
    private fun testParams(): List<Triple<Double, String, String>> {
        return listOf(
            Triple(1.0, "usd", "$1.00"),
            Triple(1.0, "eur", "€1.00"),
            Triple(1.0, "gbp", "£1.00"),
            Triple(1000.0, "usd", "$1,000.00"),
            Triple(1000.0, "eur", "€1,000.00"),
            Triple(1000.0, "gbp", "£1,000.00"),
            Triple(1000000.0, "usd", "$1,000,000.00"),
            Triple(1000000.0, "eur", "€1,000,000.00"),
            Triple(1000000.0, "gbp", "£1,000,000.00"),
            Triple(1000000000.0, "usd", "$1,000,000,000.00"),
            Triple(1000000000.0, "eur", "€1,000,000,000.00"),
            Triple(1000000000.0, "gbp", "£1,000,000,000.00"),
        )
    }
}