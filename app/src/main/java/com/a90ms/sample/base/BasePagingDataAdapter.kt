package com.a90ms.sample.base

import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.dropWhile
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

abstract class BasePagingDataAdapter<ITEM : Any, VH : RecyclerView.ViewHolder>(
    diffUtil: DiffUtil.ItemCallback<ITEM>
) : PagingDataAdapter<ITEM, VH>(diffUtil) {

    private var previousLoading: Boolean? = null

    private fun setupScrollTop(
        scope: CoroutineScope,
        scrollTop: () -> Unit
    ) {
        scope.launch {
            loadStateFlow
                .dropWhile {
                    it.refresh is LoadState.NotLoading &&
                        it.append is LoadState.NotLoading &&
                        it.prepend is LoadState.NotLoading
                }
                .distinctUntilChangedBy { it.refresh }
                .collect {
                    if (it.refresh is LoadState.NotLoading) {
                        scrollTop()
                    }
                }
        }
    }

    fun setupMediatorLoadStateListener(
        scope: CoroutineScope,
        countFlow: Flow<Int>,
        isLoading: ((Boolean) -> Unit)? = null,
        isListEmpty: ((Boolean) -> Unit)? = null,
        scrollTop: (() -> Unit)? = null,
        isError: ((String) -> Unit)? = null,
    ) {
        // scroll
        scope.launch {
            loadStateFlow.asRemotePresentationState().map {
                it == RemotePresentationState.PRESENTED
            }.collect {
                scrollTop?.invoke()
            }
        }

        var count = 0
        scope.launch(Dispatchers.IO) {
            countFlow.collect {
                count = it
            }
        }

        scope.launch {
            loadStateFlow.dropWhile {
                it.refresh is LoadState.NotLoading &&
                    it.append is LoadState.NotLoading &&
                    it.prepend is LoadState.NotLoading
            }.distinctUntilChanged()
                .collectLatest { loadState ->
                    val loading = loadState.mediator?.refresh is LoadState.Loading
                    if (previousLoading != loading) {
                        isLoading?.invoke(loading)
                        previousLoading = loading
                    }

                    isListEmpty?.invoke(
                        loadState.refresh is LoadState.NotLoading && count == 0
                    )

                    val errorState = loadState.append as? LoadState.Error
                        ?: loadState.prepend as? LoadState.Error
                        ?: loadState.refresh as? LoadState.Error
                    errorState?.let { isError?.invoke(it.error.message ?: "Error") }
                }
        }
    }
}
