package com.example.mydiary.data.model

data class SearchQueryResponse(
    val results: List<SearchResult>,
    val totalCount: Int
)

data class SearchResult(
    val title: String,
    val description: String,
    val url: String
)
