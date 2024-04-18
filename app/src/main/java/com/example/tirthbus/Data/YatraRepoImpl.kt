package com.example.tirthbus.Data

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

class YatraRepoImpl @Inject constructor(private val db : FirebaseFirestore,
                                        private val storage: FirebaseStorage) : YatraRepo {

    override fun addYatra(yatra: YatraDetailsResponse.Yatra): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)
        Log.d("Yatra","Trying to add list ")

        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid

        if (userId != null){

            val yatraWithUserId = yatra.copy(userId = userId)

            // Log the data before saving
            Log.d("Yatra", "Attempting to add $yatraWithUserId with userId $userId")

            db.collection("yatras")
                .add(yatraWithUserId)
                .addOnSuccessListener { Log.d("Yatra", "Yatra added successfully with ID: ${it.id}")
                    trySend(ResultState.Success("Yatra is added with ${it.id}")
                    ) }
                .addOnFailureListener{
                        exception ->
                    Log.e("YatraRepoImpl", "Exception in addYatra: ${exception.message}")
                    trySend(ResultState.Failure(exception))
                }
            Log.d("Yatra", "YatraName: ${yatra.yatraName}, YatraDate: ${yatra.date}")
            awaitClose {
                close()
            }
        }
    }

    override fun addYatra2 (yatra:YatraDetailsResponse.Yatra,uri:Uri,context: Context) : Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)

        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid
        var storageRef = storage.reference

        val unique_image_name = UUID.randomUUID().toString()
        var spaceRef: StorageReference

        if (userId != null){

            val yatraWithUserId = yatra.copy(userId = userId)
            //sbse pehle image upload hogi
            Log.d("Yatra", "Attempting to upload image with userId $userId")

            spaceRef = storageRef.child("images/$unique_image_name.jpg")

            val byteArray: ByteArray? =
                context.contentResolver.openInputStream(uri)?.use { it.readBytes() }

            byteArray?.let {
                var uploadTask = spaceRef.putBytes(byteArray)
                uploadTask.continueWithTask{task -> if (!task.isSuccessful){
                    task.exception?.let { throw it }
                }
                    spaceRef.downloadUrl.addOnCompleteListener { task -> if (task.isSuccessful){
                        val downloadurl = task.result
                        val imageUrl = downloadurl.toString()
                        Log.d("Yatra","Image uploaded successfully")
                        yatraWithUserId.copy(imageUrl = imageUrl)
                    }
                    }.addOnFailureListener { exception -> Log.e("Yatra","Failed to add image url in firestore")
                        trySend(ResultState.Failure(exception))}}
            }?.addOnFailureListener { exception ->
                Log.e("YatraRepoImpl", "Exception in addYatra: ${exception.message}")
                trySend(ResultState.Failure(exception))
            }

            // ab yatra add krenge
            Log.d("Yatra", "Attempting to add $yatraWithUserId with userId $userId")

            db.collection("yatras")
                .add(yatraWithUserId)
                .addOnSuccessListener { Log.d("Yatra", "Yatra added successfully with ID: ${it.id}")
                    trySend(ResultState.Success("Yatra is added with ${it.id}")
                    ) }
                .addOnFailureListener{
                        exception ->
                    Log.e("YatraRepoImpl", "Exception in addYatra: ${exception.message}")
                    trySend(ResultState.Failure(exception))
                }
            Log.d("Yatra", "YatraName: ${yatra.yatraName}, YatraDate: ${yatra.date}")
            awaitClose{
                close()
            }
        }
    }

    override fun getYatraByUserId(): Flow<ResultState<List<YatraDetailsResponse>>> = callbackFlow {
        trySend(ResultState.Loading)

        val currentUser = FirebaseAuth.getInstance().currentUser

        if (currentUser != null){
            val userId = currentUser.uid

            Log.d("Yatra","Attempting to fetch $currentUser and $userId")
            db.collection("yatras")
                .whereEqualTo("userId",userId)
                .get()
                .addOnSuccessListener {
                        querySnapshot -> val yatraList = querySnapshot.documents.map { document -> val data = document.data
                    YatraDetailsResponse(
                        yatra = YatraDetailsResponse.Yatra(
                            yatraName = data?.get("yatraName") as String?,
                            date = data?.get("yatraDate") as String?,
                            yatraTime = data?.get("yatraTime") as String?,
                            yatraLocation =  data?.get("yatraLocation") as String?,
                            userId = data?.get("userId") as String?,
                        ),
                        key = document.id
                    )
                }
                    trySend(ResultState.Success(yatraList))
                }
                .addOnFailureListener{
                        exception -> trySend(ResultState.Failure(exception))
                }
        }else{
            trySend(ResultState.Failure(Exception("User not logged in")))
            Log.d("main","User not logged in")
        }

        awaitClose{
            close()
        }
    }

    override fun getAllYatras(): Flow<ResultState<List<YatraDetailsResponse>>> = callbackFlow {
        trySend(ResultState.Loading)
        db.collection("yatras")
            .get()
            .addOnSuccessListener { val yatrasList = it.map {
                    data -> YatraDetailsResponse(
                yatra = YatraDetailsResponse.Yatra(
                    yatraName =data["yatraName"] as String?,
                    date = data["yatraDate"] as String?,
                    yatraTime = data["yatraTime"] as String?,
                    yatraLocation = data["yatraLocation"] as String?
                ),
                key = data.id
            )
            }
                trySend(ResultState.Success(yatrasList))
            }.addOnFailureListener{
                trySend(ResultState.Failure(it))
            }
        awaitClose{
            close()
        }
    }
    /*override fun getTopDestinations(): Flow<ResultState<List<TopDestinations>>> = callbackFlow {
        trySend(ResultState.Loading)
        Log.d("Search","Repo function loading")

        db.collection("topDestinations")
            .get()
            .addOnSuccessListener {
                val topDestUrlList = it.map {
                    data -> TopDestinations(
                        imageURLs = data?.get("topDestinationUrls") as? List<String?> ?: emptyList(),
                    )
                }
                trySend(ResultState.Success(topDestUrlList))
                Log.d("Top","Top destination Url list fetched successfully of size ${topDestUrlList.size}")
            }.addOnFailureListener{
                trySend(ResultState.Failure(it))
                Log.d("Top","Top destination Url list fetched Unsuccessful")
            }
        awaitClose{
            close()
        }
    }*/

    override fun fetchYatraDetail(yatraId: String): Flow<ResultState<YatraDetailsResponse>> = callbackFlow {
        trySend(ResultState.Loading)
        Log.d("Yatra","attempting to fetch yatra with id $yatraId")
        val yatraDocRef = db.collection("yatras").document(yatraId)

        yatraDocRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val data = documentSnapshot.data
                    val yatraDetails = YatraDetailsResponse(
                        yatra = YatraDetailsResponse.Yatra(
                            //Basic details
                            yatraName = data?.get("yatraName") as String?,
                           // arrivalDate = data?.get("arrivalDate") as String?,
                            date = data?.get("date") as String?,
                            yatraTime = data?.get("yatraTime") as String?,
                            yatraLocation = data?.get("yatraLocation") as String?,
                            imageUrl = data?.get("imageUrl") as String?,
                            info = data?.get("info") as
                            String?,
                            //Bus Detail
                            busType = data?.get("busType") as String?,
                            totalAmount = data?.get("totalAmount") as String?,
                            bookingAmount = data?.get("bookingAmount") as String?,
                            numberOfSeats = data?.get("numberOfSeats") as String?,
                            lastDateOfBooking = data?.get("lastDateOfBooking") as String?,
                            //Contact Details
                            organiserName = data?.get("organiserName") as String?,
                            contactName1 = data?.get("contactName1") as String?,
                            contactName2 = data?.get("contactName2") as String?,
                            contactPhn1 = data?.get("contactPhn1") as String?,
                            contactPhn2 = data?.get("contactPhn2") as String?,
                            paymentMethod = data?.get("paymentMethod") as String?,
                            //Includes/Rules
                            includesList = data?.get("includesList") as? List<String?> ?: emptyList(),
                            rulesList = data?.get("rulesList") as? List<String?> ?: emptyList(),
                            userId = data?.get("userId") as String?
                        ),
                        key = documentSnapshot.id
                    )
                    Log.d("Yatra","detail of yatra with id $yatraId fetched successfully with details $yatraDetails")
                    trySend(ResultState.Success(yatraDetails))
                } else {
                    trySend(ResultState.Failure(Exception("Yatra document does not exist")))
                }
            }
            .addOnFailureListener { exception ->
                trySend(ResultState.Failure(exception))
            }

        awaitClose {
            close()
        }
    }

    override fun getTopDestinations2(): Flow<ResultState<List<String?>>> = callbackFlow {
        trySend(ResultState.Loading)
        Log.d("Search", "Repo function loading")

        db.collection("topDestinations")
            .document("TMrOFak0rw3jIFZPEN5E\n") // Replace with your document ID
            .get()
            .addOnSuccessListener { document ->
                val imageURLs = document?.get("topDestinationUrls") as? List<String>
                if (imageURLs != null) {
                    trySend(ResultState.Success(imageURLs))
                    Log.d("Top", "Top destination list fetched successfully of size ${imageURLs.size}")
                } else {
                    trySend(ResultState.Failure(Exception("No image URLs found")))
                    Log.d("Top", "No image URLs found")
                }
            }
            .addOnFailureListener { exception ->
                trySend(ResultState.Failure(exception))
                Log.d("Top", "Top destination list fetched Unsuccessful")
            }

        awaitClose {
            close()
        }
    }

    /*override fun getAllYatrasPaging(): Flow<PagingData<YatraDetailsResponse>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                prefetchDistance = 3,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {YatraPagingSource(db)}
        ).flow
    }*/

    override fun getAllYatrasPaging(searchQuery: String): Flow<PagingData<YatraDetailsResponse>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                prefetchDistance = 3,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {YatraPagingSource(db,searchQuery)}
        ).flow
    }

    override fun uploadImage2(yatra: YatraDetailsResponse.Yatra,uri: Uri, context: Context, type: String): Flow<ResultState<String>> = callbackFlow {

        trySend(ResultState.Loading)

        var storageRef = storage.reference

        val unique_image_name = UUID.randomUUID().toString()
        var spaceRef: StorageReference

        if (type == "image") {
            spaceRef = storageRef.child("images/$unique_image_name.jpg")
            Log.d("Yatra", "Attempting to add $unique_image_name image")
        } else {
            spaceRef = storageRef.child("videos/$unique_image_name.mp4")
        }

        val byteArray: ByteArray? =
            context.contentResolver.openInputStream(uri)?.use { it.readBytes() }

        byteArray?.let {
            var uploadTask = spaceRef.putBytes(byteArray)
            uploadTask.continueWithTask{task -> if (!task.isSuccessful){
                task.exception?.let { throw it }
            }
                spaceRef.downloadUrl}.addOnCompleteListener {
                    task -> if (task.isSuccessful){
                val downloadUri = task.result
                val imageUrl = downloadUri.toString()
                Log.d("Yatra","Image uploaded successfully")
                addYatra(yatra = yatra.copy(imageUrl = imageUrl))
                trySend(ResultState.Success(imageUrl))
            }
            }/*addOnSuccessListener {
                    Log.d("Main", "image uploaded successfully ")
                    Toast.makeText(context,"Image uploaded successfully",Toast.LENGTH_SHORT).show()
                    //Getting the download URL for the uploaded image
                    /* spaceRef.downloadUrl.addOnSuccessListener{downloadUrl -> val imageUrl = downloadUrl.toString()
                            //Adding/Saving imageUrl in firestore
                                addYatra(yatra,imageUrl)*/

                    trySend(ResultState.Success("Image uploaded successfully"))
                }*/
                .addOnFailureListener { exception -> Log.e("Yatra","Failed to add image url in firestore")
                    trySend(ResultState.Failure(exception))}

        }?.addOnFailureListener { exception ->
            Log.e("YatraRepoImpl", "Exception in addYatra: ${exception.message}")
            trySend(ResultState.Failure(exception))
        } ?: kotlin.run {
            Log.e("YatraRepoImpl", "Failed to read bytes from URI")
            trySend(ResultState.Failure(Exception("Failed to read bytes from URI")))
        }
        awaitClose {}
    }

    override fun updateYatra(yatra: YatraDetailsResponse): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)

        val map = HashMap<String,Any>()
        map["yatraName"] = yatra.yatra?.yatraName!!
        map["departureDate"] = yatra.yatra!!.date!!
        //map["arrivalDate"] = yatra.yatra!!.arrivalDate!!
        map["yatraTime"] = yatra.yatra!!.yatraTime!!
        map["yatraLocation"] = yatra.yatra!!.yatraLocation!!

        db.collection("yatras")
            .document(yatra.key!!)
            .update(map)
            .addOnCompleteListener{
                if (it.isSuccessful)
                    trySend(ResultState.Success("Yatra updated successfully ..."))
            }
            .addOnFailureListener{trySend(ResultState.Failure(it))}

        awaitClose{
            close()
        }
    }

    override fun getTandC(): Flow<ResultState<List<String>>> = callbackFlow {
        try {
            Log.d("Yatra", "attempting to fetch TandC with id ")
            val yatraDocRef = db.collection("TandC").document("TandC/koOx62Z7K3DY24YwDEDe")
            // Retrieve document data
            yatraDocRef.get().addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val termsAndConditionsList = documentSnapshot.toObject<List<String>>()
                        ?: emptyList()
                    trySend(ResultState.Success(termsAndConditionsList))
                } else {
                    Log.d("TC","Document does not exist")
                    trySend(ResultState.Failure(Exception("Document does not exist")))
                }
            }.addOnFailureListener { exception ->
                trySend(ResultState.Failure(exception))
            }
            awaitClose()
        } catch (e: Exception) {
            trySend(ResultState.Failure(e))
        }
    }

}

