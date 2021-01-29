package davidsiro.invoices

import org.jetbrains.kotlin.cli.common.repl.ScriptArgsWithTypes
import org.jetbrains.kotlin.script.jsr223.KotlinJsr223JvmLocalScriptEngine
import org.jetbrains.kotlin.script.jsr223.KotlinJsr223JvmLocalScriptEngineFactory
import org.jetbrains.kotlin.script.jsr223.KotlinStandardJsr223ScriptTemplate
import org.junit.Test
import java.io.ByteArrayOutputStream
import java.nio.file.Files
import java.nio.file.Path
import javax.script.Bindings
import javax.script.ScriptContext
import kotlin.script.experimental.jvm.util.scriptCompilationClasspathFromContextOrStdlib
import kotlin.test.assertEquals

class InvoiceGeneratorTest {

    @Test
    fun scriptEngineTest() {
        val scriptEngine = KotlinJsr223JvmLocalScriptEngine(
                KotlinJsr223JvmLocalScriptEngineFactory(),
                scriptCompilationClasspathFromContextOrStdlib(
                        keyNames = *arrayOf(
                                "kotlin-script-util.jar",
                                "kotlin-compiler-embeddable.jar"),
                        wholeClasspath = true),
                KotlinStandardJsr223ScriptTemplate::class.qualifiedName!!,
                { ctx, types ->
                    ScriptArgsWithTypes(arrayOf(ctx.getBindings(ScriptContext.ENGINE_SCOPE)), types ?: emptyArray())
                },
                arrayOf(Bindings::class)
        )


        scriptEngine.eval("42.toString()")

    }

    @Test
    fun `should generate html for non zero vat`() {
        val input = InvoiceGeneratorTest::class.java.getResourceAsStream("/testInvoice.kts")!!
        val output = ByteArrayOutputStream()
        generateInvoice(input, output)

        val bytes = output.toByteArray()
        val htmlOutput = String(bytes)

        Files.write(Path.of("./target", "invoice.html"), bytes)

        assertEquals(
                """
<html>
  <head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Faktura 20170006</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha.6/css/bootstrap.min.css">
    <style>
                .translated {
                    font-size: 0.7em;
                }
            </style>
  </head>
  <body>
    <div class="container-fluid mb-4">
      <h1>Faktura - Daňový doklad <small class="text-muted translated"> (Invoice no.) </small><span class="badge badge-primary">0001</span></h1>
      <hr>
      <div class="row">
        <div class="col-6">
          <div class="card">
            <div class="card-block">
              <h2 class="card-title">Dodavatel<small class="text-muted translated"> (Supplier) </small></h2>
              <h3 class="card-subtitle mb-2 text-muted">David Siro</h3>
              <div class="row">
                <div class="col">Uhrin ves 123/32</div>
              </div>
              <div class="row">
                <div class="col">Praha 170 00</div>
              </div>
              <div class="row">
                <div class="col">Česká Republika</div>
              </div>
              <hr>
              <div class="row">
                <div class="col">IČ <small class="text-muted translated"> (Registration no.) </small>: 8127498124</div>
              </div>
              <div class="row">
                <div class="col">DIČ <small class="text-muted translated"> (VAT registration no.) </small>: 123p1o3ip123</div>
              </div>
              <div class="row">
                <div class="col">e-mail: mail@test</div>
              </div>
            </div>
          </div>
        </div>
        <div class="col-6">
          <div class="card">
            <div class="card-block">
              <h2 class="card-title">Odběratel<small class="text-muted translated"> (Client) </small></h2>
              <h3 class="card-subtitle mb-2 text-muted">My Contracting Pal, s.r.o.</h3>
              <div class="row">
                <div class="col">Sokolovska 123456</div>
              </div>
              <div class="row">
                <div class="col">Praha 186 00</div>
              </div>
              <div class="row">
                <div class="col">Česká Republika</div>
              </div>
              <hr>
              <div class="row">
                <div class="col">IČ <small class="text-muted translated"> (Registration no.) </small>: qwewqe</div>
              </div>
              <div class="row">
                <div class="col">DIČ <small class="text-muted translated"> (VAT registration no.) </small>: 1873918237</div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <hr>
      <div class="row">
        <div class="col">
          <div>Datum vystavení <small class="text-muted translated"> (Invoice date) </small>: 01.06.2017</div>
          <div>Datum splatnosti <small class="text-muted translated"> (Due date) </small>: 22.05.2017</div>
          <div>DUZP <small class="text-muted translated"> (Date of taxable supply) </small>: 31.05.2017</div>
        </div>
        <div class="col">
          <div>Platba: převodem</div>
          <div>Číslo objednávky: CS4567862313</div>
          <div>Variabilní symbol <small class="text-muted translated"> (Variable symbol) </small>: 0001</div>
        </div>
        <div class="col">
          <div>Banka: aaa</div>
          <div>Adresa: bbb</div>
          <div>Bankovní účet: 82421318472/2010</div>
          <div>IBAN: CZ89201000000002131333</div>
          <div>SWITFT: FIOBCZPP</div>
        </div>
      </div>
      <hr>
      <div class="row mb-4">
        <div class="col">Na základě Smlouvy o poskytování služeb v oblasti IT a IS Vám fakturuji konzultační činnost za období Květen 2017: </div>
      </div>
      <div class="row">
        <div class="col-5"><strong>Označení dodávky</strong><br><small class="text-muted translated"> (Service description) </small></div>
        <div class="col"><strong>Počet</strong><br><small class="text-muted translated"> (Quantity) </small></div>
        <div class="col"><strong>m.j.</strong><br><small class="text-muted translated"> (Unit) </small></div>
        <div class="col"><strong>Cena za m.j. v EUR</strong><br><small class="text-muted translated"> (Price per unit in EUR) </small></div>
        <div class="col"><strong>DPH %</strong><br><small class="text-muted translated"> (VAT %) </small></div>
        <div class="col"><strong>Bez DPH v EUR</strong><br><small class="text-muted translated"> (Price w/o VAT in EUR) </small></div>
        <div class="col"><strong>DPH v EUR</strong><br><small class="text-muted translated"> (VAT in EUR) </small></div>
        <div class="col"><strong>Celkem</strong><br><small class="text-muted translated"> (Price with VAT in EUR) </small></div>
      </div>
      <div class="row">
        <div class="col-5">dle článku 4.1. a objednávky č. xyz</div>
        <div class="col">168.0</div>
        <div class="col">hod.</div>
        <div class="col">2 500.45</div>
        <div class="col">21 %</div>
        <div class="col">420 075.60</div>
        <div class="col">88 216.00</div>
        <div class="col">508 291.60</div>
      </div>
      <div class="row">
        <div class="col-5">no VAT here</div>
        <div class="col">25</div>
        <div class="col">ks</div>
        <div class="col">100.00</div>
        <div class="col">0 %</div>
        <div class="col">2 500.00</div>
        <div class="col">.00</div>
        <div class="col">2 500.00</div>
      </div>
      <div class="row">
        <div class="col h4"><span class="badge badge-primary">NET30</span><span class="badge badge-primary">TEST</span></div>
      </div>
      <div class="row mt-4">
        <div class="col"></div>
        <div class="col-7">
          <div class="card">
            <div class="card-block">
              <p class="card-text text-right"><strong>Kurz (ČNB k 01.06.2017) </strong><small class="text-muted translated"> (Exchange rate EUR/CZK) </small>: 25.6</p>
              <div class="card-footer">
                <h4 class="text-right">Celkem v EUR<small class="text-muted translated"> (Total due in EUR) </small>: 510 791.60</h4>
                <h5 class="text-right">Celkem v CZK<small class="text-muted translated"> (Total due in CZK) </small>: 13 076 264.96</h5>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </body>
</html>

                """.trimIndent(),
                htmlOutput
        )

    }

