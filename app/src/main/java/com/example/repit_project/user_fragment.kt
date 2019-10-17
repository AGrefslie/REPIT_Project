package com.example.repit_project


import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.content.Intent.ACTION_PICK
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_user.*
import java.io.IOException


class user_fragment : Fragment() {

    private val SELECT_PICTURE = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setProfileDetails()
        userBackImage.setOnClickListener {
            addBackImage()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SELECT_PICTURE && resultCode == Activity.RESULT_OK) {
            try {
                val uri = data!!.data
                userBackImage.setImageURI(uri)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun addBackImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Image"), SELECT_PICTURE)
    }

    fun setProfileDetails() {
        val firebaseUser = FirebaseAuth.getInstance().currentUser

        val UserPhoto = firebaseUser?.photoUrl

        val picasso = Picasso.get()

        userName.setText(firebaseUser?.displayName)
        email.setText(firebaseUser?.email).toString()
        picasso.load(UserPhoto).into(userImage)
    }

}
