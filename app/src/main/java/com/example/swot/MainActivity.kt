package com.example.swot

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.core.view.get
import androidx.core.view.size
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.study_list_item.*
import kotlinx.android.synthetic.main.study_list_item.view.*

class MainActivity : AppCompatActivity() {
    var pos = 0
    var dataArr = arrayOf("어학","IT/컴퓨터","공무원","자격증","중고등","편입","기타")

    private lateinit var auth: FirebaseAuth
    val db = Firebase.firestore
    // var firestore : FirebaseFirestore? = null

    var i : Int= 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar2)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)  // 왼쪽 버튼 사용 여부 true
        supportActionBar?.setDisplayShowTitleEnabled(false)
        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        /*   if (dataArr[pos].equals("IT/컴퓨터")) {
           }*/
        c_spinner.adapter = ArrayAdapter.createFromResource(this,R.array.spinner_year,android.R.layout.simple_spinner_item)
        c_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                category.text = dataArr[position]
                pos = position
                var studyList = arrayListOf<ListViewItem>()
                when (position) {
                    0 -> { //어학
                        db.collection("LANG")
                            .get()
                            .addOnSuccessListener { result ->
                                for (document in result) {
                                    var ss_name = document.data?.get("studyname").toString()
                                    var ss_comment = document.data?.get("comment").toString()
                                    var ss_say = document.data?.get("say").toString()
                                    var ss_address = document.data?.get("address").toString()
                                    var collection = "LANG"
                                    var person = document.data?.get("person").toString()
                                    var creatuser = document.data?.get("creatuser").toString()
                                    var score = document.data?.get("score").toString()
                                    var document = document.id

                                    studyList.add(ListViewItem(R.drawable.swot1, ss_name,ss_comment ,ss_say,ss_address,collection,person,document,creatuser,score))
                                    //     i++
                                    //      Log.d("Main", "$i 다시 메인으로 돌아왔을 때 확인용")
                                }
                                val Adapter = ListViewAdapter(this@MainActivity, studyList)
                                mListView.adapter = Adapter
                            }
                            .addOnFailureListener { exception ->
                                // Toast.makeText(this, "데이터베이스 연동 실패", Toast.LENGTH_SHORT).show()
                            }
                    }
                    1 -> {
                        db.collection("IT")
                            .get()
                            .addOnSuccessListener { result ->
                                for (document in result) {
                                    var ss_name = document.data?.get("studyname").toString()
                                    var ss_comment = document.data?.get("comment").toString()
                                    var ss_say = document.data?.get("say").toString()
                                    var ss_address = document.data?.get("address").toString()
                                    var collection = "IT"
                                    var person = document.data?.get("person").toString()
                                    var creatuser = document.data?.get("creatuser").toString()
                                    var score = document.data?.get("score").toString()
                                    var document = document.id
                                    studyList.add(ListViewItem(R.drawable.swot1, ss_name,ss_comment ,ss_say,ss_address,collection,person,document,creatuser,score))
                                    //       i++
                                    //      Log.d("Main", "$i 다시 메인으로 돌아왔을 때 확인용")
                                }
                                val Adapter = ListViewAdapter(this@MainActivity, studyList)
                                mListView.adapter = Adapter
                            }
                            .addOnFailureListener { exception ->
                                // Toast.makeText(this, "데이터베이스 연동 실패", Toast.LENGTH_SHORT).show()
                            }
                    }
                    2 -> {
                        db.collection("OFFICIAL")
                            .get()
                            .addOnSuccessListener { result ->
                                for (document in result) {
                                    var ss_name = document.data?.get("studyname").toString()
                                    var ss_comment = document.data?.get("comment").toString()
                                    var ss_say = document.data?.get("say").toString()
                                    var ss_address = document.data?.get("address").toString()
                                    var collection = "OFFICIAL"
                                    var person = document.data?.get("person").toString()
                                    var creatuser = document.data?.get("creatuser").toString()
                                    var score = document.data?.get("score").toString()
                                    var document = document.id

                                    studyList.add(ListViewItem(R.drawable.swot1, ss_name,ss_comment ,ss_say,ss_address,collection,person,document,creatuser,score))
                                    //     i++
                                    //      Log.d("Main", "$i 다시 메인으로 돌아왔을 때 확인용")
                                }
                                val Adapter = ListViewAdapter(this@MainActivity, studyList)
                                mListView.adapter = Adapter
                            }
                            .addOnFailureListener { exception ->
                                // Toast.makeText(this, "데이터베이스 연동 실패", Toast.LENGTH_SHORT).show()
                            }
                    }
                    3 -> {
                        db.collection("LICENSE")
                            .get()
                            .addOnSuccessListener { result ->
                                for (document in result) {
                                    var ss_name = document.data?.get("studyname").toString()
                                    var ss_comment = document.data?.get("comment").toString()
                                    var ss_say = document.data?.get("say").toString()
                                    var ss_address = document.data?.get("address").toString()
                                    var collection = "LICENSE"
                                    var person = document.data?.get("person").toString()
                                    var creatuser = document.data?.get("creatuser").toString()
                                    var score = document.data?.get("score").toString()
                                    var document = document.id

                                    studyList.add(ListViewItem(R.drawable.swot1, ss_name,ss_comment ,ss_say,ss_address,collection,person,document,creatuser,score))
                                    //     i++
                                    //      Log.d("Main", "$i 다시 메인으로 돌아왔을 때 확인용")
                                }
                                val Adapter = ListViewAdapter(this@MainActivity, studyList)
                                mListView.adapter = Adapter
                            }
                            .addOnFailureListener { exception ->
                                // Toast.makeText(this, "데이터베이스 연동 실패", Toast.LENGTH_SHORT).show()
                            }
                    }
                    4 -> {
                        db.collection("SCHOOL")
                            .get()
                            .addOnSuccessListener { result ->
                                for (document in result) {
                                    var ss_name = document.data?.get("studyname").toString()
                                    var ss_comment = document.data?.get("comment").toString()
                                    var ss_say = document.data?.get("say").toString()
                                    var ss_address = document.data?.get("address").toString()
                                    var collection = "MHSCHOOL"
                                    var person = document.data?.get("person").toString()
                                    var creatuser = document.data?.get("creatuser").toString()
                                    var score = document.data?.get("score").toString()
                                    var document = document.id

                                    studyList.add(ListViewItem(R.drawable.swot1, ss_name,ss_comment ,ss_say,ss_address,collection,person,document,creatuser,score))
                                    //     i++
                                    //      Log.d("Main", "$i 다시 메인으로 돌아왔을 때 확인용")
                                }
                                val Adapter = ListViewAdapter(this@MainActivity, studyList)
                                mListView.adapter = Adapter
                            }
                            .addOnFailureListener { exception ->
                                // Toast.makeText(this, "데이터베이스 연동 실패", Toast.LENGTH_SHORT).show()
                            }
                    }
                    5 -> {
                        db.collection("TRANSFER")
                            .get()
                            .addOnSuccessListener { result ->
                                for (document in result) {
                                    var ss_name = document.data?.get("studyname").toString()
                                    var ss_comment = document.data?.get("comment").toString()
                                    var ss_say = document.data?.get("say").toString()
                                    var ss_address = document.data?.get("address").toString()
                                    var collection = "TRANSFER"
                                    var person = document.data?.get("person").toString()
                                    var creatuser = document.data?.get("creatuser").toString()
                                    var score = document.data?.get("score").toString()
                                    var document = document.id

                                    studyList.add(ListViewItem(R.drawable.swot1, ss_name,ss_comment ,ss_say,ss_address,collection,person,document,creatuser,score))

                                }
                                val Adapter = ListViewAdapter(this@MainActivity, studyList)
                                mListView.adapter = Adapter
                            }
                            .addOnFailureListener { exception ->
                                // Toast.makeText(this, "데이터베이스 연동 실패", Toast.LENGTH_SHORT).show()
                            }
                    }
                    6 -> {
                        db.collection("ETC")
                            .get()
                            .addOnSuccessListener { result ->
                                for (document in result) {
                                    var ss_name = document.data?.get("studyname").toString()
                                    var ss_comment = document.data?.get("comment").toString()
                                    var ss_say = document.data?.get("say").toString()
                                    var ss_address = document.data?.get("address").toString()
                                    var collection = "ETC"
                                    var person = document.data?.get("person").toString()
                                    var creatuser = document.data?.get("creatuser").toString()
                                    var score = document.data?.get("score").toString()
                                    var document = document.id

                                    studyList.add(ListViewItem(R.drawable.swot1, ss_name,ss_comment ,ss_say,ss_address,collection,person,document,creatuser,score))
                                    //     i++
                                    //      Log.d("Main", "$i 다시 메인으로 돌아왔을 때 확인용")
                                }
                                val Adapter = ListViewAdapter(this@MainActivity, studyList)
                                mListView.adapter = Adapter
                            }
                            .addOnFailureListener { exception ->
                                // Toast.makeText(this, "데이터베이스 연동 실패", Toast.LENGTH_SHORT).show()
                            }
                    }
                    //일치하는게 없는 경우
                    else -> {
                        var studyList = arrayListOf<ListViewItem>()
                    }
                }
            }
        }
        // 파이어스토어 인스턴스 초기화
        //   firestore = FirebaseFirestore.getInstance()
        /* Infoimage.setOnClickListener {
             val intent = Intent(this, MyinfoActivity::class.java)
             startActivity(intent)
         }*/

        creat.setOnClickListener {
            val text = dataArr[pos]
            val intent = Intent(this, AddActivity::class.java)
            intent.putExtra("st_category", text)
            startActivity(intent)
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.backuser,menu)

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

            R.id.user -> {
                val intent = Intent(applicationContext,MyinfoActivity::class.java)
                startActivity(intent)
                return true;
            }
        }
        return super.onOptionsItemSelected(item)
    }
}