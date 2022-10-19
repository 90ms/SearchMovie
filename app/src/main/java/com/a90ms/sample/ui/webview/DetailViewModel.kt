package com.a90ms.sample.ui.webview

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.a90ms.domain.dto.search.ItemsDto
import com.a90ms.sample.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    val item = savedStateHandle.getLiveData<ItemsDto>(DetailActivity.BUNDLE_ITEM)

    val url = MutableLiveData(item.value?.link)

    fun updateFavorite(value: Boolean) {
        item.value = item.value?.copy(favorite = value)
    }
}
