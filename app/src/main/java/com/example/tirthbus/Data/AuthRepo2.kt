package com.example.tirthbus.Data

import kotlinx.coroutines.flow.Flow

interface AuthRepo2<T> {
    fun register(auth: T): Flow<ResultState<String>>

    fun login(auth: T): Flow<ResultState<String>>

    fun logout(auth: T): Flow<ResultState<String>>

    fun addAuthDetail(user: T): Flow<ResultState<String>>

    fun resetPassword(auth: T): Flow<ResultState<String>>
    fun checkUserLoggedIn(): Boolean
}