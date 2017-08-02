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
                div("row mt-4") {
                    div("col") {}
                    div("col-7") {
                        div("card") {
                            div("card-block") {
                                div("row") {
                                    div("col") {}
                                    div("col") { strong { +"Základ" } }
                                    div("col") { strong { +"Výše DPH" } }
                                    div("col") { strong { +"Celkem" } }
                                }
                                div("row") {
                                    div("col") { strong { +"Základní sazba" } }
                                    div("col") { }
                                    div("col") { }
                                    div("col") { }
                                }
                                div("row") {
                                    div("col") { strong { +"CELKEM" } }
                                    div("col") { }
                                    div("col") { }
                                    div("col") { }
                                }
                                div("card-footer") {
                                    h4("text-right") { +"Celkem ${calculateTotal(invoice)} Kč" }
                                }

                            }
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
            details.orderNo?.let {div { +"Číslo objednávky: ${details.orderNo}" }}
            div {
                +"Konstantní symbol "
                translation("(Variable symbol)")
                +": "
                if (details.variableSymbol != null) +details.variableSymbol else +"-"
            }
        }
        div("col") {
            details.receiverAccount.accountNumber?.let {
                div { +"Bankovní účet: ${details.receiverAccount.accountNumber}" }}

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
        div("col-6") {
            strong { +"Označení dodávky" }
            br {}
            translation("(Service description)")
        }
        div("col") {
            strong { +"Počet m.j." }
            br {}
            translation("(Quantity)")
        }
        div("col") {
            strong { +"Cena za m.j. v EUR" }
            br {}
            translation("(Price per unit in EUR)")
        }
        div("col") {
            strong { +"DPH %" }
            br {}
            translation("(VAT %)")
        }
        div("col") {
            strong { +"Bez DPH v EUR" }
            br {}
            translation("(Price w/o VAT in EUR)")
        }
        div("col") {
            strong { +"DPH v EUR" }
            br {}
            translation("(VAT in EUR)")
        }
        div("col") {
            strong { +"Celkem" }
            br {}
            translation("(Price with VAT in EUR)")
        }
    }
    for (item in invoice.items) {
        div("row") {
            div("col-6") { +item.description }
            div("col") { +"${item.quantity} ${item.quantityUnit}" }
            div("col") { +item.pricePerUnit.toString() }
            div("col") { +"${item.vat.rate} %" }
            div("col") { +calculateItemTotal(item).toString() }
            div("col") { +calculateVAT(item).toString() }
            div("col") { +calculateItemTotalIncVAT(item).toString() }
        }
    }
}

private fun DIV.partyBlock(party: Party) {
    h3("card-subtitle mb-2 text-muted") { +party.name }
    div("row") {
        div("col") { +party.address.street }
    }
    div("row") {
        div("col") { +"${party.address.zip} ${party.address.city}" }
    }
    hr {}
    div("row") {
        div("col") {
            +"IČ "
            translation("(Registration no.)")
            +": "
            +party.ic
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

private fun FlowContent.translation(text: String) {
    small("text-muted translated") { +" ${text} " }
}
