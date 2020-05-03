package by.ciszkin.basicapp.ui

import java.util.*
import kotlin.math.round

object Utils {

    var amountAccuracy = 3
    var costAccuracy = 2

    fun getDateAsString(date: Long): String {
        val answer = StringBuilder()
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = date

        answer.apply {
            append(calendar.get(Calendar.DAY_OF_MONTH).toString())
            append(".")
            append((calendar.get(Calendar.MONTH) + 1).toString())
            append(".")
            append(calendar.get(Calendar.YEAR).toString())
        }

        return answer.toString()
    }

    fun Double.round(decimals: Int): Double {
        var multiplier = 1.0
        repeat(decimals) { multiplier *= 10 }
        return round(this * multiplier) / multiplier
    }


}