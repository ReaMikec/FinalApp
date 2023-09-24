package com.example.fitness.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.fitness.R
import com.example.fitness.model.MonthlyFitnessModel
import com.example.fitness.model.WeeklyFitnessModel
import com.example.fitness.utils.DayAxisValueFormatter
import com.example.fitness.viewmodel.FitnessViewModel
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*




class Statistics : Fragment() {
    // Deklaracija varijabli za grafičke prikaze i tekstualne elemente grafikona
    private lateinit var progressChart: BarChart
    private lateinit var monthlyProgressChart: BarChart
    private lateinit var averageStepsWeekTextView: TextView
    private lateinit var averageStepsMonthTextView: TextView
    // Inicijalizacija ViewModel-a
    private val fitnessViewModel: FitnessViewModel by viewModels()
    private lateinit var rootView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Učitavanje layouta za ovaj fragment
        rootView = inflater.inflate(R.layout.fragment_statistics, container, false)
        // Inicijalizacija komponenata iz layouta
        averageStepsWeekTextView = rootView.findViewById(R.id.average_steps_week)
        averageStepsMonthTextView = rootView.findViewById(R.id.average_steps_month)
        progressChart = rootView.findViewById(R.id.progress_chart)
        monthlyProgressChart = rootView.findViewById(R.id.progress_chart_month)
        // Praćenje podataka za tjedni prikaz
        fitnessViewModel.getWeeklyFitnessData(rootView.context).observe(viewLifecycleOwner, Observer { WeeklyFitness->
            loadWeeklyChart(WeeklyFitness)
            val averageStepsWeek = WeeklyFitness.dailyFitnessList.map { it.stepCount }.average()
            averageStepsWeekTextView.text = getString(R.string.average_steps_week, averageStepsWeek.toInt())
        })
        // Praćenje podataka za mjesečni prikaz
        fitnessViewModel.getMonthlyFitnessData(rootView.context).observe(viewLifecycleOwner, Observer { MonthlyFitness->
            loadMonthlyChart(MonthlyFitness)
            val averageStepsMonth = MonthlyFitness.dailyFitnessList.map { it.stepCount }.average()
            averageStepsMonthTextView.text = getString(R.string.average_steps_month, averageStepsMonth.toInt())
        })
        return rootView
    }
    // Funkcija za postavljanje grafa za prikaz tjednih podatka
    private fun loadWeeklyChart(WeeklyFitness: WeeklyFitnessModel) {
        // Konfiguracija grafa
        progressChart.description.isEnabled = false
        progressChart.setTouchEnabled(false)
        progressChart.setDrawGridBackground(false)

        // Postavljanje podataka za grafičke skupove
        val stepsDataSet = BarDataSet(mutableListOf(), "Steps")
        stepsDataSet.color = ContextCompat.getColor(rootView.context, R.color.calories)
        val objectiveSteps = fitnessViewModel.loadObjectiveSteps(rootView.context).toFloat()

        // Punjenje grafičkih skupova podacima
        WeeklyFitness.dailyFitnessList.forEachIndexed { index, fitnessData ->
            val stepsEntry = BarEntry(index.toFloat(), fitnessData.stepCount.toFloat())
            stepsDataSet.addEntry(stepsEntry)
        }

        val data = BarData()
        //data.addDataSet(caloriesDataSet)
        data.addDataSet(stepsDataSet)
        //data.addDataSet(distanceDataSet)

        progressChart.data = data

        // Konfiguracija osi grafa
        // Set X-axis properties
        val xAxis = progressChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        //xAxis.labelCount = 7
        xAxis.valueFormatter = DayAxisValueFormatter(progressChart)

        // Set Y-axis properties
        val yAxisLeft = progressChart.axisLeft
        yAxisLeft.setDrawGridLines(false)
        yAxisLeft.isEnabled = true
        yAxisLeft.axisMinimum = 0f
        // Postavljanje maksimalne vrijednosti y-osi na 110% ciljnog broja koraka
        yAxisLeft.axisMaximum = (objectiveSteps + (0.1 * objectiveSteps)).toFloat() // Ovdje dodajemo 10% na ciljani broj koraka da bi linija bila jasno vidljiva

        val yAxisRight = progressChart.axisRight
        yAxisRight.isEnabled = false
        yAxisLeft.setDrawGridLines(false)

        val barData = progressChart.barData
        barData.barWidth = 0.6f

        // Dodavanje linije za dnevni cilj
        val targetLine = LimitLine(objectiveSteps, "Dnevni cilj")
        targetLine.lineWidth = 2f
        targetLine.lineColor = ContextCompat.getColor(requireContext(), R.color.steps) // Promjena boje linije
        targetLine.labelPosition = LimitLine.LimitLabelPosition.RIGHT_TOP
        targetLine.textSize = 10f
        yAxisLeft.addLimitLine(targetLine)

        progressChart.legend.isEnabled = true

        progressChart.notifyDataSetChanged()
        progressChart.invalidate()
    }

    // Funkcija za postavljanje grafa za prikaz mjesečnih podatka
    private fun loadMonthlyChart(MonthlyFitness: MonthlyFitnessModel) {
        // Konfiguracija grafa
        monthlyProgressChart.description.isEnabled = false
        monthlyProgressChart.setTouchEnabled(false)
        monthlyProgressChart.setDrawGridBackground(false)

        // Postavljanje podataka za grafičke skupove
        val stepsDataSet = BarDataSet(mutableListOf(), "Steps")
        stepsDataSet.color = ContextCompat.getColor(rootView.context, R.color.calories)
        val objectiveSteps = fitnessViewModel.loadObjectiveSteps(rootView.context).toFloat()

        // Punjenje grafičkih skupova podacima
        MonthlyFitness.dailyFitnessList.forEachIndexed { index, fitnessData ->
            val stepsEntry = BarEntry(index.toFloat(), fitnessData.stepCount.toFloat())

            stepsDataSet.addEntry(stepsEntry)

        }

        val data = BarData()
        //data.addDataSet(caloriesDataSet)
        data.addDataSet(stepsDataSet)
        //data.addDataSet(distanceDataSet)

        monthlyProgressChart.data = data

        // Konfiguracija osi grafa
        // Set X-axis properties
        val xAxis = monthlyProgressChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM

        // Set Y-axis properties
        val yAxisLeft = monthlyProgressChart.axisLeft
        yAxisLeft.setDrawGridLines(false)
        yAxisLeft.isEnabled = true
        yAxisLeft.axisMinimum = 0f

        // Postavljanje maksimalne vrijednosti y-osi na 110% ciljnog broja koraka
        yAxisLeft.axisMaximum = (objectiveSteps + (0.1 * objectiveSteps)).toFloat() // Ovdje dodajemo 10% na ciljani broj koraka da bi linija bila jasno vidljiva
        val yAxisRight = monthlyProgressChart.axisRight
        yAxisRight.isEnabled = false
        yAxisLeft.setDrawGridLines(false)

        val barData = monthlyProgressChart.barData
        barData.barWidth = 0.6f

        // Dodavanje linije za dnevni cilj
        val targetLine = LimitLine(objectiveSteps, "Dnevni cilj")
        targetLine.lineWidth = 2f
        targetLine.lineColor = ContextCompat.getColor(requireContext(), R.color.steps) // Promijenite boju prema želji
        targetLine.labelPosition = LimitLine.LimitLabelPosition.RIGHT_TOP
        targetLine.textSize = 10f
        yAxisLeft.addLimitLine(targetLine)

        monthlyProgressChart.legend.isEnabled = true

        monthlyProgressChart.notifyDataSetChanged()
        monthlyProgressChart.invalidate()
    }
}