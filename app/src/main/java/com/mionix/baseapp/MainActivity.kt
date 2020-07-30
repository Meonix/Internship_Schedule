package com.mionix.baseapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.mionix.baseapp.ui.activity.LoginActivity
import com.mionix.baseapp.ui.activity.SettingActivity
import com.mionix.baseapp.ui.fragment.AdminMangeFragment
import com.mionix.baseapp.ui.fragment.MainHomeFragment
import com.mionix.baseapp.ui.fragment.MainMyPageFragment
import com.mionix.baseapp.utils.onClickThrottled
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_toolbar.view.*
import kotlinx.android.synthetic.main.layout_toolbar_view.view.*

class MainActivity : AppCompatActivity() {
    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var currentUser: FirebaseUser? = null
    private var usersRef: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Users")


    lateinit var homeFragment: MainHomeFragment
    lateinit var mypageFragment: MainMyPageFragment
    lateinit var adminManageFragment: AdminMangeFragment
    lateinit var currentFragment: Fragment
    lateinit var fragmentManager: FragmentManager
//    private val mPreferences by inject<Preferences>()
//    private val mLovePercentViewmodel : LovePercentViewmodel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        currentUser = mAuth.currentUser
        setupView()
        setupFragments()
        setupBottomNavigationBar()
    }

    private fun setupView() {
        toolbar.ivLeft.onClickThrottled {
            val intent = Intent(this@MainActivity,SettingActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupBottomNavigationBar() {
        val eventListener: ValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val position = dataSnapshot.child("position").getValue(String::class.java)
                navigation.menu[1].isVisible = position == "system_admin"||position== "admin"
            }

            override fun onCancelled(databaseError: DatabaseError?) {

            }
        }
        usersRef.child(currentUser?.uid.toString()).addValueEventListener(eventListener)
        navigation.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    if (currentFragment === homeFragment)
                        fragmentManager.beginTransaction().show(homeFragment).commit()
                    else
                        fragmentManager.beginTransaction().hide(currentFragment).show(homeFragment).commit()
                    currentFragment = homeFragment
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_mypage -> {
                    fragmentManager.beginTransaction().hide(currentFragment).show(mypageFragment).commit()
                    currentFragment = mypageFragment
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_management -> {
                    fragmentManager.beginTransaction().hide(currentFragment).show(adminManageFragment).commit()
                    currentFragment = adminManageFragment
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        })
    }

    private fun setupFragments() {
        fragmentManager = supportFragmentManager

        homeFragment = MainHomeFragment.newInstance()
        mypageFragment = MainMyPageFragment.newInstance()
        adminManageFragment = AdminMangeFragment.newInstance()

        currentFragment = homeFragment

        fragmentManager.beginTransaction().add(R.id.contentContainer, mypageFragment, "mypageFragment").commit()
        fragmentManager.beginTransaction().hide(mypageFragment).commit()
        fragmentManager.beginTransaction().add(R.id.contentContainer, adminManageFragment, "adminManageFragment").commit()
        fragmentManager.beginTransaction().hide(adminManageFragment).commit()
        fragmentManager.beginTransaction().add(R.id.contentContainer, homeFragment, "homeFragment").commit()
    }

    override fun onStart() {
        super.onStart()
        if (currentUser == null) {
            sendUserToLoginActivity()
        } else {
            //VerifyUserExistance()
        }
    }
//    private fun VerifyUserExistance() {
//        val currentUerID = mAuth.currentUser!!.uid
//
//        RootRef.child("Users").child(currentUerID).addValueEventListener(object :
//            ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                if (dataSnapshot.child("name").exists()) {
//                    Toast.makeText(this@MainActivity, "Welcome" + "  " + dataSnapshot.child("name").value!!.toString(), Toast.LENGTH_SHORT).show()
//                } else {
//                    //SendUserToSettingsActivity()
//                }
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {
//
//            }
//        })
//    }
    private fun sendUserToLoginActivity() {
        val loginIntent = Intent(this@MainActivity, LoginActivity::class.java)
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(loginIntent)
        finish()
    }
}
