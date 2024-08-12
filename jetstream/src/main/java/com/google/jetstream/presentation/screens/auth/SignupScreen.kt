import android.app.Activity
import android.content.Context
import android.view.KeyEvent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusRestorer
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.foundation.PivotOffsets
import androidx.tv.foundation.lazy.list.TvLazyColumn
import androidx.tv.material3.CardDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.jetstream.presentation.screens.dashboard.rememberChildPadding
import com.release.aryzaptv.R
import androidx.tv.material3.Text
// ... other imports ...

@OptIn(ExperimentalComposeUiApi::class, ExperimentalTvMaterial3Api::class)
@Composable
fun SignupScreen(
    onSignupSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit,
    appContext: Context,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var retypePassword by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val auth = FirebaseAuth.getInstance()

    var isFocused by remember { mutableStateOf(false) }
    val childPadding = rememberChildPadding()
    var searchQuery by remember { mutableStateOf("") }
    val tfFocusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val tfInteractionSource = remember { MutableInteractionSource() }

    // Google Sign-In setup
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(context.getString(R.string.default_web_client_id))
        .requestEmail()
        .build()
    val googleSignInClient = GoogleSignIn.getClient(context, gso)

    // Launcher for Google Sign-In
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            handleGoogleSignUpResult(result, auth, onSignupSuccess, context, isLoading)
        } else {
            Toast.makeText(context, "Google sign-in failed", Toast.LENGTH_SHORT).show()
        }
    }

    Image(
        painter = painterResource(id = R.drawable.login), // Replace with your image resource
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize()
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.Transparent),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Create Account",
            style = TextStyle(
                color = Color.White,
                fontSize = 32.sp,
                textAlign = TextAlign.Center
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Fill in the details to create your account",
            style = TextStyle(
                color = Color.White,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
        )
        Spacer(modifier = Modifier.height(24.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth(.4f)
                .padding(16.dp)
        ) {
            TvLazyColumn(
                modifier = Modifier.focusRestorer(),
                pivotOffsets = PivotOffsets(parentFraction = 0.07f)
            ) {
                item {
                    TextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Name", color = Color.Gray) },
                        textStyle = TextStyle(color = Color.White),
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Gray.copy(alpha = 0.1f))
                            .onFocusChanged { isFocused = it.isFocused || it.hasFocus }
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    TextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email", color = Color.Gray) },
                        textStyle = TextStyle(color = Color.White),
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Gray.copy(alpha = 0.1f))
                            .onFocusChanged { isFocused = it.isFocused || it.hasFocus }
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    TextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("Phone", color = Color.Gray) },
                        textStyle = TextStyle(color = Color.White),
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Gray.copy(alpha = 0.1f))
                            .onFocusChanged { isFocused = it.isFocused || it.hasFocus }
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    TextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password", color = Color.Gray) },
                        textStyle = TextStyle(color = Color.White),
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Gray.copy(alpha = 0.1f))
                            .onFocusChanged { isFocused = it.isFocused || it.hasFocus },
                        visualTransformation = PasswordVisualTransformation()
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    TextField(
                        value = retypePassword,
                        onValueChange = { retypePassword = it },
                        label = { Text("Retype Password", color = Color.Gray) },
                        textStyle = TextStyle(color = Color.White),
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Gray.copy(alpha = 0.1f))
                            .onFocusChanged { isFocused = it.isFocused || it.hasFocus },
                        visualTransformation = PasswordVisualTransformation()
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            if (password == retypePassword) {
                                isLoading = true
                                auth.createUserWithEmailAndPassword(email, password)
                                    .addOnCompleteListener { task ->
                                        isLoading = false
                                        if (task.isSuccessful) {
                                            onSignupSuccess()
                                        } else {
                                            Toast.makeText(appContext, task.exception.toString(), Toast.LENGTH_SHORT).show()
                                        }
                                    }
                            } else {
                                Toast.makeText(appContext, "Passwords do not match", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusChanged { isFocused = it.isFocused || it.hasFocus },
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)
                    ) {
                        Text("Sign Up", color = Color.White)
                    }


                }
                item {
                    if (isLoading) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Column (horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                            // We need to Center the CircularProgressIndicator

                            CircularProgressIndicator(
                                color = Color.Red,
                                modifier = Modifier
                                    .size(32.dp)
                                    .align(Alignment.CenterHorizontally)

                            )
                        }
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    // Google Sign-In Button
                    Button(
                        onClick = {
                            isLoading = true
                            val signInIntent = googleSignInClient.signInIntent
                            launcher.launch(signInIntent)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusChanged { isFocused = it.isFocused || it.hasFocus },
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)
                    ) {
                        Text("Sign Up with Google", color = Color.White)
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Row {
                        Text(
                            text = "Already have an account?",
                            style = TextStyle(color = Color.White, fontSize = 14.sp)
                        )
                        TextButton(onClick = onNavigateToLogin) {
                            Text("Log In", color = Color.Red, fontSize = 14.sp)
                        }
                    }
                }
            }
        }
    }
}
// Handle Google Sign-In result
fun handleGoogleSignUpResult(
    result: ActivityResult,
    auth: FirebaseAuth,
    onSignupSuccess: () -> Unit,
    context: Context,
    isLoading: Boolean
) {
    val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
    try {
        val account = task.getResult(ApiException::class.java)!!
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                isLoading == false
                if (task.isSuccessful) {

                    onSignupSuccess()
                } else {
                    Toast.makeText(
                        context,
                        "Authentication failed: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    } catch (e: ApiException) {
        Toast.makeText(context, "Google sign-in failed: ${e.message}", Toast.LENGTH_SHORT).show()
    }
}