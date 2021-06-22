package com.subhambikashsubhamgupta.instagramdownloader

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HistoryAdapter(var models: ArrayList<HistoryModel>) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {



    class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView
        var link: TextView
        var share: Button
        var play: Button

        init {
            title = itemView.findViewById(R.id.titletv)
            link = itemView.findViewById(R.id.linktv)
            share = itemView.findViewById(R.id.share)
            play = itemView.findViewById(R.id.play)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.historyitem, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.title.text = models?.get(position)?.getTitle()
        holder.link.text = models?.get(position)?.getLink()
    }

    override fun getItemCount(): Int = models.size

}