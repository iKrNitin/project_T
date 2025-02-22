package com.example.tirthbus.Data

import android.app.Activity
import android.content.Context
import android.net.Uri
import kotlinx.coroutines.flow.Flow

interface AuthRepo {
    fun createUserWithPhone(phone:String,activity:Activity):Flow<ResultState<String>>

    fun signWithOtp(otp:String):Flow<ResultState<String>>
    fun createUser(auth:UserDetailResponse.User,): Flow<ResultState<String>>

    fun loginUser(auth: UserDetailResponse.User,): Flow<ResultState<String>>

    fun logoutUser(auth: UserDetailResponse.User): Flow<ResultState<String>>

    fun addUser(user: UserDetailResponse.User): Flow<ResultState<String>>

    fun createOrganiser(organiser:OrganiserDetailsResponse.Organiser): Flow<ResultState<String>>

    fun loginOrganiser(organiser: OrganiserDetailsResponse.Organiser): Flow<ResultState<String>>

    fun logoutOrganiser(organiser: OrganiserDetailsResponse.Organiser): Flow<ResultState<String>>

    fun addOrganiser(organiser: OrganiserDetailsResponse.Organiser): Flow<ResultState<String>>

    fun uploadUserProfile(user: UserDetailResponse.User, uri: Uri, context: Context, type:String): Flow<ResultState<String>>

   /* fun fetchUserDetail(userId:String):Flow<ResultState<UserDetail>>*/

    fun fetchUserDetail():Flow<ResultState<List<UserDetailResponse>>>

    /*fun deleteUser(key:String): Flow<ResultState<String>>

    fun updateUser(user:UserDetail): Flow<ResultState<String>>*/

    fun resetPassword(auth: UserDetailResponse.User): Flow<ResultState<String>>

    fun resetOrganiserPasword(organiser: OrganiserDetailsResponse.Organiser) : Flow<ResultState<String>>

    fun checkUserLoggedIn():Boolean
}