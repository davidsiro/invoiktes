package davidsiro.invoices

import org.junit.*
import java.io.ByteArrayOutputStream
import kotlin.test.assertEquals

class InvoiceGeneratorTest {

    @Test
    fun shouldGenerateHtml() {
        val input = InvoiceGeneratorTest::class.java.getResourceAsStream("/testInvoice.kts")!!
        val output = ByteArrayOutputStream()
        generateInvoice(input, output)

        val htmlOutput = String(output.toByteArray())

        assertEquals(
                "<html>\n" +
                        "  <head>\n" +
                        "    <meta charset=\"UTF-8\">\n" +
                        "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1, shrink-to-fit=no\">\n" +
                        "    <title>Faktura 20170006</title>\n" +
                        "    <link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha.6/css/bootstrap.min.css\">\n" +
                        "  </head>\n" +
                        "  <body>\n" +
                        "    <div class=\"container-fluid mb-4\">\n" +
                        "      <h1>Faktura - Daňový doklad<span class=\"badge badge-primary badge-pill\">0001</span></h1>\n" +
                        "      <hr>\n" +
                        "      <div class=\"row\">\n" +
                        "        <div class=\"col-6\">\n" +
                        "          <div class=\"card\">\n" +
                        "            <div class=\"card-block\">\n" +
                        "              <h2 class=\"card-title\">Dodavatel</h2>\n" +
                        "              <h3 class=\"card-subtitle mb-2 text-muted\">David Siro</h3>\n" +
                        "              <div class=\"row\">\n" +
                        "                <div class=\"col\">Uhrin ves 123/32</div>\n" +
                        "              </div>\n" +
                        "              <div class=\"row\">\n" +
                        "                <div class=\"col\">170 00 Praha</div>\n" +
                        "              </div>\n" +
                        "              <hr>\n" +
                        "              <div class=\"row\">\n" +
                        "                <div class=\"col\">IČ: 8127498124</div>\n" +
                        "              </div>\n" +
                        "              <div class=\"row\">\n" +
                        "                <div class=\"col\">DIČ: 123p1o3ip123</div>\n" +
                        "              </div>\n" +
                        "              <div class=\"row\">\n" +
                        "                <div class=\"col\">e-mail: mail@test</div>\n" +
                        "              </div>\n" +
                        "            </div>\n" +
                        "          </div>\n" +
                        "        </div>\n" +
                        "        <div class=\"col-6\">\n" +
                        "          <div class=\"card\">\n" +
                        "            <div class=\"card-block\">\n" +
                        "              <h2 class=\"card-title\">Odběratel</h2>\n" +
                        "              <h3 class=\"card-subtitle mb-2 text-muted\">My Contracting Pal, s.r.o.</h3>\n" +
                        "              <div class=\"row\">\n" +
                        "                <div class=\"col\">Sokolovska 123456</div>\n" +
                        "              </div>\n" +
                        "              <div class=\"row\">\n" +
                        "                <div class=\"col\">186 00 Praha</div>\n" +
                        "              </div>\n" +
                        "              <hr>\n" +
                        "              <div class=\"row\">\n" +
                        "                <div class=\"col\">IČ: qwewqe</div>\n" +
                        "              </div>\n" +
                        "              <div class=\"row\">\n" +
                        "                <div class=\"col\">DIČ: 1873918237</div>\n" +
                        "              </div>\n" +
                        "            </div>\n" +
                        "          </div>\n" +
                        "        </div>\n" +
                        "      </div>\n" +
                        "      <hr>\n" +
                        "      <div class=\"row\">\n" +
                        "        <div class=\"col\">\n" +
                        "          <div>Datum vystavení: 01.06.2017</div>\n" +
                        "          <div>Datum splatnosti: 22.05.2017</div>\n" +
                        "          <div>DUZP: 31.05.2017</div>\n" +
                        "        </div>\n" +
                        "        <div class=\"col\">\n" +
                        "          <div>Platba: převodem</div>\n" +
                        "          <div>Číslo objednávky: CS4567862313</div>\n" +
                        "          <div>Konstantní symbol: -</div>\n" +
                        "          <div>DUZP: 31.05.2017</div>\n" +
                        "        </div>\n" +
                        "        <div class=\"col\">\n" +
                        "          <div>Bankovní účet: 82421318472/2010</div>\n" +
                        "          <div>IBAN: CZ89201000000002131333</div>\n" +
                        "          <div>SWITFT: FIOBCZPP</div>\n" +
                        "        </div>\n" +
                        "      </div>\n" +
                        "      <hr>\n" +
                        "      <div class=\"row mb-4\">\n" +
                        "        <div class=\"col\">Na základě Smlouvy o poskytování služeb v oblasti IT a IS Vám fakturuji konzultační činnost za období Květen 2017: </div>\n" +
                        "      </div>\n" +
                        "      <div class=\"row\">\n" +
                        "        <div class=\"col-4\"><strong>Označení dodávky</strong></div>\n" +
                        "        <div class=\"col\"><strong>Počet m.j.</strong></div>\n" +
                        "        <div class=\"col\"><strong>Cena za m.j.</strong></div>\n" +
                        "        <div class=\"col\"><strong>DPH %</strong></div>\n" +
                        "        <div class=\"col\"><strong>Bez DPH</strong></div>\n" +
                        "        <div class=\"col\"><strong>DPH</strong></div>\n" +
                        "        <div class=\"col\"><strong>Celkem</strong></div>\n" +
                        "      </div>\n" +
                        "      <div class=\"row\">\n" +
                        "        <div class=\"col-4\">dle článku 4.1. a objednávky č. xyz</div>\n" +
                        "        <div class=\"col\">168.0 hod.</div>\n" +
                        "        <div class=\"col\">2500.45</div>\n" +
                        "        <div class=\"col\">21 %</div>\n" +
                        "        <div class=\"col\">420075.60</div>\n" +
                        "        <div class=\"col\">88216</div>\n" +
                        "        <div class=\"col\">508291.60</div>\n" +
                        "      </div>\n" +
                        "      <div class=\"row\">\n" +
                        "        <div class=\"col-4\">no VAT here</div>\n" +
                        "        <div class=\"col\">25 ks</div>\n" +
                        "        <div class=\"col\">100</div>\n" +
                        "        <div class=\"col\">0 %</div>\n" +
                        "        <div class=\"col\">2500.00</div>\n" +
                        "        <div class=\"col\">0</div>\n" +
                        "        <div class=\"col\">2500.00</div>\n" +
                        "      </div>\n" +
                        "      <div class=\"row mt-4\">\n" +
                        "        <div class=\"col\"></div>\n" +
                        "        <div class=\"col-7\">\n" +
                        "          <div class=\"card\">\n" +
                        "            <div class=\"card-block\">\n" +
                        "              <div class=\"row\">\n" +
                        "                <div class=\"col\"></div>\n" +
                        "                <div class=\"col\"><strong>Základ</strong></div>\n" +
                        "                <div class=\"col\"><strong>Výše DPH</strong></div>\n" +
                        "                <div class=\"col\"><strong>Celkem</strong></div>\n" +
                        "              </div>\n" +
                        "              <div class=\"row\">\n" +
                        "                <div class=\"col\"><strong>Základní sazba</strong></div>\n" +
                        "                <div class=\"col\"></div>\n" +
                        "                <div class=\"col\"></div>\n" +
                        "                <div class=\"col\"></div>\n" +
                        "              </div>\n" +
                        "              <div class=\"row\">\n" +
                        "                <div class=\"col\"><strong>CELKEM</strong></div>\n" +
                        "                <div class=\"col\"></div>\n" +
                        "                <div class=\"col\"></div>\n" +
                        "                <div class=\"col\"></div>\n" +
                        "              </div>\n" +
                        "              <div class=\"card-footer\">\n" +
                        "                <h4 class=\"text-right\">Celkem 510791.60 Kč</h4>\n" +
                        "              </div>\n" +
                        "            </div>\n" +
                        "          </div>\n" +
                        "        </div>\n" +
                        "      </div>\n" +
                        "    </div>\n" +
                        "  </body>\n" +
                        "</html>\n",
                htmlOutput
        )

    }

}