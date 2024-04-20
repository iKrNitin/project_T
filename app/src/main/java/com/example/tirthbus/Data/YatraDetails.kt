package com.example.tirthbus.Data

import androidx.compose.ui.graphics.vector.ImageVector

data class EntityDetailsResponse<T>(
    var entity: T? ,
    var key: String? = "",
)

data class YatraDetailsResponse(
    var yatra:Yatra? ,
    var key:String? = "",
) {
    data class Yatra (
        // Basic Details
        val yatraName: String? = "",
        val date: String? = "",
        //val arrivalDate: String? = "",
        val yatraTime: String? = "",
        val yatraLocation: String? = "",
        val imageUrl: String? = "",
        val info: String? = "",
        // Bus Details
        val busType: String? = "",
        val busType2:List<String?> = emptyList(),
        val numberOfSeats: String? = "",
        val totalAmount: String? = "",
        val bookingAmount: String? = "",
        val lastDateOfBooking: String? = "",
        // Includes/Rules
        val userId: String? = "",
        val includesList:List<String?> = emptyList(),
        val rulesList:List<String?> = emptyList(),
        // Contact Details
        val organiserName: String? = "",
        val contactName1: String? = "",
        val contactPhn1: String? = "",
        val contactName2: String? = "",
        val contactPhn2: String? = "",
        val paymentMethod: String? = ""
    )
}

/*data class UserDetail(
    var user:User?,
    var key:String? = "")
{
    data class User(
        val userId: String? = "",
        val userName:String? = "",
        val userEmail:String? = "",
        val userEmailPassword:String? = "",
        val userPhnNumber:String? = ""
    )
}*/

