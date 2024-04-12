package com.example.tirthbus.Data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.icons.filled.StarRate
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

data class FilterChipData(
    val text: String,
    val selected: Boolean,
    val icon: ImageVector? = null
)

val filterChips = listOf(
    FilterChipData(text = "Filter", selected = false, icon = Icons.Default.FilterList),
    FilterChipData(text = "Sort", selected = false, icon = Icons.Default.Sort),
    FilterChipData(text = "AC Bus", selected = false, icon = Icons.Default.Air),
    FilterChipData(text = "Live Tracking", selected = false, icon = Icons.Default.LocationOn),
    FilterChipData(text = "Verified Organiser", selected = false, icon = Icons.Default.Verified),
    FilterChipData(text = "Highly Rated", selected = false, icon = Icons.Default.StarRate),
    // Add more chips as needed
)

data class TopDestinations(
    val imageURls:String?,
    val imageText:String?,
)

val topDestinationList1 = listOf<TopDestinations>(
    TopDestinations("https://firebasestorage.googleapis.com/v0/b/tirthbus.appspot.com/o/topDestinations%2FTYDkhatushyam.jpg?alt=media&token=71490c16-e1cd-415a-9886-f4bcca9b60b9","KhatuShyam"),
    TopDestinations("https://firebasestorage.googleapis.com/v0/b/tirthbus.appspot.com/o/images%2Fe1b33175-8fcd-4220-8e8a-24d0790c8e5e.jpg?alt=media&token=6279b91d-e01c-468e-b434-e9035a0ad4dc",
        "Balaji"),
    TopDestinations("https://firebasestorage.googleapis.com/v0/b/tirthbus.appspot.com/o/topDestinations%2FTYDHaridwar.jpg?alt=media&token=206f10cf-a43c-4696-96e4-c6b431ac1494","Haridwar"),
    TopDestinations("https://firebasestorage.googleapis.com/v0/b/tirthbus.appspot.com/o/topDestinations%2FTYDKashi.jpg?alt=media&token=c2a48101-4fcb-4d36-a8bc-b888dd64b0c4","Kashi"),
)

val topDestinationList2 = listOf<TopDestinations>(
    TopDestinations("https://firebasestorage.googleapis.com/v0/b/tirthbus.appspot.com/o/topDestinations%2FTYDPremMandir.jpg?alt=media&token=172e3701-d75e-4792-9d2a-91071c46254d","Vrindavan"),
    TopDestinations("https://firebasestorage.googleapis.com/v0/b/tirthbus.appspot.com/o/topDestinations%2FTYDRamMandir.jpg?alt=media&token=0b12b8fe-9a8c-40e2-9074-8936089784fc",
        "Ayodhya"),
    TopDestinations("https://firebasestorage.googleapis.com/v0/b/tirthbus.appspot.com/o/topDestinations%2FTYDPrayagraj.jpg?alt=media&token=38dd0cae-02ff-498d-b120-a0160937001d","Prayagraj"),
    TopDestinations("https://firebasestorage.googleapis.com/v0/b/tirthbus.appspot.com/o/images%2Fc2bac528-71bc-4bdb-9113-15c418da4eed.jpg?alt=media&token=ee55262c-97bd-47c4-83ef-22f22bb5b595","Vaishno Devi"),
)

@Composable
fun AlertDialogBox(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    //icon: ImageVector,
) {
    AlertDialog(onDismissRequest = { onDismissRequest() },
        confirmButton = { TextButton(onClick = {onConfirmation() }) {
            Text(text = "Confirm")
        } },
        dismissButton = { TextButton(onClick = {onDismissRequest()}) {
            Text(text = "Dismiss")
        } },
        title = { Text(text = dialogTitle)},
        text = { Text(text = dialogText)},
        //icon = { Icon(imageVector = icon, contentDescription = null )}
    )
}