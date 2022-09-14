package com.example.swot

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.swot.databinding.ActivityRegisterBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    data class memberInfo(
        val introduce: String? = null,
        val name : String? = null
    )

    private var _binding: ActivityRegisterBinding? = null
    private val binding get() = _binding!!
    val TAG = "MemberInfoActivity"
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_register)

        _binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(toolbar6)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)  // 왼쪽 버튼 사용 여부 true
        supportActionBar?.setDisplayShowTitleEnabled(false)
        auth = FirebaseAuth.getInstance()

        val text = binding.textView6.toString()
        FirebaseApp.initializeApp(this);

        binding.btnRegister2.setOnClickListener {
            val email = binding.editId.text.toString().trim()
            val password = binding.editPw.text.toString().trim()
            val password_re = binding.editPwRe.text.toString().trim()
            if (password == password_re) {
                createUser(email, password)

            } else {
                Toast.makeText(this, "비밀번호 확인이 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun createUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "회원가입 성공", Toast.LENGTH_SHORT).show()
                    val user = auth.currentUser
                    //  val introduce = binding.editIntroduce.text.toString().trim()
                    introUpdate(user)
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "회원가입 실패", Toast.LENGTH_SHORT).show()
                    // updateUI(null)
                }
            }
            .addOnFailureListener {
                if(!email.contains('@')){
                    Toast.makeText(this, "이메일 형식으로 입력해주세요", Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(this, "이미 존재하는 아이디 입니다. ", Toast.LENGTH_SHORT).show()
                }

            }
    }
    private fun introUpdate(user: FirebaseUser?){
        val introduce = binding.editIntroduce.text.toString().trim()
        val name = binding.name.text.toString().trim()
        val member = memberInfo(introduce,name)
        val db = Firebase.firestore
        if(user!=null) {
            db.collection("users").document(user.uid).set(member)
                .addOnSuccessListener {
                    //Toast.makeText(this, "데이터베이스 연동 성공", Toast.LENGTH_SHORT).show()
                    // Log.d(TAG, "DocumentSnapshot successfully written!")
                }
                .addOnFailureListener { e ->
                    //Toast.makeText(this, "mm데이터베이스 연동 실패", Toast.LENGTH_SHORT).show()
                    Log.w(TAG, "Error writing document", e)
                }
        }
    }
    /*private fun updateUI(user: FirebaseUser?) {
        user?.let {
            binding.txtResult.text = "Email: ${user.email}\nUid: ${user.uid}"
        }
    }*/
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
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