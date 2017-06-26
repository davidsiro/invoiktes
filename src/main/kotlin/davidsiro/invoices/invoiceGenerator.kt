package davidsiro.invoices

import java.io.*
import javax.script.ScriptEngineManager

fun generateInvoice(input: InputStream, output: OutputStream) {
    val engine = ScriptEngineManager().getEngineByExtension("kts")!!

    val inputInvoiceDef: Invoice = engine.eval(InputStreamReader(input)) as Invoice

    OutputStreamWriter(output).use { out ->
        generateHTMLInvoice(out, inputInvoiceDef)
    }
}