package com.example.ddbc.project.ui.registeredParcels

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ddbc.R
import com.example.ddbc.project.data.Parcel
import com.example.ddbc.project.data.ParcelType
import com.example.ddbc.project.data.Status
import android.widget.ArrayAdapter
import android.widget.Button
import com.example.ddbc.project.model.MyViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class RegisteredParcelsRecyclerViewAdapter(private val parcelList: ArrayList<Parcel>,private val context: Context?, private val model: MyViewModel):
RecyclerView.Adapter<RegisteredParcelsRecyclerViewAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val parcelContent: TextView = itemView.findViewById(R.id.parcelContent)
        val parcelType: TextView = itemView.findViewById(R.id.parcelType)
        val parcelWeight: TextView = itemView.findViewById(R.id.parcelWeight)
        val parcelStatus: TextView = itemView.findViewById(R.id.parcelStatus)
        val deliversMsg: TextView = itemView.findViewById(R.id.delivers_msg)
        val noDeliversMsg: TextView = itemView.findViewById(R.id.noDeliversMsg)
        val deliversSpinner: Spinner = itemView.findViewById(R.id.delivers_spinner)
        val chooseDeliverButton: Button = itemView.findViewById(R.id.chooseDeliver_Button)
        val confirmParcelArrived: Button = itemView.findViewById(R.id.confirmParcelArrived_button)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.registered_parcel_item,parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = parcelList[position]
        val delivers = getParcelDelivers(currentItem)

        holder.parcelContent.text = currentItem.content
        holder.parcelType.text = parcelTypeToHebrew(currentItem.type)
        holder.parcelWeight.text = currentItem.weight
        holder.parcelStatus.text = parcelStatusToHebrew(currentItem.status)

        if (delivers.isNotEmpty()){
            val dataAdapter: ArrayAdapter<String> =
                ArrayAdapter<String>(context!!, android.R.layout.simple_spinner_item, delivers)
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
            holder.deliversSpinner.adapter = dataAdapter
            holder.deliversSpinner.visibility = View.VISIBLE
            holder.chooseDeliverButton.visibility = View.VISIBLE

        }else if (currentItem.status == Status.SHIPPED){
            holder.deliversMsg.visibility = View.GONE
            holder.confirmParcelArrived.visibility = View.VISIBLE

        }
        else{
            holder.noDeliversMsg.visibility = View.VISIBLE
        }

        holder.chooseDeliverButton.setOnClickListener {
            model.setDelivererToParcel(currentItem, holder.deliversSpinner.selectedItem.toString())
            holder.confirmParcelArrived.visibility = View.VISIBLE
        }

        holder.confirmParcelArrived.setOnClickListener {
            currentItem.delivererName = currentItem.chosenDeliverer!!.name + " " + currentItem.chosenDeliverer!!.phone
            currentItem.receivedDate = getCurrentDateTime().toString("yyyy/MM/dd HH:mm")
            model.moveParcelToHistory(currentItem)
        }
    }

    override fun getItemCount(): Int {
        return parcelList.size
    }

    private fun parcelTypeToHebrew(type:ParcelType):String
    {
        var ret : String = ""
        when (type){
            ParcelType.ENVELOPE -> ret = "מעטפה"
            ParcelType.BIG -> ret = "גדולה"
            ParcelType.SMALL -> ret = "קטנה"
        }
        return ret
    }
    private fun parcelStatusToHebrew(status:Status):String
    {
        var ret = ""
        when (status){
            Status.IN_WAREHOUSE -> ret = "בהמתנה למשלוח"
            Status.SHIPPED -> ret = "בדרך"
        }
        return ret
    }
    private fun getParcelDelivers(parcel: Parcel):List<String>{
        val ret = ArrayList<String>()
        for (v in parcel.delivers){
            if (v.name.isNotEmpty())
                ret.add(v.name)
            else
                ret.add(v.phone)
        }
        return ret
    }
    fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }

    fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }
}