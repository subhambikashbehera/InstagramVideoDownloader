package com.subhambikashsubhamgupta.instagramdownloader

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HistoryAdapter: RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>{

    var models= arrayListOf<HistoryModel>()
    var context:Context

    constructor(models: ArrayList<HistoryModel>, context: Context) : super() {
        this.models = models
        this.context = context
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.historyitem, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
       var list=models[position]
        holder.title.text = list.title
        holder.link.text =list.link
    }

    override fun getItemCount(): Int = models.size


    inner class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var  title = itemView.findViewById<TextView>(R.id.titletv)
        var link = itemView.findViewById<TextView>(R.id.linktv)
        var share = itemView.findViewById<Button>(R.id.share)
        var play = itemView.findViewById<Button>(R.id.play)

    }
}