package com.example.ddbc.project.data


import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

object Repository {

    private val database = Firebase.database
    private val parcelsRef = database.getReference("parcels")
    private var parcelDao: ParcelDao? = null

    //fetching parcels from data base and updating the live data with the new data
    fun fetchParcels(liveData: MutableLiveData<List<Parcel>>){
        parcelsRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                val parcels : List<Parcel> = snapshot.children.map{
                        it -> it.getValue(Parcel::class.java)!! }

                liveData.postValue(parcels)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    //updating a single parcel in the firebase
    fun updateParcel(parcel: Parcel?){
        if (parcel != null)
            parcelsRef.child(parcel.id).setValue(parcel)
    }

    //removing a single parcel from firebase
    fun removeParcel(parcel: Parcel) = parcelsRef.child(parcel.id).removeValue()

    //saving a parcel to the room database
    suspend fun saveToHistory(parcel: Parcel) = parcelDao!!.insert(parcel)

    //fetching all parcels from the room database
    suspend fun fetchHistory() = parcelDao!!.getAll()

    //initializing the room database with context of the main activity
    //(It's not the correct way of doing it.
    // The correct and really complex way: https://developer.android.com/codelabs/android-room-with-a-view-kotlin#0
    fun initRoomDatabase(context: Context){
        val db = Room.databaseBuilder(
            context,
            ParcelDatabase::class.java, "parcel-database"
        ).build()
        parcelDao = db.parcelDao()
    }
}