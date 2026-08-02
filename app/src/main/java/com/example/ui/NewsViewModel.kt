package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.NewsArticle
import com.example.data.NewsDatabase
import com.example.data.NewsRegion
import com.example.data.NewsRepository
import com.example.service.RefreshScheduler
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class NewsUiState(
    val selectedRegionTab: NewsRegion = NewsRegion.CHINA,
    val searchQuery: String = "",
    val selectedTopicTag: String = "All",
    val isRefreshing: Boolean = false,
    val lastRefreshedMillis: Long = System.currentTimeMillis(),
    val scheduleStatus: RefreshScheduler.ScheduleStatus = RefreshScheduler.getScheduleStatus(System.currentTimeMillis()),
    val selectedArticleForDetail: NewsArticle? = null,
    val selectedArticleForWebView: NewsArticle? = null,
    val showBookmarksOnly: Boolean = false,
    val statusMessage: String? = null
)

class NewsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: NewsRepository
    private val _uiState = MutableStateFlow(NewsUiState())
    val uiState: StateFlow<NewsUiState> = _uiState.asStateFlow()

    val chinaArticles: StateFlow<List<NewsArticle>>
    val overseasArticles: StateFlow<List<NewsArticle>>
    val bookmarkedArticles: StateFlow<List<NewsArticle>>

    val topicsList = listOf(
        "All",
        "Economy & Trade",
        "Geopolitics",
        "Technology",
        "Finance",
        "Global Trade",
        "Science & Space"
    )

    init {
        val database = NewsDatabase.getDatabase(application)
        repository = NewsRepository(database.newsDao())

        val allFlow = repository.allArticles

        chinaArticles = combine(allFlow, _uiState) { articles, state ->
            filterArticles(articles, NewsRegion.CHINA.name, state)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        overseasArticles = combine(allFlow, _uiState) { articles, state ->
            filterArticles(articles, NewsRegion.OVERSEAS.name, state)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        bookmarkedArticles = combine(allFlow, _uiState) { articles, state ->
            filterArticles(articles, null, state, bookmarksOnly = true)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        // Initial setup and auto-refresh on app open
        viewModelScope.launch {
            updateScheduleStatus()
            refreshDigest(showToast = false)
        }
    }

    private fun filterArticles(
        articles: List<NewsArticle>,
        region: String?,
        state: NewsUiState,
        bookmarksOnly: Boolean = false
    ): List<NewsArticle> {
        return articles.filter { article ->
            val matchesRegion = region == null || article.region == region
            val matchesBookmark = !bookmarksOnly || article.isBookmarked
            val matchesSearch = state.searchQuery.isBlank() ||
                    article.title.contains(state.searchQuery, ignoreCase = true) ||
                    article.summary.contains(state.searchQuery, ignoreCase = true) ||
                    article.sourceName.contains(state.searchQuery, ignoreCase = true)
            val matchesTopic = state.selectedTopicTag == "All" ||
                    article.topicTag.contains(state.selectedTopicTag, ignoreCase = true)

            matchesRegion && matchesBookmark && matchesSearch && matchesTopic
        }
    }

    fun selectRegionTab(region: NewsRegion) {
        _uiState.value = _uiState.value.copy(
            selectedRegionTab = region,
            showBookmarksOnly = false
        )
    }

    fun toggleBookmarksView(showOnly: Boolean) {
        _uiState.value = _uiState.value.copy(showBookmarksOnly = showOnly)
    }

    fun updateSearchQuery(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }

    fun selectTopicTag(tag: String) {
        _uiState.value = _uiState.value.copy(selectedTopicTag = tag)
    }

    fun toggleBookmark(article: NewsArticle) {
        viewModelScope.launch {
            repository.toggleBookmark(article.id, article.isBookmarked)
            _uiState.value.selectedArticleForDetail?.let { current ->
                if (current.id == article.id) {
                    _uiState.value = _uiState.value.copy(
                        selectedArticleForDetail = current.copy(isBookmarked = !current.isBookmarked)
                    )
                }
            }
        }
    }

    fun markAsRead(article: NewsArticle) {
        viewModelScope.launch {
            repository.markAsRead(article.id)
        }
    }

    fun openArticleDetail(article: NewsArticle) {
        markAsRead(article)
        _uiState.value = _uiState.value.copy(selectedArticleForDetail = article)
    }

    fun closeArticleDetail() {
        _uiState.value = _uiState.value.copy(selectedArticleForDetail = null)
    }

    fun openArticleWebView(article: NewsArticle) {
        _uiState.value = _uiState.value.copy(selectedArticleForWebView = article)
    }

    fun closeArticleWebView() {
        _uiState.value = _uiState.value.copy(selectedArticleForWebView = null)
    }

    fun refreshDigest(showToast: Boolean = true) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isRefreshing = true)
            delay(600) // smooth refresh animation
            val updatedCount = repository.refreshDigest(isNetworkSync = true)
            val now = System.currentTimeMillis()
            _uiState.value = _uiState.value.copy(
                isRefreshing = false,
                lastRefreshedMillis = now,
                scheduleStatus = RefreshScheduler.getScheduleStatus(now),
                statusMessage = if (showToast) "Updated $updatedCount dispatches for ${_uiState.value.scheduleStatus.currentSessionLabel}" else null
            )
        }
    }

    fun clearStatusMessage() {
        _uiState.value = _uiState.value.copy(statusMessage = null)
    }

    private fun updateScheduleStatus() {
        val lastTime = _uiState.value.lastRefreshedMillis
        _uiState.value = _uiState.value.copy(
            scheduleStatus = RefreshScheduler.getScheduleStatus(lastTime)
        )
    }
}
