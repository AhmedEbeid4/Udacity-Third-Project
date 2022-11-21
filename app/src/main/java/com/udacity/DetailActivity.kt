package com.udacity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {
    private var context=this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)
        file_name.text=this.intent.getStringExtra("file")
        var statusText=this.intent.getStringExtra("status")
        status.text = statusText
        if(statusText.equals("Fail")){
            status.setTextColor(Color.parseColor("#FF2200"))
        }else{
            status.setTextColor(Color.parseColor("#004349"))
        }
        okBtn.setOnClickListener {
            startActivity(Intent(context,MainActivity::class.java))
        }

    }

}
