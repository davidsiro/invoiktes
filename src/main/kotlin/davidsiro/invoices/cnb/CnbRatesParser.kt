package davidsiro.invoices.cnb

import davidsiro.invoices.Currency
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

object CnbRatesParser {

    private const val EXCHANGE_RATE_FORMAT = "0.0##"

    private val symbols = DecimalFormatSymbols().apply {
        this.decimalSeparator = ','
    }
    private val exchangeRateFormat =
        DecimalFormat(EXCHANGE_RATE_FORMAT, symbols).apply { this.isParseBigDecimal = true }

    fun parse(inputText: String, currency: Currency): BigDecimal = inputText.lineSequence()
        .first { it.uppercase().contains(currency.name) }
        .let { line ->
            val cols = line.split("|")
            if (cols.size != 5) {
                throw IllegalStateException("Unexpected number of columns: $line")
            }
            val exchangeRateString = cols[4]
            exchangeRateFormat.parse(exchangeRateString) as BigDecimal
        }

}
