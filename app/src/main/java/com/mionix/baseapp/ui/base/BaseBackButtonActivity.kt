package com.mionix.baseapp.ui.base

import android.os.Bundle
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.mionix.baseapp.R
import com.mionix.baseapp.utils.dialog.SimpleProgressDialog
//import com.mionix.baseapp.utils.dialog.SimpleProgressDialog
import kotlinx.android.synthetic.main.activity_base_back_button.view.*

abstract class BaseBackButtonActivity : AppCompatActivity() {

    lateinit var toolbarTitle: TextView
    protected lateinit var progressDialog: AlertDialog
    protected lateinit var progressLoading: AlertDialog
    protected lateinit var dialogLoginOrRegister: AlertDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base_back_button)
        progressDialog = SimpleProgressDialog.build(this)
        progressLoading = SimpleProgressDialog.progressLoading(this)
//        dialogLoginOrRegister = SimpleProgressDialog.dialogLoginOrRegister(this, object : SimpleProgressDialog.ButtonListener {
//            override fun onClick(result: String?) {
//                dialogLoginOrRegister.dismiss()
//            }
//        })
    }

    override fun setContentView(layoutResID: Int) {
        val constraintLayout: ConstraintLayout = layoutInflater.inflate(R.layout.activity_base_back_button, null) as ConstraintLayout
        val container: FrameLayout = constraintLayout.findViewById(R.id.flContentView)
        toolbarTitle = constraintLayout.findViewById(R.id.tvToolbarTitle)
        toolbarTitle.text = setTitleToolbar()
        constraintLayout.rlBaseBack.imgBack.setOnClickListener{onBackPressed()}
        layoutInflater.inflate(layoutResID, container, true)
        super.setContentView(constraintLayout)

    }

    abstract fun setTitleToolbar():String
    protected fun setTitleToolbar(title: String){
        toolbarTitle.text = title
    }
}
