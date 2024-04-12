package com.example.tirthbus.ui.theme.Navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.tirthbus.ui.theme.Organiser.Screens.AddYatra2Destination
import com.example.tirthbus.ui.theme.Organiser.Screens.AddYatraDestination
import com.example.tirthbus.ui.theme.Organiser.Screens.AddYatraScreen1
import com.example.tirthbus.ui.theme.Organiser.Screens.AddYatraScreen2
import com.example.tirthbus.ui.theme.Organiser.Screens.OrganiserHomeScreen
import com.example.tirthbus.ui.theme.Organiser.Screens.OrganiserHomeScreenDestination
import com.example.tirthbus.ui.theme.Organiser.ViewModel.YatraUiState
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

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
){
    NavHost(
        navController = navController,
        //startDestination = UserHomeScreenDestination.route,
        startDestination = SignUpScreenDestination.route,
        modifier = modifier
    ){

        composable(route = UserHomeScreenDestination.route){
            UserHomeScreen(
                navigateToOraganiser = {navController.navigate(OrganiserHomeScreenDestination.route)},
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
                SearchResultScreen(searchQuery = searchQuery, navigateBack = { /*TODO*/ },
                    navigateToYatraDetail = {yatraId -> navController.navigate("${YatraDetailsScreenDestination.route}/$yatraId")} )
            }
        }
        
        composable(route = SearchScreenDestination.route){
            SearchScreen(navigateToSearchResult = {searchQuery -> navController.navigate("${SearchResultsDestination.route}/$searchQuery")})
        }

        composable(route = OrganiserHomeScreenDestination.route){
            OrganiserHomeScreen(navigateToUser = {navController.navigate(UserHomeScreenDestination.route)},
                navigateToAddYatra = {navController.navigate(AddYatraDestination.route)})
        }

        composable(route = AddYatraDestination.route){
            AddYatraScreen1(
                navigateBack = {navController.navigateUp()},
                navigateToOraganiser = {navController.navigate(OrganiserHomeScreenDestination.route)},
                navigateToAddYatra2 = { yatraUiState ->
                    val yatraUiStateJson = Gson().toJson(yatraUiState)
                    val route = "${AddYatra2Destination.route}/$yatraUiStateJson"
                    navController.navigate(route)}
            )
        }
        
        composable(route = AddYatra2Destination.route + "/{yatraUiState}"){
            val yatraUiStateJson = it.arguments?.getString("yatraUiState")
            val yatraUiState = Gson().fromJson(yatraUiStateJson,YatraUiState::class.java)
            AddYatraScreen2(
                navigateBack = { navController.navigateUp() },
                navigateToNextScreen = { /*TODO*/ },
                yatraUiState = yatraUiState
            )
        }

        /*composable(route = AddYatraDestination2.route){
            AddYatraScreen2(
                navigateBack = { navController.navigateUp() }
            )
        }*/

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

        composable(route = YatraDetailsScreenDestination.route + "/{yatraId}",
            arguments = listOf(navArgument("yatraId"){type = NavType.StringType})
        ){
            backStackEntry ->
            val yatraId = backStackEntry.arguments?.getString("yatraId")
            yatraId?.let {
                YatraDetailScreen(yatraId = it, navigateBack = {navController.navigateUp()})
            }
        }

        composable(route = UserProfileDestination.route){
            UserProfileScreen()
        }

        composable(route = UserBookingsDestination.route){
            UserBookingScreen()
        }
    }
}