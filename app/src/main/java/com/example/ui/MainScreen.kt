package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TravelExplore
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.NewsArticle
import com.example.data.NewsRegion
import com.example.ui.theme.AmberGold500
import com.example.ui.theme.ChinaRed700
import com.example.ui.theme.GlobalBlue600
import com.example.ui.theme.SlateNavy800
import com.example.ui.theme.SlateNavy900

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: NewsViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val chinaArticles by viewModel.chinaArticles.collectAsStateWithLifecycle()
    val overseasArticles by viewModel.overseasArticles.collectAsStateWithLifecycle()
    val bookmarkedArticles by viewModel.bookmarkedArticles.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.statusMessage) {
        uiState.statusMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearStatusMessage()
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = AmberGold500,
                            modifier = Modifier.padding(end = 10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Public,
                                contentDescription = null,
                                tint = SlateNavy900,
                                modifier = Modifier
                                    .padding(6.dp)
                                    .size(20.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "World News Digest",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp
                                ),
                                color = Color.White
                            )
                            Text(
                                text = "Reuters • Zaobao • SCMP • Bloomberg • BBC • FT",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                                color = Color.White.copy(alpha = 0.75f)
                            )
                        }
                    }
                },
                actions = {
                    IconButton(
                        onClick = { viewModel.toggleBookmarksView(!uiState.showBookmarksOnly) },
                        modifier = Modifier.testTag("bookmark_filter_btn")
                    ) {
                        Icon(
                            imageVector = if (uiState.showBookmarksOnly) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "Saved Articles",
                            tint = if (uiState.showBookmarksOnly) AmberGold500 else Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = SlateNavy900,
                    titleContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Main TabRow Navigation: China Focus vs Overseas & Global vs Saved
            val selectedTabIndex = when {
                uiState.showBookmarksOnly -> 2
                uiState.selectedRegionTab == NewsRegion.CHINA -> 0
                else -> 1
            }

            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = SlateNavy900,
                contentColor = Color.White,
                indicator = { tabPositions ->
                    if (selectedTabIndex < tabPositions.size) {
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                            color = AmberGold500,
                            height = 3.dp
                        )
                    }
                }
            ) {
                Tab(
                    selected = !uiState.showBookmarksOnly && uiState.selectedRegionTab == NewsRegion.CHINA,
                    onClick = { viewModel.selectRegionTab(NewsRegion.CHINA) },
                    modifier = Modifier.testTag("tab_china")
                ) {
                    Row(
                        modifier = Modifier.padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "China Focus",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            ),
                            color = if (!uiState.showBookmarksOnly && uiState.selectedRegionTab == NewsRegion.CHINA) AmberGold500 else Color.White.copy(alpha = 0.7f)
                        )
                    }
                }

                Tab(
                    selected = !uiState.showBookmarksOnly && uiState.selectedRegionTab == NewsRegion.OVERSEAS,
                    onClick = { viewModel.selectRegionTab(NewsRegion.OVERSEAS) },
                    modifier = Modifier.testTag("tab_overseas")
                ) {
                    Row(
                        modifier = Modifier.padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Overseas & Global",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            ),
                            color = if (!uiState.showBookmarksOnly && uiState.selectedRegionTab == NewsRegion.OVERSEAS) AmberGold500 else Color.White.copy(alpha = 0.7f)
                        )
                    }
                }

                Tab(
                    selected = uiState.showBookmarksOnly,
                    onClick = { viewModel.toggleBookmarksView(true) },
                    modifier = Modifier.testTag("tab_bookmarks")
                ) {
                    Row(
                        modifier = Modifier.padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Saved (${bookmarkedArticles.size})",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            ),
                            color = if (uiState.showBookmarksOnly) AmberGold500 else Color.White.copy(alpha = 0.7f)
                        )
                    }
                }
            }

            // Search & Topic Filter Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                // Search Field
                OutlinedTextField(
                    value = uiState.searchQuery,
                    onValueChange = { viewModel.updateSearchQuery(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("search_input"),
                    placeholder = { Text("Search headlines or sources...", fontSize = 14.sp) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    },
                    trailingIcon = {
                        if (uiState.searchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.updateSearchQuery("") }) {
                                Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear Search")
                            }
                        }
                    },
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        focusedBorderColor = GlobalBlue600,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Topic Filter Chips Row
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(vertical = 4.dp)
                ) {
                    items(viewModel.topicsList) { topic ->
                        val isSelected = uiState.selectedTopicTag == topic
                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.selectTopicTag(topic) },
                            label = {
                                Text(
                                    text = topic,
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                    )
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = SlateNavy900,
                                selectedLabelColor = AmberGold500,
                                containerColor = MaterialTheme.colorScheme.surface,
                                labelColor = MaterialTheme.colorScheme.onSurface
                            ),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.testTag("topic_chip_$topic")
                        )
                    }
                }
            }

            // Scheduled Digest Status Banner
            ScheduleHeader(
                scheduleStatus = uiState.scheduleStatus,
                isRefreshing = uiState.isRefreshing,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Articles List Container with Pull-to-Refresh
            val displayArticles = when {
                uiState.showBookmarksOnly -> bookmarkedArticles
                uiState.selectedRegionTab == NewsRegion.CHINA -> chinaArticles
                else -> overseasArticles
            }

            PullToRefreshBox(
                isRefreshing = uiState.isRefreshing,
                onRefresh = { viewModel.refreshDigest() },
                modifier = Modifier.fillMaxSize()
            ) {
                if (displayArticles.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.TravelExplore,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                                modifier = Modifier.size(56.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = if (uiState.showBookmarksOnly) "No Saved Dispatches" else "No matching dispatches found",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Slide down to sync or clear filters to view latest reports from Reuters, Lianhe Zaobao, SCMP, Bloomberg & BBC.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            items = displayArticles,
                            key = { it.id }
                        ) { article ->
                            ArticleCard(
                                article = article,
                                onArticleClick = { viewModel.openArticleDetail(it) },
                                onVerifyLinkClick = { viewModel.openArticleWebView(it) },
                                onBookmarkToggle = { viewModel.toggleBookmark(it) }
                            )
                        }
                    }
                }
            }
        }

        // Article Detail Sheet Modal
        uiState.selectedArticleForDetail?.let { article ->
            ArticleDetailSheet(
                article = article,
                onDismiss = { viewModel.closeArticleDetail() },
                onOpenWebView = {
                    viewModel.closeArticleDetail()
                    viewModel.openArticleWebView(it)
                },
                onToggleBookmark = { viewModel.toggleBookmark(it) }
            )
        }

        // In-App WebView Dialog for Checking Origin Link
        uiState.selectedArticleForWebView?.let { article ->
            WebViewDialog(
                article = article,
                onClose = { viewModel.closeArticleWebView() }
            )
        }
    }
}
