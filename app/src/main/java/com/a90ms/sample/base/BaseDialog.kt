package com.a90ms.sample.base

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.WindowManager
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatDialog
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

class BaseDialog<VDB : ViewDataBinding>(
    context: Context,
    @LayoutRes private val layoutResourceId: Int,
    private val enableDim: Boolean = true
) :
    AppCompatDialog(context) {

    private lateinit var binding: VDB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.inflate(layoutInflater, layoutResourceId, null, false)
        setContentView(binding.root)

        setCancelable(false)
        window?.apply {
            attributes = attributes.apply {
                width = WindowManager.LayoutParams.MATCH_PARENT
                height = WindowManager.LayoutParams.WRAP_CONTENT
            }
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            if (enableDim.not()) {
                setDimAmount(0f)
            }
        }
    }

    fun onShow(action: BaseDialog<VDB>.(binding: VDB) -> Unit) =
        apply { setOnShowListener { action(binding) } }
}
