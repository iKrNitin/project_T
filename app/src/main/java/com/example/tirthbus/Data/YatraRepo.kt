package com.example.tirthbus.Data

import android.content.Context
import android.net.Uri
import android.provider.ContactsContract.Data
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

interface YatraRepo {

    fun addYatra (yatra:YatraDetailsResponse.Yatra) : Flow<ResultState<String>>

    fun addYatra2 (yatra:YatraDetailsResponse.Yatra, uri: Uri, context: Context) : Flow<ResultState<String>>
    //fun addYatra (yatra:YatraDetailsResponse.Yatra)
    /*fun addYatra3 (yatra:YatraDetailsResponse.Preferences) : Flow<ResultState<String>>*/

    fun getYatraByUserId(): Flow<ResultState<List<YatraDetailsResponse>>>

    /*fun getYatraByYatraId(yatraId:String): Flow<ResultState<YatraDetailsResponse>>*/

    fun getAllYatras(): Flow<ResultState<List<YatraDetailsResponse>>>

    /*fun getTopDestinations():Flow<ResultState<List<TopDestinations>>>*/

    fun getTopDestinations2():Flow<ResultState<List<String?>>>

    fun getAllYatrasPaging(searchQuery:String): Flow<PagingData<YatraDetailsResponse>>

    fun fetchYatraDetail(yatraId:String): Flow<ResultState<YatraDetailsResponse>>

    fun uploadImage2(yatra: YatraDetailsResponse.Yatra, uri: Uri, context: Context, type:String): Flow<ResultState<String>>

    /*fun deleteYatra(key:String) : Flow<ResultState<String>>*/

    fun updateYatra(yatra:YatraDetailsResponse) : Flow<ResultState<String>>

    fun getTandC():Flow<ResultState<List<String>>>
}