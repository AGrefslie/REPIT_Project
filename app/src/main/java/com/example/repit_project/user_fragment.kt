package com.example.repit_project


import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.net.toUri
import com.example.repit_project.Models.UserDetail
import com.example.repit_project.home_fragment.Companion.LOGTAG
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_user.*
import java.io.IOException



class user_fragment : Fragment() {

    private val SELECT_PICTURE = 1
    private val firebaseUser = FirebaseAuth.getInstance().currentUser
    private val userPhoto = firebaseUser?.photoUrl

    private var userBackImageUri : Uri = Uri.EMPTY

    private lateinit var db : FirebaseFirestore
    private lateinit var collectionUserDetails : CollectionReference
    private lateinit var mStorageRef: FirebaseStorage

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        db = FirebaseFirestore.getInstance()
        collectionUserDetails = db.collection("UserDetails")
        mStorageRef = FirebaseStorage.getInstance()

        getUserDetailsFromFirestore()

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setProfileDetails()
        userBackImage.setOnClickListener {
            addBackImage()
        }

        signOutBtn.setOnClickListener {
            signOutUser()
        }
        saveDetailsBtn.setOnClickListener {
            saveDetails()
        }
    }

    fun getUserDetailsFromFirestore () {
        val picasso = Picasso.get()
        val docRef = collectionUserDetails.document(firebaseUser!!.uid)
        docRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document = task.getResult()
                if ( document != null){
                    Log.d("ASDF", task.getResult()!!.getData().toString())
                    val myUser = document.toObject(UserDetail::class.java)
                    bio.setText(myUser?.bio)
                    userBackImageUri = myUser.toString().toUri()
                    picasso.load(myUser?.imageUri).into(userBackImage)
                }
                else {
                    Log.d("ASDF", "not null")
                }
            } else {
                Log.d(LOGTAG, "Error getting documents: " + task.exception)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SELECT_PICTURE && resultCode == Activity.RESULT_OK) {
            try {
                val uri = data?.data
                userBackImage.setImageURI(uri)
                userBackImageUri = uri!!
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
        val picasso = Picasso.get()

        userName.setText(firebaseUser?.displayName)
        email.setText("Email: " + (firebaseUser?.email).toString())
        picasso.load(userPhoto).into(userImage)
    }

    private fun saveDetails() {

        var file = userBackImageUri
        val mImageRef = mStorageRef.reference.child("images/${file.lastPathSegment}")
        val uploadTask = mImageRef.putFile(file)

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener {
            Log.d("Upload: ", "Failure")
        }.addOnSuccessListener {
            Log.d("Upload: ", "Successful")
            mImageRef.downloadUrl.addOnSuccessListener {

                var userDetail = UserDetail(it.toString(), bio.text.toString())

                collectionUserDetails.document(firebaseUser!!.uid).set(userDetail)
                    .addOnSuccessListener { documentReference ->
                        Log.d(TAG, "DocumentSnapshot written with ID: ${firebaseUser.uid}")
                        Toast.makeText(context, "Details Saved", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error adding document", e)
                    }
            }
        }

        Log.d("ASDF", userBackImage.resources.toString())


    }

    override fun onResume() {
        super.onResume()

        //make sure that the keyboard dont push the view when activated
        getActivity()?.getWindow()?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
    }

    fun signOutUser(): Boolean {

        AuthUI.getInstance().signOut(context!!)
        return true
    }

}
