package davidsiro.invoices;

import java.math.BigDecimal
import java.math.RoundingMode
import java.time.Instant
import java.time.format.DateTimeFormatter

enum class VAT(val rate: BigDecimal) {

    STANDARD(BigDecimal.valueOf(21)),
    ZERO(BigDecimal.ZERO);

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
        val ic: String,
        val vatNo: String?)

data class PaymentDetails(
        val created: Instant,
        val due: Instant,
        val vatDate: Instant,
        val orderNo: String,
        val constSymbol: String?,
        val varSymbol: String,
        val receiverAccount: BankAccount)

data class BankAccount(
        val accountNumber: String,
        val iban: String,
        val swift: String)

data class InvoiceItem(
        val description: String,
        val quantity: BigDecimal,
        val quantityUnit: String,
        val pricePerUnit: BigDecimal,
        val vat: VAT)

data class Invoice(
        val refNo: String,
        val seller: Party,
        val buyer: Party,
        val paymentDetails: PaymentDetails,
        val introText: String,
        val items: List<InvoiceItem>)


object formatters {
    val dayFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy")
}

fun Instant.formatAsDay(): String {
    return formatters.dayFormat.format(this.atZone(java.time.ZoneId.systemDefault()))
}

fun calculateItemTotal(item: InvoiceItem): BigDecimal {
    return item.pricePerUnit.multiply(item.quantity).setScale(2, RoundingMode.HALF_UP)
}

fun calculateItemTotalIncVAT(item: InvoiceItem): BigDecimal {
    return item.pricePerUnit.multiply(item.quantity).plus(calculateVAT(item)).setScale(2, RoundingMode.HALF_UP)
}

fun calculateVAT(item: InvoiceItem): BigDecimal {
    return item.pricePerUnit.multiply(item.quantity)
            .multiply(
                    item.vat.rate.divide(BigDecimal.valueOf(100)))
            .setScale(0, RoundingMode.HALF_UP)
}

fun calculateTotal(inv: Invoice): BigDecimal = inv.items.map { calculateItemTotalIncVAT(it) }.reduce { acc, itemTotal -> acc.plus(itemTotal) }

