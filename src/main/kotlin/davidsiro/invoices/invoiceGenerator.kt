package davidsiro.invoices

import java.io.InputStream
import java.io.OutputStream
import java.io.OutputStreamWriter
import kotlin.script.experimental.api.EvaluationResult
import kotlin.script.experimental.api.ResultValue
import kotlin.script.experimental.api.valueOrThrow
import kotlin.script.experimental.host.StringScriptSource
import kotlin.script.experimental.jvmhost.BasicJvmScriptingHost
import kotlin.script.experimental.jvmhost.createJvmCompilationConfigurationFromTemplate

fun generateInvoice(input: InputStream, output: OutputStream) {
    val compilationConfiguration = createJvmCompilationConfigurationFromTemplate<InvoiceScript>()
    val evalResult = BasicJvmScriptingHost().eval(
        StringScriptSource(input.bufferedReader().use { it.readText() }), compilationConfiguration, null
    )

    val valueOrNull: EvaluationResult = evalResult.valueOrThrow()
    val returnValue: ResultValue = valueOrNull.returnValue
    val inputInvoiceDef: Invoice = when (returnValue) {
        is ResultValue.Value -> returnValue.value as Invoice
        else -> throw IllegalStateException("Failed to compile invoice script")
    }

    OutputStreamWriter(output).use { out ->
        generateHTMLInvoice(out, inputInvoiceDef)
    }
}
