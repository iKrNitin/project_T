package com.example.tirthbus.ui.theme.User.User.Screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.tirthbus.R
import com.example.tirthbus.ui.theme.Navigation.NavigationDestination

object UserBookingsDestination:NavigationDestination{
    override val route: String
        get() = "user_bookings"
    override val titleRes: Int
        get() = R.string.user_bookings

}

@Composable
fun UserBookingScreen(){
    /*UserHomeScreen(
        navigateToOraganiser = { /*TODO*/ },
        navigateToYatraDetail ={} ,
        navigateToSignUpScreen = { /*TODO*/ },
        onAccountCircleClick = { /*TODO*/ },
        onBookingsClick = { /*TODO*/ },
        onHomeClick = { /*TODO*/ })*/
    Text(text = "This is user booking screen")

}