package com.example.swot

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintSet
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_group.*
import kotlinx.android.synthetic.main.activity_join.*

class GroupActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)  // 왼쪽 버튼 사용 여부 true
        supportActionBar?.setDisplayShowTitleEnabled(false)
        var studyname = intent.getStringExtra("studyname")
        var openaddress = intent.getStringExtra("address")
        var say = intent.getStringExtra("say")
        var collection = intent.getStringExtra("collection")
        var creatuser = intent.getStringExtra("creatuser")
        var userdocument = intent.getStringExtra("document")
        groupname.text = studyname
        address.text= openaddress
        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        say_bt.setOnClickListener {
            showSettingPopup(say)
        }
        check_list_bt.setOnClickListener {
            db.collection(collection.toString())
                .get()
                .addOnSuccessListener { result ->
                    for(i in result) {
                        if(i.data?.get("address").toString().equals(openaddress)) {
                            var document = i.id
                            val intent = Intent(this, CboxActivity::class.java)
                            intent.putExtra("collection", collection)
                            intent.putExtra("document", document)
                            intent.putExtra("creatuser", creatuser)
                            startActivity(intent)
                        }
                    }
                }
        }

        studycheck.setOnClickListener {
            val intent = Intent(this,CheckActivity::class.java)
            startActivity(intent)
        }
        notify_bt.setOnClickListener {
            val intent = Intent(this,NotifyActivity::class.java)
            startActivity(intent)
        }
        attend_bt.setOnClickListener {
            db.collection(collection.toString())
                .get()
                .addOnSuccessListener { result ->
                    for(i in result) {
                        if(i.data?.get("address").toString().equals(openaddress)) {
                            var document = i.id
                            val intent = Intent(this, AttendanceActivity::class.java)
                            intent.putExtra("collection", collection)
                            intent.putExtra("document", document)
                            intent.putExtra("creatuser", creatuser)
                            startActivity(intent)
                        }
                    }
                }
        }


        delete_bt.setOnClickListener { //스터디 탈퇴
            // val intent = Intent(this,StudydeleteActivity::class.java)
            //startActivity(intent)
            var name :String = " "
            if (user !=null){
                db.collection("users").document(user.uid).get().addOnSuccessListener {
                    name = it.data?.get("name").toString()
                }
            }

            if (user != null) {
                db.collection("users").document(user.uid).collection(collection.toString()).document(userdocument.toString()) //users 경로 타고간거
                    .delete()
                    .addOnSuccessListener {//회원 개설자
                        db.collection(collection.toString())
                            .get()
                            .addOnSuccessListener { result ->
                                for(i in result) {
                                    if(i.data?.get("address").toString().equals(openaddress)) {
                                        var document = i.id
                                        val dele = hashMapOf<String,Any>(name to FieldValue.delete())
                                        // db.collection(collection.toString()).document("${document}Attendance").collection("date").document("1009").update(dele)
                                        db.collection(collection.toString()).document(document).collection(creatuser.toString()).document(user.uid)
                                            .delete()
                                            .addOnSuccessListener {
                                                Log.d("Group","성공")
                                                Toast.makeText(this,"탈퇴 완료",Toast.LENGTH_SHORT).show()
                                                val intent = Intent(this,MainActivity::class.java)
                                                startActivity(intent)
                                                db.collection(collection.toString()).document(document).collection(creatuser.toString())
                                                    .get()
                                                    .addOnSuccessListener {
                                                        Log.d("person", "${it.documents.size}")
                                                        val count = it.documents.size
                                                        if(count == 0){
                                                            db.collection(collection.toString()).document(document)
                                                                .delete()
                                                                .addOnSuccessListener {

                                                                }
                                                        }
                                                    }
                                            }
                                            .addOnFailureListener {
                                                Log.d("Group","실패")
                                                Toast.makeText(this,"탈퇴 실패",Toast.LENGTH_SHORT).show()
                                            }
                                    }
                                }
                            }
                    }
            }
        }
        delete_bt2.setOnClickListener {
            db.collection(collection.toString())
                .get()
                .addOnSuccessListener { result ->
                    for(i in result) {
                        if(i.data?.get("address").toString().equals(openaddress)) {
                            var document = i.id
                            val intent = Intent(this,StudyInfoActivity::class.java)
                            intent.putExtra("collection", collection)
                            intent.putExtra("document", document)
                            intent.putExtra("creatuser", creatuser)
                            startActivity(intent)
                        }
                    }
                }
        }
    }

    private fun showSettingPopup(say : String?) {
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.notice_popup, null)
        val textView: TextView = view.findViewById(R.id.popup)
        textView.text = say

        val alertDialog = AlertDialog.Builder(this)
            .setTitle("공지사항")
            .setNeutralButton("확인", null)
            .create()

        alertDialog.setView(view)
        alertDialog.show()

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