package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.FragmentKalkulatorBinding
import net.objecthunter.exp4j.ExpressionBuilder

class KalkulatorFragment : Fragment() {

    private var _binding: FragmentKalkulatorBinding? = null
    private val binding get() = _binding!!
    private var input = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentKalkulatorBinding.inflate(inflater, container, false)
        val b = binding

        val angka = listOf(
            b.btn0, b.btn1, b.btn2, b.btn3, b.btn4,
            b.btn5, b.btn6, b.btn7, b.btn8, b.btn9
        )

        angka.forEach { btn ->
            btn.setOnClickListener {
                input += btn.text
                b.txtInput.text = input
            }
        }

        b.btnTambah.setOnClickListener { tambahOperator("+") }
        b.btnKurang.setOnClickListener { tambahOperator("-") }
        b.btnKali.setOnClickListener { tambahOperator("*") }
        b.btnBagi.setOnClickListener { tambahOperator("/") }
        b.btnTitik.setOnClickListener { tambahOperator(".") }

        b.btnKuadrat.setOnClickListener {
            input += "^2"
            b.txtInput.text = input
        }

        b.btnAkar.setOnClickListener {
            input += "sqrt("
            b.txtInput.text = input
        }

        b.btnC.setOnClickListener {
            input = ""
            b.txtInput.text = ""
            b.txtHasil.text = getString(R.string.nol)
        }

        b.btnHapus.setOnClickListener {
            if (input.isNotEmpty()) {
                input = input.dropLast(1)
                b.txtInput.text = input
            }
        }

        b.btnSamaDengan.setOnClickListener { hitungHasil() }

        return b.root
    }

    private fun tambahOperator(op: String) {
        if (input.endsWith(")")) {
            input += op
        } else if (input.contains("sqrt(") && input.last() in '0'..'9') {
            input += ")$op"
        } else {
            input += op
        }
        binding.txtInput.text = input
    }

    private fun hitungHasil() {
        try {
            var ekspresi = input
                .replace("÷", "/")
                .replace("×", "*")

            val buka = ekspresi.count { it == '(' }
            val tutup = ekspresi.count { it == ')' }
            if (buka > tutup) {
                ekspresi += ")".repeat(buka - tutup)
            }

            val expression = ExpressionBuilder(ekspresi).build()
            val hasil = expression.evaluate()
            binding.txtHasil.text = hasil.toString()
        } catch (_: Exception) {
            binding.txtHasil.text = getString(R.string.error)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
