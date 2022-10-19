package com.a90ms.sample.ui.base

import com.a90ms.domain.dto.search.ItemsDto

sealed class MovieState {
    data class OnError(val msg: String) : MovieState()
    data class OnClickItem(val item: ItemsDto) : MovieState()
    data class ChangeFavoriteState(val updateValue: Boolean) : MovieState()
}
