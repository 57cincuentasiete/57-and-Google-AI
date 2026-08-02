package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class NewsRegion(val displayName: String) {
    CHINA("China Focus"),
    OVERSEAS("Overseas & Global")
}

@Entity(tableName = "news_articles")
data class NewsArticle(
    @PrimaryKey
    val id: String,
    val title: String,
    val summary: String,
    val fullContent: String,
    val region: String, // "CHINA" or "OVERSEAS"
    val sourceName: String, // e.g. Reuters, Bloomberg, SCMP, BBC News
    val sourceCategory: String, // "Global Major Outlet" or "Asia Regional Major"
    val originalUrl: String,
    val publishedTimeStr: String,
    val sessionBatch: String, // "8:00 AM", "12:00 PM", "6:00 PM"
    val dateStr: String, // e.g. "2026-08-02"
    val timestamp: Long,
    val topicTag: String, // "Economy & Trade", "Geopolitics", "Technology", "Finance", "Global Diplomacy"
    val isBookmarked: Boolean = false,
    val isRead: Boolean = false
)
