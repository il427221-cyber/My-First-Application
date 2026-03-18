package ru.netology.nmedia.activity

import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale
import kotlin.text.format

class FormatNumbers {
    val formatSymbols = DecimalFormatSymbols(Locale.US).apply {
        decimalSeparator = '.'
    }

    val oneDecimalFormat = DecimalFormat("0.0", formatSymbols).apply {
        roundingMode = RoundingMode.DOWN
    }

    val noDecimalFormat = DecimalFormat("0", formatSymbols).apply {
        roundingMode = RoundingMode.DOWN
    }

    fun bigNumbersFormat(number: Int): String {
        when (number) {
            in 0..999 -> {
                return "$number"
            }

            in 1000..10_000 -> {
                val value = number / 1000.0
                val formatted = oneDecimalFormat.format(value)
                return "${formatted}K"
            }

            in 10_000..999_999 -> {
                val value = number / 1000.0
                val formatted = noDecimalFormat.format(value)
                return "${formatted}K"
            }

            else -> {
                val value = number / 1_000_000.0
                val formatted = oneDecimalFormat.format(value)
                return "${formatted}M"
            }
        }

    }
}