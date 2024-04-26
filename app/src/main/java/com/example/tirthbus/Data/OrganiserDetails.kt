package com.example.tirthbus.Data

data class OrganiserDetailsResponse(
    var user:Organiser?,
    var key:String? = "")
{
    data class Organiser(
        val organiserId: String? = "",
        val organiserGroupName:String? = "",
        val organiserEmail:String? = "",
        val organiserEmailPassword:String? = "",
        val organiserPhnNumber:String? = "",
    )
}