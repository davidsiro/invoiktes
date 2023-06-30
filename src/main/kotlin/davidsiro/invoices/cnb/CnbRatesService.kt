package davidsiro.invoices.cnb

import davidsiro.invoices.Currency
import khttp.get
import java.math.BigDecimal

class CnbRatesService {

    fun fetchRateFor(currency: Currency): BigDecimal {
        val ratesAsText = get(RATES_URL).text
        return CnbRatesParser.parse(ratesAsText, currency)
    }

    companion object {
        private const val RATES_URL =
            "https://www.cnb.cz/cs/financni-trhy/devizovy-trh/kurzy-devizoveho-trhu/kurzy-devizoveho-trhu/denni_kurz.txt"
    }
}
