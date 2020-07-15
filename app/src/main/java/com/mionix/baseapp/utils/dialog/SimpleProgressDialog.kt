package com.mionix.baseapp.utils.dialog

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.mionix.baseapp.R


object SimpleProgressDialog {

    interface ButtonListener {
        fun onClick(result : String? = null)
    }

    interface OnResultListener{
        fun onResult(result : String)
    }


    fun build(activity: AppCompatActivity, isCancelable: Boolean = false): AlertDialog {
        val padding = 50
        val linearLayout = LinearLayout(activity)
        linearLayout.orientation = LinearLayout.HORIZONTAL
        linearLayout.setPadding(padding, padding, padding, padding)
        linearLayout.gravity = Gravity.START
        var params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params.gravity = Gravity.CENTER
        linearLayout.layoutParams = params

        val progressBar = ProgressBar(activity)
        progressBar.isIndeterminate = true
        progressBar.setPadding(0, 0, padding, 0)
        progressBar.layoutParams = params

        params = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        params.gravity = Gravity.CENTER
        val tvText = TextView(activity)
        tvText.text = "Loading..."
        tvText.setTextColor(Color.parseColor("#000000"))
        //tvText.textSize = 20.toFloat()
        tvText.layoutParams = params

        linearLayout.addView(progressBar)
        linearLayout.addView(tvText)

        val builder = AlertDialog.Builder(activity)
        builder.setCancelable(false)
        builder.setView(linearLayout)

        val dialog = builder.create()
        val window = dialog.window
        if (window != null) {
            val layoutParams = WindowManager.LayoutParams()
            layoutParams.copyFrom(dialog.window?.attributes)
            layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
            dialog.window?.attributes = layoutParams
        }
        return dialog
    }


    fun dialogSBS(activity: Activity,
                           message: String
                        ): Dialog {


        val alertDialog = AlertDialog.Builder(activity,
            android.R.style.Theme_Material_Light_Dialog_NoActionBar_MinWidth)
            .setMessage(message)
            .setCancelable(false)
        val tmp = alertDialog.create()
        return tmp
    }

   fun dialogWithOKButton(activity: Activity,
                          message: String,
                          okbuttonText: String? = null,
                          dialogInterface: DialogInterface.OnClickListener? = null): Dialog {

        var okText = activity.getText(R.string.ok)
        if(okbuttonText != null) okText = okbuttonText

        val alertDialog = AlertDialog.Builder(activity,  android.R.style.Theme_Material_Light_Dialog_NoActionBar_MinWidth)
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton(okText, dialogInterface)
        val tmp = alertDialog.create()
        tmp.setOnShowListener {
            val yes = tmp.getButton(AlertDialog.BUTTON_POSITIVE)
            //yes.textSize = 18F
            yes.setTextColor(ContextCompat.getColor(activity, R.color.colorPrimary))
            yes.typeface = Typeface.DEFAULT_BOLD
        }
        return tmp
    }
/*
    fun dialogOKCancelButton(context: Context, message: String, okButtonText: String? = null, cancelButtonText: String? = null, dialogInterface: DialogInterface.OnClickListener): Dialog {

        var okText = context.getText(R.string.yes)
        var cancelText = context.getText(R.string.Cancel)

        if(okButtonText != null) okText = okButtonText
        if(cancelButtonText != null) cancelText = cancelButtonText

        val alertDialog = AlertDialog.Builder(context, android.R.style.Theme_Material_Light_Dialog_NoActionBar_MinWidth *//*android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth*//*)
            .setMessage(message)
            .setNegativeButton(okText, dialogInterface )
            .setPositiveButton(cancelText){ dialog, _ -> dialog.dismiss()}
        val tmp = alertDialog.create()
        tmp.setOnShowListener {
            val yes = tmp.getButton(AlertDialog.BUTTON_NEGATIVE)
            //yes.textSize = 18F
            yes.setTextColor(ContextCompat.getColor(context, R.color.orange))
            yes.typeface = Typeface.DEFAULT_BOLD

            val cancel = tmp.getButton(AlertDialog.BUTTON_POSITIVE)
            //yes.textSize = 18F
            cancel.setTextColor(ContextCompat.getColor(context, R.color.gray_66))
            cancel.typeface = Typeface.DEFAULT_BOLD
        }

        return tmp
    }

    fun dialogQuestion(activity: Activity, isCancelable: Boolean = false,message: String,mButtonListener: ButtonListener?=null){
       val dialog =  Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(isCancelable)
        dialog.setContentView(R.layout.dialog_question)

        val tvMessage = dialog.findViewById(R.id.tvContent) as TextView
        val tvCancel =  dialog.findViewById(R.id.tvCancel) as TextView
        val tvOK =  dialog.findViewById(R.id.tvOK) as TextView

        tvMessage.text = message

        tvOK.setOnClickListener {
            mButtonListener?.onClick()
            dialog.dismiss()
        }

        tvCancel.setOnClickListener {
            dialog.dismiss()
            println()
        }

        dialog.show()
    }
*/

