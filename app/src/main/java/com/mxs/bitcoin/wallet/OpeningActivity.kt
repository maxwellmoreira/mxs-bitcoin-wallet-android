package com.mxs.bitcoin.wallet

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import com.mxs.bitcoin.wallet.core.SqlCipher

/**
 *
 */
class OpeningActivity : FragmentActivity() {

    /**
     *
     */
    private var currentImageId: Int = 0

    /**
     *
     */
    companion object {
        private val imageIds = arrayOf(
            R.drawable.logo_1,
            R.drawable.logo_2,
            R.drawable.logo_3,
            R.drawable.logo_4,
            R.drawable.logo_5,
            R.drawable.logo_6
        )
    }

    /**
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val random = java.util.Random()
        val index = random.nextInt(imageIds.size)
        currentImageId = imageIds[index]
        setContentView(R.layout.opening)
        val imageView = findViewById<ImageView>(R.id.logo)
        imageView.setImageResource(currentImageId)

        Handler(Looper.getMainLooper())
            .postDelayed(
                {
                    val databaseExist = SqlCipher(this).checkDatabaseFileExists()
                    if (databaseExist) {
                        val pinIntent = Intent(this, AccessActivity::class.java)
                        startActivity(pinIntent)
                    } else {
                        val warningIntent = Intent(this, WarningActivity::class.java)
                        startActivity(warningIntent)
                    }
                    finish()
                }, 1500
            )
    }
}