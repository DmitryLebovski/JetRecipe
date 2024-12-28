package com.example.jetrecipe.presentation.login

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jetrecipe.R
import com.example.jetrecipe.domain.model.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
@Composable
fun LoginScreen(
    onSignInSuccess: (User) -> Unit
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(R.drawable.background_login),
            contentDescription = "Dishes",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            text = stringResource(R.string.greeting_text),
            fontSize = 24.sp,
            color = Color.DarkGray,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily(Font(R.font.inter))
        )

        Text(
            text = stringResource(R.string.greeting_subtext),
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            color = Color.DarkGray,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily(Font(R.font.inter))
        )

        Spacer(modifier = Modifier.height(24.dp))

        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult()
        ) { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                Firebase.auth.signInWithCredential(credential)
                    .addOnCompleteListener { authTask ->
                        if (authTask.isSuccessful) {
                            Log.d("LoginScreen", "Login successful: ${account.displayName}")
                            Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show()

                            val firebaseUser = Firebase.auth.currentUser
                            val newUser = User(
                                username = firebaseUser?.displayName ?: "Без имени",
                                email = firebaseUser?.email ?: "Нет e-mail",
                                profilePicture = firebaseUser?.photoUrl?.toString()
                            )
                            onSignInSuccess(newUser)
                        } else {
                            Toast.makeText(context, "Login failed", Toast.LENGTH_SHORT).show()
                            Log.d("LoginScreen", "Login failed: ${authTask.exception?.message}")
                        }
                    }
            } catch (e: ApiException) {
                Toast.makeText(context, "Login failed", Toast.LENGTH_SHORT).show()
                Log.d("LoginScreen", "Login failed: ${task.exception?.message}")
            }
        }

        // Конфиг для GoogleSignIn
        val token = stringResource(R.string.default_web_client_id)

        // Кнопка (изображение) "Sign in with Google"
        Image(
            painter = painterResource(R.drawable.button_sign_in),
            contentDescription = "Login via Google",
            modifier = Modifier
                .clickable {
                    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(token)
                        .requestEmail()
                        .build()
                    val googleSignInClient = GoogleSignIn.getClient(context, gso)
                    launcher.launch(googleSignInClient.signInIntent)
                }
                .width(200.dp)
                .height(60.dp)
        )
    }
}

@Composable
@Preview
fun CheckLoginScreen() {
    LoginScreen {}
}