    fun progressLoading(activity: AppCompatActivity): AlertDialog {
        val dialogBuilder = AlertDialog.Builder(activity, R.style.CustomDialogTheme)
        val view = activity.layoutInflater.inflate(R.layout.layout_progress_loading, null)
        dialogBuilder.setView(view)
        dialogBuilder.setCancelable(true)
        return dialogBuilder.create()
    }

//    fun dialogSaveSearchConditions(activity: AppCompatActivity, onCancelListener: ButtonListener, onSaveListener: ButtonListener): AlertDialog {
//        val dialogBuilder = AlertDialog.Builder(activity)
//        val view = activity.layoutInflater.inflate(R.layout.layout_dailog_save_search_conditions, null)
//        dialogBuilder.setView(view)
//        val etSaveContent = view.findViewById<EditText>(R.id.etSaveContent)
//       val btCancel = view.findViewById<TextView>(R.id.tvCancel)
//       btCancel.onClickThrottled{
//           onCancelListener.onClick()
//       }
//       val btSave = view.findViewById<TextView>(R.id.tvSaveSearchConditions)
//       btSave.onClickThrottled {
//           onSaveListener.onClick(etSaveContent.text.toString())
//       }
//        dialogBuilder.setCancelable(true)
//        return dialogBuilder.create()
//    }

//    fun dialogCancelGuidance(activity: AppCompatActivity, title: String, subTitle: String, onCancelListener: ButtonListener, onOkListener: ButtonListener): AlertDialog {
//        val dialogBuilder = AlertDialog.Builder(activity, R.style.CustomDialogTheme2)
//        val view = activity.layoutInflater.inflate(R.layout.layout_dailog_cancel_guidance, null)
//        dialogBuilder.setView(view)
//        val tvTitle = view.findViewById<TextView>(R.id.tvTitle)
//        val tvSubTitle = view.findViewById<TextView>(R.id.tvSubTitle)
//        tvTitle.text = title
//        tvSubTitle.text = subTitle
//        val btCancel = view.findViewById<TextView>(R.id.tvCancel)
//        btCancel.onClickThrottled{
//            onCancelListener.onClick()
//        }
//        val btOk = view.findViewById<TextView>(R.id.tvOk)
//        btOk.onClickThrottled {
//            onOkListener.onClick()
//        }
//        dialogBuilder.setCancelable(true)
//        return dialogBuilder.create()
//    }

//    fun dialogLoginOrRegister(activity: AppCompatActivity, mButtonListener: ButtonListener): AlertDialog {
//        val dialogBuilder = AlertDialog.Builder(activity, R.style.CustomDialogTheme2)
//        val view = activity.layoutInflater.inflate(R.layout.dialog_login, null)
//        dialogBuilder.setView(view)
//        val btRegister = view.findViewById<Button>(R.id.btnRegister)
//        btRegister.onClickThrottled{
//
//        }
//        val btLogin = view.findViewById<Button>(R.id.btnLogin)
//        btLogin.onClickThrottled {
//            activity.startActivity(Intent(activity, LoginActivity::class.java))
//        }
//
//        val ivClose = view.findViewById<ImageView>(R.id.ivClose)
//        ivClose.onClickThrottled {
//            mButtonListener.onClick()
//        }
//        dialogBuilder.setCancelable(false)
//        return dialogBuilder.create()
//    }

}