package com.example.actronomicalhandbook

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.AbsoluteCutCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import kotlin.random.Random

data class NewsItem(
    val id: Int,
    val summary: String,
    var likeCount: Int = 0,
)

class NewsViewModel : ViewModel() {
    private val _newsList = mutableStateListOf(
        NewsItem(1, "NewsItem"),
        NewsItem(2, "NewsItem"),
        NewsItem(3, "NewsItem"),
        NewsItem(4, "NewsItem"),
        NewsItem(5, "NewsItem"),
        NewsItem(6, "NewsItem"),
        NewsItem(7, "NewsItem"),
        NewsItem(8, "NewsItem"),
        NewsItem(9, "NewsItem"),
        NewsItem(10, "NewsItem")
    )

    val displayedNews = mutableStateListOf<NewsItem>().apply {
        addAll(_newsList.shuffled().take(4))
    }

    fun likeNewsCb(newsItem: NewsItem) {
        val index = displayedNews.indexOf(newsItem)
        if (index != -1) {
            displayedNews[index] = newsItem.copy(likeCount = newsItem.likeCount++)
        }
    }

    fun reorderNews() {
        val randomIndex = Random.nextInt(0, displayedNews.size)
        val randomNews = _newsList[Random.nextInt(0, _newsList.size)]
        displayedNews[randomIndex] = randomNews
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
        }
    }
}

@Composable
private fun NewsCard(
    modifier: Modifier = Modifier,
    newsItem: NewsItem,
    onLike: (NewsItem) -> Unit
) {
    Card(
        shape = AbsoluteCutCornerShape(10.dp),
        modifier = modifier
            .fillMaxSize()
            .padding(2.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.DarkGray)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = "${newsItem.id}. ${newsItem.summary}",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.weight(1f))
                Box(
                    modifier = Modifier
                        .background(Color.Cyan)
                        .clickable { onLike(newsItem) }
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${newsItem.likeCount} \uD83D\uDC7D",
                        color = Color.Gray,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
