package com.example.swot

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_myinfo.*
import kotlinx.android.synthetic.main.study_list_item.*

class MyinfoActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    val db = Firebase.firestore
    val check = hashMapOf<String, String>()
    val List = arrayListOf<String,>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        setContentView(R.layout.activity_myinfo)
        setSupportActionBar(toolbar4)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)  // 왼쪽 버튼 사용 여부 true
        supportActionBar?.setDisplayShowTitleEnabled(false)
        val user = auth.currentUser
        val category = arrayOf("LANG", "IT", "OFFICIAL", "LICENSE", "SCHOOL", "TRANSFER", "ETC")

        for (i in category) {
            DB(i)
        }
        add.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }
    // 디비에서 회원 탈퇴
    // 그룹방도 탈퇴
    val myinfoList = arrayListOf<ListViewItem>()
    var use = db.collection("users")

    fun DB(category: String) {
        val user = auth.currentUser
        var use = db.collection("users")
        if (user != null) {
            use.document(user.uid).collection(category)
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        var ss_name = document.data?.get("studyname").toString()
                        var ss_comment = document.data?.get("comment").toString()
                        var ss_say = document.data?.get("say").toString()
                        var ss_address = document.data?.get("address").toString()
                        var person = document.data?.get("person").toString()
                        var creatuser = document.data?.get("creatuser").toString()
                        var score = document.data?.get("score").toString()
                        var document = document.id
                        myinfoList.add(ListViewItem(R.drawable.swot1, ss_name, ss_comment, ss_say, ss_address, category, person, document, creatuser, score)
                        )
                    }
                    Myinfo.layoutManager =
                        LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                    Myinfo.setHasFixedSize(true)
                    Myinfo.adapter = MyinfoAdapter(myinfoList, this)
                }
                .addOnFailureListener { exception ->

                }
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.backhome,menu)

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
        }
        return super.onOptionsItemSelected(item)
    }
}


