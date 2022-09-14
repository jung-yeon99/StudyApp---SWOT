package com.example.swot

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.renderscript.ScriptGroup
import android.text.Editable
import android.view.Menu
import android.view.MenuItem

import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.swot.databinding.ActivityCheckBinding
import com.example.swot.databinding.ListItemBinding
import com.example.swot.CheckAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_check.*
import kotlinx.android.synthetic.main.activity_myinfo.*
import kotlinx.android.synthetic.main.list_item.*
import java.lang.Exception

class CheckActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.swot.R.layout.activity_check)
        setSupportActionBar(toolbar9)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)  // 왼쪽 버튼 사용 여부 true
        supportActionBar?.setDisplayShowTitleEnabled(false)
        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        val category = arrayOf("LANG", "IT", "OFFICIAL", "LICENSE", "SCHOOL", "TRANSFER", "ETC")

        for (i in category) {
            DB(i)
        }
        /*val checkList = arrayListOf(
            CheckItem("IT", R.drawable.usericon),
            CheckItem("IT", R.drawable.usericon),
            CheckItem("IT", R.drawable.usericon)
        )

        rv_check.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_check.setHasFixedSize(true)
        rv_check.adapter = CheckAdapter(checkList)*/
    }
    val checkList = arrayListOf<CheckItem>()

    fun DB(category: String) {
        val user = auth.currentUser
        var use = db.collection("users")
        if (user != null) {
            use.document(user.uid).collection(category)
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        var ss_name = document.data?.get("studyname").toString()
                        var col = document.id
                        val score = document.data?.get("score").toString()
                        checkList.add(CheckItem(ss_name, R.drawable.user2,col,category,score))
                    }
                    rv_check.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                    rv_check.setHasFixedSize(true)
                    rv_check.adapter = CheckAdapter(checkList)
                }
                .addOnFailureListener { exception ->
                    // Toast.makeText(this, "데이터베이스 연동 실패", Toast.LENGTH_SHORT).show()
                }
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.onlyback,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        when(id){
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
