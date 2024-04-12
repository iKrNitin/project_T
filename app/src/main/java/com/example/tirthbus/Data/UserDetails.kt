package com.example.tirthbus.Data

data class UserDetailResponse(
    var user:User?,
    var key:String? = "")
{
    data class User(
        val userId: String? = "",
        val userName:String? = "",
        val userEmail:String? = "",
        val userEmailPassword:String? = "",
        val userPhnNumber:String? = "",
        val userProfileUrl:String? = "",
        val userDocId:String? = ""
    )
}