    @Test
    fun `should generate html for zero vat`() {
        val input = InvoiceGeneratorTest::class.java.getResourceAsStream("/testInvoice_zero_vat.kts")!!
        val output = ByteArrayOutputStream()
        generateInvoice(input, output)

        val bytes = output.toByteArray()
        val htmlOutput = String(bytes)

        Files.write(Path.of("./target", "invoice.html"), bytes)

        assertEquals(
            """
<html>
  <head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Faktura 20170006</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha.6/css/bootstrap.min.css">
    <style>
                .translated {
                    font-size: 0.7em;
                }
            </style>
  </head>
  <body>
    <div class="container-fluid mb-4">
      <h1>Faktura - Daňový doklad <small class="text-muted translated"> (Invoice no.) </small><span class="badge badge-primary">0001</span></h1>
      <hr>
      <div class="row">
        <div class="col-6">
          <div class="card">
            <div class="card-block">
              <h2 class="card-title">Dodavatel<small class="text-muted translated"> (Supplier) </small></h2>
              <h3 class="card-subtitle mb-2 text-muted">David Siro</h3>
              <div class="row">
                <div class="col">Uhrin ves 123/32</div>
              </div>
              <div class="row">
                <div class="col">Praha 170 00</div>
              </div>
              <div class="row">
                <div class="col">Česká Republika</div>
              </div>
              <hr>
              <div class="row">
                <div class="col">IČ <small class="text-muted translated"> (Registration no.) </small>: 8127498124</div>
              </div>
              <div class="row">
                <div class="col">DIČ <small class="text-muted translated"> (VAT registration no.) </small>: 123p1o3ip123</div>
              </div>
              <div class="row">
                <div class="col">e-mail: mail@test</div>
              </div>
            </div>
          </div>
        </div>
        <div class="col-6">
          <div class="card">
            <div class="card-block">
              <h2 class="card-title">Odběratel<small class="text-muted translated"> (Client) </small></h2>
              <h3 class="card-subtitle mb-2 text-muted">My Contracting Pal, s.r.o.</h3>
              <div class="row">
                <div class="col">Sokolovska 123456</div>
              </div>
              <div class="row">
                <div class="col">Praha 186 00</div>
              </div>
              <div class="row">
                <div class="col">Česká Republika</div>
              </div>
              <hr>
              <div class="row">
                <div class="col">IČ <small class="text-muted translated"> (Registration no.) </small>: qwewqe</div>
              </div>
              <div class="row">
                <div class="col">DIČ <small class="text-muted translated"> (VAT registration no.) </small>: 1873918237</div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <hr>
      <div class="row">
        <div class="col">
          <div>Datum vystavení <small class="text-muted translated"> (Invoice date) </small>: 01.06.2017</div>
          <div>Datum splatnosti <small class="text-muted translated"> (Due date) </small>: 22.05.2017</div>
          <div>DUZP <small class="text-muted translated"> (Date of taxable supply) </small>: 31.05.2017</div>
        </div>
        <div class="col">
          <div>Platba: převodem</div>
          <div>Číslo objednávky: CS4567862313</div>
          <div>Variabilní symbol <small class="text-muted translated"> (Variable symbol) </small>: 0001</div>
        </div>
        <div class="col">
          <div>Banka: aaa</div>
          <div>Adresa: bbb</div>
          <div>Bankovní účet: 82421318472/2010</div>
          <div>IBAN: CZ89201000000002131333</div>
          <div>SWITFT: FIOBCZPP</div>
        </div>
      </div>
      <hr>
      <div class="row mb-4">
        <div class="col">Na základě Smlouvy o poskytování služeb v oblasti IT a IS Vám fakturuji konzultační činnost za období Květen 2017: </div>
      </div>
      <div class="row">
        <div class="col-5"><strong>Označení dodávky</strong><br><small class="text-muted translated"> (Service description) </small></div>
        <div class="col"><strong>Počet</strong><br><small class="text-muted translated"> (Quantity) </small></div>
        <div class="col"><strong>m.j.</strong><br><small class="text-muted translated"> (Unit) </small></div>
        <div class="col"><strong>Cena za m.j. v EUR</strong><br><small class="text-muted translated"> (Price per unit in EUR) </small></div>
        <div class="col"><strong>DPH %</strong><br><small class="text-muted translated"> (VAT %) </small></div>
        <div class="col"><strong>Bez DPH v EUR</strong><br><small class="text-muted translated"> (Price w/o VAT in EUR) </small></div>
        <div class="col"><strong>DPH v EUR</strong><br><small class="text-muted translated"> (VAT in EUR) </small></div>
        <div class="col"><strong>Celkem</strong><br><small class="text-muted translated"> (Price with VAT in EUR) </small></div>
      </div>
      <div class="row">
        <div class="col-5">dle článku 4.1. a objednávky č. xyz</div>
        <div class="col">168.0</div>
        <div class="col">hod.</div>
        <div class="col">2 500.45</div>
        <div class="col">0 %</div>
        <div class="col">420 075.60</div>
        <div class="col">.00</div>
        <div class="col">420 075.60</div>
      </div>
      <div class="row">
        <div class="col-5">no VAT here</div>
        <div class="col">25</div>
        <div class="col">ks</div>
        <div class="col">100.00</div>
        <div class="col">0 %</div>
        <div class="col">2 500.00</div>
        <div class="col">.00</div>
        <div class="col">2 500.00</div>
      </div>
      <div class="row mt-5">
        <div class="col"><em>Faktura je v režimu přenesené daňové povinnosti. Daň odvede zákazník. <small class="text-muted translated"> (Invoice in Reverse charge mode. The buyer is obligated to fill in the VAT amounts and pay the tax.) </small></em></div>
      </div>
      <div class="row">
        <div class="col h4"><span class="badge badge-primary">NET30</span><span class="badge badge-primary">TEST</span></div>
      </div>
      <div class="row mt-4">
        <div class="col"></div>
        <div class="col-7">
          <div class="card">
            <div class="card-block">
              <p class="card-text text-right"><strong>Kurz (ČNB k 01.06.2017) </strong><small class="text-muted translated"> (Exchange rate EUR/CZK) </small>: 25.6</p>
              <div class="card-footer">
                <h4 class="text-right">Celkem v EUR<small class="text-muted translated"> (Total due in EUR) </small>: 422 575.60</h4>
                <h5 class="text-right">Celkem v CZK<small class="text-muted translated"> (Total due in CZK) </small>: 10 817 935.36</h5>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </body>
</html>

                """.trimIndent(),
            htmlOutput
        )

    }


}