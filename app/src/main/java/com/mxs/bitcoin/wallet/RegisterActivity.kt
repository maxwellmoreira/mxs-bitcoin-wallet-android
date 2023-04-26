package com.mxs.bitcoin.wallet

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.mxs.bitcoin.wallet.core.Seed

/**
 *
 */
class RegisterActivity : FragmentActivity() {

    /**
     *
     */
    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.register)

        val buttonGenerateSeeds = findViewById<Button>(R.id.btn_register_generate_seeds)
        val grayOutline = ContextCompat.getDrawable(this, R.drawable.gray_outline)
        val yellowOutline = ContextCompat.getDrawable(this, R.drawable.yellow_outline)
        val buttonNext = findViewById<Button>(R.id.btn_register_next)
        val editTextRegisterSeeds = findViewById<EditText>(R.id.et_register_seeds)
        val textViewRegisterSeeds = findViewById<TextView>(R.id.tv_register_seeds)

        buttonNext.isEnabled = false
        buttonNext.setTextColor(Color.parseColor("#FF0AA8AE"))
        buttonNext.background = grayOutline

        editTextRegisterSeeds.setText("")
        textViewRegisterSeeds.text = ""

        buttonGenerateSeeds.setOnClickListener {
            val seeds = Seed().generateSeed(editTextRegisterSeeds.text.toString())
            var formattedSeeds = "\n"
            var counter = 0

            for (word in seeds) {
                formattedSeeds += "$word    "
                counter++
                if (counter == 4) {
                    formattedSeeds += "\n"
                    counter = 0
                }
            }

            textViewRegisterSeeds.text = formattedSeeds
            buttonGenerateSeeds.setTextColor(Color.parseColor("#FF0AA8AE"))
            buttonGenerateSeeds.background = grayOutline
            buttonGenerateSeeds.isEnabled = false
            editTextRegisterSeeds.isEnabled = false

            buttonNext.isEnabled = true
            buttonNext.setTextColor(Color.parseColor("#FBAC2C"))
            buttonNext.background = yellowOutline
        }

        buttonNext.setOnClickListener {
            val pinIntent = Intent(this, PinActivity::class.java)
            pinIntent.putExtra("seeds", textViewRegisterSeeds.text.toString())
            startActivity(pinIntent)
        }
    }
}