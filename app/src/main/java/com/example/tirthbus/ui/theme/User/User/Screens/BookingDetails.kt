package com.example.tirthbus.ui.theme.User.User.Screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.tirthbus.AppBottomBar
import com.example.tirthbus.AppTopBar
import com.example.tirthbus.R
import com.example.tirthbus.ui.theme.Navigation.NavigationDestination

object BookingDetailsDestination:NavigationDestination{
    override val route: String
        get() = "booking_details"
    override val titleRes: Int
        get() = R.string.booking_details
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingDetailsScreen(){
    Scaffold(
        topBar = {
            AppTopBar(title = "Booking Details",
                canNavigateBack = true,
                navigateToSignUpScreen = { /*TODO*/ })
        },

    ){
        innerpadding -> val appBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
        Text(text = "This is booking Detail Screen",
            modifier = Modifier.padding(innerpadding))
    }
}