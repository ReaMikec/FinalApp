package com.example.fitness.repository

import android.content.Context
import android.content.SharedPreferences

// Interface za rad s SharedPreferences
interface SharedPreferencesRepository {

    // Ova metoda definira način na koji se ciljni broj koraka treba pohraniti u SharedPreferences,argumenti metode su kontekst (potreban za pristup SharedPreferences) i ciljni broj koraka
    fun saveObjectiveSteps(context: Context, objectiveSteps: Int)

    // Ova metoda definira dohvaćanje ciljanog broja koraka

    fun loadObjectiveSteps(context: Context): Int

    // Ova metoda definira kako dohvatiti instancu
    fun getSharedPreferences(context: Context): SharedPreferences
}

