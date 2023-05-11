package com.mxs.bitcoin.wallet.core

import java.security.spec.KeySpec
import java.util.*
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

/**
 * When seeds are generated, they will be stored in the KeyStore.
 * However, not its original content, but the result of its encryption.
 * When it is necessary to check the balance or carry out a transfer,
 * it will be necessary to remove the seeds from the KeyStore and perform their decryption.
 * This class implements the seed encryption and decryption process.
 */
class PrivateKey {

    /**
     *
     */
    fun encrypt(privateKey: String, iv: ByteArray, salt: ByteArray, key: String): String {
        val ivspec = IvParameterSpec(iv)
        val factory: SecretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        val spec: KeySpec = PBEKeySpec(key.toCharArray(), salt, 65536, 256)
        val tmp: SecretKey = factory.generateSecret(spec)
        val secretKey = SecretKeySpec(tmp.encoded, "AES")
        val cipher: Cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec)
        return Base64.getEncoder().encodeToString(cipher.doFinal(privateKey.toByteArray(Charsets.UTF_8)))
    }

    /**
     *
     */
    fun decrypt(encryptedPrivateKey: String, iv: ByteArray, salt: ByteArray, key: String): String {
        val ivspec = IvParameterSpec(iv)
        val factory: SecretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        val spec: KeySpec = PBEKeySpec(key.toCharArray(), salt, 65536, 256)
        val tmp: SecretKey = factory.generateSecret(spec)
        val secretKey = SecretKeySpec(tmp.encoded, "AES")
        val cipher: Cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec)
        return String(cipher.doFinal(Base64.getDecoder().decode(encryptedPrivateKey)))
    }

    /**
     * function responsible for generating the password that will be used to encrypt the seed
     *
     * @param pin
     * @param dictionary
     * @return
     */
    fun generateEncryptedPassword(pin: String, dictionary: List<String>): String {

        val etapa1 = pin.mapIndexed { index, char ->
            val pinDigit = if (char == '0') index + 1 else char.toString().toInt()
            Dictionary().getDictionaryFragment(pinDigit, dictionary.subList(index * 36, (index + 1) * 36))
        }.joinToString("")
        val etapa2 = rearrangePassword(etapa1, pin)
        return transformarSenha(etapa2)
    }

    fun rearrangePassword(senha: String, pin: String): String {
        val partesSenha = senha.chunked(4)
        val mapaPin = pin.mapIndexed { index, char -> char to partesSenha[index] }.toMap()
        val pinOrdenado = pin.toCharArray().sortedDescending().joinToString("")
        val senhaReordenada = pinOrdenado.map { mapaPin[it]!! }.joinToString("")
        return senhaReordenada
    }

    fun transformarSenha(senha: String): String {

        val primeiraParte = senha.substring(0,1) + senha.substring(3,4)
        val segundaParte = senha.substring(4,5) + senha.substring(7,8)
        val terceiraParte = senha.substring(8,9) + senha.substring(11,12)
        val quartaParte = senha.substring(12,13) + senha.substring(15, 16)
        val quintaParte = senha.substring(16,17) + senha.substring(19,20)
        val sextaParte = senha.substring(20,21) + senha.substring(23,24)

        val p1 = senha.substring(1, 3)
        val p2 = senha.substring(5, 7)
        val p3 = senha.substring(9, 11)
        val p4 = senha.substring(13, 15)
        val p5 = senha.substring(17, 19)
        val p6 = senha.substring(21, 23)

        val r1 = StringBuilder(primeiraParte).insert(1, p2).toString()
        val r2 = StringBuilder(segundaParte).insert(1, p1).toString()
        val r3 = StringBuilder(terceiraParte).insert(1, p4).toString()
        val r4 = StringBuilder(quartaParte).insert(1, p3).toString()
        val r5 = StringBuilder(quintaParte).insert(1, p6).toString()
        val r6 = StringBuilder(sextaParte).insert(1, p5).toString()

        val r = r1 + r2 + r3 + r4 + r5 + r6

        return r
    }
}