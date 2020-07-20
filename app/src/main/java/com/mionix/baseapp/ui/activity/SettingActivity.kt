package com.mionix.baseapp.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.mionix.baseapp.R
import com.mionix.baseapp.ui.base.BaseBackButtonActivity
import com.mionix.baseapp.utils.onClickThrottled
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : BaseBackButtonActivity() {
    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        btLogout.onClickThrottled {
            mAuth.signOut()
            sendUserToLoginActivity()
        }
    }

    override fun setTitleToolbar(): String {
        return "Setting"
    }

    private fun sendUserToLoginActivity() {
        val loginIntent = Intent(this@SettingActivity, LoginActivity::class.java)
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(loginIntent)
        finish()
    }
}