package com.a90ms.sample.base

import android.graphics.drawable.Drawable
import android.text.Html
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.a90ms.common.ext.isValidContext
import com.a90ms.common.utils.OnSingleClickListener
import com.a90ms.sample.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.appbar.MaterialToolbar

@BindingAdapter("bindItemList")
fun RecyclerView.bindItemList(item: List<Any>?) {
    if (item == null) return

    @Suppress("UNCHECKED_CAST")
    (adapter as? BaseSingleViewAdapter<Any>)?.run {
        submitList(item)
    }
}

class BaseSingleViewAdapter<ITEM : Any>(
    @LayoutRes private val layoutResourceId: Int,
    private val bindingItemId: Int,
    private val viewModel: Map<Int, Any>,
    diffUtil: DiffUtil.ItemCallback<ITEM>,
    val infinite: Boolean = false
) : ListAdapter<ITEM, BaseViewHolder>(diffUtil) {

    var itemClickListener: ((item: Any, pos: Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder =
        BaseViewHolder(
            parent = parent,
            layoutResourceId = layoutResourceId,
            bindingItemId = bindingItemId,
            viewModel = viewModel
        )

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        if (itemClickListener == null) {
            holder.bind(getItem(position))
        } else {
            holder.bind(getItem(position), itemClickListener!!, position)
        }
    }

    override fun getItemCount(): Int {
        return if (infinite && currentList.isNotEmpty()) {
            Integer.MAX_VALUE
        } else {
            super.getItemCount()
        }
    }

    override fun getItem(position: Int): ITEM {
        return super.getItem(
            if (infinite && currentList.isNotEmpty()) {
                position % currentList.size
            } else {
                position
            }
        )
    }
}

class BaseSingleViewPagingAdapter<ITEM : Any>(
    @LayoutRes private val layoutResourceId: Int,
    private val bindingItemId: Int,
    private val viewModel: Map<Int, ViewModel>,
    diffUtil: DiffUtil.ItemCallback<ITEM>
) : BasePagingDataAdapter<ITEM, BaseViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder =
        BaseViewHolder(
            parent = parent,
            layoutResourceId = layoutResourceId,
            bindingItemId = bindingItemId,
            viewModel = viewModel
        )

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }
}

@BindingAdapter("bindVisible")
fun View.bindVisible(show: Boolean?) {
    isVisible = show ?: false
}

@BindingAdapter("bindWebUrl")
fun WebView.bindWebUrl(url: String?) {
    url?.let { loadUrl(it) }
}

@BindingAdapter("bindSingleClick")
fun View.bindSingleClick(clickListener: View.OnClickListener?) {
    clickListener?.also {
        setOnClickListener(OnSingleClickListener(it))
    } ?: setOnClickListener(null)
}

@BindingAdapter(
    value = [
        "bindImage",
        "bindPlaceHolder"
    ],
    requireAll = false
)
fun ImageView.bindImage(
    url: String?,
    placeHolder: Drawable? = null,
) {
    if (context.isValidContext()) Glide.with(context)
        .load(url)
        .apply(
            RequestOptions()
                .error(placeHolder)
                .placeholder(placeHolder).centerCrop()
        )
        .into(this)
}

@BindingAdapter("bindHtml")
fun TextView.bindHtml(html: String?) {
    html?.let { text = Html.fromHtml(it, Html.FROM_HTML_MODE_COMPACT) }
}

@BindingAdapter("bindFavorite")
fun ImageButton.bindFavorite(favorite: Boolean) {
    setImageResource(
        if (favorite) {
            R.drawable.favorite_on
        } else {
            R.drawable.favorite_off
        }
    )
}

@BindingAdapter("bindToolbarTitle")
fun MaterialToolbar.bindToolbarTitle(html: String?) {
    html?.let { title = Html.fromHtml(it, Html.FROM_HTML_MODE_COMPACT) }
}
