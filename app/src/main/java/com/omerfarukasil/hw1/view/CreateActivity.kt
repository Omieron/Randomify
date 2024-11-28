package com.omerfarukasil.hw1.view

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar
import com.omerfarukasil.hw1.MainActivity
import com.omerfarukasil.hw1.R
import com.omerfarukasil.hw1.databinding.ActivityCreateBinding
import com.omerfarukasil.hw1.databinding.AddSubjectBinding
import com.omerfarukasil.hw1.databinding.EditApplicantCardBinding
import com.omerfarukasil.hw1.model.SweepStakes
import com.omerfarukasil.hw1.system.SweepStakeSys

class CreateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateBinding
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var addSubjectBinding: AddSubjectBinding
    private lateinit var customDialogAddSubject: Dialog
    private var subject: SweepStakes? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        SweepStakeSys.prepare()
        reloadSubjects()

        binding.btnAdd.setOnClickListener(){
            createAddSubjectDialog()
            customDialogAddSubject.show()
        }

        binding.btnStart.setOnClickListener(){
            val intent = Intent(this@CreateActivity, MainActivity::class.java)
            subject = SweepStakeSys.getSweepStakeByName(binding.spinnerSubject.selectedItem.toString())
            intent.putExtra("subjectParcelable", subject)
            startActivity(intent)
            finish()
        }

    }

    private fun reloadSubjects(){
        binding.spinnerSubject.adapter = null
        val subjectList = SweepStakeSys.getSubjectList()
        adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, subjectList)
        binding.spinnerSubject.adapter = adapter
    }

    private fun createAddSubjectDialog(){

        addSubjectBinding = AddSubjectBinding.inflate(layoutInflater)
        customDialogAddSubject = Dialog(this).apply {
            setContentView(addSubjectBinding.root)
        }

        addSubjectBinding.applyBtn.setOnClickListener(){

            val name = addSubjectBinding.createSubject.text.toString()

            if(name.isNotEmpty()) {
                val newSubject = SweepStakes(sweepStakesId = 0, sweepStakeName = name)
                SweepStakeSys.oldSweepstakes.add(newSubject)
                reloadSubjects()
                customDialogAddSubject.dismiss()
            } else {
                Snackbar.make(it, "You have to fill text blank!", Snackbar.LENGTH_SHORT).show()
            }
        }

    }
}