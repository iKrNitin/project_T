package com.example.tirthbus.Data

data class OrganiserDetailsResponse(
    var user:Organiser?,
    var key:String? = "")
{
    data class Organiser(
        val organiserId: String? = "",
        val organiserEmail:String? = "",
        val organiserEmailPassword:String? = "",
        val organiserGroupName:String? = "",
        val organiserName:String? = "",
        val organiserPhnNumber:String? = "",
        val organiserPhnNumber2:String? = "",
        val organiserAddress:String? = "",
    )
}