package davidsiro.invoices.cnb

import davidsiro.invoices.Currency
import java.time.LocalDate
import khttp.get
import java.math.BigDecimal

class CnbRatesService {

    fun fetchRateFor(currency: Currency, day: LocalDate): BigDecimal {
        get(RATES_URL)
        return BigDecimal.ONE
    }


    companion object {
        private const val RATES_URL =
            "https://www.cnb.cz/cs/financni-trhy/devizovy-trh/kurzy-devizoveho-trhu/kurzy-devizoveho-trhu/denni_kurz.txt"
    }
}