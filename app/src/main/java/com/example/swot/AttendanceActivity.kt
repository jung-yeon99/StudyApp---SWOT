package com.example.swot


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.CalendarView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.swot.databinding.ActivityJoinBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_attendance.*
import kotlinx.android.synthetic.main.activity_group.*
import java.text.SimpleDateFormat
import java.util.*


class AttendanceActivity : AppCompatActivity() {
    lateinit var calendarView: CalendarView
    lateinit var diaryTextView: TextView
    lateinit var title: TextView

    private lateinit var auth: FirebaseAuth
    val db = Firebase.firestore
    var username = ArrayList<String>()
    val attend = arrayListOf<AttendanceItem>()
    val click_attend = arrayListOf<AttendanceClickItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_attendance)

        setSupportActionBar(toolbar13)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)  // 왼쪽 버튼 사용 여부 true
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val long_now = System.currentTimeMillis()
        // 현재 시간을 Date 타입으로 변환
        val t_date = Date(long_now)
        // 날짜, 시간을 가져오고 싶은 형태 선언
        val t_dateFormat = SimpleDateFormat("MMdd", Locale("ko", "KR"))
        // 현재 시간을 dateFormat 에 선언한 형태의 String 으로 변환
        val date = t_dateFormat.format(t_date)

        var j = 0
        var collection = intent.getStringExtra("collection").toString()
        var document = intent.getStringExtra("document").toString()
        var creatuser = intent.getStringExtra("creatuser").toString()
        Log.d("1Adch", "${collection}${document}${creatuser}")

        db.collection(collection).document(document).collection(creatuser)
            .get()
            .addOnSuccessListener { it ->
                Log.d("2Adch", "${collection}${document}${creatuser}")
                val count = it.size()
                for (i in it) {
                    db.collection("users").document(i.id)
                        .get()
                        .addOnSuccessListener {
                            var name = it.data?.get("name").toString()
                            Log.d("Adch", "${collection}${document}${creatuser}")
                            Log.d("555", "${calendarView.date.toString()}")
                            attend.add(AttendanceItem(name, collection, document, creatuser))
                            j++
                            if (j == count) {
                                Log.d("3Adch", "${collection}${document}${creatuser}")
                                attendancelist.layoutManager =
                                    LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                                attendancelist.setHasFixedSize(true)
                                attendancelist.adapter = AttendanceAdapter(attend)
                            }
                        }
                }
            }
        // UI값 생성
        calendarView = findViewById(R.id.calendarView)
        title = findViewById(R.id.title)

        title.text = "출석 확인"


        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            var attendance_Date: String = " "
            //  date = String.format("%d / %d / %d", year, month + 1, dayOfMonth)
            if (dayOfMonth.toString().length == 1) {
                attendance_Date = String.format("%d0%d", month + 1, dayOfMonth)
                Log.d("달력클릭", String.format("%d0%d", month + 1, dayOfMonth))
            } else {
                attendance_Date = String.format("%d%d", month + 1, dayOfMonth)
            } // 위에 코드랑 똑같음
            Log.d("날짜 확인","${attendance_Date}")
            Log.d("날짜 확인2", "${date}")
            if (date==attendance_Date) {
                Log.d("ss","date")
                attendancelist.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                attendancelist.setHasFixedSize(true)
                attendancelist.adapter = AttendanceAdapter(attend)
            } else {
                click_attend.clear()
                db.collection(collection).document("${document}Attendance")
                    .collection("date").document(attendance_Date)
                    .get().addOnSuccessListener {
                        var  attendance_key = it.data?.keys
                        var attendance_value = it.data?.values
                        if (attendance_key != null) {
                            for (i in 0..(attendance_key.size - 1)) {
                                if( attendance_value?.elementAt(i).toString().equals("true")){
                                    click_attend.add(AttendanceClickItem(attendance_key.elementAt(i), "출석"))
                                }
                                else{
                                    click_attend.add(AttendanceClickItem(attendance_key.elementAt(i), "결석"))
                                }

                                Log.d("달력클릭 데이터 확인", "${attendance_key.elementAt(i)}")
                                Log.d("달력클릭 데이터 확인", "${attendance_value?.elementAt(i)}")
                            }
                        }
                        attendancelist.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                        attendancelist.setHasFixedSize(true)
                        attendancelist.adapter = AttendanceClickAdapter(click_attend)

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