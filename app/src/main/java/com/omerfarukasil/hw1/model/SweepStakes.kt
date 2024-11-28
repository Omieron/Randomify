package com.omerfarukasil.hw1.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class SweepStakes(
    var sweepStakesId: Int,
    var sweepStakeName: String): Parcelable
{
    companion object{
        var idNo = 10004
    }

    init {
        sweepStakesId = idNo
        idNo++
        println("Sweepstake is created with name: $sweepStakeName")
    }

    override fun toString(): String {
        return  "Name: $sweepStakeName\n" +
                "Id: $sweepStakesId"
    }
}