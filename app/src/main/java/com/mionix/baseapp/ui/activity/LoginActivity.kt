package com.mionix.baseapp.ui.activity

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
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
import com.mionix.baseapp.MainActivity
import com.mionix.baseapp.R
import com.mionix.baseapp.utils.KeyboardUtils.hideSoftKeyboard
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var usersRef: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Users")

    private lateinit var loadingBar: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setUpView()
        handleOnclick()
        setUpActionKeyBoard(clLogin)
    }

    private fun handleOnclick() {
        loginButton.setOnClickListener {  allowUserToLogin()  }
    }

    private fun allowUserToLogin() {
        val email = loginEmail.text.toString()
        val password = loginPassword.text.toString()
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter email....", Toast.LENGTH_SHORT).show()
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter password....", Toast.LENGTH_SHORT).show()
        } else {
            loadingBar.show()

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val currentUserID = mAuth.currentUser!!.uid
                    val deviceToken = FirebaseInstanceId.getInstance().token

                    usersRef.child(currentUserID).child("device_token")
                        .setValue(deviceToken)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                sendUserToMainActivity()
                                Toast.makeText(this@LoginActivity, "Logged in  Successful....", Toast.LENGTH_SHORT).show()
                                loadingBar.dismiss()
                            }
                        }
                } else {
                    val message = task.exception!!.toString()
                    Toast.makeText(this@LoginActivity, "Error :$message", Toast.LENGTH_SHORT).show()
                    loadingBar.dismiss()
                }
            }
        }
    }

    private fun sendUserToMainActivity() {
        val mainIntent = Intent(this@LoginActivity, MainActivity::class.java)
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)

        startActivity(mainIntent)
        finish()
    }
    @SuppressLint("ClickableViewAccessibility")
    private fun setUpActionKeyBoard(view: View){
        // Set up touch listener for non-text box views to hide keyboard.
        // Set up touch listener for non-text box views to hide keyboard.
        if (view !is EditText) {
            view.setOnTouchListener { v, event ->
                hideSoftKeyboard(this@LoginActivity)
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
    private fun setUpView() {
        loadingBar= ProgressDialog(this)
        loadingBar.setTitle("Sign In")
        loadingBar.setMessage("Please wait....")
        loadingBar.setCanceledOnTouchOutside(true)
    }
//    private fun sendUserToRegisterActivity() {
//        val registerIntent = Intent(this@LoginActivity, RegisterActivity::class.java)
//        startActivity(registerIntent)
//    }
}