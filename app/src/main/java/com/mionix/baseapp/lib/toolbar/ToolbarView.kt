package com.mionix.baseapp.lib.toolbar

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.mionix.baseapp.R
import kotlinx.android.synthetic.main.layout_toolbar_view.view.*

class ToolbarView : Toolbar {
    constructor(context: Context) : super(context) {
        initializeView(context)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?
    ) : super(context, attrs) {
        initializeView(context)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        initializeView(context)
    }

    private fun initializeView(aContext: Context) {
        View.inflate(aContext, R.layout.layout_toolbar_view, this)

        //Setup for Toolbar
        setContentInsetsRelative(0, 0)
        setContentInsetsAbsolute(0, 0)
    }

    val leftAction: RelativeLayout
        get() = rlActionLeft

    val titleView: TextView
        get() = tvTitle

    val itemToolbar: Toolbar
        get() = tbItemToolbar
}