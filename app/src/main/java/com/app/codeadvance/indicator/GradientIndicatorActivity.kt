package com.app.codeadvance.indicator

import android.animation.ObjectAnimator
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.app.codeadvance.databinding.ActivityGradientIndicatorBinding

class GradientIndicatorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGradientIndicatorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGradientIndicatorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ObjectAnimator.ofFloat(binding.gradientIndicator,"progress",0f,1f).setDuration(5000).start();
    }
}