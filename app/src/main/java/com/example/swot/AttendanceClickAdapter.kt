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

class AttendanceClickAdapter(val attendancechecklist : ArrayList<AttendanceClickItem>) : RecyclerView.Adapter<AttendanceClickAdapter.CustomViewHolder>() {
    private lateinit var auth: FirebaseAuth
    val db = Firebase.firestore
    // var i = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttendanceClickAdapter.CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.attendance_click_item, parent, false)
        return CustomViewHolder(view)
    }

    override fun getItemCount(): Int {;
        return attendancechecklist.size
    }

    override fun onBindViewHolder(holder: AttendanceClickAdapter.CustomViewHolder, position: Int) {
        holder.name.text = attendancechecklist.get(position).Name
        holder.check.text = attendancechecklist.get(position).Check.toString()
        // auth = FirebaseAuth.getInstance()
        // val user = auth.currentUser
    }

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.findViewById<TextView>(R.id.Name)
        val check = itemView.findViewById<TextView>(R.id.Check)

    }


}