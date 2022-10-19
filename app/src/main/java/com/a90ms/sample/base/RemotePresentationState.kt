package com.a90ms.sample.base

import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.scan

/**
 * An enum representing the status of items in the as fetched by the
 * [Pager] when used with a [RemoteMediator]
 */
enum class RemotePresentationState {
    INITIAL, REMOTE_LOADING, SOURCE_LOADING, PRESENTED, REMOTE_ERROR, SOURCE_ERROR
}

/**
 * Reduces [CombinedLoadStates] into [RemotePresentationState]. It operates ton the assumption that
 * successful [RemoteMediator] fetches always cause invalidation of the [PagingSource] as in the
 * case of the [PagingSource] provide by Room.
 */
fun Flow<CombinedLoadStates>.asRemotePresentationState(): Flow<RemotePresentationState> =
    scan(RemotePresentationState.INITIAL) { state, loadState ->
        when (state) {
            RemotePresentationState.PRESENTED -> when (loadState.mediator?.refresh) {
                is LoadState.Loading -> RemotePresentationState.REMOTE_LOADING
                is LoadState.Error -> RemotePresentationState.REMOTE_ERROR
                else -> state
            }
            RemotePresentationState.INITIAL -> when (loadState.mediator?.refresh) {
                is LoadState.Loading -> RemotePresentationState.REMOTE_LOADING
                is LoadState.Error -> RemotePresentationState.REMOTE_ERROR
                else -> state
            }
            RemotePresentationState.REMOTE_LOADING -> when (loadState.source.refresh) {
                is LoadState.Loading -> RemotePresentationState.SOURCE_LOADING
                is LoadState.Error -> RemotePresentationState.SOURCE_ERROR
                else -> state
            }
            RemotePresentationState.SOURCE_LOADING -> when (loadState.source.refresh) {
                is LoadState.NotLoading -> RemotePresentationState.PRESENTED
                is LoadState.Error -> RemotePresentationState.SOURCE_ERROR
                else -> state
            }
            RemotePresentationState.REMOTE_ERROR -> when (loadState.mediator?.refresh) {
                is LoadState.Loading -> RemotePresentationState.REMOTE_LOADING
                else -> state
            }
            RemotePresentationState.SOURCE_ERROR -> when (loadState.source.refresh) {
                is LoadState.Loading -> RemotePresentationState.SOURCE_LOADING
                else -> state
            }
        }
    }.distinctUntilChanged()
