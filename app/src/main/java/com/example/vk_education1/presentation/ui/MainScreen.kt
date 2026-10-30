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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vk_education1.domain.model.AppInfo
import com.example.vk_education1.presentation.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreenWithViewModel(
    onAppClick: (Int) -> Unit,
    onShowcaseAppClick: (AppInfo) -> Unit,
    viewModel: MainViewModel = viewModel()
) {
    val apps by viewModel.apps.collectAsStateWithLifecycle()
    val snackbarMessage by viewModel.snackbarMessage.collectAsStateWithLifecycle()
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
                title = {
                    Text(
                        text = "Приложения",
                        modifier = Modifier.clickable { viewModel.onToolbarClick() }
                    )
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text("Все приложения", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            }
            itemsIndexed(apps) { index, app ->
                AppItem(app = app, onClick = { onAppClick(index) })
            }
            item {
                Spacer(modifier = Modifier.height(12.dp))
                Text("Витрина", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))
                Showcase(apps = apps, onAppClick = onShowcaseAppClick)
            }
        }
    }
}

@Composable
fun AppItem(app: AppInfo, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Card(modifier = Modifier.size(52.dp), shape = RoundedCornerShape(12.dp)) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(app.name.take(1), style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                }
            }
            Column(modifier = Modifier.weight(1f).padding(start = 14.dp)) {
                Text(app.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text(app.description, style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Text(app.category, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
fun Showcase(apps: List<AppInfo>, onAppClick: (AppInfo) -> Unit) {
    val pagerState = rememberPagerState(pageCount = { 3 })
    Column(modifier = Modifier.fillMaxWidth()) {
        HorizontalPager(state = pagerState, modifier = Modifier.fillMaxWidth(), pageSpacing = 12.dp) { page ->
            Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                val startIndex = page * 3
                for (index in startIndex until startIndex + 3) {
                    if (index < apps.size) {
                        AppItem(app = apps[index], onClick = { onAppClick(apps[index]) })
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            for (page in 0 until 3) {
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
fun AppDetailsScreen(app: AppInfo, onBackClick: () -> Unit) {
    Scaffold(topBar = { TopAppBar(title = { Text(app.name) }) }) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding).padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(app.name, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text("Категория: ${app.category}", style = MaterialTheme.typography.titleMedium)
            Text(app.description, style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(12.dp))
            Button(onClick = onBackClick, modifier = Modifier.fillMaxWidth()) {
                Text("Назад к списку")
            }
        }
    }
}