package com.example.budgetbonsai.utils

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

//fun String.findFloat(): ArrayList<Float> {
//    //get digits from result
//    if (this.isEmpty()) return ArrayList<Float>()
//    val originalResult = ArrayList<Float>()
//    val matchedResults = Regex(pattern = "[+-]?([0-9]*[.])?[0-9]+").findAll(this)
//    for (txt in matchedResults) {
//        if (txt.value.isFloatAndWhole()) originalResult.add(txt.value.toFloat())
//    }
//    return originalResult
//}

fun String.findFormattedNumber(): ArrayList<Int> {
    if (this.isEmpty()) return ArrayList()

    val originalResult = ArrayList<Int>()
    val pattern = "##,###,###.##"
    val decimalFormat = DecimalFormat(pattern)

    val decimalFormatSymbols = DecimalFormatSymbols()
    decimalFormatSymbols.decimalSeparator = '.'
    decimalFormatSymbols.groupingSeparator = ','
    decimalFormat.decimalFormatSymbols = decimalFormatSymbols

    val matchedResults = Regex(pattern = "[0-9]{1,3}(,[0-9]{3})*(\\.[0-9]+)?").findAll(this)
    for (txt in matchedResults) {
        //val formattedNumber = txt.value.replace("[.,]".toRegex(), "") // Remove dots and commas
        val parsedNumber = decimalFormat.parse(txt.value)?.toInt()
        if (parsedNumber != null) {
            originalResult.add(parsedNumber)
        }
    }

    return originalResult
}


fun String.findWholeNumbers(): ArrayList<Int> {
    if (this.isEmpty()) return ArrayList<Int>()
    // Get whole numbers from the result
    val originalResult = ArrayList<Int>()
    val matchedResults = Regex(pattern = "/(\\d)(?=(\\d\\d\\d)+(?!\\d))/g\n").findAll(this)
    for (txt in matchedResults) {
        val number = txt.value.replace(",", "").toInt()
        originalResult.add(number)
    }
    return originalResult
}

fun String.firstLine(): String {
    if (this.isEmpty()) return ""
    return this.split("\n").get(0)
}

//private fun String.isFloatAndWhole() = this.matches("\\d*\\.\\d*".toRegex())
//
//private fun String.isFloat(): Boolean {
//    try {
//        val d = java.lang.Double.valueOf(this)
//        return d != d.toInt().toDouble()
//    } catch (e: Exception) {
//        return false
//    }
//}