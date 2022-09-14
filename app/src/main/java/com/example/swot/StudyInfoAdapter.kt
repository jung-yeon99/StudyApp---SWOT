package com.example.swot

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import java.lang.reflect.Array


class StudyInfoAdapter (val studyinfoList: ArrayList<StudyItem>) : RecyclerView.Adapter<StudyInfoAdapter.CustomviewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudyInfoAdapter.CustomviewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.study_person_list, parent, false)
        return CustomviewHolder(view).apply {
            itemView.setOnClickListener {
                val curPos : Int = adapterPosition
                val studyuser : StudyItem = studyinfoList.get(curPos)
                val name = studyuser.name
                val introduce = studyuser.introduce
                val collection = studyuser.collection
                val document = studyuser.document
                val creatuser = studyuser.creatuser
                val user = studyuser.user

                val intent = Intent(view.context, StudyuserActivity::class.java)
                intent.putExtra("name",name)
                intent.putExtra("introduce",introduce)
                intent.putExtra("collection",collection)
                intent.putExtra("document",document)
                intent.putExtra("creatuser",creatuser)
                intent.putExtra("user",user)
                view.context.startActivity(intent)
            }
        }
    }

    override fun onBindViewHolder(holder: StudyInfoAdapter.CustomviewHolder, position: Int) {
        holder.image.setImageResource(studyinfoList.get(position).profile)
        holder.name.text = studyinfoList.get(position).name
    }

    override fun getItemCount(): Int {
        return studyinfoList.size
    }

    class CustomviewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image = itemView.findViewById<ImageView>(R.id.iv_profile)
        val name = itemView.findViewById<TextView>(R.id.s_name)

    }


}

