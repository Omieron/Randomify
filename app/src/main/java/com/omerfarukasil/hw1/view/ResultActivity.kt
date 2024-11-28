package com.omerfarukasil.hw1.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.omerfarukasil.hw1.databinding.ActivityResultBinding
import com.omerfarukasil.hw1.model.Applicant
import com.omerfarukasil.hw1.model.SweepStakes

class ResultActivity : AppCompatActivity() {

    lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // SEEKBAR LOGİC
        val duration = 2000
        val maxProgress = 100
        binding.seekBar.max = maxProgress
        val handler = Handler(Looper.getMainLooper())
        var progress = 0
        val runnable = object : Runnable {
            override fun run() {
                if (progress <= maxProgress) {
                    binding.seekBar.progress = progress
                    progress++
                    binding.processPercentage.text = "THE CHOSEEN ONE\nLoading: %$progress"
                    handler.postDelayed(this, (duration / maxProgress).toLong())
                } else {
                    // SeekBar will gone, others will be showed
                    binding.processPercentage.visibility = View.GONE
                    binding.seekBar.visibility = View.GONE
                    binding.widgetContainer.visibility = View.VISIBLE


                }
            }
        }
        handler.post(runnable)

        val userData = intent.getParcelableExtra<Applicant>("luckyGuy")
        val subject = intent.getParcelableExtra<SweepStakes>("concept")
        var name: String? = null
        var concept: String? = null
        userData?.let {
           name = it.name
        }
        subject?.let {
            concept = it.sweepStakeName
        }
        val str = "Winner for $concept is $name"
        binding.resultView.text = str

        // BUTTON
        binding.backBtn.setOnClickListener{
            val intent = Intent()
            intent.putExtra("resultUser", str)
            setResult(RESULT_OK, intent)
            finish()
        }


    }

}