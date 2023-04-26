package com.mxs.bitcoin.wallet

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

/**
 *
 */
class PinActivity : FragmentActivity() {

    /**
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pin)
        val grayOutline = ContextCompat.getDrawable(this, R.drawable.gray_outline)
        val yellowOutline = ContextCompat.getDrawable(this, R.drawable.yellow_outline)
        val editTextPin = findViewById<EditText>(R.id.et_pin_pin)
        val buttonFinish = findViewById<Button>(R.id.btn_pin_finish)
        buttonFinish.background = grayOutline
        buttonFinish.setTextColor(Color.parseColor("#FF0AA8AE"))
        buttonFinish.isEnabled = false

        val seeds = intent.getStringExtra("seeds")

        buttonFinish.setOnClickListener {
            val accessIntent = Intent(this, AccessActivity::class.java)
            accessIntent.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(accessIntent)
        }

        editTextPin.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if ((s?.length ?: 0) >= 6) {
                    buttonFinish.isEnabled = true
                    buttonFinish.background = yellowOutline
                    buttonFinish.setTextColor(Color.parseColor("#FBAC2C"))
                } else {
                    buttonFinish.isEnabled = false
                    buttonFinish.background = grayOutline
                    buttonFinish.setTextColor(Color.parseColor("#FF0AA8AE"))
                }
            }
        })
    }
}