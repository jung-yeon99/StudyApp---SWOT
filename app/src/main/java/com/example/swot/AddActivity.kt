package com.example.swot

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.swot.databinding.ActivityAddBinding
import android.content.Intent
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_add.*
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.HashMap

class AddActivity : AppCompatActivity() {
    var pos = 0
    val TAG = "AddActivity"
    var cnt = 1
    val check = hashMapOf(
        "오늘 공부 할 양은 다했나요?" to false,
        "약속 한 시간에 늦지 않았나요?" to false,
        "약속 한 과제는 해왔나요?" to false,
        "스터디중에 집중을 잘 하였나요?" to false,
        "스터디원들과 협력을 잘했나요?" to false
    )

    data class memberInfo(
        var studyname: String? = null,
        var say: String? = null,
        var type: String? = null,
        var address: String? = null,
        var person: String? = null,
        var comment: String? = null,
        var score: String? = null,
        var creatuser: String? = null
    )

    private var _binding: ActivityAddBinding? = null
    private val binding get() = _binding!!


    private lateinit var auth: FirebaseAuth

    val itemList = listOf("선택", "1", "2", "3", "4", "5")
    val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_add)
        _binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(toolbar10)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)  // 왼쪽 버튼 사용 여부 true
        supportActionBar?.setDisplayShowTitleEnabled(false)
        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        val long_now = System.currentTimeMillis()
        // 현재 시간을 Date 타입으로 변환
        val t_date = Date(long_now)
        // 날짜, 시간을 가져오고 싶은 형태 선언
        val t_dateFormat = SimpleDateFormat("MMdd", Locale("ko", "KR"))
        // 현재 시간을 dateFormat 에 선언한 형태의 String 으로 변환
        val date = t_dateFormat.format(t_date)
        // println("현재 날짜 및 시간 : "+str_date)
        val adapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, itemList)
        binding.spinner.adapter = adapter
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                pos = position
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
        binding.make.setOnClickListener {
            var studytype: String = ""
            var userInfo = memberInfo()
            userInfo.studyname = binding.studyeditname.text.toString().trim()
            userInfo.say = binding.studyeditname2.text.toString().trim()
            if (binding.radioButton.isChecked) {
                studytype = "대면"
            } else if (binding.radioButton2.isChecked) {
                studytype = "비대면"
            }
            userInfo.type = studytype
            userInfo.address = binding.studyeditname3.text.toString().trim()
            userInfo.person = itemList[pos]
            userInfo.comment = binding.studyeditname5.text.toString().trim()
            userInfo.score = "0"
            if (user != null) {
                userInfo.creatuser = user.uid
            }
            var data = hashMapOf<Any,Any>()
            //val text = binding.textView1.toString()
            //val category = SpinnerActivity.memberInfo(text)
            var flag = false
            val random = Random()
            val doclist = arrayListOf<String>()
            var number = random.nextInt(10) + 1
            //  var name : String? = null
            if (user != null) {
                db.collection("users").document(user.uid).get().addOnSuccessListener {
                    var name = it.data?.get("name").toString()
                    data.put(name,false)
                }
                if (intent.getStringExtra("st_category").equals("어학")) { // when 으로 구현 가능
                    db.collection("LANG")
                        .get()
                        .addOnSuccessListener { result ->
                            for (i in result) {
                                doclist.add(i.id)
                            }
                            while (true) {
                                if (doclist.contains("LANG$number")) {
                                    number = random.nextInt(10) + 1
                                    Log.d("Add", "동일 숫자$number")
                                } else {

                                    db.collection("LANG").document("LANG${number}Attendance").collection("date").document(date).set(data)
                                    db.collection("LANG").document("LANG$number").set(userInfo)

                                    db.collection("LANG").document("LANG$number").collection(user.uid)
                                        .document(user.uid).set(check)
                                    db.collection("users").document(user.uid).collection("LANG")
                                        .document("LANG$number").set(userInfo).addOnSuccessListener {
                                            //Toast.makeText(this, "데이터베이스 연동 성공", Toast.LENGTH_SHORT)
                                            //.show()
                                            Toast.makeText(this, "그룹이 개설되었습니다. ", Toast.LENGTH_LONG)
                                                .show()
                                            // Log.d(TAG, "DocumentSnapshot successfully written!")
                                            val intent = Intent(this, MainActivity::class.java)
                                            intent.putExtra("creatuser", user.uid)
                                            startActivity(intent)
                                        }
                                    break
                                }
                            }
                        }
                }

                else if (intent.getStringExtra("st_category").equals("IT/컴퓨터")) { // when 으로 구현 가능
                    db.collection("IT")
                        .get()
                        .addOnSuccessListener { result ->
                            for (i in result) {
                                doclist.add(i.id)
                            }
                            while (true) {
                                if (doclist.contains("IT$number")) {
                                    number = random.nextInt(10) + 1
                                    Log.d("Add", "동일 숫자$number")
                                } else {

                                    db.collection("IT").document("IT${number}Attendance").collection("date").document(date).set(data)
                                    db.collection("IT").document("IT$number").set(userInfo)

                                    db.collection("IT").document("IT$number").collection(user.uid)
                                        .document(user.uid).set(check)
                                    db.collection("users").document(user.uid).collection("IT")
                                        .document("IT$number").set(userInfo).addOnSuccessListener {
                                            //Toast.makeText(this, "데이터베이스 연동 성공", Toast.LENGTH_SHORT)
                                            //.show()
                                            Toast.makeText(this, "그룹이 개설되었습니다. ", Toast.LENGTH_LONG)
                                                .show()
                                            // Log.d(TAG, "DocumentSnapshot successfully written!")
                                            val intent = Intent(this, MainActivity::class.java)
                                            intent.putExtra("creatuser", user.uid)
                                            startActivity(intent)
                                        }
                                    break
                                }
                            }
                        }
                } else if (intent.getStringExtra("st_category").equals("공무원")) {
                    db.collection("OFFICIAL")
                        .get()
                        .addOnSuccessListener { result ->
                            for (i in result) {
                                doclist.add(i.id)
                            }
                            while (true) {
                                if (doclist.contains("OFFICIAL$number")) {
                                    number = random.nextInt(10) + 1
                                    Log.d("Add", "동일 숫자$number")
                                } else {
                                    db.collection("OFFICIAL").document("OFFICIAL${number}Attendance").collection("date").document(date).set(data)
                                    db.collection("OFFICIAL").document("OFFICIAL$number")
                                        .set(userInfo)
                                    db.collection("OFFICIAL").document("OFFICIAL$number")
                                        .collection(user.uid).document(user.uid).set(check)

                                    db.collection("users").document(user.uid).collection("OFFICIAL")
                                        .document("OFFICIAL$number").set(userInfo)
                                        .addOnSuccessListener {
                                            //Toast.makeText(this, "데이터베이스 연동 성공", Toast.LENGTH_SHORT)
                                            //.show()
                                            Toast.makeText(this, "그룹이 개설되었습니다. ", Toast.LENGTH_LONG)
                                                .show()
                                            val intent = Intent(this, MainActivity::class.java)
                                            startActivity(intent)
                                        }
                                    break
                                }
                            }
                        }
                } else if (intent.getStringExtra("st_category").equals("자격증")) {
                    db.collection("LICENSE")
                        .get()
                        .addOnSuccessListener { result ->
                            for (i in result) {
                                doclist.add(i.id)
                            }
                            while (true) {
                                if (doclist.contains("LICENSE$number")) {
                                    number = random.nextInt(10) + 1
                                    Log.d("Add", "동일 숫자$number")
                                } else {
                                    db.collection("LICENSE").document("LICENSE${number}Attendance").collection("date").document(date).set(data)
                                    db.collection("LICENSE").document("LICENSE$number")
                                        .set(userInfo)
                                    db.collection("LICENSE").document("LICENSE$number")
                                        .collection(user.uid).document(user.uid).set(check)
                                    db.collection("users").document(user.uid).collection("LICENSE")
                                        .document("LICENSE$number").set(userInfo)
                                        .addOnSuccessListener {
                                            //Toast.makeText(this, "데이터베이스 연동 성공", Toast.LENGTH_SHORT)
                                            //.show()
                                            Toast.makeText(this, "그룹이 개설되었습니다. ", Toast.LENGTH_LONG)
                                                .show()
                                            val intent = Intent(this, MainActivity::class.java)
                                            startActivity(intent)
                                        }
                                    break
                                }
                            }
                        }
                }
                else if (intent.getStringExtra("st_category").equals("중고등")) {
                    db.collection("SCHOOL")
                        .get()
                        .addOnSuccessListener { result ->
                            for (i in result) {
                                doclist.add(i.id)
                            }
                            while (true) {
                                if (doclist.contains("SCHOOL$number")) {
                                    number = random.nextInt(10) + 1
                                    Log.d("Add", "동일 숫자$number")
                                } else {
                                    db.collection("SCHOOL").document("SCHOOL${number}Attendance").collection("date").document(date).set(data)
                                    db.collection("SCHOOL").document("SCHOOL$number").set(userInfo)
                                    db.collection("SCHOOL").document("SCHOOL$number")
                                        .collection(user.uid).document(user.uid).set(check)
                                    db.collection("users").document(user.uid).collection("SCHOOL")
                                        .document("SCHOOL$number").set(userInfo)
                                        .addOnSuccessListener {
                                            //Toast.makeText(this, "데이터베이스 연동 성공", Toast.LENGTH_SHORT)
                                            //.show()
                                            Toast.makeText(this, "그룹이 개설되었습니다. ", Toast.LENGTH_LONG)
                                                .show()
                                            val intent = Intent(this, MainActivity::class.java)
                                            startActivity(intent)
                                        }
                                    break
                                }
                            }
                        }
                }
                else if (intent.getStringExtra("st_category").equals("편입")) {
                    db.collection("TRANSFER")
                        .get()
                        .addOnSuccessListener { result ->
                            for (i in result) {
                                doclist.add(i.id)
                            }
                            while (true) {
                                if (doclist.contains("TRANSFER$number")) {
                                    number = random.nextInt(10) + 1
                                    Log.d("Add", "동일 숫자$number")
                                } else {
                                    db.collection("TRANSFER").document("TRANSFER${number}Attendance").collection("date").document(date).set(data)
                                    db.collection("TRANSFER").document("TRANSFER$number").set(userInfo)
                                    db.collection("TRANSFER").document("TRANSFER$number")
                                        .collection(user.uid).document(user.uid).set(check)
                                    db.collection("users").document(user.uid).collection("TRANSFER")
                                        .document("TRANSFER$number").set(userInfo)
                                        .addOnSuccessListener {
                                            Toast.makeText(this, "데이터베이스 연동 성공", Toast.LENGTH_SHORT)
                                                .show()
                                            Toast.makeText(this, "그룹이 개설되었습니다. ", Toast.LENGTH_LONG)
                                                .show()
                                            val intent = Intent(this, MainActivity::class.java)
                                            startActivity(intent)
                                        }
                                    break
                                }
                            }
                        }
                }
                else if (intent.getStringExtra("st_category").equals("기타")) {
                    db.collection("ETC")
                        .get()
                        .addOnSuccessListener { result ->
                            for (i in result) {
                                doclist.add(i.id)
                            }
                            while (true) {
                                if (doclist.contains("ETC$number")) {
                                    number = random.nextInt(10) + 1
                                    Log.d("Add", "동일 숫자$number")
                                } else {
                                    db.collection("ETC").document("ETC${number}Attendance").collection("date").document(date).set(data)
                                    db.collection("ETC").document("ETC$number").set(userInfo)
                                    db.collection("ETC").document("ETC$number")
                                        .collection(user.uid).document(user.uid).set(check)
                                    db.collection("users").document(user.uid).collection("ETC")
                                        .document("ETC$number").set(userInfo)
                                        .addOnSuccessListener {
                                            Toast.makeText(this, "데이터베이스 연동 성공", Toast.LENGTH_SHORT)
                                                .show()
                                            Toast.makeText(this, "그룹이 개설되었습니다. ", Toast.LENGTH_LONG)
                                                .show()
                                            val intent = Intent(this, MainActivity::class.java)
                                            startActivity(intent)
                                        }
                                    break
                                }
                            }
                        }
                }
            } else {
                Toast.makeText(this, " 유저 실패", android.widget.Toast.LENGTH_SHORT).show()
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