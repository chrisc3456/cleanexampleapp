package com.example.clean.ui

import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

/**
 * Custom divider class to handle not being shown at the end of the last entry in a list
 */
class DividerItemDecorator(private val mDivider: Drawable, private val viewOrientation: ViewOrientation) : ItemDecoration() {

    enum class ViewOrientation {
        HORIZONTAL,
        VERTICAL
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {

        val position = parent.getChildAdapterPosition(view)
        val listCount = parent.adapter?.itemCount ?: 0

        when (viewOrientation) {
            ViewOrientation.HORIZONTAL -> setHorizontalOffsets(outRect, 0, 0)
            ViewOrientation.VERTICAL -> setVerticalOffsets(outRect, position, listCount)
        }
    }

    private fun setVerticalOffsets(outRect: Rect, listPosition: Int, listCount: Int) {
        outRect.top = mDivider.intrinsicHeight
        if (listPosition == listCount - 1) {
            outRect.bottom = mDivider.intrinsicHeight
        }
    }

    private fun setHorizontalOffsets(outRect: Rect, listPosition: Int, listCount: Int) {
        outRect.left = mDivider.intrinsicWidth
        outRect.right = mDivider.intrinsicWidth
    }

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {

        var dividerTop = 0
        var dividerBottom = 0
        var dividerLeft = 0
        var dividerRight = 0

        /*when (viewOrientation) {
            ViewOrientation.HORIZONTAL -> {
                dividerTop = parent.paddingTop
                dividerBottom = parent.height - parent.paddingBottom
            }
            ViewOrientation.VERTICAL -> {
                dividerLeft = parent.paddingLeft
                dividerRight = parent.width - parent.paddingRight
            }
        }


        val childCount = parent.childCount
        for (i in 0..childCount - 1) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams

            when (viewOrientation) {
                ViewOrientation.HORIZONTAL -> {
                    dividerLeft = child.right + params.rightMargin
                    dividerRight = dividerLeft + mDivider.intrinsicWidth
                }
                ViewOrientation.VERTICAL -> {
                    dividerTop = child.bottom + params.bottomMargin
                    dividerBottom = dividerTop + mDivider.intrinsicHeight
                }
            }

            mDivider.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom)
            mDivider.draw(canvas)
        }*/

        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams

            dividerLeft = parent.paddingLeft
            dividerRight = parent.width - parent.paddingRight
            dividerTop = child.bottom + params.bottomMargin
            dividerBottom = dividerTop + mDivider.intrinsicHeight

            drawDivider(canvas, dividerLeft, dividerRight, dividerTop, dividerBottom)

            // Draw extra divider before the first entry
            if (i == 0) {
                drawDivider(canvas, dividerLeft, dividerRight, child.top - params.topMargin - mDivider.intrinsicHeight, child.top - params.topMargin)
            }
        }
    }

    private fun drawDivider(canvas: Canvas, left: Int, right: Int, top: Int, bottom: Int) {
        mDivider.setBounds(left, top, right, bottom)
        mDivider.draw(canvas)
    }
}