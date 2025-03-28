package com.example.randomstring

import android.content.pm.PackageManager
import android.os.Bundle
import android.os.StrictMode
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.randomstring.screens.MainViewModel
import com.example.randomstring.screens.StringListScreen
import com.example.randomstring.ui.theme.RandomStringTheme

class MainActivity : ComponentActivity() {
    private val REQUEST_PERMISSION_CODE = 123
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        StrictMode.setThreadPolicy(
            StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build()
        )
        setContent {
            // ✅ Request permission if not granted
            if (ContextCompat.checkSelfPermission(
                    this,
                    "com.iav.contestdataprovider.READ"
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf("com.iav.contestdataprovider.READ"),
                    REQUEST_PERMISSION_CODE
                )
            } else {
                launchApp()
            }
        }
    }


    private fun launchApp(){
        setContent {
            RandomStringTheme {
                val viewModel = MainViewModel()
                viewModel.initialize(applicationContext)
                StringListScreen(viewModel)
            }
        }
    }



    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
        deviceId: Int
    ) {
        if (requestCode == REQUEST_PERMISSION_CODE && grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            launchApp()
        } else {
            finish()
        }
    }
}


