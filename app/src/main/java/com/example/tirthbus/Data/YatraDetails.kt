package com.example.tirthbus.Data

data class EntityDetailsResponse<T>(
    var entity: T?,
    var key: String? = "",
)

data class YatraDetailsResponse(
    var yatra:Yatra?,
    var key:String? = "",
) {
    data class Yatra (
        // Basic Details
        val yatraTitle: String? = "",
        val departureDate: String? = "",
        val departureTime: String? = "",
        val arrivalDate: String? = "",
        val arrivalTime: String? = "",
        val departurePoint:String? = "",
        val imageUrl: String? = "",
        val yatraDescription: String? = "",
        val imageList:List<String?> = emptyList(),
        // Bus Details
        val busFacilities:List<String?> = emptyList(),
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