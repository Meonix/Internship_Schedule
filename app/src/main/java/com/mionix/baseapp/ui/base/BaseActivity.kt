package com.mionix.baseapp.ui.base

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.FrameLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.mionix.baseapp.R
import com.mionix.baseapp.lib.toolbar.ToolbarView
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.android.synthetic.main.layout_toolbar_view.view.*

abstract class BaseActivity : AppCompatActivity() {

    protected lateinit var progressLoading: AlertDialog
    protected lateinit var dialogLoginOrRegister: AlertDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        window.requestFeature(Window.FEATURE_ACTION_BAR)
        super.onCreate(savedInstanceState)
//        progressLoading = SimpleProgressDialog.progressLoading(this)
//        dialogLoginOrRegister = SimpleProgressDialog.dialogLoginOrRegister(this, object : SimpleProgressDialog.ButtonListener {
//            override fun onClick(result: String?) {
//                dialogLoginOrRegister.dismiss()
//            }
//        })

        supportActionBar?.hide()
        setContentView(R.layout.activity_base)
        overridePendingTransitionEnter()
    }

    abstract fun onClickActionLeftListener()
    abstract fun setTitleToolbar():String?


    override fun setContentView(layoutResID: Int) {
        val coordinatorLayout: ConstraintLayout = layoutInflater.inflate(R.layout.activity_base, null) as ConstraintLayout
        val activityContainer: FrameLayout = coordinatorLayout.findViewById(R.id.flResult)
        val toolbarView = coordinatorLayout.findViewById(R.id.toolbarView) as ToolbarView
        layoutInflater.inflate(layoutResID, activityContainer, true)
        super.setContentView(coordinatorLayout)

        setTitleToolbar()?.let { toolbarView.tvTitle.text = it }

        toolbarView.rlActionLeft.setOnClickListener{
            onClickActionLeftListener()
        }
        toolbarView.ivActionRight.setOnClickListener(View.OnClickListener {
            onBackPressed()
        })
    }
    protected  fun setActionLeftVisibles(boolean: Boolean){
        if (boolean) {
            toolbarView.tvActionLeft.visibility = View.GONE
            toolbarView.ivActionLeft.visibility = View.VISIBLE
        }
        else{
            toolbarView.tvActionLeft.visibility = View.VISIBLE
            toolbarView.ivActionLeft.visibility = View.VISIBLE
        }

    }

    protected fun setBackgroundToolbar(drawable: Drawable){
        toolbarView.itemToolbar.background = drawable
    }

    protected fun setTextActionLeft(string: String){
        toolbarView.tvActionLeft.text = string
    }

    protected fun setActionLeftIcon(boolean: Boolean){
        if(boolean){
            toolbarView.ivActionLeft.setImageResource(R.drawable.ic_back)
            toolbarView.ivActionLeft.visibility = View.VISIBLE
        }else{
            toolbarView.ivActionLeft.visibility = View.GONE
        }
    }



    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransitionExit()
    }

    override fun finish() {
        super.finish()
        overridePendingTransitionExit()
    }

    /**
     * Overrides the pending Activity transition by performing the "Enter" animation.
     */
    private fun overridePendingTransitionEnter() {
        overridePendingTransition(R.anim.slide_left,  R.anim.no_animation)
    }

    /**
     * Overrides the pending Activity transition by performing the "Exit" animation.
     */
    private fun overridePendingTransitionExit() {
        overridePendingTransition(R.anim.no_animation, R.anim.slide_right)
    }

}