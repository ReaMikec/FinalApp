package com.example.fitness.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.fitness.model.DailyFitnessModel
import com.example.fitness.model.MonthlyFitnessModel
import com.example.fitness.model.WeeklyFitnessModel
import com.google.android.gms.auth.api.signin.GoogleSignInAccount


// Definira interface za rad s podacima vezanim uz Googel fit
interface FitnessRepository {

    // Funkcija koja vraća dnevne podatke.
    fun getDailyFitnessData(context: Context): MutableLiveData<DailyFitnessModel>

    // Funkcija koja vraća tjedne podatke o kondiciji
    fun getWeeklyFitnessData(context: Context): MutableLiveData<WeeklyFitnessModel>

    // Funkcija koja vraća mjesečne podatke o kondiciji
    fun getMonthlyFitnessData(context: Context): MutableLiveData<MonthlyFitnessModel>

    // Funkcija koja dohvaća podatke Google računa korisnika

    fun getGoogleAccount(context: Context): GoogleSignInAccount


}