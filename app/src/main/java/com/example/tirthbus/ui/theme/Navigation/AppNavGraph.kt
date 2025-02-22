package com.example.tirthbus.ui.theme.Navigation

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.tirthbus.ui.theme.Organiser.Screens.AddYatra2Destination
import com.example.tirthbus.ui.theme.Organiser.Screens.AddYatra3Destination
import com.example.tirthbus.ui.theme.Organiser.Screens.AddYatraDestination
import com.example.tirthbus.ui.theme.Organiser.Screens.AddYatraScreen1
import com.example.tirthbus.ui.theme.Organiser.Screens.AddYatraScreen2
import com.example.tirthbus.ui.theme.Organiser.Screens.AddYatraScreen3
import com.example.tirthbus.ui.theme.Organiser.Screens.OrganiserHomeScreen
import com.example.tirthbus.ui.theme.Organiser.Screens.OrganiserHomeScreenDestination
import com.example.tirthbus.ui.theme.Organiser.Screens.OrganiserSignInScreen
import com.example.tirthbus.ui.theme.Organiser.Screens.OrganiserSignUpDestination
import com.example.tirthbus.ui.theme.Organiser.Screens.OrganiserSignUpScreen
import com.example.tirthbus.ui.theme.Organiser.Screens.OrganiserSigninDestination
import com.example.tirthbus.ui.theme.Organiser.ViewModel.YatraUiState
import com.example.tirthbus.ui.theme.User.User.Screens.BookingDetailsDestination
import com.example.tirthbus.ui.theme.User.User.Screens.BookingDetailsScreen
import com.example.tirthbus.ui.theme.User.User.Screens.SearchResultScreen
import com.example.tirthbus.ui.theme.User.User.Screens.SearchResultsDestination
import com.example.tirthbus.ui.theme.User.User.Screens.SearchScreen
import com.example.tirthbus.ui.theme.User.User.Screens.SearchScreenDestination
import com.example.tirthbus.ui.theme.User.User.Screens.SignInScreen
import com.example.tirthbus.ui.theme.User.User.Screens.SignInScreenDestination
import com.example.tirthbus.ui.theme.User.User.Screens.SignUpScreen
import com.example.tirthbus.ui.theme.User.User.Screens.SignUpScreenDestination
import com.example.tirthbus.ui.theme.User.User.Screens.UserBookingScreen
import com.example.tirthbus.ui.theme.User.User.Screens.UserBookingsDestination
import com.example.tirthbus.ui.theme.User.User.Screens.UserHomeScreen
import com.example.tirthbus.ui.theme.User.User.Screens.UserHomeScreenDestination
import com.example.tirthbus.ui.theme.User.User.Screens.UserProfileDestination
import com.example.tirthbus.ui.theme.User.User.Screens.UserProfileScreen
import com.example.tirthbus.ui.theme.User.User.Screens.YatraDetailScreen
import com.example.tirthbus.ui.theme.User.User.Screens.YatraDetailsScreenDestination
import com.google.firebase.firestore.auth.User
import com.google.gson.Gson

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
){
    NavHost(
        navController = navController,
        startDestination = UserHomeScreenDestination.route,
        //startDestination = OrganiserSignUpDestination.route,
        modifier = modifier
    ){

        composable(route = UserHomeScreenDestination.route){
            UserHomeScreen(
                navigateToOraganiserHomeScreen = {navController.navigate(OrganiserHomeScreenDestination.route)},
                navigateToOrganiserSignUpScreen = {navController.navigate(OrganiserSignUpDestination.route)},
                navigateToYatraDetail = {
                        yatraId -> navController.navigate("${YatraDetailsScreenDestination.route}/$yatraId")
                },
                navigateToSearchResult = {searchQuery -> navController.navigate("${SearchResultsDestination.route}/$searchQuery")},
                navigateToSignUpScreen = {navController.navigate(SignUpScreenDestination.route)},
                navigateToSearchScreen = {navController.navigate(SearchScreenDestination.route)},
                onHomeClick = {navController.navigate(UserHomeScreenDestination.route)},
                onBookingsClick = {navController.navigate(UserBookingsDestination.route)},
                onAccountCircleClick = {navController.navigate(UserProfileDestination.route)})
        }

        composable(route = SearchResultsDestination.route  + "/{searchQuery}",
            arguments = listOf(navArgument("searchQuery"){type = NavType.StringType})){
                backStackEntry ->
            val searchQuery = backStackEntry.arguments?.getString("searchQuery")
            searchQuery?.let {
                SearchResultScreen(searchQuery = searchQuery, navigateBack = { navController.popBackStack() },
                    navigateToYatraDetail = {yatraId -> navController.navigate("${YatraDetailsScreenDestination.route}/$yatraId")})
            }
        }
        
        composable(route = SearchScreenDestination.route){
            SearchScreen(navigateToSearchResult = {searchQuery -> navController.navigate("${SearchResultsDestination.route}/$searchQuery")})
        }

        composable(route = OrganiserHomeScreenDestination.route){
            OrganiserHomeScreen(navigateToUser = {navController.navigate(UserHomeScreenDestination.route)},
                navigateToAddYatra = {navController.navigate(AddYatraDestination.route)},
                navigateToOrganiserSignUp = {navController.navigate(OrganiserSignUpDestination.route)})
        }

        /*composable(route = AddYatraDestination.route){
            AddYatraScreen1(
                navigateBack = {navController.navigateUp()},
                navigateToOraganiser = {navController.navigate(OrganiserHomeScreenDestination.route)},
                navigateToAddYatra2 = { yatraUiState ->
                    val yatraUiStateJson = Gson().toJson(yatraUiState)
                    val route = "${AddYatra2Destination.route}/$yatraUiStateJson"
                    navController.navigate(route)}
            )
        }*/

        composable(route = AddYatraDestination.route) {
            AddYatraScreen1(
                navigateBack = { navController.navigateUp() },
                navigateToOraganiser = { navController.navigate(OrganiserHomeScreenDestination.route) },
                navigateToAddYatra2 = { yatraUiState, uri ->
                    val yatraUiStateJson = Uri.encode(Gson().toJson(yatraUiState))
                    val uriString = Uri.encode(uri.toString())
                    val route = "${AddYatra2Destination.route}/$yatraUiStateJson/$uriString"
                    navController.navigate(route)
                }
            )
        }


        /*composable(route = AddYatra2Destination.route + "/{yatraUiState}"){
            val yatraUiStateJson = it.arguments?.getString("yatraUiState")
            val yatraUiState = Gson().fromJson(yatraUiStateJson,YatraUiState::class.java)
            AddYatraScreen2(
                navigateBack = { navController.navigateUp() },
                navigateToAddYatra3 = { yatraUiState ->
                    val yatraUiStateJson = Gson().toJson(yatraUiState)
                    val route = "${AddYatra3Destination.route}/$yatraUiStateJson"
                    navController.navigate(route)},
                yatraUiState = yatraUiState
            )
        }*/

        composable(
            route = AddYatra2Destination.route + "/{yatraUiState}/{uri}",
            arguments = listOf(
                navArgument("yatraUiState") { type = NavType.StringType },
                navArgument("uri") { type = NavType.StringType }
            )
        ) {
            val yatraUiStateJson = it.arguments?.getString("yatraUiState")
            val yatraUiState = Gson().fromJson(Uri.decode(yatraUiStateJson), YatraUiState::class.java)
            val uriString = it.arguments?.getString("uri")
            val uri = Uri.parse(Uri.decode(uriString))

            AddYatraScreen2(
                navigateBack = { navController.navigateUp() },
                navigateToAddYatra3 = { yatraUiState ->
                    val yatraUiStateJson = Gson().toJson(yatraUiState)
                    val route = "${AddYatra3Destination.route}/$yatraUiStateJson"
                    navController.navigate(route)
                },
                yatraUiState = yatraUiState,
                uri = uri // Pass URI to AddYatraScreen2
            )
        }


        composable(route = AddYatra3Destination.route + "/{yatraUiState2}"){
            val yatraUiStateJson = it.arguments?.getString("yatraUiState2")
            val yatraUiState = Gson().fromJson(yatraUiStateJson,YatraUiState::class.java)
            AddYatraScreen3(
                navigateBack = { navController.navigateUp() },
                yatraUiState = yatraUiState,
                navigateToOraganiser = {navController.navigate(UserHomeScreenDestination.route)}
            )
        }

       /* composable(route = "${YatraDetailsDestination.route}/{$yatraIdArg}",
            arguments = listOf(navArgument(yatraIdArg){type = NavType.StringType})
        ){
                backStackEntry ->
            val yatraId = backStackEntry.arguments?.getString(yatraIdArg)
            yatraId?.let {
                YatraDetaiScreen(navigateBack = { navController.navigateUp() }, yatraId = it)
            }
        }*/

        composable(route = SignUpScreenDestination.route){
            SignUpScreen(navigateToUserHomeScreen = { navController.navigate(UserHomeScreenDestination.route) },
                navigateToSignInScreen = {navController.navigate(SignInScreenDestination.route)})
        }

        composable(route = SignInScreenDestination.route){
            SignInScreen(navigateToHomeScreen = {navController.navigate(UserHomeScreenDestination.route)})
        }

        composable(route = OrganiserSignUpDestination.route){
            OrganiserSignUpScreen(
                navigateToOrganiserHomeScreen = {navController.navigate(OrganiserHomeScreenDestination.route)},
                navigateToOrganiserSignInScreen = { navController.navigate(OrganiserSigninDestination.route) })
        }

        composable(route = OrganiserSigninDestination.route){
            OrganiserSignInScreen(
                navigateToOrganiserHomeScreen = {navController.navigate(OrganiserHomeScreenDestination.route)},
               navigateToForgotPasswordScreen = {})
        }

        composable(route = YatraDetailsScreenDestination.route + "/{yatraId}",
            arguments = listOf(navArgument("yatraId"){type = NavType.StringType})
        ){
            backStackEntry ->
            val yatraId = backStackEntry.arguments?.getString("yatraId")
            yatraId?.let {
                YatraDetailScreen(yatraId = it,
                    navigateBack = {navController.navigateUp()},
                    navigateToBookingDetailScreen = {navController.navigate(BookingDetailsDestination.route)})
            }
        }

        composable(route = UserProfileDestination.route){
            UserProfileScreen()
        }

        composable(route = UserBookingsDestination.route){
            UserBookingScreen(navigateToHomeScreen = {navController.navigate(UserHomeScreenDestination.route)})
        }

        composable(route = BookingDetailsDestination.route){
            BookingDetailsScreen()
        }
    }
}

object Graph {
    const val ROOT = "root_graph"
    const val AUTHENTICATION = "auth_graph"
    const val HOME = "home_graph"
    const val DETAILS = "details_graph"
}