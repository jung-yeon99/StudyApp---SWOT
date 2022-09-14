package com.example.swot

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.swot.databinding.ActivityAttendanceBinding
import com.example.swot.databinding.ActivityJoinBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_studyuser.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AttendanceAdapter(val attendancelist:  ArrayList<AttendanceItem>) : RecyclerView.Adapter<AttendanceAdapter.CustomViewHolder>()
{
    private var _binding: ActivityAttendanceBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    val db = Firebase.firestore
    var doc = hashMapOf<String,Any>()
    val long_now = System.currentTimeMillis()
    // 현재 시간을 Date 타입으로 변환
    val t_date = Date(long_now)
    // 날짜, 시간을 가져오고 싶은 형태 선언
    val t_dateFormat = SimpleDateFormat("MMdd", Locale("ko", "KR"))
    // 현재 시간을 dateFormat 에 선언한 형태의 String 으로 변환
    val date = t_dateFormat.format(t_date)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttendanceAdapter.CustomViewHolder
    {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.attendance_list_item,parent,false)
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: AttendanceAdapter.CustomViewHolder, position: Int) {
        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        // var Check = false
        holder.name.text=attendancelist.get(position).name
        //  Log.d("Attendance","${holder.submit}")

        // 오늘 날짜의 데이터를 이용해서 파이어베이스의 연결]

        db.collection(attendancelist[position].col).document("${attendancelist[position].category}Attendance")
            .collection("date").document(date).get().addOnSuccessListener {
                if( it.data.isNullOrEmpty()){
                    for(i in 0..(attendancelist.size-1))
                    //   if(attendancelist.get(i).name){
                        doc.put(attendancelist.get(i).name,false)
                    //    }
                    db.collection(attendancelist[position].col).document("${attendancelist[position].category}Attendance")
                        .collection("date").document(date).set(doc)

                }
            }
        db.collection(attendancelist[position].col).document("${attendancelist[position].category}Attendance")
            .collection("date").get().addOnSuccessListener {
                for(i in it){
                    if( i.id.equals(date)){
                        if(i.data?.get(attendancelist.get(position).name) == "true") {
                            holder.check.text = "출석"
                        }
                    }
                }
            }
        var myname : String= " "
        if (user != null) {
            db.collection("users").document(user.uid).get().addOnSuccessListener {
                myname = it.data?.get("name").toString()
            }
        }
        holder.submit.setOnClickListener {
            if (myname.equals(attendancelist.get(position).name)) {
                var doc = hashMapOf(attendancelist.get(position).name to "출석")
                holder.check.text = "출석"
                db.collection(attendancelist[position].col)
                    .document("${attendancelist[position].category}Attendance")
                    .collection("date")
                    .document(date)
                    .update(holder.name.text.toString(), "true")
            }
            else{
                Log.d("dsf","자기꺼만 눌러 ")
            }
        }

    }

    override fun getItemCount(): Int {
        return attendancelist.size
    }

    class CustomViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView){
        val name = ItemView.findViewById<TextView>(R.id.name)
        val submit = ItemView.findViewById<Button>(R.id.checkbutton)
        val check = ItemView.findViewById<TextView>(R.id.check)
    }
}