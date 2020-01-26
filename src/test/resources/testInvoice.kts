import davidsiro.invoices.*
import java.math.BigDecimal
import java.time.ZoneId
import java.time.ZonedDateTime

val davidSiro = Party(
        "David Siro",
        Address(
                "Uhrin ves 123/32",
                "170 00",
                "Praha",
                "Česká Republika"),
        null,
        "mail@test",
        "8127498124",
        "123p1o3ip123")

val acamar = Party(
        "My Contracting Pal, s.r.o.",
        Address(
                "Sokolovska 123456",
                "186 00",
                "Praha",
                "Česká Republika"),
        null,
        null,
        "qwewqe",
        "1873918237")

val bankAccount = BankAccount("aaa", "bbb", "82421318472/2010", "CZ89201000000002131333", "FIOBCZPP")

val created = ZonedDateTime.of(2017, 6, 1, 0, 0, 0, 0, ZoneId.systemDefault()).toInstant()
val dueDate = ZonedDateTime.of(2017, 5, 22, 0, 0, 0, 0, ZoneId.systemDefault()).toInstant()
val vatDate = ZonedDateTime.of(2017, 5, 31, 0, 0, 0, 0, ZoneId.systemDefault()).toInstant()


val invoice = Invoice("0001", davidSiro, acamar,
        PaymentDetails(created, dueDate, vatDate, "CS4567862313", "0001", bankAccount),
        "Na základě Smlouvy o poskytování služeb v oblasti IT a IS Vám fakturuji konzultační činnost za období Květen 2017: ",
        BigDecimal.valueOf(25.6),
        listOf(
                InvoiceItem("dle článku 4.1. a objednávky č. xyz", BigDecimal.valueOf(168.00), "hod.", BigDecimal
                        .valueOf(2500.45), VAT.STANDARD),
                InvoiceItem("no VAT here", BigDecimal.valueOf(25), "ks", BigDecimal.valueOf(100), VAT.ZERO)))
invoice

