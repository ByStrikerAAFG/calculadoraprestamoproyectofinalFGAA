package com.alancaps.calculadoraprestamo

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val etMonto: EditText = findViewById(R.id.etMonto)
        val spPlazo: Spinner = findViewById(R.id.spPlazo)
        val btnCalcular: Button = findViewById(R.id.btnCalcular)
        val tvResultado: TextView = findViewById(R.id.tvResultado)

        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.seleccione_plazo,  
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spPlazo.adapter = adapter

        val idioma = Locale.getDefault().language

        btnCalcular.setOnClickListener {
            val montoStr = etMonto.text.toString()

            if (montoStr.isEmpty()) {
                val mensaje = if (idioma == "es") {
                    "Por favor ingrese el monto"
                } else {
                    "Please enter the amount"
                }
                Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val monto = montoStr.toDouble()

            val plazoSeleccionado = spPlazo.selectedItem.toString()

            val (tasaInteres, plazoEnMeses) = when (plazoSeleccionado) {
                "3 meses (10% interés)" -> 0.10 to 3
                "6 meses (25% interés)" -> 0.25 to 6
                "1 año (35% interés)" -> 0.35 to 12
                "3 months (10% interest)" -> 0.10 to 3
                "6 months (25% interest)" -> 0.25 to 6
                "1 year (35% interest)" -> 0.35 to 12
                else -> 0.0 to 0
            }

            val totalInteres = monto * tasaInteres
            val totalPagar = monto + totalInteres


            val resultado = if (idioma == "es") {
                """
                    Monto prestado: $%.2f
                    Tasa de interés: %.2f%%
                    Plazo: %d meses
                    Total a pagar: $%.2f
                """.trimIndent().format(monto, tasaInteres * 100, plazoEnMeses, totalPagar)
            } else {
                """
                    Loan amount: $%.2f
                    Interest rate: %.2f%%
                    Term: %d months
                    Total to pay: $%.2f
                """.trimIndent().format(monto, tasaInteres * 100, plazoEnMeses, totalPagar)
            }

            tvResultado.text = resultado
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
