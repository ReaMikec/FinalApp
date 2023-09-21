package com.example.fitness.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.fitness.model.DailyFitnessModel
import com.example.fitness.model.MonthlyFitnessModel
import com.example.fitness.model.WeeklyFitnessModel
import com.example.fitness.repository.FitnessRepository
import com.example.fitness.repository.FitnessRepositoryImpl
import com.example.fitness.repository.SharedPreferencesRepository
import com.example.fitness.repository.SharedPreferencesRepositoryImpl


class FitnessViewModel: ViewModel() {

    // Instanciranje repozitorija s podacima s google fit-a
    val fitnessRepo: FitnessRepository = FitnessRepositoryImpl()
    // Instanciranje repozitorija koji se bavi SharedPreferences-om (lokalnim spremanjem podataka)
    val sharedPreferencesRepo: SharedPreferencesRepository = SharedPreferencesRepositoryImpl()

    // Funkcija za dohvaćanje dnevnih podataka o kondiciji
    fun getDailyFitnessData(context: Context): LiveData<DailyFitnessModel> {
        val dailyFitnessLiveData = fitnessRepo.getDailyFitnessData(context)
        return dailyFitnessLiveData
    }

    // Funkcija za dohvaćanje tjednih podataka o kondiciji
    fun getWeeklyFitnessData(context: Context): LiveData<WeeklyFitnessModel> {
        val weeklyFitnessLiveData = fitnessRepo.getWeeklyFitnessData(context)
        return weeklyFitnessLiveData
    }

    // Funkcija za dohvaćanje mjesečnih podataka o kondiciji
    fun getMonthlyFitnessData(context: Context): LiveData<MonthlyFitnessModel> {
        val monthlyFitnessLiveData = fitnessRepo.getMonthlyFitnessData(context)
        return monthlyFitnessLiveData
    }

    // Funkcija za spremanje ciljane količine koraka korisnika u SharedPreferences
    fun saveObjectiveSteps(context: Context, objectiveSteps: Int) {
        sharedPreferencesRepo.saveObjectiveSteps(context, objectiveSteps)
    }

    // Funkcija za učitavanje ciljane količine koraka korisnika iz SharedPreferences-a
    fun loadObjectiveSteps(context: Context): Int {
        return sharedPreferencesRepo.loadObjectiveSteps(context)
    }

}