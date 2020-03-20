package com.example.animefinder

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity

class AboutFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_about, container, false)
        val thanksText: TextView = view.findViewById(R.id.about_thanks)
        thanksText.movementMethod = LinkMovementMethod.getInstance()
        return view
    }

    companion object {
        fun newInstance(): AboutFragment = AboutFragment()
    }
}