package com.example.swot

import android.widget.Button
import com.google.firebase.auth.FirebaseUser

//클래스 모델 객체
class ListViewItem(val profile : Int,
                   val name: String,
                   val greet : String ,
                   val say : String,
                   val address : String,
                   val collection : String,
                   val person : String,
                   val document: String,
                   val creatuser : String,
                   val score : String
)