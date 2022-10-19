package com.a90ms.domain.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.a90ms.common.paging.RemoteMediatorEntity
import com.a90ms.domain.dto.search.ItemsDto

@Entity(tableName = "movie_table")
data class LocalMovieEntity(
    @PrimaryKey
    val id: String = "",
    val title: String,
    val link: String,
    val image: String,
    val subtitle: String,
    val pubDate: String,
    val director: String,
    val actor: String,
    val userRating: String,
    val favorite: Boolean?,
    override val nextKey: Int?,
    override val prevKey: Int?
) : RemoteMediatorEntity {
    companion object {

        fun toDto(entity: LocalMovieEntity) = ItemsDto(
            id = entity.id,
            title = entity.title,
            link = entity.link,
            image = entity.image,
            subtitle = entity.subtitle,
            pubDate = entity.pubDate,
            director = "감독 : ${entity.director.convertStr()}",
            actor = "출연 : ${entity.actor.convertStr()}",
            userRating = "평점 : " + entity.userRating,
            favorite = entity.favorite ?: false
        )

        private fun String.convertStr() = if (this.isNotEmpty()) {
            this.split("|").joinToString(
                separator = ","
            ) { it }.dropLast(1)
        } else {
            ""
        }

        fun fromDto(
            dto: ItemsDto,
            prevKey: Int?,
            nextKey: Int?
        ) = LocalMovieEntity(
            id = dto.id,
            title = dto.title,
            link = dto.link,
            image = dto.image,
            subtitle = dto.subtitle,
            pubDate = dto.pubDate,
            director = dto.director,
            actor = dto.actor,
            userRating = dto.userRating,
            favorite = dto.favorite,
            prevKey = prevKey,
            nextKey = nextKey
        )
    }
}
