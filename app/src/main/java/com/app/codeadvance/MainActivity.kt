package com.app.codeadvance

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.app.codeadvance.databinding.ActivityMainBinding
import com.app.codeadvance.flowlayout.FlowLayoutActivity
import com.app.codeadvance.indicator.GradientIndicatorActivity
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        clickEvent()
        jsonFormat()
    }

    private fun jsonFormat() {
        val jsonStr =
            "{\"merSn\":\"15133613919\",\"orderId\":\"T401161706164776309\",\"serialNumber\":\"26382484\",\"merOrderNo\":\"9824011695976281\",\"totalAmount\":0.01,\"refundTotalAmount\":0.00,\"orderStatus\":5,\"OrderStatusCn\":\"成功\"}"

        val gson = Gson()
        val itemData = gson.fromJson(jsonStr,ItemData::class.java)
        val str = gson.toJson(itemData)
        Log.e("JSON", "jsonFormat: $str")

    }

    private fun clickEvent() {
        binding.flowBtn.setOnClickListener {
            startActivity(Intent(this, FlowLayoutActivity::class.java))
        }
        binding.customIndicatorBtn.setOnClickListener {
            startActivity(Intent(this, GradientIndicatorActivity::class.java))
        }
    }
}

data class ItemData(
    val OrderStatusCn: String,
    val merOrderNo: String,
    val merSn: String,
    val orderId: String,
    val orderStatus: Int,
    val refundTotalAmount: Number,
    val serialNumber: String,
    val totalAmount: Number
)