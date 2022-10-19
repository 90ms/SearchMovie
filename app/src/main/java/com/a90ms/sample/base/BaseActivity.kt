package com.a90ms.sample.base

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDialog
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.lifecycleScope
import com.a90ms.sample.R
import com.a90ms.sample.databinding.DialogLoadingBinding
import javax.inject.Inject

abstract class BaseActivity<VDB : ViewDataBinding>(
    @LayoutRes private val layoutResourceId: Int
) : AppCompatActivity(), BaseInterface {

    protected lateinit var binding: VDB

    @Inject
    lateinit var loadingState: LoadingState

    private var loadingDialog: AppCompatDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        binding = DataBindingUtil.setContentView<VDB>(this, layoutResourceId).apply {
            lifecycleOwner = this@BaseActivity
        }
    }

    fun setupLoadingObserver(vararg viewModels: BaseViewModel) {
        lifecycleScope.launchWhenStarted {
            viewModels.forEach { viewModel ->
                viewModel.loadingState.loading.observe(this@BaseActivity) {
                    if (it) showSpinnerLoading() else hideSpinnerLoading()
                }
            }
        }
    }

    private fun showSpinnerLoading() {
        if (loadingDialog == null) {
            loadingDialog = BaseDialog<DialogLoadingBinding>(
                this,
                R.layout.dialog_loading,
                enableDim = false
            )
        }
        if (loadingDialog?.isShowing == false && !isFinishing) {
            loadingDialog?.show()
        }
    }

    private fun hideSpinnerLoading() {
        loadingDialog?.dismiss()
    }

    fun isLoadingState(isShow: Boolean) {
        loadingState(isShow)
    }

    override fun loadingState(isShow: Boolean) {
        if (isShow) showSpinnerLoading() else hideSpinnerLoading()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.clear()
    }
}
