package com.example.fitness.repository

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

// Implementacija SharedPreferencesRepository interfejsa
class SharedPreferencesRepositoryImpl: SharedPreferencesRepository {

    // Kasna inicijalizacija SharedPreferences objekta.
    private lateinit var sharedPreferences: SharedPreferences
    // Konstantna vrijednost za ključ koji se koristi za pohranu i dohvat ciljnog broja koraka.
    private val OBJECTIVE_STEPS_KEY = "objective_steps"

    // Funkcija za dohvat SharedPreferences objekta. Ako objekt nije već inicijaliziran,
    // inicijalizira ga s imenom "my_prefs" u privatnom modu
    override fun getSharedPreferences(context: Context): SharedPreferences {
        if (!::sharedPreferences.isInitialized) {
            sharedPreferences = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        }
        return sharedPreferences
    }

    // Funkcija za spremanje ciljnog broja koraka u SharedPreferences.
    override fun saveObjectiveSteps(context: Context, objectiveSteps: Int) {
        val prefs = getSharedPreferences(context)
        prefs.edit().putInt(OBJECTIVE_STEPS_KEY, objectiveSteps).apply()
        Log.d("SharedPreferences", "Saved objective steps: $objectiveSteps")

    }

    // Funkcija za dohvat ciljnog broja koraka iz SharedPreferences.
    // Ako vrijednost ne postoji, vraća se zadanu vrijednost 8000 koraka.
    override fun loadObjectiveSteps(context: Context): Int {
        val prefs = getSharedPreferences(context)
        val loadedSteps = prefs.getInt(OBJECTIVE_STEPS_KEY, 8000)
        Log.d("SharedPreferences", "Loaded objective steps: $loadedSteps")
        return loadedSteps

    }
}