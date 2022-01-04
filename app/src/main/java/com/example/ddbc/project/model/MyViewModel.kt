package com.example.ddbc.project.model

import android.content.Context
import android.location.Location
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ddbc.project.data.Parcel
import com.example.ddbc.project.data.Repository
import com.example.ddbc.project.data.Status
import com.example.ddbc.project.data.User
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MyViewModel : ViewModel() {

    private var _parcels = MutableLiveData<List<Parcel>>(ArrayList<Parcel>())
    private val _user = MutableLiveData<User>(User())
    val parcels : LiveData<List<Parcel>> = _parcels
    val isSigned = MutableLiveData<Boolean>(false)
    val user : User get() = _user.value!!
    lateinit var myLocation: Location

    init {
        Repository.fetchParcels(_parcels)
    }

    fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        if (result.resultCode == AppCompatActivity.RESULT_OK) {

            // Successfully signed in
            val firebase_user = FirebaseAuth.getInstance().currentUser
            firebase_user?.uid?.let { user.id = it }
            firebase_user?.displayName?.let { user.name = it}
            firebase_user?.email?.let { user.email = it }
            firebase_user?.phoneNumber?.let { user.phone = it }
            isSigned.value = true
        } else {
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            isSigned.value = false        }
    }

    fun signOut(context: Context) {
        AuthUI.getInstance()
            .signOut(context)
            .addOnCompleteListener {
                user.name = ""
                user.email = ""
                user.phone = ""
                isSigned.value = false
            }
    }

    fun getParcelsCloseToMyLocation() : ArrayList<Parcel>
    {
        if (isSigned.value!!)
            return ArrayList<Parcel>(_parcels.value!!.filter {
                //if the parcel is within 50 km
                distanceInKm(it.latitude.toDouble(), it.longitude.toDouble(), myLocation.latitude, myLocation.longitude) < 50 &&
                //and the parcel doesn't belong to the current user (only other users can deliver the parcel the the parcel owner)
                !(it.email == user.email || it.phone == user.phone) &&
                //the parcel is in the warehouse or was shipped and the only user who can see it is the chosen deliverer
                (it.status == Status.IN_WAREHOUSE || (it.status == Status.SHIPPED && it.chosenDeliverer?.id.equals(user.id)))
            })

        return ArrayList<Parcel>()
    }

    fun getUserParcels(): ArrayList<Parcel>
    {
        if (isSigned.value!!)
            return ArrayList<Parcel>(_parcels.value!!.filter {
                it.email.equals(user.email) || it.phone.equals(user.phone)
            })

        return ArrayList<Parcel>()
    }

    fun addUserToParcelDelivers(parcelId:String){
        val p:Parcel? = _parcels.value!!.find{it.id.equals(parcelId)}
        if (p!=null){
            p.delivers.add(user)
            Repository.updateParcel(p)
        }
    }

    fun setDelivererToParcel(parcel:Parcel, deliverer: String){
        val user:User? = parcel.delivers.find {
            it.name.equals(deliverer) || it.phone.equals(deliverer) }

        if (user != null){
            parcel.delivers.clear()
            parcel.chosenDeliverer = user
            parcel.status = Status.SHIPPED
            Repository.updateParcel(parcel)
        }
    }

    suspend fun getHistoryParcels() = Repository.fetchHistory()

    //moving a parcel from firebase to be saved in room
    fun moveParcelToHistory(parcel: Parcel){
        Repository.removeParcel(parcel)
        parcel.status = Status.DELIVERED
        GlobalScope.launch {
            Repository.saveToHistory(parcel)
        }
    }

    fun distanceInKm(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val theta = lon1 - lon2
        var dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta))
        dist = Math.acos(dist)
        dist = rad2deg(dist)
        dist = dist * 60 * 1.1515
        dist = dist * 1.609344
        if (dist.isNaN())
            return 0.0
        return dist
    }
    private fun deg2rad(deg: Double) = deg * Math.PI / 180.0
    private fun rad2deg(rad: Double) = rad * 180.0 / Math.PI

}