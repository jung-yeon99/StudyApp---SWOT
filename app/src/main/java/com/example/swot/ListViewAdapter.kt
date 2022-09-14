package com.example.swot

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.DataBindingUtil.setContentView
import com.example.swot.databinding.ActivityAddBinding
import com.example.swot.databinding.ActivityJoinBinding
import com.example.swot.databinding.StudyListItemBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.study_list_item.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ListViewAdapter(val context: Context, val studyList: ArrayList<ListViewItem>) : BaseAdapter() {
    val check = hashMapOf(
        "오늘 공부 할 양은 다했나요?" to false,
        "약속 한 시간에 늦지 않았나요?" to false,
        "약속 한 과제는 해왔나요?" to false,
        "스터디중에 집중을 잘 하였나요?" to false,
        "스터디원들과 협력을 잘했나요?" to false
    )
    private lateinit var auth: FirebaseAuth
    val db = Firebase.firestore
    var count: Int? = 0
    val long_now = System.currentTimeMillis()
    // 현재 시간을 Date 타입으로 변환
    val t_date = Date(long_now)
    // 날짜, 시간을 가져오고 싶은 형태 선언
    val t_dateFormat = SimpleDateFormat("MMdd", Locale("ko", "KR"))
    // 현재 시간을 dateFormat 에 선언한 형태의 String 으로 변환
    val date = t_dateFormat.format(t_date)
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = LayoutInflater.from(context).inflate(R.layout.study_list_item, null)
        val profile = view.findViewById<ImageView>(R.id.iv_profile) //binding으로도 가능
        val name = view.findViewById<TextView>(R.id.s_name)
        val greet = view.findViewById<TextView>(R.id.s_greet)
        val person = view.findViewById<TextView>(R.id.person)
        val join = view.findViewById<Button>(R.id.join)
        val study = studyList[position] //postion 은 0 부터
        auth = FirebaseAuth.getInstance()
        profile.setImageResource(study.profile)
        name.text = study.name
        greet.text = study.greet
        val user = auth.currentUser
        val con = db.collection("users")
        var flag = 0
        val arrayadress = arrayListOf<String>() //주소

        Log.d("ListPerson", "${study.creatuser}")
        if (user != null) { // 회원이면
            db.collection(study.collection).document(study.document).collection(study.creatuser)
                .get()
                .addOnSuccessListener {
                    Log.d("person", "${it.documents.size}")
                    count = it.documents.size
                    person.text = "${count}/${study.person}"
                }
            con.document(user.uid).collection(study.collection)
                .get()
                .addOnSuccessListener {
                    for (i in it) {
                        arrayadress.add(i.get("address").toString())
                        Log.d("List", "${i.get("address").toString()}")
                        Log.d("List", "${study.address.toString()}")
                    }
                    join.setOnClickListener { //끝까지 다 검사를 해야될 수 있 때문에 성능이 안좋음 최악 -> O(n^2)
                        if (user != null) { // 회원이면
                            db.collection(study.collection).document(study.document)
                                .collection(study.creatuser)
                                .get()
                                .addOnSuccessListener {
                                    Log.d("person", "${it.documents.size}")
                                    count = it.documents.size
                                    for (i in 0..(arrayadress.size - 1)) {
                                        Log.d("ListView", "${arrayadress.size - 1}")
                                        if (study.address.equals(arrayadress[i])) {
                                            flag = 1
                                            break;
                                        }
                                    }
                                    if (flag == 1) {
                                        Toast.makeText(
                                            context,
                                            "이미 가입이 되어있습니다.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else if (flag == 0 && count.toString().equals(study.person)) {
                                        Log.d("ListView", "${count}")
                                        Log.d("ListView", "${study.person}")
                                        Toast.makeText(context, "인원이 가득찼습니다.", Toast.LENGTH_SHORT)
                                            .show()
                                    } else {
                                        Log.d("ListView", "${count}")
                                        Log.d("ListView", "${study.person}")
                                        val intent = Intent(context, JoinActivity::class.java)
                                        //  intent.putExtra("user",study.user)
                                        intent.putExtra("studyname", study.name)
                                        intent.putExtra("say", study.say)
                                        intent.putExtra("address", study.address)
                                        intent.putExtra("score", study.score)
                                        intent.putExtra("comment", study.greet)
                                        intent.putExtra("creatuser", study.creatuser)
                                        intent.putExtra("document", study.document)

                                        if (user != null) {
                                            val add = hashMapOf<String,Any>()
                                            db.collection(study.collection).document(study.document)
                                                .collection(study.creatuser)
                                                .document(user.uid)
                                                .set(check)
                                        }
                                        intent.putExtra("category", study.collection)
                                        context.startActivity(intent)
                                    }
                                }
                        }
                    }
                }
            con.document(user.uid)
                .get()
                .addOnSuccessListener {
                    var introduce = it.data?.get("introduce")
                    val name = it.data?.get("name").toString()
                    var data = hashMapOf<String,Any>(name to false)
                    db.collection(study.collection).document("${study.document}Attendance").collection("date").document(date).update(data)
                    // arrayinfo = hashMapOf(introduce, name)
                }

        }
        return view
    }
    override fun getItem(position: Int): Any {
        return studyList[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return studyList.size
    }
}

