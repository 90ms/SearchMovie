package com.a90ms.data.entity.search

import com.a90ms.domain.dto.search.ItemsDto
import com.a90ms.domain.dto.search.ItemsPageDto
import kotlin.math.ceil

data class ItemsPageEntity(
    val lastBuildDate: String,
    val total: Int,
    val start: Int,
    val display: Int,
    val items: List<ItemsEntity>,
    val errorCode: String?,
    val errorMessage: String?
) {
    fun toDto(): ItemsPageDto = ItemsPageDto(
        lastBuildDate = lastBuildDate,
        total = total,
        currentPage = start,
        maxPage = ceil(total.toDouble() / display).toInt(),
        content = items.map(ItemsEntity::toDto),
        errorCode = errorCode ?: "",
        errorMessage = errorMessage ?: ""
    )
}

data class ItemsEntity(
    val title: String,
    val link: String,
    val image: String,
    val subtitle: String,
    val pubDate: String,
    val director: String,
    val actor: String,
    val userRating: String
) {
    fun toDto() = ItemsDto(
        title = title,
        link = link,
        image = image,
        subtitle = subtitle,
        pubDate = pubDate,
        director = director,
        actor = actor,
        userRating = userRating,
    )
}
