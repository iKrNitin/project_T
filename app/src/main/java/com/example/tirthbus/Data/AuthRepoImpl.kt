package com.example.tirthbus.Data

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthEmailException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.UUID
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AuthRepoImpl @Inject constructor(
    private val authDb : FirebaseAuth,
    private val db: FirebaseFirestore,
    private val storage:FirebaseStorage
) : AuthRepo{

    private lateinit var onVerificationCode:String
    override fun createUserWithPhone(phone: String,activity: Activity ): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)
        Log.d("Auth","Trying to authenticate with phone number")

        val onVerificationCallback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                TODO("Not yet implemented")
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                trySend(ResultState.Failure(p0))
                Log.d("Auth","OTP not Sent Successfully")
            }

            override fun onCodeSent(verificationCode: String, p1: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(verificationCode, p1)
                trySend(ResultState.Success("OTP Sent Successfully"))
                Log.d("Auth","OTP Sent Successfully")
                onVerificationCode = verificationCode
            }

        }

        val options = PhoneAuthOptions.newBuilder(authDb)
            .setPhoneNumber("+91$phone")
            .setTimeout(60L,TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(onVerificationCallback)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
        awaitClose{
            close()
        }
    }

    override fun signWithOtp(otp: String): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)
        val credential = PhoneAuthProvider.getCredential(onVerificationCode,otp)
        authDb.signInWithCredential(credential)
            .addOnCompleteListener{
                if (it.isSuccessful){
                    trySend(ResultState.Success("otp verified"))
                }
            }.addOnFailureListener{
                trySend(ResultState.Failure(it))
            }
        awaitClose {
            close()
        }
    }

    override fun createUser(auth: UserDetailResponse.User): Flow<ResultState<String>> = callbackFlow {
        Log.d("main","create user function called ")
        trySend(ResultState.Loading)

        if (auth.userEmail != null && auth.userEmailPassword != null && auth.userName != null){

        authDb.createUserWithEmailAndPassword(auth.userEmail!!,auth.userEmailPassword!!).addOnCompleteListener {
            if (it.isSuccessful) {
                addUser(auth)
                trySend(ResultState.Success("User  created Successfully"))
                Log.d("main", "current user id: ${authDb.currentUser?.uid}")
            } else {
                Log.d("main", "SignUp Failed")
                val exception = it.exception
                if (exception is FirebaseAuthWeakPasswordException) {
                    val reason = exception.reason
                    Log.d("main", "weak password")
                } else if (exception is FirebaseAuthInvalidCredentialsException) {
                    Log.d("main", "invalid credentials")
                } else if (exception is FirebaseAuthUserCollisionException) {
                    Log.d("main", "user collision")
                } else if (exception is FirebaseAuthInvalidUserException) {
                    Log.d("main", "invalid user")
                } else if (exception is FirebaseAuthEmailException) {
                    Log.d("main", "email exception")
                } else {
                    Log.d("main", "pta nhi kya dikkat hai")
                }
            }
        }
            .addOnFailureListener {
                trySend(ResultState.Failure(it))
            }
        }else{
            trySend(ResultState.Failure(Exception("Email or password is null")))
        }

        awaitClose{
            close()
        }
    }

    override fun loginUser(auth: UserDetailResponse.User): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)

        if (auth.userEmail != null && auth.userEmailPassword != null){

            authDb.signInWithEmailAndPassword(auth.userEmail!!,auth.userEmailPassword!!).addOnCompleteListener {
                if (it.isSuccessful) {
                    trySend(ResultState.Success("User  logged in Successfully"))
                    Log.d("main", "current user id: ${authDb.currentUser?.uid}")
                } else {
                    Log.d("main", "Login Failed")
                    val exception = it.exception
                    if (exception is FirebaseAuthWeakPasswordException) {
                        val reason = exception.reason
                        Log.d("main", "weak password")
                    } else if (exception is FirebaseAuthInvalidCredentialsException) {
                        Log.d("main", "invalid credentials")
                    } else if (exception is FirebaseAuthUserCollisionException) {
                        Log.d("main", "user collision")
                    } else if (exception is FirebaseAuthInvalidUserException) {
                        Log.d("main", "invalid user")
                    } else if (exception is FirebaseAuthEmailException) {
                        Log.d("main", "email exception")
                    } else {
                        Log.d("main", "pta nhi kya dikkat hai")
                    }
                }
            }
                .addOnFailureListener {
                    trySend(ResultState.Failure(it))
                }
        }else{
            trySend(ResultState.Failure(Exception("Email or password is null")))
        }

        awaitClose{
            close()
        }
    }

    override fun logoutUser(auth: UserDetailResponse.User): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)

        try {
            Firebase.auth.signOut()
            trySend(ResultState.Success("User logged out successfully"))
        }
        catch (e:Exception){
            trySend(ResultState.Failure(e))
        }
        awaitClose{
            close( )
        }
    }

    override fun resetPassword(auth: UserDetailResponse.User): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)

        if (auth.userEmail!!.isNotEmpty()){
            Firebase.auth.sendPasswordResetEmail(auth.userEmail).addOnCompleteListener{
                trySend(ResultState.Success(
                    "link sent successfully"
                ))
                Log.d("main","link sent successfully")
            }
                .addOnFailureListener{
                    trySend(ResultState.Failure(it))
                }
        }
        awaitClose{
            close()
        }
    }

    override fun resetOrganiserPasword(organiser: OrganiserDetailsResponse.Organiser): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)

        if (organiser.organiserEmail!!.isNotEmpty()){
            Firebase.auth.sendPasswordResetEmail(organiser.organiserEmail).addOnCompleteListener{
                trySend(ResultState.Success(
                    "link sent successfully"
                ))
                Log.d("Auth","link sent successfully")
            }
                .addOnFailureListener{
                    trySend(ResultState.Failure(it))
                }
        }
        awaitClose{
            close()
        }
    }

    override fun addUser(
        user: UserDetailResponse.User
    ): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)
        Log.d("User","Trying to add User")

        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid

        if (userId != null){
            val userWithUserId = user.copy(userId = userId)
            Log.d("User","Attempting to add user with id $userId")

            db.collection("Users")
                .add(userWithUserId)
                .addOnSuccessListener {
                    documentReference ->
                    trySend(ResultState.Success("${documentReference.id}"))
                    db.collection("Users").document(documentReference.id).set(userWithUserId)
                }
                .addOnFailureListener{
                    exception -> trySend(ResultState.Failure(exception))
                }
            awaitClose{
                close()
            }
        }
    }

    override fun createOrganiser(organiser: OrganiserDetailsResponse.Organiser): Flow<ResultState<String>> = callbackFlow {
        Log.d("Auth","create organiser function called ")
        trySend(ResultState.Loading)

        if (organiser.organiserEmail != null && organiser.organiserEmailPassword != null && organiser.organiserGroupName != null){

            authDb.createUserWithEmailAndPassword(organiser.organiserEmail,organiser.organiserEmailPassword).addOnCompleteListener {
                if (it.isSuccessful) {
                    addOrganiser(organiser)
                    trySend(ResultState.Success("Organiser created Successfully"))
                    Log.d("Auth", "current organiser id: ${authDb.currentUser?.uid}")
                } else {
                    Log.d("Auth", "SignUp Failed")
                    val exception = it.exception
                    if (exception is FirebaseAuthWeakPasswordException) {
                        val reason = exception.reason
                        Log.d("Auth", "weak password")
                    } else if (exception is FirebaseAuthInvalidCredentialsException) {
                        Log.d("Auth", "invalid credentials")
                    } else if (exception is FirebaseAuthUserCollisionException) {
                        Log.d("Auth", "user collision")
                    } else if (exception is FirebaseAuthInvalidUserException) {
                        Log.d("Auth", "invalid user")
                    } else if (exception is FirebaseAuthEmailException) {
                        Log.d("Auth", "email exception")
                    } else {
                        Log.d("Auth", "pta nhi kya dikkat hai")
                    }
                }
            }
                .addOnFailureListener {
                    trySend(ResultState.Failure(it))
                }
        }else{
            trySend(ResultState.Failure(Exception("Email or password is null")))
        }

        awaitClose{
            close()
        }
    }

    override fun loginOrganiser(organiser: OrganiserDetailsResponse.Organiser): Flow<ResultState<String>> = callbackFlow {
        Log.d("Auth","create organiser function called ")
        trySend(ResultState.Loading)

        if (organiser.organiserEmail != null && organiser.organiserEmailPassword != null){

            authDb.signInWithEmailAndPassword(organiser.organiserEmail,organiser.organiserEmailPassword).addOnCompleteListener {
                if (it.isSuccessful) {
                    addOrganiser(organiser)
                    trySend(ResultState.Success("Organiser logged in Successfully"))
                    Log.d("Auth", "current organiser id: ${authDb.currentUser?.uid}")
                } else {
                    Log.d("Auth", "SignIn Failed")
                    val exception = it.exception
                    if (exception is FirebaseAuthWeakPasswordException) {
                        val reason = exception.reason
                        Log.d("Auth", "weak password")
                    } else if (exception is FirebaseAuthInvalidCredentialsException) {
                        Log.d("Auth", "invalid credentials")
                    } else if (exception is FirebaseAuthUserCollisionException) {
                        Log.d("Auth", "user collision")
                    } else if (exception is FirebaseAuthInvalidUserException) {
                        Log.d("Auth", "invalid user")
                    } else if (exception is FirebaseAuthEmailException) {
                        Log.d("Auth", "email exception")
                    } else {
                        Log.d("Auth", "pta nhi kya dikkat hai")
                    }
                }
            }
                .addOnFailureListener {
                    trySend(ResultState.Failure(it))
                }
        }else{
            trySend(ResultState.Failure(Exception("Email or password is null")))
        }

        awaitClose{
            close()
        }
    }
    override fun logoutOrganiser(organiser: OrganiserDetailsResponse.Organiser): Flow<ResultState<String>>  = callbackFlow {
        trySend(ResultState.Loading)

        try {
            Firebase.auth.signOut()
            trySend(ResultState.Success("Organiser logged out successfully"))
        }
        catch (e:Exception){
            trySend(ResultState.Failure(e))
        }
        awaitClose{
            close( )
        }
    }

    override fun addOrganiser(organiser: OrganiserDetailsResponse.Organiser): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)
        Log.d("Auth","Trying to add Organiser")

        val currentOrganiser = FirebaseAuth.getInstance().currentUser
        val organiserId = currentOrganiser?.uid

        if (organiserId != null){
            val organiserWithOrganiserId= organiser.copy(organiserId = organiserId)
            Log.d("Auth","Attempting to add organiser with id $organiserId")

            db.collection("Organisers")
                .add(organiserWithOrganiserId)
                .addOnSuccessListener {
                        documentReference ->
                    trySend(ResultState.Success("${documentReference.id}"))
                    db.collection("Organisers").document(documentReference.id).set(organiserWithOrganiserId)
                }
                .addOnFailureListener{
                        exception -> trySend(ResultState.Failure(exception))
                }
            awaitClose{
                close()
            }
        }
    }

    override fun uploadUserProfile(
        user: UserDetailResponse.User,
        uri: Uri,
        context: Context,
        type: String
    ): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)

        var storageRef = storage.reference
        val unique_image_name = UUID.randomUUID() .toString()
        var spaceRef: StorageReference

        if (type == "image"){
            spaceRef = storageRef.child(
                "userProfiles/$unique_image_name.jpg"
            )
        }
        else {
            spaceRef = storageRef.child("videos/$unique_image_name.mp4")
        }

        val byteArray: ByteArray? =
            context.contentResolver.openInputStream(uri)?.use { it.readBytes() }

        byteArray?.let {
            var uploadTask = spaceRef.putBytes(byteArray)
            uploadTask.continueWithTask{task -> if (!task.isSuccessful){
                task.exception?.let { throw it }
            }
                spaceRef.downloadUrl}.addOnCompleteListener { task -> if (task.isSuccessful){
                val downloadUri = task.result
                val imageUrl = downloadUri.toString()
                Log.d("Yatra","Image uploaded successfully")
                addUser(user = user.copy(userProfileUrl = imageUrl))
                trySend(ResultState.Success(imageUrl))
            }
            }

        }?.addOnFailureListener { exception -> Log.e("Yatra","Failed to add image url in firestore")
            trySend(ResultState.Failure(exception))}
            ?: kotlin.run {
                Log.e("YatraRepoImpl", "Failed to read bytes from URI")
                trySend(ResultState.Failure(Exception("Failed to read bytes from URI")))
            }
        awaitClose {}
    }
    /*override fun fetchUserDetail(userDocId: String): Flow<ResultState<UserDetail>> = callbackFlow {
        trySend(ResultState.Loading)
        Log.d("User","attempting to fetch yatra with id $userDocId")
        val UserDocRef = db.collection("Users").document(userDocId)

        UserDocRef.get()
            .addOnSuccessListener {
                documentSnapshot -> if (documentSnapshot.exists()){
                    val data = documentSnapshot.data
                val userDetails = UserDetail(user = UserDetail.User(
                    userName = data?.get("userName") as String?,
                    userEmail = data?.get("userEmail") as String?,
                    userEmailPassword = data?.get("userEmailPassword") as String?,
                    userProfileUrl = data?.get("userProfileUrl") as String?,
                    userPhnNumber = data?.get("userPhnNumber") as String?,
                ),
                    key = documentSnapshot.id)
            }
            }
            .addOnFailureListener { exception ->
                trySend(ResultState.Failure(exception))
            }
        awaitClose {
            close()
        }
    }*/

    override fun fetchUserDetail(): Flow<ResultState<List<UserDetailResponse>>> = callbackFlow {
        trySend(ResultState.Loading)

        val currentUser = FirebaseAuth.getInstance().currentUser

        if (currentUser != null){
            val userId = currentUser.uid

            Log.d("User","Attempting to fetch $currentUser and $userId")

            db.collection("Users")
                .whereEqualTo("userId",userId)
                .get()
                .addOnSuccessListener {
                    querySnapshot -> val userData = querySnapshot.documents.map { document -> val data = document.data
                    UserDetailResponse(
                        user = UserDetailResponse.User(
                            userName = data?.get("userName") as String?,
                            userEmail = data?.get("userEmail") as String?
                        ),
                        key = document.id
                    )
                    }
                    trySend(ResultState.Success(userData))
                }
                .addOnFailureListener{
                    exception -> trySend(
                        ResultState.Failure(exception)
                    )
                }
        }
        else{
            trySend(ResultState.Failure(Exception("User not logged in")))
            Log.d("User","User detail not fetched")
        }

        awaitClose{
            close()
        }
    }

    override fun checkUserLoggedIn(): Boolean {
        return authDb.currentUser != null
    }
}