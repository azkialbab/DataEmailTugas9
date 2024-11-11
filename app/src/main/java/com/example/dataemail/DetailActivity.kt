package com.example.dataemail

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso

class DetailActivity : AppCompatActivity() {

    private lateinit var ivUserAvatar: ImageView
    private lateinit var tvUserName: TextView
    private lateinit var tvUserEmail: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)


        ivUserAvatar = findViewById(R.id.iv_user_avatar)
        tvUserName = findViewById(R.id.tv_user_name)
        tvUserEmail = findViewById(R.id.tv_user_email)


        val userName = intent.getStringExtra("USER_NAME")
        val userEmail = intent.getStringExtra("USER_EMAIL")
        val userAvatar = intent.getStringExtra("USER_AVATAR")


        tvUserName.text = userName
        tvUserEmail.text = userEmail


        if (!userAvatar.isNullOrEmpty()) {
            Picasso.get().load(userAvatar).into(ivUserAvatar)
        }
    }
}
