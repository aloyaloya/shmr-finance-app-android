package com.example.shmr_finance_app_android.core.di

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import com.example.shmr_finance_app_android.domain.repository.SettingsRepository
import com.example.shmr_finance_app_android.presentation.feature.settings.model.HapticStrength
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HapticFeedbackManager @Inject constructor(
    private val context: Context,
    private val settingsRepository: SettingsRepository
) {
    private val vibrator: Vibrator by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager =
                context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
    }

    private fun hasVibrator(): Boolean = vibrator.hasVibrator()

    fun vibrateClick() {
        if (!hasVibrator()) return

        val strength = settingsRepository.getHapticStrength().uppercase()
        val hapticStrength = HapticStrength.valueOf(strength)

        if (hapticStrength == HapticStrength.OFF) return

        val (duration, amplitude) = when (hapticStrength) {
            HapticStrength.STRONG -> 50L to 255
            HapticStrength.MEDIUM -> 40L to 128
            HapticStrength.WEAK -> 30L to 64
            HapticStrength.OFF -> 0L to 0
        }

        vibrate(duration, amplitude)
    }

    fun vibrateFromSettings() {
        if (!hasVibrator()) return

        val strength = settingsRepository.getHapticStrength().uppercase()
        val hapticStrength = HapticStrength.valueOf(strength)

        if (hapticStrength == HapticStrength.OFF) return

        val (duration, amplitude) = when (hapticStrength) {
            HapticStrength.STRONG -> 200L to 255
            HapticStrength.MEDIUM -> 150L to 128
            HapticStrength.WEAK -> 100L to 64
            HapticStrength.OFF -> 0L to 0
        }

        vibrate(duration, amplitude)
    }

    private fun vibrate(duration: Long, amplitude: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (vibrator.hasAmplitudeControl()) {
                val effect = VibrationEffect.createOneShot(duration, amplitude)
                vibrator.vibrate(effect)
            } else {
                val effect =
                    VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE)
                vibrator.vibrate(effect)
            }
        } else {
            vibrator.vibrate(duration)
        }
    }
}