package com.example.swot

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_join.view.*
import kotlinx.android.synthetic.main.activity_notify.view.*
import kotlinx.android.synthetic.main.activity_studyuser.*
import kotlinx.android.synthetic.main.list_item.view.*

class StudyuserActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_studyuser)
        setSupportActionBar(toolbar7)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)  // 왼쪽 버튼 사용 여부 true
        supportActionBar?.setDisplayShowTitleEnabled(false)
        auth = FirebaseAuth.getInstance()
        //val user = auth.currentUser
        val name = intent.getStringExtra("name")
        val introduce = intent.getStringExtra("introduce")
        val collection = intent.getStringExtra("collection").toString()
        val document = intent.getStringExtra("document").toString()
        val creatuser = intent.getStringExtra("creatuser").toString()
        val user = intent.getStringExtra("user")
        username.setText(name)
        userintroduce.setText(introduce)
        if (user != null) {
            db.collection(collection).document(document).collection(creatuser)
                .get()
                .addOnSuccessListener {
                    for(document in it){
                        if(document.id.equals(user)){
                            check1.text = document.data.keys.elementAt(0).toString()
                            check1.isChecked = document.data.values.elementAt(0).toString().toBoolean()
                            // check1.isSelected
                            check2.text = document.data.keys.elementAt(1).toString()
                            check2.isChecked = document.data.values.elementAt(1).toString().toBoolean()
                            //  check2.isSelected
                            check3.text = document.data.keys.elementAt(2).toString()
                            check3.isChecked = document.data.values.elementAt(2).toString().toBoolean()
                            //   check3.isSelected
                            check4.text = document.data.keys.elementAt(3).toString()
                            check4.isChecked = document.data.values.elementAt(3).toString().toBoolean()
                            //  check4.isSelected
                            check5.text = document.data.keys.elementAt(4).toString()
                            check5.isChecked = document.data.values.elementAt(4).toString().toBoolean()
                            //   check5.isSelected

                            Log.d("Studyuser","${document.data.keys.elementAt(0).toString()}")
                        }
                    }
                }
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main,menu)

        val user = menu?.findItem(R.id.user)

        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        when(id){
            android.R.id.home -> {
                finish()
                return true
            }
            R.id.main-> {
                val intent = Intent(applicationContext,MainActivity::class.java)
                startActivity(intent)
                return true;
            }
            R.id.user -> {
                val intent = Intent(applicationContext,MyinfoActivity::class.java)
                startActivity(intent)
                return true;
            }
        }
        return super.onOptionsItemSelected(item)
    }
}