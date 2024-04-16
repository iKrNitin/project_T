package com.example.tirthbus.ui.theme.User.User.Screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.tirthbus.AppBottomBar
import com.example.tirthbus.AppTopBar
import com.example.tirthbus.R
import com.example.tirthbus.ui.theme.Navigation.NavigationDestination

object UserBookingsDestination:NavigationDestination{
    override val route: String
        get() = "user_bookings"
    override val titleRes: Int
        get() = R.string.user_bookings

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserBookingScreen(
    navigateToHomeScreen:() ->Unit,
){
    Scaffold(
        topBar = {
            AppTopBar(title = "My Bookings",
                canNavigateBack = false,
                navigateToSignUpScreen = { /*TODO*/ },
            )
        },
        bottomBar = {
            AppBottomBar(
                onHomeClick = {navigateToHomeScreen()},
                onAccountCircleClick = {},
                onBookingsClick = {}
            )
        }
    ) {innerpadding ->
                val appScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
              Text(text = "This is user booking screen", modifier = Modifier.padding(innerpadding))

    }
}