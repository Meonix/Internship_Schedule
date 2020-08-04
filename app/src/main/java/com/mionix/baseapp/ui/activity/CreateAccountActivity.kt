package com.mionix.baseapp.ui.activity

import android.app.ProgressDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId
import com.mionix.baseapp.R
import com.mionix.baseapp.model.local.Preferences
import com.mionix.baseapp.ui.base.BaseBackButtonActivity
import com.mionix.baseapp.ui.fragment.AdminMangeFragment
import com.mionix.baseapp.utils.AppExecutors
import com.mionix.baseapp.utils.KeyboardUtils
import com.mionix.baseapp.utils.onClickThrottled
import kotlinx.android.synthetic.main.activity_create_account.*
import org.koin.android.ext.android.inject
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class CreateAccountActivity : BaseBackButtonActivity() {
    private var loadingBar: ProgressDialog? = null
    private var mAuth: FirebaseAuth? = FirebaseAuth.getInstance()
    private var currentUser = mAuth?.currentUser
    private var RootRef: DatabaseReference? = FirebaseDatabase.getInstance().reference
    private var appExecutors = AppExecutors()
    private val mPreferences by inject<Preferences>()

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

    override fun onStart() {
        appExecutors = AppExecutors()
        super.onStart()
    }
    private fun createNewAccount() {
        val email = etEmail.text.toString()
        val firstName = edFirstName.text.toString()
        val lastName = edLastName.text.toString()
        val nickName = edNickName.text.toString()

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter email....", Toast.LENGTH_SHORT).show()
        }
//        if (TextUtils.isEmpty(password)) {
//            Toast.makeText(this, "Please enter password....", Toast.LENGTH_SHORT).show()
//        }
        else {
            loadingBar?.show()
            mAuth?.createUserWithEmailAndPassword(email, "gumi7393")
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val deviceToken = FirebaseInstanceId.getInstance().token
                        val currentUserID = mAuth!!.currentUser!!.uid
                        RootRef!!.child("Users").child(currentUserID).setValue("")
                        RootRef!!.child("Users").child(currentUserID).child("device_token")
                            .setValue(deviceToken)
                        RootRef!!.child("Users").child(currentUserID).child("first_name")
                            .setValue(firstName)
                        RootRef!!.child("Users").child(currentUserID).child("last_name")
                            .setValue(lastName)
                        RootRef!!.child("Users").child(currentUserID).child("nick_name")
                            .setValue(nickName)
                        RootRef!!.child("Users").child(currentUserID).child("uid")
                            .setValue(currentUserID)
//                        RootRef!!.child("Users").child(currentUserID).child("position")
//                            .setValue(edPosition.text.toString())
                        onBackPressed()
                        Toast.makeText(
                            this@CreateAccountActivity,
                            "Account created Successfully...",
                            Toast.LENGTH_SHORT
                        ).show()
                        loadingBar?.dismiss()
                        sendEmail()
                    } else {
                        val message = task.exception!!.toString()
                        Toast.makeText(
                            this@CreateAccountActivity,
                            "Error :$message",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                    if (mPreferences.getLocalEmail() != null) {
                        if (mPreferences.getLocalPassword() != null) {
                            mAuth?.signInWithEmailAndPassword(
                                mPreferences.getLocalEmail()!!,
                                mPreferences.getLocalPassword()!!
                            )
                        }
                    }

                }
        }
    }
        private fun sendEmail() {
            appExecutors.diskIO().execute {
                val props = System.getProperties()
                props["mail.smtp.host"] = "smtp.gmail.com"
                props["mail.smtp.socketFactory.port"] = "465"
                props["mail.smtp.socketFactory.class"] = "javax.net.ssl.SSLSocketFactory"
                props["mail.smtp.auth"] = "true"
                props["mail.smtp.port"] = "465"

                val session = Session.getInstance(props,
                    object : javax.mail.Authenticator() {
                        //Authenticating the password
                        override fun getPasswordAuthentication(): PasswordAuthentication {
                            return PasswordAuthentication(
                                mPreferences.getLocalEmail(), mPreferences.getLocalPassword()
                            )
                        }
                    })

                try {
                    //Creating MimeMessage object
                    val mm = MimeMessage(session)
                    val emailId = etEmail.text.toString().trim()
                    //Setting sender address
                    mm.setFrom(InternetAddress(mPreferences.getLocalEmail()))
                    //Adding receiver
                    mm.addRecipient(
                        Message.RecipientType.TO,
                        InternetAddress(emailId)
                    )
                    //Adding subject
                    mm.subject = "Welcome to Gumi Company"
                    //Adding message
                    mm.setText(
                        "your account is :${etEmail.text.toString()
                            .trim()} and password defaut is: gumi7393"
                    )

                    //Sending email
                    Transport.send(mm)

                    appExecutors.mainThread().execute {
                        //Something that should be executed on main thread.
                    }

                } catch (e: MessagingException) {
                    e.printStackTrace()
                }
            }
        }

        override fun setTitleToolbar(): String {
            return "Create Account"
        }
}