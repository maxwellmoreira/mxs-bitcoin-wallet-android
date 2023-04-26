package com.mxs.bitcoin.wallet

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.FragmentActivity

/**
 *
 */
class WarningActivity : FragmentActivity() {

    /**
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.warning)
        val buttonNext = findViewById<Button>(R.id.btn_warning_next)
        buttonNext.setOnClickListener {
            val registerIntent = Intent(this, RegisterActivity::class.java)
            startActivity(registerIntent)
        }
    }
}