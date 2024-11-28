package com.omerfarukasil.hw1.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Applicant(
    var appId: Int,
    var sweepStakeId: Int,
    var name: String,
    var email: String,
    var phone: String,
    var age: Int,
    var gender: String): Parcelable {

    init {
        println("Applicant is created in $sweepStakeId sweepstake")
    }

    override fun toString(): String {
        return  "Name: $name\n" +
                "Email: $email\n" +
                "Phone: $phone\n" +
                "Age: $age\n" +
                "Gender: $gender"
    }
}