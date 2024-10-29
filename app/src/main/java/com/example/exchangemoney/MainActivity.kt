package com.example.exchangemoney

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var sourceAmount: EditText
    private lateinit var targetAmount: EditText
    private lateinit var sourceCurrency: Spinner
    private lateinit var targetCurrency: Spinner

    // Giả lập tỷ giá (đơn giản hóa)
    private val exchangeRates = mapOf(
        "USD" to 1.0,
        "VND" to 23000.0,
        "EUR" to 0.85,
        "CAD" to 1.37,
        "CNY" to 7.30,
        "INR" to 83.0
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sourceAmount = findViewById(R.id.sourceAmount)
        targetAmount = findViewById(R.id.targetAmount)
        sourceCurrency = findViewById(R.id.sourceCurrency)
        targetCurrency = findViewById(R.id.targetCurrency)

        // Cài đặt dữ liệu cho Spinners
        val currencies = exchangeRates.keys.toList()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, currencies)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sourceCurrency.adapter = adapter
        targetCurrency.adapter = adapter

        // Lắng nghe thay đổi từ EditText và Spinner
        sourceAmount.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) { convertCurrency() }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        sourceCurrency.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                convertCurrency()
            }
        }

        targetCurrency.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                convertCurrency()
            }
        }
    }

    // Hàm chuyển đổi tiền tệ
    private fun convertCurrency() {
        val sourceCurrencyCode = sourceCurrency.selectedItem.toString()
        val targetCurrencyCode = targetCurrency.selectedItem.toString()

        // Đọc số tiền nguồn
        val amount = sourceAmount.text.toString().toDoubleOrNull() ?: return

        // Lấy tỷ giá chuyển đổi
        val sourceRate = exchangeRates[sourceCurrencyCode] ?: 1.0
        val targetRate = exchangeRates[targetCurrencyCode] ?: 1.0

        // Tính kết quả và hiển thị
        val convertedAmount = amount * (targetRate / sourceRate)
        targetAmount.setText(String.format("%.2f", convertedAmount))
    }
}
