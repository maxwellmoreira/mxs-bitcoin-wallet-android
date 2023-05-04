package com.mxs.bitcoin.wallet

import android.database.sqlite.SQLiteDatabase
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.biometric.BiometricManager
import androidx.fragment.app.FragmentActivity
import com.mxs.bitcoin.wallet.core.Biometric
import com.mxs.bitcoin.wallet.core.SqlCipher
import java.util.*

/**
 * Activity responsible for accessing the wallet using the PIN (Personal Identification Number)
 */
class AccessActivity : FragmentActivity() {

    private lateinit var sqlCipher: SqlCipher

    /**
     * Function responsible for initializing the activity, defining the layout and configuring the
     * initial behavior of the screen
     *
     * @param savedInstanceState used to store information about the previous state of the activity
     * in case it has been destroyed and needs to be recreated
     */
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.access)

        val button0 = findViewById<Button>(R.id.btn_0)
        val button1 = findViewById<Button>(R.id.btn_1)
        val button2 = findViewById<Button>(R.id.btn_2)
        val button3 = findViewById<Button>(R.id.btn_3)
        val button4 = findViewById<Button>(R.id.btn_4)
        val button5 = findViewById<Button>(R.id.btn_5)
        val button6 = findViewById<Button>(R.id.btn_6)
        val button7 = findViewById<Button>(R.id.btn_7)
        val button8 = findViewById<Button>(R.id.btn_8)
        val button9 = findViewById<Button>(R.id.btn_9)
        val buttonX = findViewById<Button>(R.id.btn_x)
        val buttonOk = findViewById<Button>(R.id.btn_ok)
        val editTextPin = findViewById<EditText>(R.id.edt_pin)

        /**
         * Function responsible for shuffling a list of numbers from 0 to 9
         *
         * @return list of shuffled numbers
         */
        fun shuffleNumbers(): List<String> {
            val numberList = (0..9).map { it.toString() }
            Collections.shuffle(numberList)
            return numberList
        }

        fun loadNumericKeypad() {
            val numberList = shuffleNumbers()
            val buttons = listOf(
                button0,
                button1,
                button2,
                button3,
                button4,
                button5,
                button6,
                button7,
                button8,
                button9
            )
            buttons.forEachIndexed { index, button -> button.text = numberList[index] }
        }

        /**
         *
         */
        fun erasePinAndLoadNumericKeypad() {
            editTextPin.setText("")
            loadNumericKeypad()
        }

        /**
         * Function responsible for filling in the text box with the information corresponding to the PIN
         *
         * @param corresponds to the digit chosen on the keyboard
         */
        fun fillPassword(digit: String) {
            if (editTextPin.text.length < 7) {
                editTextPin.append(digit)
            }
        }

        erasePinAndLoadNumericKeypad()

        button0.setOnClickListener {
            fillPassword(button0.text.toString())
            loadNumericKeypad()
        }

        button1.setOnClickListener {
            fillPassword(button1.text.toString())
            loadNumericKeypad()
        }

        button2.setOnClickListener {
            fillPassword(button2.text.toString())
            loadNumericKeypad()
        }

        button3.setOnClickListener {
            fillPassword(button3.text.toString())
            loadNumericKeypad()
        }

        button4.setOnClickListener {
            fillPassword(button4.text.toString())
            loadNumericKeypad()
        }

        button5.setOnClickListener {
            fillPassword(button5.text.toString())
            loadNumericKeypad()
        }

        button6.setOnClickListener {
            fillPassword(button6.text.toString())
            loadNumericKeypad()
        }

        button7.setOnClickListener {
            fillPassword(button7.text.toString())
            loadNumericKeypad()
        }

        button8.setOnClickListener {
            fillPassword(button8.text.toString())
            loadNumericKeypad()
        }

        button9.setOnClickListener {
            fillPassword(button9.text.toString())
            loadNumericKeypad()
        }

        buttonX.setOnClickListener {
            erasePinAndLoadNumericKeypad()
        }

        buttonOk.setOnClickListener {

            if (editTextPin.text.length != 6) {
                Toast.makeText(this, "O PIN deve ter 6 digitos.", Toast.LENGTH_LONG).show()
            } else {

                val biometricManager = BiometricManager.from(this)
                val biometric = Biometric(biometricManager)
                val isTrue = biometric.validateBiometrics(this)

                if (!isTrue) {

                    Toast.makeText(
                        this, "A biometria não está configurada no dispositivo. " +
                                "Para mais segurança, ative-a.", Toast.LENGTH_LONG
                    ).show()

                    Handler(Looper.getMainLooper())
                        .postDelayed(
                            {

                            // AQUI

                            }, 3000
                        )
                }
            }
        }
    }
}