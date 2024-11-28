package com.omerfarukasil.hw1.view

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.omerfarukasil.hw1.MainActivity
import com.omerfarukasil.hw1.R
import com.omerfarukasil.hw1.databinding.ActivityEnteranceBinding
import com.omerfarukasil.hw1.system.SweepStakeSys

class EnteranceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEnteranceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityEnteranceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startTitleAnimation()

        binding.myButton.setOnClickListener(){
            val intent = Intent(this@EnteranceActivity, CreateActivity::class.java)

            startActivity(intent)
        }
    }

    private fun startTitleAnimation(){

        val background = binding.title.background as GradientDrawable

        val startColor = ContextCompat.getColor(this, R.color.startColor) // Başlangıç rengi
        val endColor = ContextCompat.getColor(this, R.color.endColor)     // Bitiş rengi

        // Animasyon oluşturun
        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), startColor, endColor).apply {
            duration = 1000 // Animasyon süresi
            repeatCount = ValueAnimator.INFINITE // Sonsuz döngü
            repeatMode = ValueAnimator.REVERSE  // Geri sarma modu

            // Renk değişimi sırasında güncelleme yap
            addUpdateListener { animator ->
                val animatedValue = animator.animatedValue as Int
                background.setColor(animatedValue) // solid rengini değiştir
            }
        }

        colorAnimation.start()
    }
}