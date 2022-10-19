package com.a90ms.sample.ui.main

import androidx.paging.PagingData
import com.a90ms.domain.dto.search.ItemsDto

sealed class MainState {
    data class OnError(val msg: String) : MainState()
    data class OnUpdateList(val data: PagingData<ItemsDto>) : MainState()
}
