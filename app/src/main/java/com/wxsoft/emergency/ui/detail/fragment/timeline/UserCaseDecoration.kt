/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wxsoft.emergency.ui.detail.fragment.timeline

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.graphics.Rect
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.State
import android.text.Layout
import android.text.SpannableStringBuilder
import android.text.StaticLayout
import android.text.TextPaint
import android.view.View
import com.wxsoft.emergency.R
import com.wxsoft.emergency.data.entity.EmrItem

/**
 * A [RecyclerView.ItemDecoration] which draws sticky headers marking the days in a given list of
 * [Block]s. It also inserts gaps between days.
 */
class UserCaseDecoration(
        context: Context,
        items:List<EmrItem>
) : RecyclerView.ItemDecoration() {

    private val paint: TextPaint
    private val width: Int
    private val paddingTop: Int


    init {
        val attrs = context.obtainStyledAttributes(
            R.style.Widget_Emergency_ProcessHeaders,
            R.styleable.EmrItemHeader
        )
        paint = TextPaint(ANTI_ALIAS_FLAG).apply {
            color = attrs.getColor(R.styleable.TimeHeader_android_textColor,0)
            textSize = attrs.getDimension(R.styleable.EmrItemHeader_userCaseTextSize,14f)
            try {
                typeface = ResourcesCompat.getFont(
                    context,
                    attrs.getResourceId(R.styleable.TimeHeader_android_fontFamily,0)
                )
            } catch (_: Exception) {
                // ignore
            }
        }
        width = attrs.getDimensionPixelSize(R.styleable.TimeHeader_android_width,0)
        paddingTop = attrs.getDimensionPixelSize(R.styleable.TimeHeader_android_paddingTop,0)

        attrs.recycle()
    }

    // Get the emr index:usercase and create header layouts for each
    private val daySlots: Map<Int, StaticLayout> =
        indexHeader(items).map {
            it.first to createHeader(it.second)
        }.toMap()

    /**
     *  Add gaps between days, split over the last and first block of a day.
     */
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: State) {
        val position = parent.getChildAdapterPosition(view)
        if (position <= 0) return

//        if (daySlots.containsKey(position)) {
//            // first block of day, pad top
//            outRect.top =  paddingTop
//
//            outRect.bottom = paddingTop
//        } else if (daySlots.containsKey(position + 1)) {
//            // last block of day, pad bottom
//            outRect.bottom = paddingTop
//        }

    }

    /**
     * Loop over each child and draw any corresponding headers i.emr. items who's position is a key in
     * [daySlots]. We also look back to see if there are any headers _before_ the first header we
     * found i.emr. which needs to be sticky.
     */

    override fun onDraw(c: Canvas, parent: RecyclerView, state: State) {
        if (daySlots.isEmpty() || parent.childCount==0) return

        var earliestFoundHeaderPos = -1
        var prevHeaderTop = Int.MAX_VALUE

        // Loop over each attached view looking for header items.
        // Loop backwards as a lower header can push another higher one upward.
        for (i in parent.childCount - 1 downTo 0) {
            val view = parent.getChildAt(i)
            val viewTop = view.top + view.translationY.toInt()
            if (view.bottom > 0 && viewTop < parent.height) {
                val position = parent.getChildAdapterPosition(view)
                daySlots[position]?.let { layout ->
                    paint.alpha = (view.alpha * 255).toInt()
                    val top = (viewTop + paddingTop)
                            .coerceAtLeast(paddingTop)
                            .coerceAtMost(prevHeaderTop - layout.height)
                    c.withTranslation(y = top.toFloat()) {
                        layout.draw(c)
                    }

                    earliestFoundHeaderPos = position
                    prevHeaderTop = viewTop - paddingTop - paddingTop
                }
            }
        }

        // If no headers found, ensure header of the first shown item is drawn.
        if (earliestFoundHeaderPos < 0) {
            earliestFoundHeaderPos = parent.getChildAdapterPosition(parent.getChildAt(0)) + 1
        }

        // Look back over headers to see if a prior item should be drawn sticky.
        for (headerPos in daySlots.keys.reversed()) {
            if (headerPos < earliestFoundHeaderPos) {
                daySlots[headerPos]?.let { layout ->
                    val top = (prevHeaderTop - layout.height).coerceAtMost(paddingTop)
                    c.withTranslation(y = top.toFloat()) {
                        layout.draw(c)
                    }
                }
                break
            }
        }
    }

    /**
     * Create a header layout for the given [userCase]
     */
    private fun createHeader(userCase: String): StaticLayout {
        val text = SpannableStringBuilder(userCase)

//                .apply {
//            append(System.lineSeparator())
//        }

        return StaticLayout(text, paint, width, Layout.Alignment.ALIGN_CENTER, 1f, 0f, true)
    }
}


inline fun Canvas.withTranslation(
    x: Float = 0.0f,
    y: Float = 0.0f,
    block: Canvas.() -> Unit
) {
    val checkpoint = save()
    translate(x, y)
    try {
        block()
    } finally {
        restoreToCount(checkpoint)
    }
}