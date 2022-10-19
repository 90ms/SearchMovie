package com.a90ms.domain.dto.search

import android.os.Parcelable
import com.a90ms.common.dto.PageDto
import kotlinx.parcelize.Parcelize

class ItemsPageDto(
    val lastBuildDate: String,
    override val total: Int,
    val currentPage: Int,
    val maxPage: Int,
    override val content: List<ItemsDto>,
    override val errorCode: String,
    override val errorMessage: String
) : PageDto<ItemsDto>()

@Parcelize
data class ItemsDto(
    val id: String = "",
    val title: String,
    val link: String,
    val image: String,
    val subtitle: String,
    val pubDate: String,
    val director: String,
    val actor: String,
    val userRating: String,
    val favorite: Boolean = false
) : Parcelable
