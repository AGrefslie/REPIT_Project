package com.example.repit_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.zip.Inflater


class MainActivity : AppCompatActivity() {

    private lateinit var firebaseAuth : FirebaseAuth
    private lateinit var authStateListener : FirebaseAuth.AuthStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        setUpBottomNavMenu(navController)

        createAuthenticationListener()

        firebaseAuth = FirebaseAuth.getInstance()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.signOutBtn -> {
                AuthUI.getInstance().signOut(this!!)
                return true
            }
        }

        return false
    }

    override fun onResume() {
        super.onResume()

        firebaseAuth.addAuthStateListener(authStateListener)

    }

    override fun onPause() {
        super.onPause()

        firebaseAuth.removeAuthStateListener(authStateListener)
    }

    private fun createAuthenticationListener() {
        authStateListener = FirebaseAuth.AuthStateListener {
            val firebaseUser = firebaseAuth.currentUser
            if (firebaseUser == null) {
                startActivityForResult(
                    AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders( arrayListOf(
                            AuthUI.IdpConfig.GoogleBuilder().build(),
                            AuthUI.IdpConfig.EmailBuilder().build()))
                        .setIsSmartLockEnabled(false)
                        .build(), RC_SIGN_IN
                )
            }
            else {
                userNameText.text = "Signed in as " + firebaseUser?.displayName
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                val user = firebaseAuth.currentUser
                Toast.makeText(this, "Signed in as " + user?.displayName, Toast.LENGTH_SHORT).show()
                userNameText.text = "Signed in as " + user?.displayName
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Sign in canceled", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setUpBottomNavMenu(navController : NavController) {
        bottom_navigation?.let{
            NavigationUI.setupWithNavController(it, navController)
        }
    }

    companion object {
        const val RC_SIGN_IN = 1
        private val LOGTAG = MainActivity::class.java.simpleName
    }
}
