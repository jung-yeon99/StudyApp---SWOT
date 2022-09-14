package com.example.swot

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.CompoundButton
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_cbox.*

class CboxActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cbox)
        setSupportActionBar(toolbar3)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)  // 왼쪽 버튼 사용 여부 true
        supportActionBar?.setDisplayShowTitleEnabled(false)
        var collection = intent.getStringExtra("collection")
        var document = intent.getStringExtra("document")
        var creatuser = intent.getStringExtra("creatuser")
        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        commit.setOnClickListener { view ->
            val update = hashMapOf<String, Any>(
                "오늘 공부 할 양은 다했나요?" to que1.isChecked,
                "약속 한 시간에 늦지 않았나요?" to que2.isChecked,
                "약속 한 과제는 해왔나요?" to que3.isChecked,
                "스터디중에 집중을 잘 하였나요?" to que4.isChecked,
                "스터디원들과 협력을 잘했나요?" to que5.isChecked,
            )
            if (user != null) {
                db.collection(collection.toString()).document(document.toString())
                    .collection(creatuser.toString()).document(user.uid)
                    .update(update)
                    .addOnCompleteListener {
                        Log.d("Cbox", "${collection.toString()}")
                        Log.d("Cbox", "${document.toString()}")
                        Log.d("Cbox", "${creatuser.toString()}")
                        Log.d("Cbox", "${user.uid}")
                        Log.d("Cbox", "$update")
                        Toast.makeText(this, "제출을 완료했습니다", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this,GroupActivity::class.java)
                        startActivity(intent)
                    }
                    .addOnFailureListener {
                        Log.d("Cbox", "실패$update")
                        Toast.makeText(this, "제출을 실패했습니다", Toast.LENGTH_SHORT).show()
                    }
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
