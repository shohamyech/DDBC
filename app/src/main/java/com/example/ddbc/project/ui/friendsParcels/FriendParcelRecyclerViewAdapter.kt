package com.example.ddbc.project.ui.friendsParcels

import android.location.Location
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ddbc.project.data.Parcel
import com.example.ddbc.R
import com.example.ddbc.project.model.MyViewModel

class FriendParcelRecyclerViewAdapter(private val parcelList: ArrayList<Parcel>, private val model:MyViewModel):
    RecyclerView.Adapter<FriendParcelRecyclerViewAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val recipientName:TextView = itemView.findViewById(R.id.recipientName)
        val recipientPhone:TextView = itemView.findViewById(R.id.recipientPhone)
        val recipientAddress:TextView = itemView.findViewById(R.id.recipientAddress)
        val parcelWeight:TextView = itemView.findViewById(R.id.parcelWeight)
        val deliverParcelButton: Button = itemView.findViewById(R.id.deliverParcel_Button)
        val statusMassage:TextView = itemView.findViewById(R.id.statusMessage)
        val distance:TextView = itemView.findViewById(R.id.parcelDistance)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.friend_parcel_item,parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = parcelList[position]
        holder.recipientName.text = currentItem.recipient
        holder.recipientPhone.text = currentItem.phone
        holder.recipientAddress.text = currentItem.address
        holder.parcelWeight.text = currentItem.weight
        holder.distance.text = String.format("%.2f",model.distanceInKm(currentItem.latitude.toDouble(), currentItem.longitude.toDouble(),
            model.myLocation.latitude, model.myLocation.longitude))
        if (currentItem.chosenDeliverer?.id.equals(model.user.id)){
            holder.statusMassage.text = "אושר לשליחה!"
            holder.statusMassage.visibility = View.VISIBLE
        }
        else if (currentItem.delivers.contains(model.user)){
            holder.statusMassage.text = "ממתין לאישור"
            holder.statusMassage.visibility = View.VISIBLE
        }
        else{
            holder.deliverParcelButton.visibility = View.VISIBLE
        }
        holder.deliverParcelButton.setOnClickListener {
            model.addUserToParcelDelivers(currentItem.id)
            it.visibility = View.GONE
            holder.statusMassage.text = "ממתין לאישור"
            holder.statusMassage.visibility = View.VISIBLE
        }
    }

    override fun getItemCount(): Int {
        return parcelList.size
    }


}