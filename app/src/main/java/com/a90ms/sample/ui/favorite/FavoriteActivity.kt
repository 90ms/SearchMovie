package com.a90ms.sample.ui.favorite

import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.SimpleItemAnimator
import com.a90ms.common.ext.startActivity
import com.a90ms.common.ext.toast
import com.a90ms.common.utils.RecyclerViewDividerDecoration
import com.a90ms.domain.dto.search.ItemsDto
import com.a90ms.sample.BR
import com.a90ms.sample.R
import com.a90ms.sample.base.BaseActivity
import com.a90ms.sample.base.BaseSingleViewAdapter
import com.a90ms.sample.databinding.ActivityFavoriteBinding
import com.a90ms.sample.ui.base.MovieState
import com.a90ms.sample.ui.base.MovieViewModel
import com.a90ms.sample.ui.webview.DetailActivity
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class FavoriteActivity : BaseActivity<ActivityFavoriteBinding>(R.layout.activity_favorite) {

    private val viewModel by viewModels<FavoriteViewModel>()
    private val movieViewModel by viewModels<MovieViewModel>()

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
            mtbFavorite.setNavigationOnClickListener {
                finish()
            }
        }
    }

    private fun setupObserver() {
        val owner = this
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
        viewModel.itemList.observe(owner) {
            Timber.d(it.toString())
        }
    }

    private fun setupRecyclerView() {
        binding.rvItems.run {
            val diffUtil = object : DiffUtil.ItemCallback<ItemsDto>() {
                override fun areItemsTheSame(
                    oldItem: ItemsDto,
                    newItem: ItemsDto
                ): Boolean = oldItem == newItem

                override fun areContentsTheSame(
                    oldItem: ItemsDto,
                    newItem: ItemsDto
                ): Boolean = oldItem == newItem
            }

            val animator = itemAnimator
            if (animator is SimpleItemAnimator) {
                animator.supportsChangeAnimations = false
            }

            addItemDecoration(RecyclerViewDividerDecoration())

            adapter = BaseSingleViewAdapter(
                layoutResourceId = R.layout.item_movie,
                bindingItemId = BR.item,
                viewModel = mapOf(BR.vm to movieViewModel),
                diffUtil = diffUtil
            )
        }
    }
}
