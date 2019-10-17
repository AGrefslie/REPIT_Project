package com.example.repit_project


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


class user_fragment : Fragment() {

    //private val pickImage = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setProfileDetails()

        /*userBackImage.setOnClickListener {

            val galleryIntent = Intent(ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            //val pendingIntent = PendingIntent.getBroadcast(context, 0, galleryIntent, PendingIntent.FLAG_UPDATE_CURRENT)

            startActivityForResult(Intent.createChooser(galleryIntent, "Sellect image", pickImage))

        }*/
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


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
