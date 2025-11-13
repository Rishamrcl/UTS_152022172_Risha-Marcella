package com.example.myapplication

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment

class KontakFragment : Fragment() {

    private val contactIds = listOf(
        R.id.a1, R.id.a2, R.id.a3,
        R.id.b1,
        R.id.c1, R.id.c2,
        R.id.d1, R.id.d2, R.id.d3,
        R.id.e1, R.id.e2, R.id.e3,
        R.id.f1, R.id.f2, R.id.f3
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_kontak, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setContact(view, R.id.a1, R.string.a1_name, R.string.a1_phone)
        setContact(view, R.id.a2, R.string.a2_name, R.string.a2_phone)
        setContact(view, R.id.a3, R.string.a3_name, R.string.a3_phone)
        setContact(view, R.id.b1, R.string.b1_name, R.string.b1_phone)
        setContact(view, R.id.c1, R.string.c1_name, R.string.c1_phone)
        setContact(view, R.id.c2, R.string.c2_name, R.string.c2_phone)
        setContact(view, R.id.d1, R.string.d1_name, R.string.d1_phone)
        setContact(view, R.id.d2, R.string.d2_name, R.string.d2_phone)
        setContact(view, R.id.d3, R.string.d3_name, R.string.d3_phone)
        setContact(view, R.id.e1, R.string.e1_name, R.string.e1_phone)
        setContact(view, R.id.e2, R.string.e2_name, R.string.e2_phone)
        setContact(view, R.id.e3, R.string.e3_name, R.string.e3_phone)
        setContact(view, R.id.f1, R.string.f1_name, R.string.f1_phone)
        setContact(view, R.id.f2, R.string.f2_name, R.string.f2_phone)
        setContact(view, R.id.f3, R.string.f3_name, R.string.f3_phone)

        val searchInput = view.findViewById<EditText>(R.id.inputSearch)
        val tvNoResult = view.findViewById<TextView>(R.id.tvNoResult)

        searchInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString().trim().lowercase()
                var visibleCount = 0
                for (id in contactIds) {
                    val includeView = view.findViewById<View>(id)
                    val nameView = includeView.findViewById<TextView>(R.id.txtName)
                    val nameText = nameView.text.toString().lowercase()
                    if (nameText.contains(query)) {
                        includeView.visibility = View.VISIBLE
                        visibleCount++
                    } else {
                        includeView.visibility = View.GONE
                    }
                }
                tvNoResult.visibility = if (visibleCount == 0) View.VISIBLE else View.GONE
            }
        })
    }

    private fun setContact(root: View, includeId: Int, nameRes: Int, phoneRes: Int) {
        val includeView = root.findViewById<View>(includeId)
        val nameView = includeView.findViewById<TextView>(R.id.txtName)
        val phoneView = includeView.findViewById<TextView>(R.id.txtPhone)
        nameView.text = getString(nameRes)
        phoneView.text = getString(phoneRes)
    }
}
