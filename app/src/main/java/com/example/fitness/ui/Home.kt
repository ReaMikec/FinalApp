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
    private lateinit var textViewStepsBig: TextView
    private lateinit var stepsProgressBar: ProgressBar
    private lateinit var welcomeMessage: TextView

    // Instanciranje ViewModel-a za dohvaćanje podataka o kondiciji
    private val fitnessViewModel: FitnessViewModel by viewModels()

    // Handler i Runnable za ponavljajuće ažuriranje svake sekunde
    private val handler = Handler()
    private val refreshRunnable = object : Runnable {
        override fun run() {
            loadDailyFitnessData()
            handler.postDelayed(this, 1000) // Ponavlja se svake sekunde
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate postavljanje layout-a
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)
        requireContext()

        // Povezivanje UI komponenti s varijablama
        welcomeMessage = rootView.findViewById(R.id.welcomeMessage)

        textViewStepsBig = rootView.findViewById(R.id.steps_big)
        stepsProgressBar = rootView.findViewById(R.id.stepsProgressBar)

        // Postavljanje max. vrijednosti za ProgressBar koristeći ViewModel
        stepsProgressBar.max = fitnessViewModel.loadObjectiveSteps(rootView.context)

        // Učitavanje podataka o kondiciji za tekući dan
        loadDailyFitnessData()

        return rootView
    }

    // Fragment postaje vidljiv korisniku
    override fun onResume() {
        super.onResume()
        startRefreshing() // Pokreće ažuriranje podataka
    }

    // Fragment prestaje biti vidljiv korisniku
    override fun onPause() {
        super.onPause()
        stopRefreshing() // Zaustavlja ažuriranje podataka
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


            textViewStepsBig.text = DailyFitness.stepCount.toString()
            stepsProgressBar.progress = DailyFitness.stepCount
        })
    }
}