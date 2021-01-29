package davidsiro.invoices

import kotlinx.html.*
import kotlinx.html.stream.appendHTML

fun generateHTMLInvoice(out: Appendable, invoice: Invoice) {
    out.appendHTML().html {
        head {
            header()
        }
        body {
            div("container-fluid mb-4") {
                caption(invoice)
                hr { }
                partiesSection(invoice)
                hr {}
                paymentDetails(invoice.paymentDetails)
                hr {}
                introText(invoice)
                items(invoice)
                labels(invoice)
                totals(invoice)
            }
        }
    }
}

private fun DIV.totals(invoice: Invoice) {
    div("row mt-4") {
        div("col") {}
        div("col-7") {
            div("card") {
                div("card-block") {
                    p("card-text text-right") {
                        strong {
                            +"Kurz (ČNB k ${invoice.paymentDetails.created.formatAsDay()}) "
                        }
                        translation("(Exchange rate ${invoice.currency}/CZK)")
                        +": ${invoice.exchangeRate}"

                    }
                    div("card-footer") {
                        h4("text-right") {
                            +"Celkem v ${invoice.currency}"
                            translation("(Total due in ${invoice.currency})")
                            +": ${calculateTotal(invoice).formatAsPrice()}"
                        }
                        h5("text-right") {
                            +"Celkem v CZK"
                            translation("(Total due in CZK)")
                            +": ${convertToCZK(invoice).formatAsPrice()}"
                        }
                    }

                }
            }
        }
    }
}

private fun DIV.caption(invoice: Invoice) {
    h1 {
        +"Faktura - Daňový doklad "
        translation("(Invoice no.)")
        span("badge badge-primary") { +invoice.refNo }
    }
}

private fun HEAD.header() {
    meta {
        this.charset = "UTF-8"
    }
    meta(name = "viewport", content = "width=device-width, initial-scale=1, shrink-to-fit=no")
    title("Faktura 20170006")
    link {
        this.rel = "stylesheet"
        this.href = "https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha.6/css/bootstrap.min.css"
    }
    style {
        unsafe {
            raw("""
                .translated {
                    font-size: 0.7em;
                }
            """)
        }
    }
}

private fun DIV.partiesSection(invoice: Invoice) {
    div("row") {
        div("col-6") {
            div("card") {
                div("card-block") {
                    h2("card-title") {
                        +"Dodavatel"
                        translation("(Supplier)")
                    }
                    partyBlock(invoice.seller)
                }
            }
        }
        div("col-6") {
            div("card") {
                div("card-block") {
                    h2("card-title") {
                        +"Odběratel"
                        translation("(Client)")
                    }
                    partyBlock(invoice.buyer)
                }
            }
        }
    }
}

private fun DIV.paymentDetails(details: PaymentDetails) {
    div("row") {
        div("col") {
            div {
                +"Datum vystavení "
                translation("(Invoice date)")
                +": ${details.created.formatAsDay()}"
            }
            div {
                +"Datum splatnosti "
                translation("(Due date)")
                +": ${details.due.formatAsDay()}"
            }
            div {
                +"DUZP "
                translation("(Date of taxable supply)")
                +": ${details.vatDate.formatAsDay()}"
            }
        }
        div("col") {
            div { +"Platba: převodem" }
            details.orderNo?.let { div { +"Číslo objednávky: ${details.orderNo}" } }
            div {
                +"Variabilní symbol "
                translation("(Variable symbol)")
                +": "
                if (details.variableSymbol != null) +details.variableSymbol else +"-"
            }
        }
        div("col") {
            details.receiverAccount.bankName?.let {
                div { +"Banka: ${details.receiverAccount.bankName}" }
            }
            details.receiverAccount.bankAddress?.let {
                div { +"Adresa: ${details.receiverAccount.bankAddress}" }
            }
            details.receiverAccount.accountNumber?.let {
                div { +"Bankovní účet: ${details.receiverAccount.accountNumber}" }
            }

            div { +"IBAN: ${details.receiverAccount.iban}" }
            div { +"SWITFT: ${details.receiverAccount.swift}" }

        }
    }
}

private fun DIV.introText(invoice: Invoice) {
    invoice.introText?.let {
        div("row mb-4") {
            div("col") { +invoice.introText }
        }
    }
}

private fun DIV.items(invoice: Invoice) {
    div("row") {
        div("col-5") {
            strong { +"Označení dodávky" }
            br {}
            translation("(Service description)")
        }
        div("col") {
            strong { +"Počet" }
            br {}
            translation("(Quantity)")
        }
        div("col") {
            strong { +"m.j." }
            br {}
            translation("(Unit)")
        }
        div("col") {
            strong { +"Cena za m.j. v ${invoice.currency}" }
            br {}
            translation("(Price per unit in ${invoice.currency})")
        }
        div("col") {
            strong { +"DPH %" }
            br {}
            translation("(VAT %)")
        }
        div("col") {
            strong { +"Bez DPH v ${invoice.currency}" }
            br {}
            translation("(Price w/o VAT in ${invoice.currency})")
        }
        div("col") {
            strong { +"DPH v ${invoice.currency}" }
            br {}
            translation("(VAT in ${invoice.currency})")
        }
        div("col") {
            strong { +"Celkem" }
            br {}
            translation("(Price with VAT in ${invoice.currency})")
        }
    }
    for (item in invoice.items) {
        div("row") {
            div("col-5") { +item.description }
            div("col") { +"${item.quantity}" }
            div("col") { +item.quantityUnit }
            div("col") { +item.pricePerUnit.formatAsPrice() }
            div("col") { +"${item.vat.rate} %" }
            div("col") { +calculateItemTotal(item).formatAsPrice() }
            div("col") { +calculateVAT(item).formatAsPrice() }
            div("col") { +calculateItemTotalIncVAT(item).formatAsPrice() }
        }
    }
    if (invoice.isZeroVatOnly()) {
        div("row mt-5") {
            div("col") {
                em {
                    +"Faktura je v režimu přenesené daňové povinnosti. Daň odvede zákazník. "
                    translation("(Invoice in Reverse charge mode. The buyer is obligated to fill in the VAT amounts and pay " +
                            "the tax.)")
                }
            }
        }
    }
}

private fun DIV.partyBlock(party: Party) {
    h3("card-subtitle mb-2 text-muted") { +party.name }
    div("row") {
        div("col") { +party.address.street }
    }
    div("row") {
        div("col") { +"${party.address.city} ${party.address.zip}" }
    }
    div("row") {
        div("col") { +"${party.address.country}" }
    }
    hr {}
    party.ic?.let {
        div("row") {
            div("col") {
                +"IČ "
                translation("(Registration no.)")
                +": "
                +party.ic
            }
        }

    }
    party.vatNo?.let {
        div("row") {
            div("col") {
                +"DIČ "
                translation("(VAT registration no.)")
                +": "
                +party.vatNo
            }
        }
    }
    party.email?.let {
        div("row") {
            div("col") {
                +"e-mail: "
                +party.email
            }
        }
    }
    party.phone?.let {
        div("row") {
            div("col") {
                +"tel.: "
                +party.phone
            }
        }
    }

}

private fun DIV.labels(invoice: Invoice) {
    invoice.labels.takeUnless { it.isEmpty() }?.let { labels ->
        div("row") {
            div("col h4") {
                for (label in labels) {
                    span("badge badge-primary") {
                        +label
                    }

                }
            }
        }
    }
}


private fun FlowContent.translation(text: String) {
    small("text-muted translated") { +" ${text} " }
}
