package com.a90ms.sample.ui.main

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.SimpleItemAnimator
import com.a90ms.common.ext.hideKeyboard
import com.a90ms.common.ext.showKeyboard
import com.a90ms.common.ext.startActivity
import com.a90ms.common.ext.toast
import com.a90ms.common.utils.RecyclerViewDividerDecoration
import com.a90ms.domain.dto.search.ItemsDto
import com.a90ms.sample.BR
import com.a90ms.sample.R
import com.a90ms.sample.base.BaseActivity
import com.a90ms.sample.base.BaseSingleViewPagingAdapter
import com.a90ms.sample.base.bindSingleClick
import com.a90ms.sample.databinding.ActivityMainBinding
import com.a90ms.sample.ui.base.MovieState
import com.a90ms.sample.ui.base.MovieViewModel
import com.a90ms.sample.ui.favorite.FavoriteActivity
import com.a90ms.sample.ui.webview.DetailActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@ExperimentalPagingApi
@AndroidEntryPoint
class MainActivity :
    BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private val viewModel by viewModels<MainViewModel>()
    private val movieViewModel by viewModels<MovieViewModel>()
    private lateinit var adapter: BaseSingleViewPagingAdapter<ItemsDto>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupData()
        setupLoadingObserver(viewModel, movieViewModel)
        setupObserver()
        setupRecyclerView()
    }

    private fun setupData() {
        with(binding) {
            vm = viewModel

            srlMain.setOnRefreshListener {
                adapter.refresh()
                srlMain.isRefreshing = false
            }
            tilSearch.setEndIconOnClickListener {
                viewModel.clearSearchQuery()
                showKeyboard(binding.tieSearch)
            }
            tieSearch.setOnEditorActionListener { v, actionId, _ ->
                var handled = false

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    when {
                        v.text.isNullOrBlank() -> toast(getString(R.string.toast_empty_search))
                        v.text.isEmpty() -> toast(getString(R.string.toast_search_error))
                        else -> {
                            hideKeyboard(v)
                            viewModel.fetchData()
                        }
                    }
                    handled = true
                }
                handled
            }
            tvFavorite.bindSingleClick {
                startActivity<FavoriteActivity>()
            }
        }
    }

    private fun setupObserver() {
        val owner = this
        viewModel.run {
            state.observe(owner) {
                when (it) {
                    is MainState.OnError -> {}
                    is MainState.OnUpdateList -> {
                        lifecycleScope.launch {
                            binding.srlMain.isRefreshing = false
                            adapter.submitData(it.data)
                        }
                    }
                }
            }
        }
        movieViewModel.run {
            state.observe(owner) {
                when (it) {
                    is MovieState.OnError -> toast(it.msg)
                    is MovieState.OnClickItem -> {
                        startActivity<DetailActivity>(
                            DetailActivity.createBundle(it.item)
                        )
                    }
                    else -> {
                    }
                }
            }
        }
    }

    private fun setupRecyclerView() {
        binding.rvItems.run {
            val diffUtil = object : DiffUtil.ItemCallback<ItemsDto>() {
                override fun areItemsTheSame(
                    oldItem: ItemsDto,
                    newItem: ItemsDto
                ) = oldItem == newItem

                override fun areContentsTheSame(
                    oldItem: ItemsDto,
                    newItem: ItemsDto
                ) = oldItem == newItem
            }

            val animator = itemAnimator
            if (animator is SimpleItemAnimator) {
                animator.supportsChangeAnimations = false
            }

            addItemDecoration(RecyclerViewDividerDecoration())

            this@MainActivity.adapter = BaseSingleViewPagingAdapter(
                layoutResourceId = R.layout.item_movie,
                bindingItemId = BR.item,
                viewModel = mapOf(BR.vm to movieViewModel),
                diffUtil = diffUtil,
            ).apply {
                setupMediatorLoadStateListener(
                    scope = lifecycleScope,
                    countFlow = viewModel.countFlow,
                    isLoading = {
                        loadingState(it)
                    },
                    scrollTop = {
                        postDelayed(
                            {
                                scrollToPosition(0)
                            },
                            100
                        )
                    },
                    isListEmpty = {
                        binding.tvEmpty.isVisible = it
                    }
                )
            }
            adapter = this@MainActivity.adapter
        }
    }
}
