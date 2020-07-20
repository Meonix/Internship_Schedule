package com.mionix.baseapp.ui.activity

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId
import com.mionix.baseapp.R
import com.mionix.baseapp.ui.base.BaseBackButtonActivity
import com.mionix.baseapp.utils.KeyboardUtils
import com.mionix.baseapp.utils.onClickThrottled
import kotlinx.android.synthetic.main.activity_create_account.*

class CreateAccountActivity : BaseBackButtonActivity() {
    private var loadingBar: ProgressDialog? = null
    private var mAuth: FirebaseAuth? = FirebaseAuth.getInstance()
    private var RootRef: DatabaseReference? = FirebaseDatabase.getInstance().reference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)
        setupView()
        setUpActionKeyBoard(rlCreateAccount)
        btCreateAccount.onClickThrottled { createNewAccount() }
    }

    private fun setupView() {
        loadingBar = ProgressDialog(this)
        loadingBar!!.setTitle("Creating New Account")
        loadingBar!!.setMessage("Please wait, while we are creating new account for you...")
        loadingBar!!.setCanceledOnTouchOutside(true)
    }
    private fun setUpActionKeyBoard(view: View){
        // Set up touch listener for non-text box views to hide keyboard.
        // Set up touch listener for non-text box views to hide keyboard.
        if (view !is EditText) {
            view.setOnTouchListener { v, event ->
                KeyboardUtils.hideSoftKeyboard(this@CreateAccountActivity)
                false
            }
        }
        //If a layout container, iterate over children and seed recursion.
        //If a layout container, iterate over children and seed recursion.
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                val innerView = view.getChildAt(i)
                setUpActionKeyBoard(innerView)
            }
        }
    }
    private fun createNewAccount() {
        val email = etEmail!!.text.toString()
        val password = edPassword!!.text.toString()
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter email....", Toast.LENGTH_SHORT).show()
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter password....", Toast.LENGTH_SHORT).show()
        }
        else{
            loadingBar?.show()
            mAuth?.createUserWithEmailAndPassword(email,password)?.addOnCompleteListener{task ->
                    if(task.isSuccessful){
                        val deviceToken = FirebaseInstanceId.getInstance().token
                        val currentUserID = mAuth!!.currentUser!!.uid
                        RootRef!!.child("Users").child(currentUserID).setValue("")

                        RootRef!!.child("Users").child(currentUserID).child("device_token")
                            .setValue(deviceToken)
                        RootRef!!.child("Users").child(currentUserID).child("position")
                            .setValue(edPosition.text.toString())
                        onBackPressed()
                        Toast.makeText(this@CreateAccountActivity, "Account created Successfully...", Toast.LENGTH_SHORT).show()
                        loadingBar?.dismiss()
                    }
                else
                    {
                        val message = task.exception!!.toString()
                        Toast.makeText(this@CreateAccountActivity, "Error :$message", Toast.LENGTH_SHORT).show()
                        loadingBar?.dismiss()
                    }

            }
        }
    }

    override fun setTitleToolbar(): String {
        return "Create Account"
    }
}