package com.example.tirthbus.ui.theme.Organiser.Screens

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tirthbus.Data.CommonDialog
import com.example.tirthbus.Data.OtpView
import com.example.tirthbus.Data.ResultState
import com.example.tirthbus.ui.theme.Organiser.ViewModel.OrganiserAuthViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun PhoneAuthScreen(
    modifier: Modifier = Modifier,
    viewModel:OrganiserAuthViewModel = hiltViewModel(),
    activity: Activity
){
    var mobile by remember { mutableStateOf("")}
    var otp by remember { mutableStateOf("")}
    val scope = rememberCoroutineScope()
    var isDialog by remember{ mutableStateOf(false)}

    if(isDialog)
        CommonDialog()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Enter Mobile Number")
            Spacer(modifier = Modifier.height(20.dp))
            OutlinedTextField(value = mobile, onValueChange = {
                mobile = it
            }, label = {Text("+91")}, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(20.dp))
            Button(onClick = {
                scope.launch(Dispatchers.Main){
                    viewModel.createOrganiserWithPhone(
                        mobile,
                        activity
                    ).collect{
                        when(it){
                            is ResultState.Success->{
                                isDialog = false
                                Toast.makeText(activity,it.data,Toast.LENGTH_SHORT).show()
                            }
                            is ResultState.Failure->{
                                isDialog = false
                                Toast.makeText(activity,it.msg.toString(),Toast.LENGTH_SHORT).show()
                            }
                            ResultState.Loading->{
                                isDialog = true
                            }
                        }
                    }
                }
            }) {
                Text(text = "Submit")
            }

            Spacer(modifier = Modifier.height(20.dp))
            Text(text = "Enter Otp")
            Spacer(modifier = Modifier.height(20.dp))
            OtpView(otpText = otp){
                otp = it
            }
            Spacer(modifier = Modifier.height(20.dp))
            Button(onClick = {
                scope.launch(Dispatchers.Main){
                    viewModel.signInWithCredential(
                        otp
                    ).collect{
                        when(it){
                            is ResultState.Success->{
                                isDialog = false
                                Toast.makeText(activity,it.data,Toast.LENGTH_SHORT).show()
                            }
                            is ResultState.Failure->{
                                isDialog = false
                                Toast.makeText(activity,it.msg.toString(),Toast.LENGTH_SHORT).show()
                            }
                            ResultState.Loading->{
                                isDialog = true
                            }
                        }
                    }
                }
            }) {
                Text(text = "Verify")
            }
        }
    }
}