package com.example.tirthbus.ui.theme.Organiser.Screens

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationRequest
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

/*private lateinit var fusedLocationClient:FusedLocationProviderClient
private lateinit var locationCallback: LocationCallback
private var locationRequired: Boolean = false*/

private val permissions = arrayOf(
    android.Manifest.permission.ACCESS_FINE_LOCATION,
    android.Manifest.permission.ACCESS_COARSE_LOCATION
)

/*@Composable
fun LocationScreen(currentLocation:LatLng,context:Context){

    val launcherMultiplePermission = rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){
        permissionMaps ->
        val areGranted = permissionMaps.values.reduce{acc, next -> acc && next }
        if (areGranted){
            locationRequired = true
            startLocationUpdates()
            Toast.makeText(context,"Permission Granted",Toast.LENGTH_SHORT).show()
        }
        else{
            Toast.makeText(context,"Permission Denied",Toast.LENGTH_SHORT).show()
        }
    }

    Box(modifier = Modifier.fillMaxSize()){
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Your location ${currentLocation.latitude}/${currentLocation.longitude}")
            Button(onClick = {
                if (permissions.all {
                    ContextCompat.checkSelfPermission(context,it) == PackageManager.PERMISSION_GRANTED
                    })
                {
                    startLocationUpdates()
                }
                else{
                    launcherMultiplePermission.launch(permissions)
                }
            }) {
                Text(text = "Get Current Location")
            }
        }
    }
}

@SuppressLint("MissingPermission")
private fun startLocationUpdates() {
    locationCallback?.let {
        val locationRequest = com.google.android.gms.location.LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,100
        )
            .setWaitForAccurateLocation(false)
            .setMinUpdateIntervalMillis(3000)
            .setMaxUpdateDelayMillis(100)
            .build()

        fusedLocationClient?.requestLocationUpdates(
            locationRequest,
            it,
            Looper.getMainLooper()
        )
    }
}*/


@Composable
fun AutoCompletePlacesScreen(placesClient: PlacesClient) {
    var query by remember { mutableStateOf(TextFieldValue()) }
    var places by remember { mutableStateOf<List<Place>>(emptyList()) }
    var selectedPlace by remember { mutableStateOf<Place?>(null) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = query,
            onValueChange = { query = it },
            label = { Text(text = "Enter a location") })
        AutocompletePlacesList2(
            places = places,
            placesClient = placesClient,
            query = query.text,
            onPlaceSelected = { place ->
                query = TextFieldValue(place.name ?: "")
                selectedPlace = place
            },
            onPlacesFetched = { newPlaces ->
                places = newPlaces
            }
        )
    }

    if (selectedPlace != null) {
        // Populate the text field with the selected place
        query = TextFieldValue(selectedPlace!!.address ?: "")
        // Reset the selected place
        selectedPlace = null
    }
}

@Composable
fun AutocompletePlacesList2(
    places: List<Place>,
    placesClient: PlacesClient,
    query: String,
    onPlacesFetched: (List<Place>) -> Unit,
    onPlaceSelected: (Place) -> Unit
) {
    Column {
        places.take(3).forEach { place ->
            Text(text = place.name ?: "", modifier = Modifier
                .padding(8.dp)
                .clickable { onPlaceSelected(place) })
            Text(text = place.address ?: "", modifier = Modifier.padding(8.dp))
            Divider()
        }
    }

    LaunchedEffect(query) {
        if (query.isNotEmpty()) {
            try {
                val response =
                    placesClient.findAutocompletePredictions(
                        FindAutocompletePredictionsRequest.newInstance(query)
                    ).await()

                val predictions = response.autocompletePredictions

                val fetchedPlaces = predictions.map { prediction ->
                    val placeId = prediction.placeId
                    val placeRequest = FetchPlaceRequest.newInstance(placeId, listOf(Place.Field.NAME, Place.Field.ADDRESS))
                    val fetchPlaceResponse = placesClient.fetchPlace(placeRequest).await()
                    fetchPlaceResponse.place
                }

                onPlacesFetched(fetchedPlaces)
            } catch (e: Exception) {
                // Handle exception (e.g., logging, showing error message)
                Log.e("AutoCompletePlaces", "Error fetching places: $e")
            }
        } else {
            // Clear the places list when query is empty
            onPlacesFetched(emptyList())
        }
    }
}

@Composable
fun PlaceItem(place: Place, onPlaceSelected: (Place) -> Unit) {
    // Define the UI for displaying information about the place
    // For example, you might display the place name and address
    Text(
        text = "${place.name}, ${place.address}",
        modifier = Modifier.clickable { onPlaceSelected(place) }
    )
}

@Composable
fun GetCurrentLocation(onLocationReceived: (Location) -> Unit) {
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    // Function to handle the result of the permission request
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // If permission is granted, request the last known location
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    location?.let {
                        onLocationReceived(location)
                    }
                }
                .addOnFailureListener { e ->
                    // Handle failure
                    Log.e(TAG, "Error getting location: $e")
                }
        } else {
            // Handle case where permission is not granted
            Log.d(TAG, "Location permission denied")
        }
    }

    LaunchedEffect(Unit) {
        // Check if location permissions are granted
        if (ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // If permissions are already granted, request the last known location
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    location?.let {
                        onLocationReceived(location)
                    }
                }
                .addOnFailureListener { e ->
                    // Handle failure
                    Log.e(TAG, "Error getting location: $e")
                }
        } else {
            // If permissions are not granted, request permission from the user
            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }
}

@Composable
fun LocationPermissionScreen(activity: ComponentActivity, onPermissionGranted: () -> Unit) {
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            onPermissionGranted()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Location Permission Required")

        Button(
            onClick = {
                requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
            }
        ) {
            Text("Request Location Permission")
        }
    }
}

@Composable
fun MainScreen(activity: ComponentActivity) {
    var hasLocationPermission by remember { mutableStateOf(false) }

    if (!hasLocationPermission) {
        LocationPermissionScreen(
            activity = activity,
            onPermissionGranted = { hasLocationPermission = true }
        )
    } else {
        // Display your main screen content here, including any UI components
        // that require access to the user's location

    }
}

/*@Composable
fun LocationPermissionScreen(onPermissionGranted: () -> Unit) {
    val permissionState = rememberPermissionState(android.Manifest.permission.ACCESS_FINE_LOCATION)

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Location Permission Required")

        Button(
            onClick = {
                if (permissionState.hasPermission) {
                    onPermissionGranted()
                } else {
                    permissionState.launchPermissionRequest()
                }
            }
        ) {
            Text("Request Location Permission")
        }
    }

    LaunchedEffect(permissionState) {
        if (permissionState.hasPermission) {
            onPermissionGranted()
        }
    }
}*/