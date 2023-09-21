package com.example.fitness.model

// DailyFitnessModel je model podataka koji reprezentira informacije o dnevnim fitnes aktivnostima korisnika,
// uključujući broj koraka, potrošene kalorije i prijeđenu udaljenost
data class DailyFitnessModel (
    var stepCount: Int,
    var caloriesBurned: Int,
    var distance: Float

)