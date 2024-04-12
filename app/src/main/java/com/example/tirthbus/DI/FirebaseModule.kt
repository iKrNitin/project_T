package com.example.tirthbus.DI

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule{

    @Provides
    @Singleton
    fun providesFirestoreDb() : FirebaseFirestore = Firebase.firestore

    @Provides
    @Singleton
    fun providesFirestoreAuth() :
            FirebaseAuth = Firebase.auth

    @Provides
    @Singleton
    fun providesFirebaseStorage():
            FirebaseStorage = Firebase.storage
}