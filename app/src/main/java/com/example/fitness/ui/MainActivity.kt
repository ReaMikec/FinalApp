package com.example.fitness.ui

import android.Manifest
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.fitness.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.DataType
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity() {
    private val GOOGLE_FIT_PERMISSIONS_REQUEST_CODE = 1

    // Referenca na navigacijsku traku
    private lateinit var bottomNavigationView: BottomNavigationView

    private val fitnessOptions = FitnessOptions.builder()
        .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
        .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
        .build()

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Provjerava dopuštenja i pokreće odgovarajuće metode pri stvaranju aktivnosti
        checkPermissionsAndRun(GOOGLE_FIT_PERMISSIONS_REQUEST_CODE)
    }

    // Postavljanje kontroler navigacije za donju navigacijsku traku
    private fun loadNav_controller() {
        bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val navController = findNavController(R.id.nav_controller)
        val currentDestinationId = navController.currentDestination?.id

        if (currentDestinationId != null) {
            navController.popBackStack(currentDestinationId, false)
            navController.navigate(currentDestinationId)
        }

        bottomNavigationView.setupWithNavController(navController)
    }

    // Provjera potrebnih dopuštenja, ako postoje, pokreće se Google Fit prijava
    @RequiresApi(Build.VERSION_CODES.Q)
    private fun checkPermissionsAndRun(fitActionRequestCode: Int) {
        if (permissionApproved()) {
            fitSignIn(fitActionRequestCode)
        } else {
            requestRuntimePermissions(fitActionRequestCode)
        }
    }

    // Postavljanje zahtjev za Google Fit dopuštenjima
    private fun requestPermissions() {
        GoogleSignIn.requestPermissions(
            this, // your activity
            GOOGLE_FIT_PERMISSIONS_REQUEST_CODE,
            getGoogleAccount(),
            fitnessOptions
        )
    }
    // Prijava na Google Fit servis
    private fun fitSignIn(requestCode: Int) {
        if (!GoogleSignIn.hasPermissions(getGoogleAccount(), fitnessOptions)) {
            requestPermissions()
        } else {
            loadNav_controller()
            subscribeToSteps()
        }
    }

    // Dohvaća Google račun korisnika vezan uz Google Fit
    private fun getGoogleAccount() = GoogleSignIn.getAccountForExtension(this, fitnessOptions)

    // Provjerava jesu li odobrena sva potrebna dopuštenja za aplikaciju
    private fun permissionApproved(): Boolean {
        val locationPermission =
            PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )

        val activityRecognitionPermission = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACTIVITY_RECOGNITION
            )
        } else {
            true
        }

        return locationPermission && activityRecognitionPermission
    }

    // Zahtijeva dopuštenja za pristup fizičkoj aktivnosti i lokaciji korisnika u runtime-u
    @RequiresApi(Build.VERSION_CODES.Q)
    private fun requestRuntimePermissions(requestCode: Int) {
        // Check rationale for both location and physical activity permissions
        val shouldProvideRationaleLocation =
            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )

        val shouldProvideRationaleActivity =
            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACTIVITY_RECOGNITION
            )

        val shouldProvideRationale = shouldProvideRationaleLocation || shouldProvideRationaleActivity

        requestCode.let {
            if (shouldProvideRationale) {
                Log.i(TAG, "Presenting permission explanation to offer further context.")
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Permissions Denied",
                    Snackbar.LENGTH_INDEFINITE
                )
                    .setAction("Settings") {
                        // Request both permissions
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACTIVITY_RECOGNITION
                            ),
                            requestCode
                        )
                    }
                    .show()
            } else {
                Log.i(TAG, "Requesting permissions")
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACTIVITY_RECOGNITION
                    ),
                    requestCode
                )
            }
        }
        checkPermissionsAndRun(requestCode)
    }

    // Ažuriranja broja koraka koristeći Google Fit API
    private fun subscribeToSteps() {
        val recordingClient = Fitness.getRecordingClient(this, getGoogleAccount())
        recordingClient.subscribe(DataType.TYPE_STEP_COUNT_DELTA)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.i(TAG, "You have successfully subscribed to receive step count updates!!")
                } else {
                    Log.w(
                        TAG,
                        "An issue occurred while attempting to subscribe to step count updates.",
                        task.exception
                    )
                }
            }
    }

    // Metoda se poziva kada aplikacija dobije odgovor nakon što je postavila zahtjev za dopuštenje
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> when (requestCode) {
                GOOGLE_FIT_PERMISSIONS_REQUEST_CODE -> loadNav_controller()
                else -> {

                }
            }
            else -> {
                Toast.makeText(this, "Permission deined", Toast.LENGTH_LONG).show()
                requestPermissions()
            }
        }
    }
}