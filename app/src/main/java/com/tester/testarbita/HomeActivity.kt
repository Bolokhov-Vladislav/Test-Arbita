package com.tester.testarbita

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_home)
    }

    fun game(view: View) {
        startActivity(Intent(this@HomeActivity, MainActivity::class.java))
    }
    fun service(view: View) {
        startActivity(Intent(this@HomeActivity, ServiceActivity::class.java))
    }
}