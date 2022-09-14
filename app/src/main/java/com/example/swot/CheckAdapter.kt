package com.example.swot

import android.content.Intent
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.os.persistableBundleOf
import androidx.recyclerview.widget.RecyclerView
import com.example.swot.CheckItem
import com.example.swot.MyinfoActivity
import com.example.swot.R
import com.example.swot.databinding.ActivityAddBinding
import com.example.swot.databinding.ActivityJoinBinding
import com.example.swot.databinding.ListItemBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_join.*
import kotlinx.android.synthetic.main.list_item.view.*

class CheckAdapter(val profilelist : ArrayList<CheckItem>) : RecyclerView.Adapter<CheckAdapter.CustomViewHolder>() {
    private lateinit var auth: FirebaseAuth
    val db = Firebase.firestore
    // var i = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckAdapter.CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)

        /* view.button1.setOnClickListener {
               val score = view.findViewById<EditText>(R.id.edit).editableText
               view.progress.progress = score.toString().toInt()
           }*/
        return CustomViewHolder(view)
    }

    override fun getItemCount(): Int {;
        return profilelist.size
    }

    override fun onBindViewHolder(holder: CheckAdapter.CustomViewHolder, position: Int) {
        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        holder.image.setImageResource(profilelist.get(position).image)
        holder.two.text = profilelist.get(position).two
        holder.score.setText(profilelist.get(position).score)
        holder.progress.progress = profilelist.get(position).score.toInt()

        if (user != null) {
            holder.button1.setOnClickListener {
                Log.d("Check", "?")
                holder.progress.progress = holder.score.text.toString().toInt()
                db.collection("users").document(user.uid).collection(profilelist[position].category)
                    .document(profilelist[position].col)
                    .update("score",holder.score.text.toString())
                Log.d("Check", "${profilelist[position].category}")
                //가입이 완료되면 내 정보로 이동하게 해놨음 추후 변경도 고려
            }
        }
    }

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image = itemView.findViewById<ImageView>(R.id.id_profile) //프로필 사진
        val two = itemView.findViewById<TextView>(R.id.studyname2) //무슨 스터디인지 이름 예) 알고리즘 스터디
        val score = itemView.findViewById<EditText>(R.id.edit)
        val progress = itemView.findViewById<ProgressBar>(R.id.progress)
        val button1 = itemView.findViewById<Button>(R.id.button1)
    }

}