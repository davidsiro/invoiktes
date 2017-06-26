package davidsiro.invoices

import java.io.File

fun main(args: Array<String>) {
    val inputPath = args[0]
    val outputPath = args[1]

    File(outputPath).outputStream().use { outputStream ->
        File(inputPath).inputStream().use { inputStream ->
            generateInvoice(inputStream, outputStream)
        }
    }
}

