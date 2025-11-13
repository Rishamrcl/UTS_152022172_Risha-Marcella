package com.example.myapplication

import android.app.DatePickerDialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.*
import androidx.fragment.app.Fragment
import java.text.SimpleDateFormat
import java.util.*

class BiodataFragment : Fragment() {

    private lateinit var spinnerGender: Spinner
    private lateinit var iconDropdown: ImageView
    private lateinit var radioGroupStatus: RadioGroup
    private lateinit var radioSingle: RadioButton
    private lateinit var radioMarried: RadioButton
    private lateinit var edtTglLahir: EditText
    private val PREFS_NAME = "UserPrefs"
    private val KEY_GENDER = "gender"
    private val KEY_STATUS = "status"
    private val KEY_TGL_LAHIR = "tgl_lahir"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_biodata, container, false)
        val prefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        spinnerGender = view.findViewById(R.id.spinnerGender)
        iconDropdown = view.findViewById(R.id.iconDropdownGender)
        radioGroupStatus = view.findViewById(R.id.radioGroupStatus)
        radioSingle = view.findViewById(R.id.radioSingle)
        radioMarried = view.findViewById(R.id.radioMarried)
        edtTglLahir = view.findViewById(R.id.edtTglLahir)

        val options = resources.getStringArray(R.array.jenis_kelamin_array).toList()
        val adapter = object : ArrayAdapter<String>(requireContext(), R.layout.spinner_item, options) {
            override fun isEnabled(position: Int) = position != 0
            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val v = super.getDropDownView(position, convertView, parent) as TextView
                v.setBackgroundColor(Color.WHITE)
                v.setTextColor(if (position == 0) Color.GRAY else Color.parseColor("#112D4E"))
                return v
            }
        }
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        spinnerGender.adapter = adapter

        val savedGender = prefs.getString(KEY_GENDER, null)
        if (savedGender != null) {
            val position = options.indexOf(savedGender)
            if (position >= 0) spinnerGender.setSelection(position)
        } else spinnerGender.setSelection(0)

        val rotateUp = RotateAnimation(
            0f, -180f,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        ).apply {
            duration = 200
            fillAfter = true
        }

        val rotateDown = RotateAnimation(
            -180f, 0f,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        ).apply {
            duration = 200
            fillAfter = true
        }

        spinnerGender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, v: View?, position: Int, id: Long) {
                if (position != 0) prefs.edit().putString(KEY_GENDER, options[position]).apply()
                iconDropdown.startAnimation(rotateDown)
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        spinnerGender.setOnTouchListener { _, _ ->
            iconDropdown.startAnimation(rotateUp)
            false
        }

        val savedStatus = prefs.getString(KEY_STATUS, null)
        if (savedStatus != null) {
            when (savedStatus) {
                "Single" -> radioGroupStatus.check(R.id.radioSingle)
                "Married" -> radioGroupStatus.check(R.id.radioMarried)
            }
        }

        radioGroupStatus.setOnCheckedChangeListener { _, checkedId ->
            val selectedStatus = when (checkedId) {
                R.id.radioSingle -> "Single"
                R.id.radioMarried -> "Married"
                else -> null
            }
            selectedStatus?.let { prefs.edit().putString(KEY_STATUS, it).apply() }
        }

        val radioButtons = listOf(radioSingle, radioMarried)
        val states = arrayOf(
            intArrayOf(android.R.attr.state_checked),
            intArrayOf(-android.R.attr.state_checked)
        )
        val colors = intArrayOf(
            Color.parseColor("#00519D"),
            Color.parseColor("#112D4E")
        )
        val colorStateList = ColorStateList(states, colors)
        radioButtons.forEach { it.buttonTintList = colorStateList }

        val savedTgl = prefs.getString(KEY_TGL_LAHIR, null)
        if (savedTgl != null) edtTglLahir.setText(savedTgl)

        edtTglLahir.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePicker = DatePickerDialog(requireContext(),
                { _, y, m, d ->
                    val selectedCalendar = Calendar.getInstance()
                    selectedCalendar.set(y, m, d)
                    val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    val dateStr = format.format(selectedCalendar.time)
                    edtTglLahir.setText(dateStr)
                    prefs.edit().putString(KEY_TGL_LAHIR, dateStr).apply()
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.show()
        }

        return view
    }
}
