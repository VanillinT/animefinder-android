package com.example.animefinder

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import org.w3c.dom.Text

class AnimeListAdapter(private val context: Context,
                       private val dataSource: List<AnimeTitle>): BaseAdapter() {
    private val inflater:LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return dataSource.size
    }

    override fun getItem(position: Int): Any {
        return dataSource[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val rowView =  inflater.inflate(R.layout.anime_list_item, parent, false)

        val originalName = rowView.findViewById(R.id.original_name) as TextView
        val englishName = rowView.findViewById(R.id.english_name) as TextView
        val probabilityNum = rowView.findViewById(R.id.probability) as TextView
        val episodeNum = rowView.findViewById(R.id.episode_number) as TextView

        val title = getItem(position) as AnimeTitle

        if(title.title_native != "") originalName.text = title.title_native else  originalName.text = ""
        if(title.title_english != "") englishName.text = title.title_english else englishName.text = ""
        probabilityNum.text = "%.0f".format(title.similarity*100) + "%"
        if(title.episode != "") episodeNum.text = title.episode else episodeNum.text = "–"

        rowView.setOnClickListener {
            val url = "https://anilist.co/anime/" + title.anilist_id
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(intent)
        }

        return rowView
    }
}