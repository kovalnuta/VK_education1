package com.example.vk_education1.presentation.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.vk_education1.domain.model.AppInfo
import com.example.vk_education1.presentation.viewmodel.AppDetailsState
import com.example.vk_education1.presentation.viewmodel.DetailsViewModel
import com.example.vk_education1.presentation.viewmodel.InstallState
import com.example.vk_education1.presentation.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreenWithViewModel(
    onAppClick: (String) -> Unit,
    onShowcaseAppClick: (AppInfo) -> Unit,
    viewModel: MainViewModel = hiltViewModel()
) {
    val apps by viewModel.apps.collectAsStateWithLifecycle()
    val ratings by viewModel.ratings.collectAsStateWithLifecycle()
    val snackbarMessage by viewModel.snackbarMessage.collectAsStateWithLifecycle()
    val isLoadingRatings by viewModel.isLoadingRatings.collectAsStateWithLifecycle() // <-- Добавлено
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.onSnackbarShown()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Приложения", modifier = Modifier.clickable { viewModel.onToolbarClick() }) }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text("Все приложения", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { viewModel.loadRatingsSequentially() },
                        modifier = Modifier.weight(1f),
                        enabled = !isLoadingRatings // Блокируем, пока идет загрузка
                    ) {
                        Text(
                            text = if (isLoadingRatings) "Ждите..." else "Последовательно",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    Button(
                        onClick = { viewModel.loadRatingsInParallel() },
                        modifier = Modifier.weight(1f),
                        enabled = !isLoadingRatings
                    ) {
                        Text(
                            text = if (isLoadingRatings) "Ждите..." else "Параллельно",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
            itemsIndexed(apps) { index, app ->
                AppItem(
                    app = app,
                    rating = ratings[app.id],
                    onClick = { onAppClick(app.id) }
                )
            }
            item {
                Spacer(modifier = Modifier.height(12.dp))
                Text("Витрина", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))
                Showcase(apps = apps, ratings = ratings, onAppClick = onShowcaseAppClick)
            }
        }
    }
}

@Composable
fun AppItem(app: AppInfo, rating: Float? = null, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Card(modifier = Modifier.size(52.dp), shape = RoundedCornerShape(12.dp)) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(app.name.take(1), style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                }
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 14.dp)
            ) {
                Text(app.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text(app.description, style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(app.category, style = MaterialTheme.typography.bodySmall)
                    if (rating != null) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("⭐ $rating", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }
    }
}

@Composable
fun Showcase(apps: List<AppInfo>, ratings: Map<String, Float>, onAppClick: (AppInfo) -> Unit) {
    if (apps.isEmpty()) return
    val pagerState = rememberPagerState(pageCount = { (apps.size + 2) / 3 })
    Column(modifier = Modifier.fillMaxWidth()) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth(),
            pageSpacing = 12.dp
        ) { page ->
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                val startIndex = page * 3
                for (index in startIndex until startIndex + 3) {
                    if (index < apps.size) {
                        val app = apps[index]
                        AppItem(app = app, rating = ratings[app.id], onClick = { onAppClick(app) })
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            for (page in 0 until pagerState.pageCount) {
                Text(
                    text = if (pagerState.currentPage == page) "●" else "○",
                    modifier = Modifier.padding(horizontal = 6.dp),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDetailsScreenWithId(
    appId: String,
    onBackClick: () -> Unit,
    viewModel: DetailsViewModel = hiltViewModel()
) {
    LaunchedEffect(appId) {
        viewModel.loadApp(appId)
    }
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            val title = when (val s = state) {
                is AppDetailsState.Content -> s.app.name
                else -> "Загрузка..."
            }
            TopAppBar(title = { Text(title) })
        }
    ) { innerPadding ->
        when (val currentState = state) {
            is AppDetailsState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is AppDetailsState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Приложение не найдено")
                }
            }
            is AppDetailsState.Content -> {
                val app = currentState.app
                val installState = currentState.installState
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(app.name, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                    Text("Категория: ${app.category}", style = MaterialTheme.typography.titleMedium)
                    Text(app.description, style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.height(12.dp))

                    when (installState) {
                        is InstallState.NotInstalled, is InstallState.Installed -> {
                            Button(
                                onClick = { viewModel.onInstallClick() },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(if (installState is InstallState.Installed) "Установлено (переустановить)" else "Установить")
                            }
                        }
                        is InstallState.Installing -> {
                            Button(
                                onClick = { viewModel.onInstallClick() },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.error
                                )
                            ) {
                                Text("Отменить (${installState.progress}%)")
                            }
                            LinearProgressIndicator(
                                progress = { installState.progress / 100f },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Button(onClick = onBackClick, modifier = Modifier.fillMaxWidth()) {
                        Text("Назад к списку")
                    }
                }
            }
        }
    }
}