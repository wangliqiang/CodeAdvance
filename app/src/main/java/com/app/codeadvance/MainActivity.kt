package com.app.codeadvance

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.app.codeadvance.databinding.ActivityMainBinding
import com.app.codeadvance.flowlayout.FlowLayoutActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        clickEvent()
    }

    private fun clickEvent() {
        binding.flowBtn.setOnClickListener {
            startActivity(Intent(this, FlowLayoutActivity::class.java))
        }
    }
}