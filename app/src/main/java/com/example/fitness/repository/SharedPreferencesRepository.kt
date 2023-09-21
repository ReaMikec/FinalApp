package com.example.fitness.repository

import android.content.Context
import android.content.SharedPreferences

// Interfejs za rad s SharedPreferences
interface SharedPreferencesRepository {

    // Metoda koja definira kako ciljni broj koraka treba biti spremljen u SharedPreferences.
    // Argumenti su kontekst (potreban za pristup SharedPreferences) i ciljni broj koraka.
    fun saveObjectiveSteps(context: Context, objectiveSteps: Int)

    // Metoda koja definira kako dohvatiti ciljni broj koraka iz SharedPreferences.
    // Vraća ciljni broj koraka kao cijeli broj.
    fun loadObjectiveSteps(context: Context): Int

    // Metoda koja definira kako dohvatiti instancu SharedPreferences.
    // Vraća instancu SharedPreferences za dan kontekst.
    fun getSharedPreferences(context: Context): SharedPreferences
}

// context se koristi u SharedPreferencesRepository interfejsu i njegovim implementacijama kako bi se pristupilo SharedPreferences
// sustavu Androida (koji omogućava pohranu ključ-vrijednost podataka) jer pristup SharedPreferences zahtijeva kontekst aplikacije
// ili komponente.