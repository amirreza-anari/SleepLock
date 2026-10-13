package ir.amirrezaanari.sleeptouch

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.net.toUri
import androidx.fragment.app.FragmentActivity
import ir.amirrezaanari.sleeptouch.ui.SetupScreen
import ir.amirrezaanari.sleeptouch.ui.SleepLockScreen
import ir.amirrezaanari.sleeptouch.ui.theme.SleepTouchTheme

class MainActivity : FragmentActivity() {

    private lateinit var sleepPreferences: SleepPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        sleepPreferences = SleepPreferences(this)
        configureLockWindowFlags()

        val showUnlockFromIntent = intent?.getBooleanExtra("SHOW_UNLOCK_DIALOG", false) ?: false

        setContent {
            SleepTouchTheme {
                var isSleepActive by remember { mutableStateOf(sleepPreferences.isSleepActive) }
                var hasOverlayPermission by remember { mutableStateOf(Settings.canDrawOverlays(this)) }
                val isBiometricAvailable = remember { BiometricHelper.isBiometricAvailable(this) }

                // Intercept and disable Back button when Sleep Mode is active
                BackHandler(enabled = isSleepActive) {
                    relaunchLockActivity()
                }

                if (isSleepActive) {
                    // Full Screen Lock View
                    SleepLockScreen(
                        isBiometricAvailable = isBiometricAvailable,
                        initialShowPin = showUnlockFromIntent,
                        onPinSubmitted = { enteredPin ->
                            if (enteredPin == sleepPreferences.savedPin) {
                                unlockPhone()
                                isSleepActive = false
                                true
                            } else {
                                false
                            }
                        },
                        onEmergencyBiometricClick = {
                            triggerEmergencyBiometricUnlock {
                                unlockPhone()
                                isSleepActive = false
                            }
                        }
                    )
                } else {
                    // Setup Screen UI
                    SetupScreen(
                        hasOverlayPermission = hasOverlayPermission,
                        isBiometricAvailable = isBiometricAvailable,
                        onRequestOverlayPermission = {
                            requestOverlayPermission()
                        },
                        onStartSleepMode = { pin ->
                            sleepPreferences.savedPin = pin
                            sleepPreferences.isSleepActive = true
                            isSleepActive = true

                            if (Settings.canDrawOverlays(this)) {
                                SleepOverlayService.startService(this)
                            }
                            startKioskLockMode()
                            Toast.makeText(this, "SleepTouch Activated & Screen Locked!", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (sleepPreferences.isSleepActive) {
            startKioskLockMode()
            if (Settings.canDrawOverlays(this)) {
                SleepOverlayService.startService(this)
            }
        }
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        if (sleepPreferences.isSleepActive) {
            relaunchLockActivity()
        }
    }

    @Suppress("DEPRECATION")
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (!hasFocus && sleepPreferences.isSleepActive) {
            try {
                val closeIntent = Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)
                sendBroadcast(closeIntent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            relaunchLockActivity()
        }
    }

    private fun relaunchLockActivity() {
        if (sleepPreferences.isSleepActive) {
            if (Settings.canDrawOverlays(this)) {
                SleepOverlayService.startService(this)
            }
            val intent = Intent(this, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            }
            startActivity(intent)
        }
    }

    private fun startKioskLockMode() {
        try {
            startLockTask()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun stopKioskLockMode() {
        try {
            stopLockTask()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Suppress("DEPRECATION")
    private fun configureLockWindowFlags() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        } else {
            window.addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
            )
        }
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    private fun requestOverlayPermission() {
        if (!Settings.canDrawOverlays(this)) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                "package:$packageName".toUri(),
            )
            startActivity(intent)
        }
    }

    private fun triggerEmergencyBiometricUnlock(onSuccess: () -> Unit) {
        BiometricHelper.showBiometricPrompt(
            activity = this,
            title = "Emergency Fingerprint Unlock",
            subtitle = "Scan your registered fingerprint to deactivate SleepTouch",
            onSuccess = {
                onSuccess()
            },
            onError = { errorMsg ->
                Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun unlockPhone() {
        stopKioskLockMode()
        sleepPreferences.clearSleepSession()
        SleepOverlayService.stopService(this)
        Toast.makeText(this, "Phone Unlocked! Have a good day.", Toast.LENGTH_SHORT).show()
    }
}
