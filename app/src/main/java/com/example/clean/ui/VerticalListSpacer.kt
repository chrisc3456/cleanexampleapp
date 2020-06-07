package com.example.clean.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class VerticalListSpacer(context: Context, verticalSpacingDrawableId: Int?, horizontalSpacingDrawableId: Int?, private val includeStartEndSpacing: Boolean): RecyclerView.ItemDecoration() {

    private var verticalSpacerDrawable: Drawable? = null
    private var horizontalSpacerDrawable: Drawable? = null

    init {
        verticalSpacingDrawableId?.let { verticalSpacerDrawable = ContextCompat.getDrawable(context, it) }
        horizontalSpacingDrawableId?.let { horizontalSpacerDrawable = ContextCompat.getDrawable(context, it) }
    }

    /**
     * Specifies the offsets which apply to each view as defined by the width and height of the provided spacer drawables
     */
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val listPosition = parent.getChildAdapterPosition(view)
        val listCount = parent.adapter?.itemCount ?: 0

        // Apply spacing before each item with the exception of the first entry if not required with further additional spacing after the last entry if required
        verticalSpacerDrawable?.let {
            if (listPosition != 0 || includeStartEndSpacing) {
                outRect.top = verticalSpacerDrawable?.intrinsicHeight ?: 0
            }

            if (listPosition == listCount - 1 && includeStartEndSpacing) {
                outRect.bottom = verticalSpacerDrawable?.intrinsicHeight ?: 0
            }
        }

        // Apply additional spacing to the left and right if required
        horizontalSpacerDrawable?.let {
            outRect.left = it.intrinsicWidth
            outRect.right = it.intrinsicWidth
        }
    }

    /**
     * Draw a horizontal and vertical spacer image as required
     */
    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val childCount = parent.childCount
        for (index in 0 until childCount) {
            val childLayout = parent.getChildAt(index)

            drawVerticalSpacers(canvas, childLayout, index, childCount)
            drawHorizontalSpacers(canvas, childLayout)
        }
    }

    /**
     * Draw vertical spacers to the top and bottom of the specified child layout if required
     */
    private fun drawVerticalSpacers(canvas: Canvas, childLayout: View, childIndex: Int, childCount: Int) {
        verticalSpacerDrawable?.let {

            // Adjust the left and right bounds of the vertical spacer drawable to take account of where a horizontal spacer is also present
            var spacerLeft = childLayout.left
            var spacerRight = childLayout.right
            horizontalSpacerDrawable?.let { drawable ->
                spacerLeft -= drawable.intrinsicWidth
                spacerRight += drawable.intrinsicWidth
            }

            val childLayoutParams = childLayout.layoutParams as RecyclerView.LayoutParams

            // Only draw a top spacer for the first entry if start and end spacers are to be included
            if (childIndex == 0 && includeStartEndSpacing) {
                val spacerBottom = childLayout.top - childLayoutParams.topMargin
                val spacerTop = spacerBottom - it.intrinsicHeight
                drawSpacer(canvas, it, spacerLeft, spacerRight, spacerTop, spacerBottom)
            }

            // Draw a bottom spacer for all entries except the last if start and end spacers are not to be included
            if (childIndex != childCount - 1 || includeStartEndSpacing) {
                val spacerTop = childLayout.bottom + childLayoutParams.bottomMargin
                val spacerBottom = spacerTop + it.intrinsicHeight
                drawSpacer(canvas, it, spacerLeft, spacerRight, spacerTop, spacerBottom)
            }
        }
    }

    /**
     * Draw horizontal spacers to the left and right of the specified child layout if required
     */
    private fun drawHorizontalSpacers(canvas: Canvas, childLayout: View) {
        horizontalSpacerDrawable?.let {
            drawSpacer(canvas, it, childLayout.left - it.intrinsicWidth, childLayout.left, childLayout.top, childLayout.bottom)
            drawSpacer(canvas, it, childLayout.right, childLayout.right + it.intrinsicWidth, childLayout.top, childLayout.bottom)
        }
    }

    /**
     * Draw the spacer to the canvas from the provided position params
     */
    private fun drawSpacer(canvas: Canvas, drawable: Drawable, left: Int, right: Int, top: Int, bottom: Int) {
        drawable.setBounds(left, top, right, bottom)
        drawable.draw(canvas)
    }
}