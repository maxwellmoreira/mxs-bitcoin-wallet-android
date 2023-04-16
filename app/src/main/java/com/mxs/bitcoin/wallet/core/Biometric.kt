package com.mxs.bitcoin.wallet.core

import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

/**
 * Class responsible for the validation process using biometrics
 */
class Biometric(private val biometricManager: BiometricManager) {

    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    /**
     * Function responsible for verifying that biometrics can be used
     *
     * @param fragmentActivity interface module to access validation by biometrics
     * @return biometric validation status
     */
    fun validateBiometrics(fragmentActivity: FragmentActivity): Boolean {
        return when (biometricManager.canAuthenticate()) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                setupBiometricPrompt(fragmentActivity)
                true
            }
            else -> false
        }
    }

    /**
     * Function responsible for configuring BiometricPrompt to display the information defined in
     * promptInfo and start the authentication process through biometrics
     *
     * @param fragmentActivity interface module to access validation by biometrics
     */
    private fun setupBiometricPrompt(fragmentActivity: FragmentActivity) {
        biometricPrompt = BiometricPrompt(
            fragmentActivity,
            ContextCompat.getMainExecutor(fragmentActivity),
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    Toast.makeText(fragmentActivity, "Biometria validada com sucesso!", Toast.LENGTH_LONG).show()
                }
            }
        )

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Autenticação biométrica")
            .setSubtitle("Faça a autenticação com a sua biometria")
            .setNegativeButtonText("Cancelar")
            .build()

        biometricPrompt.authenticate(promptInfo)
    }
}