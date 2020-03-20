package com.example.animefinder

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class AnimeListFragment : Fragment() {

    private lateinit var list: String
    private lateinit var textview: TextView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_favourites, container, false)
        val text = view!!.findViewById<TextView>(R.id.animeList)
        text.text = arguments?.getString("list")
    }

    companion object {
        fun newInstance(): AnimeListFragment = AnimeListFragment()
    }
}