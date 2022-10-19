package com.a90ms.common.utils

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.RecyclerView
import com.a90ms.common.ext.px

open class RecyclerViewDividerDecoration(
    private val height: Float = 1f,
    protected open val padding: Float = 16f,
    @ColorInt private val color: Int = Color.parseColor("#f1f1f1"),
    private val startPosition: Int = 0
) : RecyclerView.ItemDecoration() {

    private val paint = Paint()

    init {
        paint.color = color
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val height = this.height.px
        val padding = this.padding.px

        val left = parent.paddingStart + padding
        val right = parent.width - parent.paddingEnd - padding
        for (i in 0 until parent.childCount - 1) {
            val child = parent.getChildAt(i)
            if (parent.getChildAdapterPosition(child) < startPosition) continue
            val params = child.layoutParams as RecyclerView.LayoutParams
            val top = (child.bottom + params.bottomMargin).toFloat()
            val bottom = top + height
            c.drawRect(left, top, right, bottom, paint)
        }
    }
}
