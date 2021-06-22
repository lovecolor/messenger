package com.example.appchat

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage


import kotlinx.android.synthetic.main.activity_register.*
import java.util.*

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        register_button_register.setOnClickListener {
            performRegister()
        }
        already_account_text_view.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        selectphoto_button_register.setOnClickListener{
            val intent=Intent(Intent.ACTION_PICK)
            intent.type="image/*"
            startActivityForResult(intent,0)
        }

    }
    var selectedPhotoUri: Uri?=null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode==0&&resultCode==Activity.RESULT_OK&&data!=null){
            selectedPhotoUri=data.data
            val bitmap=MediaStore.Images.Media.getBitmap(contentResolver,selectedPhotoUri)
            selectphoto_imageview_register.setImageBitmap(bitmap)
            selectphoto_button_register.alpha=0f
        }
    }
    private fun performRegister() {
        val email = email_edittext_register.text.toString()
        val password = password_edittext_register.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter in email/pw", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d("RegisterActivity", "Email:" + email)
        Log.d("RegisterActivity", "Pass:" + password)

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (!it.isSuccessful) return@addOnCompleteListener

            Log.d("Main", "Sucess :${it.result?.user?.uid}")

            uploadImageToFirebaseStorage()
        }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to create user:${it.message}", Toast.LENGTH_SHORT).show()
                }
    }
    private fun uploadImageToFirebaseStorage(){
        if(selectedPhotoUri==null) return
        val filename=UUID.randomUUID().toString()
        val ref=FirebaseStorage.getInstance().getReference("/image/$filename")

        ref.putFile(selectedPhotoUri!!).addOnSuccessListener {
            ref.downloadUrl.addOnSuccessListener {
                Log.d("Register",it.toString())
                saveUserToFirebaseDatabase(it.toString())
            }
        }
    }
    private lateinit var database: DatabaseReference
    private fun saveUserToFirebaseDatabase(profileImageUrl:String){
        val uid=FirebaseAuth.getInstance().uid?:""
        val ref=FirebaseDatabase.getInstance().getReference("/users/$uid")
        database = Firebase.database.reference
        val user=User(uid,username_edittext_register.text.toString(),profileImageUrl)
        Log.d("RegisterActivity",user.uid)
        Log.d("RegisterActivity",user.username)
        Log.d("RegisterActivity",user.profileImageUrl)

        database.child("users").child(uid).setValue(user).addOnSuccessListener {
            Log.d("RegisterActivity","Register user successfully")




        }.addOnFailureListener {
            Log.d("RegisterActivity","save user Failed")
        }
    }
    class User(val uid:String,val username:String,val profileImageUrl: String)
}