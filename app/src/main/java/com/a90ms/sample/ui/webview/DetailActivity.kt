package com.a90ms.sample.ui.webview

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.webkit.CookieManager
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebViewClient
import androidx.activity.viewModels
import androidx.core.os.bundleOf
import com.a90ms.common.ext.toast
import com.a90ms.domain.dto.search.ItemsDto
import com.a90ms.sample.R
import com.a90ms.sample.base.BaseActivity
import com.a90ms.sample.databinding.ActivityDetailBinding
import com.a90ms.sample.ui.base.MovieState
import com.a90ms.sample.ui.base.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity : BaseActivity<ActivityDetailBinding>(R.layout.activity_detail) {

    private val viewModel by viewModels<DetailViewModel>()
    private val movieViewModel by viewModels<MovieViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupBinding()
        setupLoadingObserver(viewModel)
        setupWebSetting()
        setupObserver()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebSetting() {
        binding.webView.apply {
            webChromeClient = WebChromeClient()
            webViewClient = WebViewClient()

            setLayerType(View.LAYER_TYPE_HARDWARE, null)
            settings.apply {
                javaScriptEnabled = true
                useWideViewPort = true
                loadWithOverviewMode = true
                builtInZoomControls = true
                setSupportZoom(true)
                displayZoomControls = false
                mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                javaScriptCanOpenWindowsAutomatically = true
                setSupportMultipleWindows(true)
                CookieManager.getInstance().run {
                    setAcceptCookie(true)
                    setAcceptThirdPartyCookies(binding.webView, true)
                }
            }
        }
    }

    private fun setupBinding() {
        with(binding) {
            vm = viewModel
            movieVm = movieViewModel
            mtbWeb.setNavigationOnClickListener {
                onBackPressed()
            }
        }
    }

    private fun setupObserver() {
        viewModel.item.observe(this) {
        }
        movieViewModel.state.observe(this) {
            when (it) {
                is MovieState.ChangeFavoriteState -> {
                    viewModel.updateFavorite(it.updateValue)
                }
                is MovieState.OnError -> toast(it.msg)
                else -> {
                }
            }
        }
    }

    override fun onBackPressed() {
        if (binding.webView.canGoBack()) {
            binding.webView.goBack()
        } else {
            super.onBackPressed()
        }
    }

    companion object {

        const val BUNDLE_ITEM = "item"

        fun createBundle(item: ItemsDto) = bundleOf(
            BUNDLE_ITEM to item
        )
    }
}
