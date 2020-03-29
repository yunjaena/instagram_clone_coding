package com.yunjaena.instagram

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    var auth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()
        email_login_button.setOnClickListener {
            signinAndSignUp()
        }
    }

    fun signinAndSignUp() {
        auth?.createUserWithEmailAndPassword(
            email_edittext.text.toString(),
            password_edittext.text.toString()
        )?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                //Creating a user account
                verifyEmail()
            } else {
                //Login if you have account
                signinEmail()
            }
        }
    }

    fun verifyEmail() {
        auth?.currentUser?.sendEmailVerification()?.addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(this, R.string.email_check, Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, R.string.email_send_fail, Toast.LENGTH_LONG).show()
            }
        }
    }

    fun signinEmail() {
        auth?.signInWithEmailAndPassword(
            email_edittext.text.toString(),
            password_edittext.text.toString()
        )?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                //Login
                checkEmailVerified(task.result?.user)
            } else {
                //Show the  error message
                Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    fun moveMainPage(user: FirebaseUser?) {
        if (user != null) {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    fun checkEmailVerified(user: FirebaseUser?) {
        if (user?.isEmailVerified!!) {
            moveMainPage(user)
        } else {
            Toast.makeText(applicationContext, R.string.email_check, Toast.LENGTH_SHORT).show()
        }
    }
}
