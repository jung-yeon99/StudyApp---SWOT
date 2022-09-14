package com.example.swot

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_myinfo.*
import kotlinx.android.synthetic.main.activity_study_info.*

//클래스 정의

class StudyInfoActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    val db = Firebase.firestore
    val studyinfoList = arrayListOf<StudyItem>()
    val studyuser = arrayListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {

        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_study_info)
        setSupportActionBar(toolbar8)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)  // 왼쪽 버튼 사용 여부 true
        supportActionBar?.setDisplayShowTitleEnabled(false)
        val collection = intent.getStringExtra("collection").toString()
        val document = intent.getStringExtra("document").toString()
        val creatuser = intent.getStringExtra("creatuser").toString()

        if (user != null) {
            db.collection(collection).document(document).collection(creatuser)
                .get()
                .addOnSuccessListener {
                    for (doc in it) {
                        studyuser.add(doc.id)
                    }
                    for (i in 0..(studyuser.size-1) ){
                        db.collection("users").document(studyuser[i])
                            .get()
                            .addOnSuccessListener { result ->
                                Log.d("StudyInfoActivity", "${collection}${document}${creatuser}${studyuser[i]}")
                                val name = result.data?.get("name").toString()
                                val introduce = result.data?.get("introduce").toString()
                                Log.d("StudyInfoActivity", "$name")
                                Log.d("StudyInfoActivity", "$introduce")
                                studyinfoList.add(StudyItem(R.drawable.swot1, name, introduce, collection, document, creatuser,studyuser[i]))
                                if(i == studyuser.size-1){
                                    Log.d("StudyInfoActivity", "${studyinfoList}")
                                    studyinfo.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                                    studyinfo.setHasFixedSize(true)
                                    studyinfo.adapter = StudyInfoAdapter(studyinfoList)
                                }
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
