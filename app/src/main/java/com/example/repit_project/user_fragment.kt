package com.example.repit_project


import android.content.Intent
import android.content.Intent.ACTION_PICK
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_user.*


class user_fragment : Fragment() {

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

            val galleryIntent = Intent(ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

            startActivity(galleryIntent)

            val loadedImage = galleryIntent.data

            Picasso.get().load(loadedImage.toString()).into(userBackImage)

            backImageUrl.setText(loadedImage.toString())
        }
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
