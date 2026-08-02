package com.example.data

import com.example.service.NewsFetcher
import kotlinx.coroutines.flow.Flow

class NewsRepository(private val newsDao: NewsDao) {

    val allArticles: Flow<List<NewsArticle>> = newsDao.getAllArticles()
    val bookmarkedArticles: Flow<List<NewsArticle>> = newsDao.getBookmarkedArticles()

    fun getArticlesByRegion(region: String): Flow<List<NewsArticle>> {
        return newsDao.getArticlesByRegion(region)
    }

    suspend fun getArticleById(id: String): NewsArticle? {
        return newsDao.getArticleById(id)
    }

    suspend fun toggleBookmark(id: String, isCurrentlyBookmarked: Boolean) {
        newsDao.updateBookmarkStatus(id, !isCurrentlyBookmarked)
    }

    suspend fun markAsRead(id: String) {
        newsDao.markAsRead(id)
    }

    suspend fun refreshDigest(forcedSessionBatch: String? = null, isNetworkSync: Boolean = false): Int {
        val session = forcedSessionBatch ?: NewsFetcher.getScheduledSession()
        val latestArticles = NewsFetcher.fetchLatestDispatches(session, isNetworkSync)
        newsDao.insertArticles(latestArticles)
        return latestArticles.size
    }

    suspend fun ensureInitialData() {
        refreshDigest()
    }
}
