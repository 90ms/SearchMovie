package com.a90ms.sample.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.a90ms.domain.base.onError
import com.a90ms.domain.base.onException
import com.a90ms.domain.base.onSuccess
import com.a90ms.domain.dto.search.ItemsDto
import com.a90ms.domain.usecase.FavoriteParams
import com.a90ms.domain.usecase.UpdateFavoriteUseCase
import com.a90ms.sample.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val updateFavoriteUseCase: UpdateFavoriteUseCase
) : BaseViewModel() {

    private val _state = MutableLiveData<MovieState>()
    val state: LiveData<MovieState> get() = _state

    fun onClickFavorite(dto: ItemsDto) {
        viewModelScope.launch {
            updateFavoriteUseCase(FavoriteParams(dto.id, dto.favorite)).onSuccess {
                _state.value = MovieState.ChangeFavoriteState(!dto.favorite)
            }.onError { _, message ->
                _state.value = MovieState.OnError(message)
            }.onException {
                _state.value = MovieState.OnError(it.message ?: "exception")
            }
        }
    }

    fun onClickItem(item: ItemsDto) {
        _state.value = MovieState.OnClickItem(item)
    }
}
