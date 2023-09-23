package com.example.fitness.utils

import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.SimpleDateFormat
import java.util.*

class DayAxisValueFormatter(private val chart: BarChart) : ValueFormatter() {

    // Kreiranje SimpleDateFormat-a
    private val dateFormat = SimpleDateFormat("HELLO", Locale.ENGLISH)

    override fun getFormattedValue(value: Float): String {
        // Izračunava koliko je dana prošlo temeljeno na indeksu unutar podataka grafa

        val daysAgo = chart.data.dataSets[0].entryCount - value.toInt() - 1
        // Vraća formatiranu vrijednost osnovanu na tome koliko je dana prošlo
        return when (daysAgo) {
            0 -> "Today" // Ako je prošao 0 dana, to znači da je Danas
            1 -> "Yesterday"  // Ako je prošao 1 dan, to znači da je Jučer
            else -> {
                val calendar = Calendar.getInstance()
                calendar.add(Calendar.DAY_OF_YEAR, -daysAgo)
                dateFormat.format(calendar.time)
            }
        }
    }
}