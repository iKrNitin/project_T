package com.example.tirthbus.ui.theme.User.User.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.tirthbus.AppTopBar
import com.example.tirthbus.Data.UserDetailResponse
import com.example.tirthbus.R
import com.example.tirthbus.ui.theme.Navigation.NavigationDestination
import com.example.tirthbus.ui.theme.Theme.TirthBusTheme
import com.example.tirthbus.ui.theme.User.User.ViewModel.UserProfileViewModel

object UserProfileDestination:NavigationDestination{
    override val route: String
        get() = "user_profile"
    override val titleRes: Int
        get() = R.string.user_profile
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(
    //userDocId:String,
    viewModel:UserProfileViewModel = hiltViewModel()
){
    val scope = rememberCoroutineScope()
    val res2 = viewModel.result2.value

    Scaffold(
        topBar = {
            AppTopBar(title = "User Profile",
                canNavigateBack = true,
                navigateToSignUpScreen = { /*TODO*/ })
        }
    ) {innerpadding ->
        if (res2.data2.isNotEmpty()){
            LazyColumn(contentPadding = innerpadding){
                items(
                    res2.data2,
                    key = {it.key!!}
                ){
                    items->
                    UserProfileLayout(item = items)
                }
            }
        }
    }
}

@Composable
fun UserProfileLayout(
    modifier:Modifier = Modifier,
    item: UserDetailResponse
){
    Column(modifier = Modifier.padding(5.dp)) {
        if (item != null) {
            AsyncImage(model = item.user?.userProfileUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,)
        }else{
            Image(painter = painterResource(id = R.drawable.ic_launcher_foreground), contentDescription = null)
        }

        if (item != null) {
            item.user?.userName?.let { Text(text = it) }
        }

        if (item != null) {
            CardTextRow(text1 = "EmailId", item.user?.userEmail)
        }
        if (item != null) {
            CardTextRow(text1 = "Password", item.user?.userEmailPassword)
        }
        if (item != null) {
            CardTextRow(text1 = "Phn Nmbr", item.user?.userPhnNumber)
        }

        Button(
            onClick = { },
            shape = MaterialTheme.shapes.small,
            modifier = Modifier
                .fillMaxWidth()
                .padding(2.dp)) {
            Text(text = stringResource(id = R.string.logout))
            Icon(imageVector = Icons.Filled.ExitToApp, contentDescription = null)
        }

    }
}

@Composable
fun CardTextRow(
    text1:String?,text2:String?
) {
    Card(modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(5.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
    Row() {
        if (text1 != null) {
            Text(text = text1,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Start)
        }
        //Spacer(modifier = Modifier.width(10.dp))
        if (text2 != null) {
            Text(
                text = text2,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.End
            )
        }
        Icon(imageVector = Icons.Filled.Edit,
            modifier = Modifier.weight(0.2f),
            contentDescription = stringResource(id = R.string.edit))
    }
}
}

@Composable
fun IconTextRow(){

}

@Preview(showBackground = true)
@Composable
fun UserProfilePreview(){
    TirthBusTheme {
        //UserProfileLayout()
    }
}