package com.example.ddbc.project.data

import androidx.databinding.BaseObservable

data class User(var id:String = "") : BaseObservable(){
    var name:String = ""
    set(value) {
        field = value
        notifyChange()
    }
    var email:String=""
        set(value) {
            field = value
            notifyChange()
        }
    var phone:String=""
        set(value) {
            field = value
            notifyChange()
        }
}