package com.omerfarukasil.hw1.system

import com.omerfarukasil.hw1.model.Applicant
import com.omerfarukasil.hw1.model.SweepStakes

object SweepStakeSys {

    var oldWinnerList: ArrayList<Applicant> = ArrayList()
    var oldSweepstakes: ArrayList<SweepStakes> = ArrayList()
    var newSweepStakeList: ArrayList<Applicant> = ArrayList()

    fun prepare(){

        oldWinnerList.add(Applicant(3, 10000, "Jane Doe", "jane@doe.com", "(555)5555555", 32, "F"))
        oldWinnerList.add(Applicant(56, 10001, "John Doe", "john@doe.com", "(111)1111111", 46, "M"))
        oldWinnerList.add(Applicant(21, 10002, "Jacob Smith", "jacob@smith.com", "(222)2222222", 12, "M"))
        oldWinnerList.add(Applicant(67, 10003, "Bilbo Baggins", "bilbo@baggins.com", "(666)6666666", 125, "M"))

        oldSweepstakes.add(SweepStakes(10000, "Christmas"))
        oldSweepstakes.add(SweepStakes(10001, "Thanks Giving"))
        oldSweepstakes.add(SweepStakes(10002, "Ramadan"))
        oldSweepstakes.add(SweepStakes(10003, "Graduation"))

    }

    fun findApplicantFromNewListApps(name: String): Applicant?{
        return newSweepStakeList.find { it.name.equals(name, ignoreCase = true) }
    }

    fun getSubjectList(): ArrayList<String>{
        val nameList: ArrayList<String> = ArrayList()
        for (sweepstake in oldSweepstakes) {
            nameList.add(sweepstake.sweepStakeName)
        }
        return nameList
    }

    fun getSweepStakeByName(name: String): SweepStakes? {
        return oldSweepstakes.find { it.sweepStakeName == name }
    }

    fun chooseOne(): Applicant{
        return newSweepStakeList.random()
    }

}