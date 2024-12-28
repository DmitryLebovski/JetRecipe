package com.example.jetrecipe.presentation.profile

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.jetrecipe.domain.model.User

@Composable
fun ProfileScreen(
    user: User?,
    onSignOut: () -> Unit
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Account Screen", style = MaterialTheme.typography.bodySmall)

        Spacer(modifier = Modifier.height(16.dp))

        user?.let {
            Image(
                painter = rememberAsyncImagePainter(it.profilePicture),
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .width(100.dp)
                    .height(100.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Имя: ${it.username}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Email: ${it.email}", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
        } ?: run {
            Text(text = "Данные о пользователе отсутствуют")
            Spacer(modifier = Modifier.height(8.dp))
        }

        Button(onClick = {
            Log.d("ProfileScreen", "Logout")
            Toast.makeText(context, "Sign out", Toast.LENGTH_SHORT).show()
            onSignOut()
        }) {
            Text(text = "Sign out")
        }
    }
}