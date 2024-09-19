package com.example.actronomicalhandbook

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.AbsoluteCutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
import kotlin.random.Random

data class NewsItem(
    val id: Int,
    val summary: String,
    val description: String,
    var likeCount: Int = 0,
)

class NewsViewModel : ViewModel() {
    private val _newsList = mutableStateListOf(
        NewsItem(1, "Илон Маск объявил о запуске миссии на Марс в 2026 году", "SpaceX планирует запустить первую пилотируемую миссию на Марс уже в 2026 году. Маск обещает колонизировать Красную планету в ближайшие десятилетия."),
        NewsItem(2, "Роскосмос завершил испытания первого российского лунного модуля", "Роскосмос объявил об успешных испытаниях первого лунного посадочного модуля, который должен отправиться на Луну в 2025 году в рамках российской лунной программы."),
        NewsItem(3, "NASA отправляет телескоп нового поколения к Юпитеру", "NASA анонсировало запуск телескопа нового поколения, который позволит детально изучить атмосферу и магнетосферу Юпитера. Ожидается, что телескоп откроет новые тайны газового гиганта."),
        NewsItem(4, "Илон Маск представил проект космического отеля на орбите Земли", "SpaceX совместно с крупными инвесторами планирует построить первый в мире космический отель на низкой околоземной орбите. Гостям обещают виды на Землю и полный комфорт в условиях невесомости."),
        NewsItem(5, "NASA и Роскосмос договорились о совместной миссии на Венеру", "Впервые за последние 30 лет NASA и Роскосмос решили объединить усилия для исследования Венеры. Ожидается, что миссия стартует в 2028 году и изучит атмосферу планеты."),
        NewsItem(6, "SpaceX запускает сеть спутников для наблюдения за климатом", "В рамках проекта Starlink Илон Маск объявил о запуске новой серии спутников, которые будут собирать данные о климатических изменениях на Земле и помогать в прогнозировании природных катастроф."),
        NewsItem(7, "Роскосмос испытал новый двигатель для межпланетных миссий", "Роскосмос успешно протестировал революционный ионный двигатель, который может использоваться для долгосрочных межпланетных миссий, в том числе для полетов к Марсу и Юпитеру."),
        NewsItem(8, "NASA начинает подготовку миссии для возвращения образцов с Марса", "В рамках программы Mars Sample Return NASA разрабатывает план по доставке на Землю образцов марсианского грунта, собранных ровером Perseverance. Миссия запланирована на 2030 год."),
        NewsItem(9, "Илон Маск заявил о намерении построить город на Луне к 2035 году", "Маск объявил о долгосрочных планах SpaceX по созданию постоянной лунной базы, которая со временем превратится в полноценный город на Луне с тысячами жителей."),
        NewsItem(10, "Роскосмос и NASA обсудили возможность создания совместной орбитальной станции", "На конференции в Москве представители Роскосмоса и NASA обсудили перспективы создания новой международной космической станции, которая заменит МКС после её вывода из эксплуатации в 2031 году.")
    )

    val displayedNews = mutableStateListOf<NewsItem>().apply {
        addAll(_newsList.shuffled().take(4))
    }

    fun likeNewsCb(newsItem: NewsItem) {
        val index = displayedNews.indexOf(newsItem)
        if (index != -1) {
            displayedNews[index] = newsItem.copy(likeCount = newsItem.likeCount+1)
            newsItem.likeCount++
        }
    }

    fun reorderNews() {
        val randomIndex = Random.nextInt(0, displayedNews.size)
        var randomNews: NewsItem
        do {
            randomNews = _newsList[Random.nextInt(0, _newsList.size)]
        } while (randomNews in displayedNews)
        displayedNews[randomIndex] = randomNews
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NewsBanner()
        }
    }
}

@Preview
@Composable
private fun NewsBanner(viewModel: NewsViewModel = viewModel()) {
    LaunchedEffect(Unit) {
        while (true) {
            delay(5000)
            viewModel.reorderNews()
        }
    }
    Column(modifier = Modifier.fillMaxSize()) {
        Row(Modifier.weight(1f)) {
            NewsCard(modifier = Modifier.weight(1f), newsItem = viewModel.displayedNews[0], onLike = { viewModel.likeNewsCb(it) })
            NewsCard(modifier = Modifier.weight(1f), newsItem = viewModel.displayedNews[1], onLike = { viewModel.likeNewsCb(it) })
        }
        Row(Modifier.weight(1f)) {
            NewsCard(modifier = Modifier.weight(1f), newsItem = viewModel.displayedNews[2], onLike = { viewModel.likeNewsCb(it) })
            NewsCard(modifier = Modifier.weight(1f), newsItem = viewModel.displayedNews[3], onLike = { viewModel.likeNewsCb(it) })
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
                Box(
                    modifier = Modifier.weight(9f)
                ) {
                    Column {
                        Text(
                            text = newsItem.summary,
                            color = Color.White,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(10.dp)
                        )
                        Text(
                            text = newsItem.description,
                            color = Color.White,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .background(Color.Transparent, RoundedCornerShape(13.dp))
                        .border(5.dp, Color.LightGray, RoundedCornerShape(15.dp))
                        .width(80.dp)
                        .weight(1f)
                        .clickable { onLike(newsItem) }
                        .padding(10.dp),
                    contentAlignment = Alignment.CenterEnd
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
