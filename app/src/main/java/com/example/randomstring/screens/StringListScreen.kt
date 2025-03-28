package com.example.randomstring.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.randomstring.database.StringEntity

@Composable
fun StringListScreen(viewModel: MainViewModel) {
    var stringLength by remember { mutableStateOf("") }
    val context = LocalContext.current;
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(1f)
            .fillMaxWidth(1f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Box(modifier = Modifier.size(100.dp))
        // Input field for length
        BasicTextField(
            value = stringLength,
            onValueChange = { stringLength = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(Color.LightGray)
                .padding(8.dp)
        )

        // Generate Button
        Button(
            onClick = {
                val length = stringLength.toIntOrNull()
                if (length != null && length > 0) {
                    viewModel.fetchRandomString(context = context, length = length)
                } else {
                    Toast.makeText(
                        context,
                        "Enter a valid length",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Generate String")
        }

        // Delete All Button
        Button(
            onClick = { viewModel.deleteAllStrings() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            Text("Delete All Strings")
        }

        // Display generated strings
        val strings by viewModel.allStrings.collectAsState()
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(strings) { string ->
                StringItem(string, viewModel)
            }
        }
    }
}

@Composable
fun StringItem(string: StringEntity, viewModel: MainViewModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(text = "String: ${string.value}")
            Text(text = "Length: ${string.length}")
            Text(text = "Created: ${string.created}")

            // Delete Button
            Button(
                onClick = { viewModel.deleteString(string) },
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(top = 8.dp)
            ) {
                Text("Delete")
            }
        }
    }
}