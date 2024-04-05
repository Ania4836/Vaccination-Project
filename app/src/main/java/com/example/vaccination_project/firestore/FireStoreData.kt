package com.example.vaccination_project.firestore



data class FireStoreData(
    var email: String = "",
    var selNumb: List<Int>?=null,
    var drawNumb: List<Int>?=null,
    var win: Double = 0.0
    )
