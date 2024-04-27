package com.example.tirthbus.Data

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.AirlineSeatReclineExtra
import androidx.compose.material.icons.filled.Bed
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.icons.filled.Sos
import androidx.compose.material.icons.filled.StarRate
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

data class FilterChipData(
    val text: String,
    val selected: Boolean,
    val icon: ImageVector? = null
)

data class inputChipData(
    val data:Pair<String?,ImageVector>
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

val bannerList = listOf<String>("https://firebasestorage.googleapis.com/v0/b/tirthbus.appspot.com/o/topDestinations%2FTYDkhatushyam.jpg?alt=media&token=71490c16-e1cd-415a-9886-f4bcca9b60b9",
    "https://firebasestorage.googleapis.com/v0/b/tirthbus.appspot.com/o/images%2Fe1b33175-8fcd-4220-8e8a-24d0790c8e5e.jpg?alt=media&token=6279b91d-e01c-468e-b434-e9035a0ad4dc",
    "https://firebasestorage.googleapis.com/v0/b/tirthbus.appspot.com/o/topDestinations%2FTYDHaridwar.jpg?alt=media&token=206f10cf-a43c-4696-96e4-c6b431ac1494",
    "https://firebasestorage.googleapis.com/v0/b/tirthbus.appspot.com/o/topDestinations%2FTYDKashi.jpg?alt=media&token=c2a48101-4fcb-4d36-a8bc-b888dd64b0c4"
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

// Create a list of pairs of string and image vector
val list3: List<Pair<String, ImageVector>> = listOf(
    "Seater" to Icons.Default.AirlineSeatReclineExtra,
    "Sleeper" to Icons.Default.Bed,
    "AC" to Icons.Default.AcUnit,
    "NON-AC" to Icons.Default.Clear,
    "Water Bottle" to Icons.Default.WaterDrop,
    "Emergency Contact Number" to Icons.Default.Sos,
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

const val OTP_VIEW_TYPE_NONE = 0
const val OTP_VIEW_TYPE_UNDERLINE = 1
const val OTP_VIEW_TYPE_BORDER = 2

@Composable
fun CommonDialog() {

    Dialog(onDismissRequest = { }) {
        Text(text = "Loading..")
    }

}


@Composable
fun OtpView(
    modifier:Modifier = Modifier,
    otpText: String = "",
    charColor:androidx.compose.ui.graphics.Color = androidx.compose.ui.graphics.Color.Black,
   // charBackground :Color = Color.Transparent,
    charSize: TextUnit = 20.sp,
    containerSize: Dp = charSize.value.dp * 2,
    otpCount: Int = 6,
    type: Int = OTP_VIEW_TYPE_BORDER,
    enabled: Boolean = true,
    password: Boolean = false,
    passwordChar: String = "",
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
    onOtpTextChange: (String) -> Unit
) {
    BasicTextField(
        modifier = modifier,
        value = otpText,
        onValueChange = {
            if (it.length <= otpCount) {
                onOtpTextChange.invoke(it)
            }
        },
        enabled = enabled,
        keyboardOptions = keyboardOptions,
        decorationBox = {
            Row(horizontalArrangement = Arrangement.SpaceAround) {
                repeat(otpCount) { index ->
                    Spacer(modifier = Modifier.width(2.dp))
                    CharView(
                        index = index,
                        text = otpText,
                        charColor = charColor,
                        charSize = charSize,
                        containerSize = containerSize,
                        type = type,
                       // charBackground = charBackground,
                        password = password,
                        passwordChar = passwordChar,
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                }
            }
        })
}

@Composable
private fun CharView(
    index: Int,
    text: String,
    charColor: androidx.compose.ui.graphics.Color,
    charSize: TextUnit,
    containerSize: Dp,
    type: Int = OTP_VIEW_TYPE_UNDERLINE,
    charBackground: androidx.compose.ui.graphics.Color = androidx.compose.ui.graphics.Color.Transparent,
    password: Boolean = false,
    passwordChar: String = ""
) {
    val modifier = if (type == OTP_VIEW_TYPE_BORDER) {
        Modifier
            .size(containerSize)
            .border(
                width = 1.dp,
                color = charColor,
                shape = MaterialTheme.shapes.medium
            )
            .padding(bottom = 4.dp)
            .background(charBackground)
    } else Modifier
        .width(containerSize)
        .background(charBackground)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        val char = when {
            index >= text.length -> ""
            password -> passwordChar
            else -> text[index].toString()
        }
        Text(
            text = char,
            color = Color.Black,
            modifier = modifier.wrapContentHeight(),
            style = MaterialTheme.typography.bodyMedium,
            fontSize = charSize,
            textAlign = TextAlign.Center,
        )
        if (type == OTP_VIEW_TYPE_UNDERLINE) {
            Spacer(modifier = Modifier.height(2.dp))
            Box(
                modifier = Modifier
                    .background(charColor)
                    .height(1.dp)
                    .width(containerSize)
            )
        }
    }
}