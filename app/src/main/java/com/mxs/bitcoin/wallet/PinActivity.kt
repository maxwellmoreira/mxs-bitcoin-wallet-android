package com.mxs.bitcoin.wallet

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.mxs.bitcoin.wallet.core.Crypto
import java.util.Collections

/**
 * Activity responsible for accessing the wallet using the PIN (Personal Identification Number)
 */
class PinActivity : AppCompatActivity() {

    /**
     * Function responsible for initializing the activity, defining the layout and configuring the
     * initial behavior of the screen
     *
     * @param savedInstanceState used to store information about the previous state of the activity
     * in case it has been destroyed and needs to be recreated
     */
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.pin)

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

        val crypto = Crypto()

        val seeds = "grass scare swamp any pond purchase repeat dutch catch garbage trigger scene chef spice omit merge illness ankle win equal topic deliver input machine"
        val pin = "278913"

        println("Sementes: $seeds")

        val mensagemCriptografada = crypto.encryptSeeds(seeds, pin)
        println("Mensagem criptografada: ${mensagemCriptografada.contentToString()}")

        val mensagemDescriptografada = crypto.decryptSeeds(mensagemCriptografada, pin)
        println("Mensagem descriptografada: $mensagemDescriptografada")

        /**
         * Function responsible for shuffling a list of numbers from 0 to 9
         *
         * @return list of scrambled numbers
         */
        fun shuffleNumbers(): List<String> {
            val numberList = (0..9).map { it.toString() }
            Collections.shuffle(numberList)
            return numberList
        }

        /**
         * Function responsible for erasing the content of the text box corresponding to the PIN
         * and shuffling the keyboard digits
         */
        fun eraseAndShuffle() {
            editTextPin.setText("")
            val numberList = shuffleNumbers()
            val buttons = listOf(button0, button1, button2, button3, button4, button5, button6, button7, button8, button9)
            buttons.forEachIndexed { index, button -> button.text = numberList[index] }
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

        eraseAndShuffle()

        button0.setOnClickListener {
            fillPassword(button0.text.toString())
        }

        button1.setOnClickListener {
            fillPassword(button1.text.toString())
        }

        button2.setOnClickListener {
            fillPassword(button2.text.toString())
        }

        button3.setOnClickListener {
            fillPassword(button3.text.toString())
        }

        button4.setOnClickListener {
            fillPassword(button4.text.toString())
        }

        button5.setOnClickListener {
            fillPassword(button5.text.toString())
        }

        button6.setOnClickListener {
            fillPassword(button6.text.toString())
        }

        button7.setOnClickListener {
            fillPassword(button7.text.toString())
        }

        button8.setOnClickListener {
            fillPassword(button8.text.toString())
        }

        button9.setOnClickListener {
            fillPassword(button9.text.toString())
        }

        buttonX.setOnClickListener {
            eraseAndShuffle()
        }

        buttonOk.setOnClickListener {
            val intent = Intent(this, WalletActivity::class.java)
            startActivity(intent)
        }
    }
}