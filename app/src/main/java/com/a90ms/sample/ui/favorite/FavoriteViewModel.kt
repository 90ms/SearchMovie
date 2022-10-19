package com.a90ms.sample.ui.favorite

import androidx.lifecycle.asLiveData
import com.a90ms.domain.usecase.GetFavoriteListUseCase
import com.a90ms.sample.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    getFavoriteListUseCase: GetFavoriteListUseCase
) : BaseViewModel() {
    val itemList = getFavoriteListUseCase.getFavoriteList().asLiveData()
}
