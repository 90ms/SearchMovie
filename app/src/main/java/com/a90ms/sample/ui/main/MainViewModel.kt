package com.a90ms.sample.ui.main

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.cachedIn
import androidx.paging.map
import com.a90ms.domain.usecase.ClearDatabaseUseCase
import com.a90ms.domain.usecase.GetMovieListUseCase
import com.a90ms.sample.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel @Inject constructor(
    private val clearDatabaseUseCase: ClearDatabaseUseCase,
    private val getMovieListUseCase: GetMovieListUseCase
) : BaseViewModel() {

    private val _state = MutableLiveData<MainState>()
    val state: LiveData<MainState> get() = _state

    val existItem: LiveData<Boolean> = getMovieListUseCase.exist().asLiveData()

    val countFlow = getMovieListUseCase.count()

    val searchQuery = ObservableField<String>()

    private val _initView = MutableLiveData(true)
    val initView: LiveData<Boolean> get() = _initView

    init {
        viewModelScope.launch { clearDatabaseUseCase() }
    }

    @ExperimentalPagingApi
    fun fetchData() {
        viewModelScope.launch {
            _initView.value = false
            val query = searchQuery.get() ?: ""
            getMovieListUseCase(query).map {
                it.map { dto ->
                    dto
                }
            }.cachedIn(viewModelScope).collect {
                _state.value = MainState.OnUpdateList(it)
            }
        }
    }

    fun clearSearchQuery() {
        searchQuery.set("")
    }
}
