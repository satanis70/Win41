package com.example.win41

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.win41.model.PlayersModel
import com.example.win41.services.RetrofitInterface
import com.example.win41.ui.theme.Win41Theme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory

class QuizActivity : ComponentActivity() {
    val arrayListTopPlayers = ArrayList<PlayersModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val url = intent.getStringExtra("url")
        Log.i("QUIZURL", url.toString())
        CoroutineScope(Dispatchers.IO).launch {
            getPlayers()
            launch(Dispatchers.Main) {
                setContent {
                    Image(
                        painter = rememberAsyncImagePainter("http://49.12.202.175/win41/gradient.png"),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.FillBounds
                    )
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceEvenly,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        itemsIndexed(
                            arrayListTopPlayers[0].questions
                        ) { index, item ->
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.SpaceEvenly,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .height(250.dp)
                                    .padding(12.dp)
                                    .background(
                                        colorResource(id = R.color.box_color),
                                        shape = RoundedCornerShape(4.dp)
                                    )
                            ) {
                                Image(
                                    modifier = Modifier.size(80.dp),
                                    painter = rememberAsyncImagePainter(model = item.img),
                                    contentDescription = ""
                                )
                                Text(text = item.name)
                                Text(text = "Points: ${item.points}")
                                Text(text = "Nationality: ${item.nationality}")
                            }
                        }
                    }
                }
            }
        }
    }

    private suspend fun getPlayers() {
        val playersApi = Retrofit.Builder()
            .baseUrl("http://49.12.202.175/win41/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RetrofitInterface::class.java)
        arrayListTopPlayers.add(playersApi.getPlayers().awaitResponse().body()!!)
    }
}

