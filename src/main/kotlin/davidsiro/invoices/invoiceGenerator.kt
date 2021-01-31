package davidsiro.invoices

import org.jetbrains.kotlin.cli.common.repl.ScriptArgsWithTypes
import org.jetbrains.kotlin.script.jsr223.KotlinJsr223JvmLocalScriptEngine
import org.jetbrains.kotlin.script.jsr223.KotlinJsr223JvmLocalScriptEngineFactory
import org.jetbrains.kotlin.script.jsr223.KotlinStandardJsr223ScriptTemplate
import java.io.*
import javax.script.Bindings
import javax.script.ScriptContext
import javax.script.ScriptEngine
import javax.script.ScriptEngineManager
import kotlin.script.experimental.jvm.util.scriptCompilationClasspathFromContextOrStdlib

fun generateInvoice(input: InputStream, output: OutputStream) {
    val engine = getScriptEngine()

    val inputInvoiceDef: Invoice = engine.eval(InputStreamReader(input)) as Invoice

    OutputStreamWriter(output).use { out ->
        generateHTMLInvoice(out, inputInvoiceDef)
    }
}

private fun getScriptEngine(): ScriptEngine {
    return KotlinJsr223JvmLocalScriptEngine(
        KotlinJsr223JvmLocalScriptEngineFactory(),
        scriptCompilationClasspathFromContextOrStdlib(
            keyNames = arrayOf(
                "kotlin-script-util.jar",
                "kotlin-compiler-embeddable.jar"),
            wholeClasspath = true),
        KotlinStandardJsr223ScriptTemplate::class.qualifiedName!!,
        { ctx, types ->
            ScriptArgsWithTypes(arrayOf(ctx.getBindings(ScriptContext.ENGINE_SCOPE)), types ?: emptyArray())
        },
        arrayOf(Bindings::class)
    )
}