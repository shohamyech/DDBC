package com.example.ddbc.project.data

import androidx.databinding.BaseObservable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "parcels")
data class Parcel(@PrimaryKey var id:String = "") : BaseObservable() {
    var type: ParcelType = ParcelType.SMALL
    var isFragile:Boolean = false
    var weight:String = ""
    @Ignore var latitude: String = ""
    @Ignore var longitude: String = ""
    @Ignore var recipient:String = ""
    var content: String = ""
    @Ignore var address: String = ""
    @Ignore var phone: String = ""
    @Ignore var email: String = ""
    @Ignore var status: Status = Status.IN_WAREHOUSE
    @Ignore var delivers = ArrayList<User>()
    @Ignore var chosenDeliverer:User? = null
    var delivererName = ""
    var receivedDate: String = ""

}