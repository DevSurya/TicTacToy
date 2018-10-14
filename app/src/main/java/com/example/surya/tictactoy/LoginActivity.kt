package com.example.surya.tictactoy

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private var mAuth:FirebaseAuth?=null
    private var mDatabase=FirebaseDatabase.getInstance()
    private var myRef=mDatabase.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mAuth = FirebaseAuth.getInstance()
    }

    fun buLogInEvent(view:View){
        logInToFirebase(etEmail.text.toString(),etPassword.text.toString())
    }

    override fun onStart() {
        super.onStart()
        LoadMain()
    }

    fun logInToFirebase(email:String,password:String){
        mAuth!!.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this){ task ->
                    if (task.isSuccessful){
                        var currentUser = mAuth!!.currentUser
                        if (currentUser!=null){
                            // save in database
                            myRef.child("Users").child(SplitString(currentUser.email.toString())).setValue(currentUser.uid)
                        }
                        Toast.makeText(applicationContext,"Sucessful login",Toast.LENGTH_LONG).show()
                        LoadMain()
                    } else{
                        Toast.makeText(applicationContext,"Falied login",Toast.LENGTH_LONG).show()
                    }
                }
    }

    fun LoadMain(){
        var currentUser = mAuth!!.currentUser

        if (currentUser!=null){
            var intent=Intent(this,MainActivity::class.java)
            intent.putExtra("email",currentUser!!.email)
            intent.putExtra("uid",currentUser!!.uid)

            startActivity(intent)
        }

    }

    fun  SplitString(str:String):String{
        var split=str.split("@")
        return split[0]
    }
}
