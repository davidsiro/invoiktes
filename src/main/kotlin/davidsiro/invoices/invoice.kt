package davidsiro.invoices;

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.time.Instant
import java.time.format.DateTimeFormatter

enum class VAT(val rate: BigDecimal) {

    STANDARD(BigDecimal.valueOf(21)),
    ZERO(BigDecimal.ZERO);

}

enum class Currency {
    CZK, EUR, USD
}

data class Address(
    val street: String,
    val zip: String,
    val city: String,
    val country: String
)

data class Party(
    val name: String,
    val address: Address,
    val phone: String?,
    val email: String?,
    val ic: String?,
    val vatNo: String?
)

data class PaymentDetails(
    val created: Instant,
    val due: Instant,
    val vatDate: Instant,
    val orderNo: String?,
    val variableSymbol: String?,
    val receiverAccount: BankAccount
)

data class BankAccount(
    val bankName: String?,
    val bankAddress: String?,
    val accountNumber: String?,
    val iban: String,
    val swift: String
)

data class InvoiceItem(
    val description: String,
    val quantity: BigDecimal,
    val quantityUnit: String,
    val pricePerUnit: BigDecimal,
    val vat: VAT
)

data class Invoice(
    val refNo: String,
    val seller: Party,
    val buyer: Party,
    val paymentDetails: PaymentDetails,
    val introText: String?,
    val exchangeRate: BigDecimal,
    val currency: Currency,
    val items: List<InvoiceItem>,
    val labels: List<String>
) {

    fun isZeroVatOnly() = items.map { it.vat }.toSet().let { vatRates ->
        vatRates.size == 1 && vatRates.contains(VAT.ZERO)
    }

}


object Formatters {
    val priceFormat = DecimalFormat("###,###.00", DecimalFormatSymbols().apply { groupingSeparator = ' ' })
    val dayFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy")
}

fun Instant.formatAsDay(): String = Formatters.dayFormat.format(this.atZone(java.time.ZoneId.systemDefault()))

fun BigDecimal.formatAsPrice(): String = Formatters.priceFormat.format(this)

fun calculateItemTotal(item: InvoiceItem): BigDecimal {
    return item.pricePerUnit.multiply(item.quantity).setScale(2, RoundingMode.HALF_UP)
}

fun calculateItemTotalIncVAT(item: InvoiceItem): BigDecimal {
    return item.pricePerUnit.multiply(item.quantity).plus(calculateVAT(item)).setScale(2, RoundingMode.HALF_UP)
}

fun calculateVAT(item: InvoiceItem): BigDecimal {
    return item.pricePerUnit.multiply(item.quantity)
        .multiply(
            item.vat.rate.divide(BigDecimal.valueOf(100))
        )
        .setScale(0, RoundingMode.HALF_UP)
}

fun calculateTotal(inv: Invoice): BigDecimal =
    inv.items.map { calculateItemTotalIncVAT(it) }.reduce { acc, itemTotal -> acc.plus(itemTotal) }

fun calculateTotalVATExcluded(inv: Invoice): BigDecimal = inv.items.map { calculateItemTotal(it) }.reduce { acc,
                                                                                                            itemTotal ->
    acc.plus(itemTotal)
}

fun convertToCZK(inv: Invoice): BigDecimal =
    calculateTotal(inv).multiply(inv.exchangeRate).setScale(2, RoundingMode.HALF_UP)

fun convertToCZK(amount: BigDecimal, inv: Invoice): BigDecimal =
    amount.multiply(inv.exchangeRate).setScale(2, RoundingMode.HALF_UP)