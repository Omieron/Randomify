package com.omerfarukasil.hw1

import android.app.Dialog
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet.Layout
import com.google.android.material.snackbar.Snackbar
import com.omerfarukasil.hw1.databinding.ActivityMainBinding
import com.omerfarukasil.hw1.databinding.AppliciantCardBinding
import com.omerfarukasil.hw1.databinding.EditApplicantCardBinding
import com.omerfarukasil.hw1.model.Applicant
import com.omerfarukasil.hw1.model.SweepStakes
import com.omerfarukasil.hw1.system.SweepStakeSys
import com.omerfarukasil.hw1.view.ResultActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var applicantCardBinding: AppliciantCardBinding
    private lateinit var editApplicantCard: EditApplicantCardBinding
    private lateinit var customDialog: Dialog
    private var customSweepStakeId: Int = 0
    private var customUserId: Int = 1
    private lateinit var userObj: Applicant
    private var nameOfSubject: String = ""
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private var resultUser : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val subject = intent.getParcelableExtra<SweepStakes>("subjectParcelable")
        subject?.let {
            customSweepStakeId = it.sweepStakesId
            nameOfSubject = it.sweepStakeName}
        createToastMessage("$nameOfSubject is started")
        reloadData()

        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if(result.resultCode == RESULT_OK){
                resultUser = result.data?.getStringExtra("resultUser")
                resultUser?.let { finishDialog(it) }
            }
        }

        binding.addApplicant.setOnClickListener(){
            createUserApplicantDialog()
        }

        binding.cancelBtn.setOnClickListener(){
            createAlertDialog()
        }

        binding.drawLot.setOnClickListener(){
            if(SweepStakeSys.newSweepStakeList.isNotEmpty()){
                val intent = Intent(this@MainActivity, ResultActivity::class.java)
                val luckyGuy = SweepStakeSys.chooseOne()
                intent.putExtra("luckyGuy", luckyGuy)
                intent.putExtra("concept", subject)
                resultLauncher.launch(intent)
            }
            else{
                Snackbar.make(it, "At least add one applicant", Snackbar.LENGTH_SHORT).show()
            }
        }

    }

    private fun createUserApplicantDialog() {
        // Yeni bir dialog nesnesi oluştur
        applicantCardBinding = AppliciantCardBinding.inflate(layoutInflater)
        val createDialog = Dialog(this@MainActivity).apply {
            setContentView(applicantCardBinding.root)
        }

        applicantCardBinding.addBtn.setOnClickListener {
            val names = applicantCardBinding.nameTxt.text.toString()
            val email = applicantCardBinding.mailTxt.text.toString()
            val phone = applicantCardBinding.phoneTxt.text.toString()
            val age = applicantCardBinding.ageTxt.text.toString().toIntOrNull() ?: 0
            val gender: String = if (applicantCardBinding.womenBtn.isChecked) "F" else "M"

            if (names.isNotEmpty()) {
                val app = Applicant(
                    appId = customUserId,
                    sweepStakeId = customSweepStakeId,
                    name = names,
                    email = email,
                    phone = phone,
                    age = age,
                    gender = gender
                )
                SweepStakeSys.newSweepStakeList.add(app)
                customUserId++
                reloadData()
                createDialog.dismiss()
                createToastMessage("$names is added to the list")
            } else {
                Snackbar.make(it, "You have to fill name text first!", Snackbar.LENGTH_SHORT).show()
            }
        }

        applicantCardBinding.cancelBtn.setOnClickListener {
            createDialog.dismiss()
        }

        createDialog.show()

    }


    private fun editUserApplicantCard() {

        editApplicantCard = EditApplicantCardBinding.inflate(layoutInflater)
        customDialog = Dialog(this@MainActivity).apply {
            setContentView(editApplicantCard.root)
        }


        editApplicantCard.nameTxt.setText(userObj.name)
        editApplicantCard.mailTxt.setText(userObj.email)
        editApplicantCard.phoneTxt.setText(userObj.phone)
        editApplicantCard.ageTxt.setText(userObj.age.toString())
        if (userObj.gender == "F")
            editApplicantCard.womenBtn.isChecked = true
        else
            editApplicantCard.manBtn.isChecked = true


        editApplicantCard.addBtn.setOnClickListener {
            val names = editApplicantCard.nameTxt.text.toString()
            val email = editApplicantCard.mailTxt.text.toString()
            val phone = editApplicantCard.phoneTxt.text.toString()
            val age = editApplicantCard.ageTxt.text.toString().toIntOrNull() ?: 0
            val gender = if (editApplicantCard.womenBtn.isChecked) "F" else "M"

            if (names.isNotEmpty()) {
                userObj.name = names
                userObj.email = email
                userObj.phone = phone
                userObj.age = age
                userObj.gender = gender

                reloadData()
                customDialog.dismiss()
                createToastMessage("${userObj.name} is updated")

            } else {
                Snackbar.make(it, "You have to fill name text first!", Snackbar.LENGTH_SHORT).show()
            }
        }

        editApplicantCard.deleteBtn.setOnClickListener(){
            SweepStakeSys.newSweepStakeList.removeAll { it.appId == userObj.appId }
            reloadData()
            customDialog.dismiss()
        }

        editApplicantCard.cancelBtn.setOnClickListener {
            customDialog.dismiss()
        }
    }

    private fun reloadData(){

        if (binding.personList.childCount > 1) {
            for (i in 1 until binding.personList.childCount) {
                binding.personList.removeViewAt(1)
            }
        }

        if(SweepStakeSys.newSweepStakeList.isEmpty()){
            val textView = TextView(this)
            textView.text = "There is no applicant right now"
            textView.textSize = 24f
            textView.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
            textView.typeface = Typeface.MONOSPACE
            binding.personList.addView(textView)
        } else {
            for (person in SweepStakeSys.newSweepStakeList) {
                // CREATE TEXTVIEW
                val textView = TextView(this)
                val str = "${person.appId}-) ${person.name}"
                textView.text = str
                textView.textSize = 24f
                textView.typeface = Typeface.MONOSPACE
                textView.setPadding(0, 8, 0, 8)

                // CREATE EDIT BUTTON
                val button = Button(this)
                button.text = "Edit"
                button.textSize = 18f
                button.setOnClickListener {
                    userObj = person
                    editUserApplicantCard()
                    customDialog.show()
                }

                // TextView and Button add to personListContainer
                binding.personList.addView(textView)
                binding.personList.addView(button)
            }
        }
    }

    private fun finishDialog(str : String){
        val finishAlertDialog = AlertDialog.Builder(this)

        finishAlertDialog.setTitle("Finish")
        finishAlertDialog.setMessage("$str\nThe application will restart")
        finishAlertDialog.setIcon(R.drawable.restart_icon)

        finishAlertDialog.setPositiveButton("OK") { _, _ ->
            customUserId = 1
            SweepStakeSys.newSweepStakeList = ArrayList()
            SweepStakeSys.oldSweepstakes = ArrayList()
            finish()
        }

        finishAlertDialog.setCancelable(false)
        finishAlertDialog.create().show()
    }

    private fun createAlertDialog() {
        val quitAlertDialog = AlertDialog.Builder(this)

        quitAlertDialog.setTitle("Warning")
        quitAlertDialog.setMessage("Do you really want to quit?\nIf you leave, all data will be lost!")
        quitAlertDialog.setIcon(R.drawable.warning_icon)

        quitAlertDialog.setPositiveButton("OK") { _, _ ->
            customUserId = 1
            SweepStakeSys.newSweepStakeList = ArrayList()
            SweepStakeSys.oldSweepstakes = ArrayList()
            finish()
        }

        quitAlertDialog.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }

        quitAlertDialog.create().show()
    }

    private fun createToastMessage(str: String){
        Toast.makeText(this@MainActivity, str, Toast.LENGTH_SHORT).show()
    }

}