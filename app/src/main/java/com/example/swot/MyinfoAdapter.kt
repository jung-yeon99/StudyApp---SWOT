package com.example.swot

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.swot.CheckAdapter
import kotlinx.android.synthetic.main.list_item.view.*
import kotlinx.android.synthetic.main.study_list_item.view.*
import org.w3c.dom.Text


class MyinfoAdapter(val myInfoList: ArrayList<ListViewItem>, val context: Context) : RecyclerView.Adapter<MyinfoAdapter.MyinfoViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyinfoAdapter.MyinfoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.study_list_item,parent,false)

        return MyinfoViewHolder(view)
    }

    override fun getItemCount(): Int {
        return myInfoList.size
    }

    override fun onBindViewHolder(holder: MyinfoAdapter.MyinfoViewHolder, position: Int) {
        holder.image.setImageResource(myInfoList.get(position).profile)
        holder.name.text = myInfoList.get(position).name
        holder.greet.text = myInfoList.get(position).greet
        holder.join.text = "입장"
        holder.join.setOnClickListener {
            val intent = Intent(context,GroupActivity::class.java)
            intent.putExtra("studyname", holder.name.text)
            intent.putExtra("address", myInfoList.get(position).address)
            intent.putExtra("say", myInfoList.get(position).say)
            intent.putExtra("collection",myInfoList.get(position).collection)
            intent.putExtra("document",myInfoList.get(position).document)
            intent.putExtra("creatuser",myInfoList.get(position).creatuser)
            context.startActivity(intent)
        }
    }

    class MyinfoViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val image = itemView.findViewById<ImageView>(R.id.iv_profile)
        val name = itemView.findViewById<TextView>(R.id.s_name)
        val greet = itemView.findViewById<TextView>(R.id.s_greet)
        val join = itemView.findViewById<Button>(R.id.join)
        val address = itemView.findViewById<TextView>(R.id.address)

    }

}