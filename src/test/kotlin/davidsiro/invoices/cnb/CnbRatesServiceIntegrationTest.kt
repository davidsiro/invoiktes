package davidsiro.invoices.cnb

import davidsiro.invoices.Currency
import org.junit.Test
import java.math.BigDecimal
import kotlin.test.assertTrue

class CnbRatesServiceIntegrationTest {


    @Test
    fun `should fetch rates from CNB`() {
        val service = CnbRatesService()

        val rate = service.fetchRateFor(Currency.EUR)

        assertTrue { rate.compareTo(BigDecimal.ZERO) > 0 }
    }
}
