package com.example.ddbc.project.ui.historyParcels

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ddbc.R
import com.example.ddbc.project.data.Parcel
import com.example.ddbc.project.data.ParcelType
import com.example.ddbc.project.model.MyViewModel
import com.example.ddbc.project.ui.friendsParcels.FriendParcelRecyclerViewAdapter

class HistoryParcelsRecyclerViewAdapter(private val parcelList: ArrayList<Parcel>):
    RecyclerView.Adapter<HistoryParcelsRecyclerViewAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val parcelWeight: TextView = itemView.findViewById(R.id.parcelWeight)
        val parcelContent: TextView = itemView.findViewById(R.id.parcelContent)
        val parcelType: TextView = itemView.findViewById(R.id.parcelType)
        val parcelDeliverer: TextView = itemView.findViewById(R.id.parcelDeliverer)
        val deliveredTime: TextView = itemView.findViewById(R.id.deliveredTime)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryParcelsRecyclerViewAdapter.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.history_parcel_item,parent,false)
        return HistoryParcelsRecyclerViewAdapter.MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: HistoryParcelsRecyclerViewAdapter.MyViewHolder, position: Int) {
        val currentItem = parcelList[position]
        holder.parcelWeight.text = currentItem.weight
        holder.parcelContent.text = currentItem.content
        holder.parcelType.text = parcelTypeToHebrew(currentItem.type)
        holder.parcelDeliverer.text = currentItem.delivererName
        holder.deliveredTime.text = currentItem.receivedDate
    }

    override fun getItemCount(): Int {
        return parcelList.size
    }

    private fun parcelTypeToHebrew(type: ParcelType):String
    {
        var ret : String = ""
        when (type){
            ParcelType.ENVELOPE -> ret = "מעטפה"
            ParcelType.BIG -> ret = "גדולה"
            ParcelType.SMALL -> ret = "קטנה"
        }
        return ret
    }
}