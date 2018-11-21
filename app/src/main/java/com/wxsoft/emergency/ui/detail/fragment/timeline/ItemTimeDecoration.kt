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
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
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
import com.wxsoft.emergency.ui.emr.indexTimeLineHeader
import com.wxsoft.emergency.widget.withTranslation

/**
 * A [RecyclerView.ItemDecoration] which draws sticky headers marking the days in a given list of
 * [Block]s. It also inserts gaps between days.
 */
class ItemTimeDecoration(
        context: Context,
        items:List<EmrItem>
) : RecyclerView.ItemDecoration() {

    private val paddingLeft: Int
    private val width: Int
    private val paint: TextPaint
    init {
        val attrs = context.obtainStyledAttributes(
                R.style.Widget_Emergency_ProcessHeaders,
                R.styleable.EmrItemHeader
        )
        paint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            color = attrs.getColor(R.styleable.TimeHeader_android_textColor,0)
            textSize = attrs.getDimension(R.styleable.EmrItemHeader_timeTextSize,10f)
            try {
                typeface = ResourcesCompat.getFont(
                    context,
                    attrs.getResourceId(R.styleable.TimeHeader_android_fontFamily,0)
                )
            } catch (_: Exception) {
                // ignore
            }
        }
        paddingLeft = attrs.getDimensionPixelSize(R.styleable.TimeHeader_android_width,0)
        width = attrs.getDimensionPixelSize(R.styleable.TimeHeader_android_width,0)
    }

    // Get the emr index:usercase and create header layouts for each
    private val daySlots: Map<Int, StaticLayout> =
            indexLineTime(items).map {
                it.first to createHeader(it.second)
            }.toMap()

    /**
     *  Add gaps between days, split over the last and first block of a day.
     */
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: State) {
        val position = parent.getChildAdapterPosition(view)
        if (position <= 0) return



    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: State) {
        if (daySlots.isEmpty() || parent.childCount==0) return
        var earliestFoundHeaderPos = -1
        var prevHeaderTop = Int.MAX_VALUE

        for (i in parent.childCount - 1 downTo 0) {
            val view = parent.getChildAt(i)
            val viewTop = view.top + view.translationY.toInt()
            if (view.bottom > 0 && viewTop < parent.height) {
                val position = parent.getChildAdapterPosition(view)
                daySlots[position]?.let { layout ->
                    paint.alpha = (view.alpha * 255).toInt()
                    val top = (viewTop)
                        .coerceAtMost(prevHeaderTop - layout.height)
                    c.withTranslation(y = top.toFloat()+((view.height-layout.height)/2).toFloat(),x= paddingLeft.toFloat()) {
                        layout.draw(c)
                    }

                    earliestFoundHeaderPos = position
                    prevHeaderTop = viewTop
                }
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
