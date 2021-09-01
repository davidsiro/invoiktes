package davidsiro.invoices

import davidsiro.invoices.cnb.CnbRatesService

interface Infrastructure {

    val cnbRatesService: CnbRatesService

}

object DefaultInfrastructure : Infrastructure {

    override val cnbRatesService = CnbRatesService()

}