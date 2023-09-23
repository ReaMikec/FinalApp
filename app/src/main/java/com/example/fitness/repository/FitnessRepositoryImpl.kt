package com.example.fitness.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.fitness.model.DailyFitnessModel
import com.example.fitness.model.MonthlyFitnessModel
import com.example.fitness.model.WeeklyFitnessModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.data.Field
import com.google.android.gms.fitness.request.DataReadRequest
import java.util.*
import java.util.concurrent.TimeUnit

class FitnessRepositoryImpl(): FitnessRepository {

    // Definiranje FitnessOptions koja će se vrsta podataka tražiti od Google Fit-a
    private val fitnessOptions = FitnessOptions.builder()
        .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
        .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
        .build()

    // Metoda koja vraća podatke o dnevnim koracima korisnika
    override fun getDailyFitnessData(context: Context): MutableLiveData<DailyFitnessModel> {
        val dailyFitnessLiveData = MutableLiveData<DailyFitnessModel>()

        // Postavljanje vremenskog raspona za "danas"
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        val startTime = calendar.timeInMillis

        // Kreiranje zahtjeva za čitanje podataka od Google Fit-a za "danas"
        val readRequest = DataReadRequest.Builder()
            .aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
            .bucketByTime(1, TimeUnit.DAYS)
            .setTimeRange(startTime, System.currentTimeMillis(), TimeUnit.MILLISECONDS)
            .build()

        // Pozivanje Google Fit History API-ja za čitanje podataka
        Fitness.getHistoryClient(context, getGoogleAccount(context))
            .readData(readRequest)
            .addOnSuccessListener { data ->
                val buckets = data.buckets
                val bucket = if (buckets.isNotEmpty()) buckets[0] else null
                var stepCount = 0
                bucket?.dataSets?.forEach { dataSet ->
                    dataSet.dataPoints.forEach { dataPoint ->
                        when (dataPoint.dataType) {
                            DataType.TYPE_STEP_COUNT_DELTA -> {
                                stepCount = dataPoint.getValue(Field.FIELD_STEPS).asInt()
                            }
                        }
                    }
                }
                val dailyFitness = DailyFitnessModel(stepCount)

                dailyFitnessLiveData.postValue(dailyFitness)
            }
            .addOnFailureListener { exception ->

            }

        return dailyFitnessLiveData
    }

    // Metoda koja vraća podatke o tjednom broju koraka korisnika,zadnjih 7 dana
    override fun getWeeklyFitnessData(context: Context): MutableLiveData<WeeklyFitnessModel> {
        val weeklyFitnessLiveData = MutableLiveData<WeeklyFitnessModel>()

        // Postavljanje vremenskog raspona za zadnjih 7 dana
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.add(Calendar.DAY_OF_YEAR, -7)
        val startTime = calendar.timeInMillis

        // Kreiranje zahtjeva za čitanje podataka od Google Fit-a za zadnjih 7 dana
        val readRequest = DataReadRequest.Builder()
            .aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
            .bucketByTime(1, TimeUnit.DAYS)
            .setTimeRange(startTime, System.currentTimeMillis(), TimeUnit.MILLISECONDS)
            .build()

        Fitness.getHistoryClient(context, getGoogleAccount(context))
            .readData(readRequest)
            .addOnSuccessListener { data ->
                val buckets = data.buckets
                val dailyFitnessList = mutableListOf<DailyFitnessModel>()

                buckets.forEach { bucket ->
                    var stepCount = 0

                    bucket.dataSets.forEach { dataSet ->
                        dataSet.dataPoints.forEach { dataPoint ->
                            when (dataPoint.dataType) {
                                DataType.TYPE_STEP_COUNT_DELTA -> {
                                    stepCount = dataPoint.getValue(Field.FIELD_STEPS).asInt()
                                }
                            }
                        }
                    }

                    val dailyFitness = DailyFitnessModel(stepCount)
                    dailyFitnessList.add(dailyFitness)
                }

                val weeklyFitness = WeeklyFitnessModel(dailyFitnessList)
                weeklyFitnessLiveData.postValue(weeklyFitness)
            }
            .addOnFailureListener { exception ->
            }

        return weeklyFitnessLiveData
    }

    // Metoda koja vraća podatke o mjesečnom broju koraka zadnjih 30 dana
    override fun getMonthlyFitnessData(context: Context): MutableLiveData<MonthlyFitnessModel> {
        val monthlyFitnessLiveData = MutableLiveData<MonthlyFitnessModel>()

        // Postavljanje vremenskog raspona za "zadnjih 30 dana"
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.add(Calendar.DAY_OF_YEAR, -30)
        val startTime = calendar.timeInMillis

        // Kreiranje zahtjeva za čitanje podataka od Google Fit-a za zadnjih 30 dana.
        val readRequest = DataReadRequest.Builder()
            .aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
            .bucketByTime(1, TimeUnit.DAYS)
            .setTimeRange(startTime, System.currentTimeMillis(), TimeUnit.MILLISECONDS)
            .build()

        Fitness.getHistoryClient(context, getGoogleAccount(context))
            .readData(readRequest)
            .addOnSuccessListener { data ->
                val buckets = data.buckets
                val dailyFitnessList = mutableListOf<DailyFitnessModel>()

                buckets.forEach { bucket ->
                    var stepCount = 0

                    bucket.dataSets.forEach { dataSet ->
                        dataSet.dataPoints.forEach { dataPoint ->
                            when (dataPoint.dataType) {
                                DataType.TYPE_STEP_COUNT_DELTA -> {
                                    stepCount = dataPoint.getValue(Field.FIELD_STEPS).asInt()
                                }
                            }
                        }
                    }

                    val dailyFitness = DailyFitnessModel(stepCount)
                    dailyFitnessList.add(dailyFitness)
                }

                val monthlyFitness = MonthlyFitnessModel(dailyFitnessList)
                monthlyFitnessLiveData.postValue(monthlyFitness)
            }
            .addOnFailureListener { exception ->
            }

        return monthlyFitnessLiveData
    }

    // Metoda koja vraća GoogleSignInAccount koji je povezan s Google Fit-om
    override fun getGoogleAccount(context: Context): GoogleSignInAccount = GoogleSignIn.getAccountForExtension(context, fitnessOptions)

}