private const val PAGE_SIZE = 20

class YatraPagingSource @Inject constructor(private val db: FirebaseFirestore,
    private val searchQuery:String) : PagingSource<DocumentSnapshot, YatraDetailsResponse>(){

    override suspend fun load(params: LoadParams<DocumentSnapshot>): LoadResult<DocumentSnapshot, YatraDetailsResponse> {
        return try {
            val currentPageSnapshot = params.key ?: db.collection("yatras")
                .orderBy("yatraName", Query.Direction.DESCENDING)
                .limit(PAGE_SIZE.toLong())
                .get()
                .await()

            val currentPage = currentPageSnapshot as QuerySnapshot

            val filteredYatras = currentPage.documents.filter { document ->
                val yatraName = document.getString("yatraName") ?: ""
                yatraName.contains(searchQuery, ignoreCase = true)
            }

            val yatras = filteredYatras.map{
                    document -> YatraDetailsResponse(
                yatra = YatraDetailsResponse.Yatra(
                    yatraName = document.getString("yatraName"),
                    date = document.getString("date"),
                    totalAmount = document.getString("totalAmount"),
                    organiserName = document.getString("organiserName"),
                    yatraTime = document.getString("yatraTime"),
                    yatraLocation = document.getString("yatraLocation"),
                    userId = document.getString("userId"),
                    imageUrl = document.getString("imageUrl")
                ),
                key = document.id
            )
            }
            val nextPage = currentPage.documents.lastOrNull()

            LoadResult.Page(
                data = yatras,
                prevKey = null,
                nextKey = nextPage
            )
        }
        catch (e:Exception){
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<DocumentSnapshot, YatraDetailsResponse>): DocumentSnapshot? {
        TODO("Not yet implemented")
    }
}