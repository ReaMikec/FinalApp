package com.example.fitness.model

// WeeklyFitnessModel je model podataka koji reprezentira kolekciju dnevnih fitnes aktivnosti (DailyFitnessModel) za proteklih 7 dana
data class WeeklyFitnessModel(
    // Lista koja sadrži objekte tipa DailyFitnessModel. Ova lista predstavlja skup svih dnevnih
    // fitness aktivnosti za određeni mjesec. Budući da je deklarirana kao 'val',
    // ova lista se ne može ponovno dodijeliti nakon inicijalizacije, ali elementi unutar liste
    // (ako su var tipa) mogu se mijenjati.
    val dailyFitnessList: List<DailyFitnessModel>
)