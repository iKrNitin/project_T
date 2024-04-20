package com.example.tirthbus

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import com.example.tirthbus.ui.theme.Organiser.Screens.AddList
import com.example.tirthbus.ui.theme.Organiser.Screens.AddYatraScreen1
import com.example.tirthbus.ui.theme.Organiser.Screens.AddYatraScreen2
import com.example.tirthbus.ui.theme.Organiser.Screens.AutoCompletePlacesScreen
import com.example.tirthbus.ui.theme.Organiser.Screens.GetCurrentLocation
import com.example.tirthbus.ui.theme.Organiser.Screens.MainScreen
import com.example.tirthbus.ui.theme.Organiser.Screens.TandCScreen
import com.example.tirthbus.ui.theme.Theme.TirthBusTheme
import com.example.tirthbus.ui.theme.User.User.Screens.SearchBar
import com.example.tirthbus.ui.theme.User.User.Screens.SearchBarSample
import com.example.tirthbus.ui.theme.User.User.Screens.SignUpLayout
import com.example.tirthbus.ui.theme.User.User.Screens.SignUpScreen
import com.example.tirthbus.ui.theme.User.User.Screens.UserHomeScreen
import com.example.tirthbus.ui.theme.User.User.Screens.UserProfileScreen
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.type.LatLng
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
   /* private lateinit var placesClient: PlacesClient
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private var locationRequired: Boolean = false

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        locationCallback?.let {
            val locationRequest = com.google.android.gms.location.LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY, 100
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        Places.initialize(applicationContext, "AIzaSyBw73OMOGz5xtZi-p6Ylr2NMNHZex9zHbc")
        placesClient = Places.createClient(this)*/
        setContent {
            var currentLocation by remember {
                mutableStateOf(com.google.android.gms.maps.model.LatLng(0.toDouble(), 0.toDouble()))
            }

            /*locationCallback = object : LocationCallback() {
                override fun onLocationResult(p0: LocationResult) {
                    super.onLocationResult(p0)
                    for (location in p0.locations) {
                        currentLocation = com.google.android.gms.maps.model.LatLng(
                            location.latitude,
                            location.longitude
                        )
                    }
                }
            }*/

            TirthBusTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    //ॐ श्री गणेशाय नम:
                    MyApp()
                    //LocationScreen(currentLocation = currentLocation, context = this@MainActivity)
                }
            }
        }
    }


    private val permissions = arrayOf(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION
    )

    /*@Composable
    fun LocationScreen(
        currentLocation: com.google.android.gms.maps.model.LatLng,
        context: Context
    ) {

        val launcherMultiplePermission =
            rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissionMaps ->
                val areGranted = permissionMaps.values.reduce { acc, next -> acc && next }
                if (areGranted) {
                    locationRequired = true
                    startLocationUpdates()
                    Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
                }
            }

        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Your location ${currentLocation.latitude}/${currentLocation.longitude}")
                Button(onClick = {
                    if (permissions.all {
                            ContextCompat.checkSelfPermission(
                                context,
                                itf
                            ) == PackageManager.PERMISSION_GRANTED
                        }) {
                        startLocationUpdates()
                    } else {
                        launcherMultiplePermission.launch(permissions)
                    }
                }) {
                    Text(text = "Get Current Location")
                }
            }
        }
    }*/
}