package com.example.chatrealtime

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        backtoregistration_text_view.setOnClickListener{
            val intent=Intent(this,RegisterActivity::class.java)
            startActivity(intent)
        }
        login_button_login.setOnClickListener{
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email_edittext_login.text.toString(),password_edittext_login.text.toString()).addOnSuccessListener {

                val intent=Intent(this,LatestMessengesActivity::class.java)
                intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }.addOnFailureListener{
                Toast.makeText(this, "Email or password is wrong", Toast.LENGTH_SHORT).show()
            }
        }
    }
}