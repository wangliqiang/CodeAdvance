package com.app.codeadvance.indicator

import android.animation.ObjectAnimator
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.app.codeadvance.databinding.ActivityGradientTextBinding

class GradientTextActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGradientTextBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGradientTextBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ObjectAnimator.ofFloat(binding.gradientIndicator, "progress", 0f, 1f).setDuration(5000)
            .start();
    }
}