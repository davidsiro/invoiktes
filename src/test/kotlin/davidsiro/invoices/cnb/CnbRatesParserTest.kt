package davidsiro.invoices.cnb

import davidsiro.invoices.Currency
import org.junit.Test
import java.math.BigDecimal
import kotlin.test.assertEquals

class CnbRatesParserTest {


    @Test
    fun `should parse the exchange rate for currency`() {
        val ratesString = CnbRatesParser::class.java.getResourceAsStream("/rates.txt")?.bufferedReader()?.readText()!!

        val eurRate = CnbRatesParser.parse(ratesString, Currency.EUR)

        assertEquals(BigDecimal("23.695"), eurRate);
    }
}
