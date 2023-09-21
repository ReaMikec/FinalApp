package com.example.fitness.ui

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.viewModels
import com.example.fitness.R
import com.example.fitness.viewmodel.FitnessViewModel

class Setting : Fragment() {

    // Definiranje varijabli za UI elemente
    private lateinit var stepsTextView: TextView
    private lateinit var changeObjButton: Button

    // Instanciranje ViewModel-a za komunikaciju s backend logikom
    private val fitnessViewModel: FitnessViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflacija layouta fragmenta
        val rootView = inflater.inflate(R.layout.fragment_setting, container, false)

        // Povezivanje UI elemenata s varijablama
        stepsTextView = rootView.findViewById(R.id.steps)
        changeObjButton = rootView.findViewById(R.id.changeObj)

        // Prikaz trenutnog cilja koraka korisnika
        showObjectiveSteps(rootView.context)

        // Postavljanje listenera na gumb koji će pokazati dijalog za promjenu cilja koraka
        changeObjButton.setOnClickListener{
            showObjectiveDialog(rootView.context)
        }

        return rootView
    }

    // Metoda koja prikazuje trenutni cilj koraka korisnika
    private fun showObjectiveSteps(context: Context) {
        stepsTextView.text = fitnessViewModel.loadObjectiveSteps(context).toString()
    }

    // Metoda koja prikazuje dijalog s NumberPicker-om gdje korisnik može odabrati novi cilj koraka
    private fun showObjectiveDialog(context: Context) {

        // Inflacija custom layouta za dijalog
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_objective, null)
        val dialogBuilder = AlertDialog.Builder(context)
            .setView(dialogView)
            .setTitle("Postavi ciljani broj dnevnih koraka")

        // Postavljanje vrijednosti i konfiguracija NumberPicker-a
        val objectiveSeekBar = dialogView.findViewById<NumberPicker>(R.id.stepsPicker)
        objectiveSeekBar.minValue = 8
        objectiveSeekBar.maxValue = 40
        objectiveSeekBar.value = fitnessViewModel.loadObjectiveSteps(context) / 1000

        // Postavljanje formatera za prikaz vrijednosti u povećanjima od 1000
        objectiveSeekBar.setFormatter( object : NumberPicker.Formatter {
            override fun format(value: Int): String {
                return "${value * 1000}"
            }
        })

        // Postavljanje listenera na "Save" gumb dijaloga za spremanje novog cilja koraka
        dialogBuilder.setPositiveButton("Spremi") { _, _ ->
            val newObjectiveSteps = objectiveSeekBar.value * 1000
            fitnessViewModel.saveObjectiveSteps(context, newObjectiveSteps)
            showObjectiveSteps(context)
            Toast.makeText(context, "Ciljani broj koraka je spremljen", Toast.LENGTH_SHORT).show()
        }

        // Prikaz dijaloga
        dialogBuilder.create().show()
    }

}
