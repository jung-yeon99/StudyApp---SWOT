package com.example.swot

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.swot.databinding.ActivityAddBinding
import com.example.swot.databinding.ActivityJoinBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_add.*
import kotlinx.android.synthetic.main.activity_join.*


class JoinActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private var _binding: ActivityJoinBinding? = null
    private val binding get() = _binding!!
    val db = Firebase.firestore
    var cnt = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)
        auth = FirebaseAuth.getInstance()
        _binding = ActivityJoinBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(toolbar9)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)  // 왼쪽 버튼 사용 여부 true
        supportActionBar?.setDisplayShowTitleEnabled(false)
        data class memberInfo(
            var studyname: String? = intent.getStringExtra("studyname"),
            var say: String? = intent.getStringExtra("say"), //공지사항
            var address: String? = intent.getStringExtra("address"),
            var comment: String? = intent.getStringExtra("comment"),
            var score : String? = intent.getStringExtra("score"),
            var creatuser : String? = intent.getStringExtra("creatuser")
        )
        var doc : String? = intent.getStringExtra("document")
        binding.study.text= intent.getStringExtra("studyname")
        binding.mastercomment.text = intent.getStringExtra("say")

        var category = intent.getStringExtra("category")

        val user = auth.currentUser
        if (user != null) {
            db.collection("users").document(user.uid)
                .get()
                .addOnSuccessListener { document ->
                    binding.editTextTextMultiLine2.setText(document["introduce"].toString())
                    studyjoin.setOnClickListener{ //가입하기
                        db.collection("users").document(user.uid).update("introduce", binding.editTextTextMultiLine2.text.toString())
                        db.collection("users").document(user.uid).collection(category.toString())
                            .get()
                            .addOnSuccessListener { result ->
                                cnt = 1
                                for (document in result) {
                                    cnt++
                                    //Log.d("2Add", "$cnt")
                                }
                                //  Log.d("3. Add", "$cnt")
                                db.collection("users").document(user.uid).collection(category.toString())
                                    .document(doc.toString()).set(memberInfo())
                                    .addOnSuccessListener {
                                        //Toast.makeText(this, "데이터베이스 연동 성공", Toast.LENGTH_SHORT).show()
                                        //Toast.makeText(this, "그룹이 개설되었습니다", Toast.LENGTH_LONG).show()
                                        // Log.d(TAG, "DocumentSnapshot successfully written!")
                                    }
                                    .addOnFailureListener { e ->
                                        Toast.makeText(this, "데이터베이스 연동 실패", Toast.LENGTH_SHORT).show()
                                        // Log.w(TAG, "Error writing document", e)
                                    }
                            }
                        Toast.makeText(this, "가입이 완료 되었습니다.", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        //가입이 완료되면 내 정보로 이동하게 해놨음 추후 변경도 고려
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