package com.example.fitness.ui

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.fitness.R
import com.example.fitness.viewmodel.FitnessViewModel


class Home : Fragment() {

    // Definiranje varijabli za UI komponente
    private lateinit var textViewSteps: TextView
    private lateinit var textViewStepsBig: TextView
    private lateinit var textViewCalories: TextView
    private lateinit var textViewDistance: TextView
    private lateinit var stepsProgressBar: ProgressBar
    private lateinit var welcomeMessage: TextView

    // Instanciranje ViewModel-a za dohvaćanje podataka o kondiciji
    private val fitnessViewModel: FitnessViewModel by viewModels()

    // Handler i Runnable za ponavljajuće ažuriranje svake sekunde
    private val handler = Handler()
    private val refreshRunnable = object : Runnable {
        override fun run() {
            loadDailyFitnessData()
            handler.postDelayed(this, 1000) // Ponavljanje svake sekunde
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate (postavljanje) layout-a za ovaj fragment
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)
        requireContext()

        // Povezivanje UI komponenti s varijablama
        welcomeMessage = rootView.findViewById(R.id.welcomeMessage)
        textViewSteps = rootView.findViewById(R.id.steps)
        textViewStepsBig = rootView.findViewById(R.id.steps_big)
        textViewCalories = rootView.findViewById(R.id.burned_calories)
        textViewDistance = rootView.findViewById(R.id.distance)
        stepsProgressBar = rootView.findViewById(R.id.stepsProgressBar)

        // Postavljanje maksimalne vrijednosti za ProgressBar koristeći ViewModel
        stepsProgressBar.max = fitnessViewModel.loadObjectiveSteps(rootView.context)

        // Učitavanje podataka o kondiciji za tekući dan
        loadDailyFitnessData()

        return rootView
    }

    // Kada fragment postane vidljiv korisniku
    override fun onResume() {
        super.onResume()
        startRefreshing() // Pokreće ponavljajuće ažuriranje podataka
    }

    // Kada fragment prestane biti vidljiv korisniku
    override fun onPause() {
        super.onPause()
        stopRefreshing() // Zaustavlja ponavljajuće ažuriranje podataka
    }

    // Metoda za pokretanje ponavljajućeg ažuriranja podataka
    private fun startRefreshing() {
        handler.post(refreshRunnable)
    }

    // Metoda za zaustavljanje ponavljajućeg ažuriranja podataka
    private fun stopRefreshing() {
        handler.removeCallbacks(refreshRunnable)
    }

    // Metoda za učitavanje dnevnih podataka o kondiciji
    private fun loadDailyFitnessData() {
        fitnessViewModel.getDailyFitnessData(requireContext()).observe(viewLifecycleOwner, Observer { DailyFitness->
            // Ažuriranje UI komponenti s novim podacima
            textViewSteps.text = DailyFitness.stepCount.toString()
            textViewStepsBig.text = DailyFitness.stepCount.toString()
            textViewCalories.text = DailyFitness.caloriesBurned.toString()
            textViewDistance.text = String.format("%.2f", DailyFitness.distance)
            stepsProgressBar.progress = DailyFitness.stepCount
        })
    }
}