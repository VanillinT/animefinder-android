package com.example.animefinder

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.ArrayList

class AnimeListFragment : Fragment() {

    private lateinit var list: List<AnimeTitle>
    private lateinit var textview: TextView


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)

        val jsonList = arguments?.getString("listStr")
        val listType = object: TypeToken<List<AnimeTitle>>(){}.type
        list = Gson().fromJson(jsonList, listType)
        list = list.distinctBy { it.title_native }
        return inflater.inflate(R.layout.fragment_anime_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(list.size == 1) {
            val url = "https://anilist.co/anime/" + list[0].anilist_id
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context!!.startActivity(intent)
        }
        val listView = view.findViewById(R.id.animeList) as ListView
        val animeListAdapter = AnimeListAdapter(context!!, list)

        listView.adapter = animeListAdapter
    }

    companion object {
        fun newInstance(listStr: String): AnimeListFragment {
            val fragment = AnimeListFragment()
            val args = Bundle()
            args.putString("listStr", listStr)
            fragment.arguments = args
            return fragment
        }
    }
}