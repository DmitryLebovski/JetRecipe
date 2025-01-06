package com.example.jetrecipe.presentation.profile

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.jetrecipe.R
import com.example.jetrecipe.domain.model.User

@Composable
fun ProfileScreen(
    user: User?,
    onSignOut: () -> Unit
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
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

            Text(
                text = "Имя: ${it.username}",
                style = MaterialTheme.typography.bodyMedium,
                fontFamily = FontFamily(Font(R.font.inter)
            ))
            Text(
                text = "Email: ${it.email}",
                style = MaterialTheme.typography.bodyMedium,
                fontFamily = FontFamily(Font(R.font.inter))
            )
            Spacer(modifier = Modifier.height(8.dp))

        } ?: run {
            Text(text = stringResource(R.string.empty_user_data))
            Spacer(modifier = Modifier.height(8.dp))
        }

        Button(
            onClick = { onSignOut() },
            colors = ButtonDefaults.buttonColors(Color.Red)
        ) {
            Text(text = stringResource(R.string.sign_out))
        }
    }
}