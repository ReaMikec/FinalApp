package com.example.fitness.model

// WeeklyFitnessModel je model podataka koji prikazuje kolekciju dnevnih fitnes aktivnosti (DailyFitnessModel) za proteklih 7 dana
data class WeeklyFitnessModel(
    // Lista koja sadrži objekte tipa DailyFitnessModel predstavlja skup svih dnevnih fitness aktivnosti za određeni mjesec. Nakon inicijalizacije, ova lista se ne može ponovno dodijeliti jer je deklarirana kao 'val', ali elementi unutar liste (ako su varijable) mogu se mijenjati.
    val dailyFitnessList: List<DailyFitnessModel>
)