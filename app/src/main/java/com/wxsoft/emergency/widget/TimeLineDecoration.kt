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

package com.wxsoft.emergency.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.State
import android.view.View
import com.wxsoft.emergency.R
import com.wxsoft.emergency.ui.emr.indexTimeLineHeader

/**
 * A [RecyclerView.ItemDecoration] which draws sticky headers marking the days in a given list of
 * [Block]s. It also inserts gaps between days.
 */
class TimeLineDecoration(
        context: Context,
        items:List<Any>
) : RecyclerView.ItemDecoration() {

    private val drawableStart: Drawable = context.resources.getDrawable(R.drawable.bg_vertical_line_start,context.theme)
    private val drawableEnd: Drawable = context.resources.getDrawable(R.drawable.bg_vertical_line_end,context.theme)
    private val drawable: Drawable = context.resources.getDrawable(R.drawable.bg_vertical_line,context.theme)
    private val paddingTop: Int
    private val paddingLeft: Int
    private val width: Int
    init {
        val attrs = context.obtainStyledAttributes(
                R.style.Widget_Emergency_ProcessHeaders,
                R.styleable.EmrItemHeader
        )

        paddingTop = attrs.getDimensionPixelSize(R.styleable.TimeHeader_android_paddingTop,0)
        paddingLeft = attrs.getDimensionPixelSize(R.styleable.TimeHeader_android_paddingTop,0)
        width = attrs.getDimensionPixelSize(R.styleable.TimeHeader_android_width,0)
    }

    // Get the emr index:usercase and create header layouts for each
    private val daySlots: Map<Int, Drawable> =
            indexTimeLineHeader(items).map {
                it.first to if(it.second)  drawableEnd else drawable
            }.toMap()

    /**
     *  Add gaps between days, split over the last and first block of a day.
     */
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: State) {
        val position = parent.getChildAdapterPosition(view)
        if (position <= 0) return

        if (daySlots.containsKey(position)) {
            // first block of day, pad top
            outRect.top =  paddingTop

            outRect.right=paddingTop
            outRect.bottom = paddingTop
        } else if (daySlots.containsKey(position + 1)) {
            // last block of day, pad bottom
            outRect.right=paddingTop
            outRect.bottom = paddingTop
        }

    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: State) {
        if (daySlots.isEmpty() || parent.childCount==0) return

        for (i in parent.childCount - 1 downTo 0) {
            val view = parent.getChildAt(i)

            val position = parent.getChildAdapterPosition(view)
            daySlots[position]?.let { layout ->

                var rect =  Rect(0, 0, 0, 0);

                rect.top = view.top-paddingTop;
                rect.bottom = view.bottom+paddingTop;
                rect.left = width;
                rect.right = rect.left + layout.intrinsicWidth;
                layout.bounds=rect
                layout.draw(c)
            }
        }

    }



}
