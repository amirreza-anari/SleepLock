package ir.amirrezaanari.sleeptouch.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private enum class SetupStep {
    ENTER_PIN,
    CONFIRM_PIN
}

@Composable
fun SetupScreen(
    hasOverlayPermission: Boolean,
    isBiometricAvailable: Boolean,
    onRequestOverlayPermission: () -> Unit,
    onStartSleepMode: (pin: String) -> Unit
) {
    var step by remember { mutableStateOf(SetupStep.ENTER_PIN) }
    var firstPin by remember { mutableStateOf("") }
    var confirmPin by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isError by remember { mutableStateOf(false) }

    val currentPin = if (step == SetupStep.ENTER_PIN) firstPin else confirmPin

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF0B0F19)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.height(12.dp))

                // Header Icon & Title
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF161B26)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "🌙", fontSize = 36.sp)
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "SleepTouch",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(12.dp))

                if (!hasOverlayPermission) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF261828)),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = "⚠️ Overlay Permission Required",
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFEF4444),
                                fontSize = 15.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "SleepTouch needs overlay permission to cover the full screen over other apps.",
                                fontSize = 12.sp,
                                color = Color(0xFFD1D5DB)
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Button(
                                onClick = onRequestOverlayPermission,
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6366F1))
                            ) {
                                Text("Grant Permission", fontSize = 13.sp)
                            }
                        }
                    }
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF161B26)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "🔐", fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Emergency Biometric Unlock",
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White,
                                fontSize = 13.sp
                            )
                            Text(
                                text = if (isBiometricAvailable) "Fingerprint ready for emergency unlock"
                                else "Fingerprint not available",
                                fontSize = 11.sp,
                                color = Color(0xFF9CA3AF)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // PIN Setup Title & Dots
                Text(
                    text = if (step == SetupStep.ENTER_PIN) "Set 4-Digit Temporary PIN" else "Confirm 4-Digit PIN",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Text(
                    text = if (step == SetupStep.ENTER_PIN) "Enter 4 digits for tonight's lock" else "Re-enter the same 4 digits to confirm",
                    fontSize = 13.sp,
                    color = Color(0xFF9CA3AF),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
                )

                PinDots(pinLength = currentPin.length, isError = isError)

                if (errorMessage != null) {
                    Text(
                        text = errorMessage ?: "",
                        color = Color(0xFFEF4444),
                        fontSize = 13.sp,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Numpad Keypad for Setup
            NumpadView(
                onDigitClick = { digit ->
                    if (currentPin.length < 4) {
                        isError = false
                        errorMessage = null
                        if (step == SetupStep.ENTER_PIN) {
                            firstPin += digit
                            if (firstPin.length == 4) {
                                step = SetupStep.CONFIRM_PIN
                            }
                        } else {
                            confirmPin += digit
                            if (confirmPin.length == 4) {
                                if (confirmPin == firstPin) {
                                    onStartSleepMode(firstPin)
                                } else {
                                    isError = true
                                    errorMessage = "PINs do not match. Try again."
                                    confirmPin = ""
                                    firstPin = ""
                                    step = SetupStep.ENTER_PIN
                                }
                            }
                        }
                    }
                },
                onDeleteClick = {
                    isError = false
                    errorMessage = null
                    if (step == SetupStep.ENTER_PIN) {
                        if (firstPin.isNotEmpty()) firstPin = firstPin.dropLast(1)
                    } else {
                        if (confirmPin.isNotEmpty()) {
                            confirmPin = confirmPin.dropLast(1)
                        } else {
                            // Back to first PIN step
                            step = SetupStep.ENTER_PIN
                            firstPin = ""
                        }
                    }
                },
                onCancelClick = if (step == SetupStep.CONFIRM_PIN) {
                    {
                        step = SetupStep.ENTER_PIN
                        firstPin = ""
                        confirmPin = ""
                        errorMessage = null
                        isError = false
                    }
                } else null
            )
        }
    }
}

@Composable
fun SleepLockScreen(
    isBiometricAvailable: Boolean,
    initialShowPin: Boolean = false,
    onPinSubmitted: (String) -> Boolean,
    onEmergencyBiometricClick: () -> Unit
) {
    var currentTime by remember { mutableStateOf("") }
    var currentDate by remember { mutableStateOf("") }
    var showPinInput by remember { mutableStateOf(initialShowPin) }
    var enteredPin by remember { mutableStateOf("") }
    var isPinError by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val timeSdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        val dateSdf = SimpleDateFormat("EEEE, MMMM d", Locale.getDefault())
        while (true) {
            val now = Date()
            currentTime = timeSdf.format(now)
            currentDate = dateSdf.format(now)
            delay(1000)
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF090D16)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header / Clock Section
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 16.dp)
            ) {
                // Large, Bold, Ultra Visible White Clock
                Text(
                    text = currentTime,
                    fontSize = 68.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    letterSpacing = 2.sp
                )

                if (currentDate.isNotEmpty()) {
                    Text(
                        text = currentDate,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF818CF8)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF161B26))
                        .border(1.dp, Color(0xFF312E81), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "🌙", fontSize = 40.sp)
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Sleep Time",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Screen is locked by SleepTouch",
                    fontSize = 14.sp,
                    color = Color(0xFF9CA3AF)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Bottom Interactive Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (!showPinInput) {
                    Button(
                        onClick = {
                            showPinInput = true
                            isPinError = false
                            enteredPin = ""
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(18.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6366F1))
                    ) {
                        Text(
                            text = "🔓 Unlock",
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    if (isBiometricAvailable) {
                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedButton(
                            onClick = onEmergencyBiometricClick,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(18.dp),
                            border = BorderStroke(1.dp, Color(0xFF38BDF8))
                        ) {
                            Text(
                                text = "☝️ Emergency Fingerprint Unlock",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF38BDF8)
                            )
                        }
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color(0xFF161B26))
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Enter 4-Digit PIN",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        PinDots(pinLength = enteredPin.length, isError = isPinError)

                        if (isPinError) {
                            Text(
                                text = "Incorrect PIN. Try again.",
                                color = Color(0xFFEF4444),
                                fontSize = 13.sp,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Numpad for Unlock
                        NumpadView(
                            onDigitClick = { digit ->
                                if (enteredPin.length < 4) {
                                    isPinError = false
                                    enteredPin += digit
                                    if (enteredPin.length == 4) {
                                        val success = onPinSubmitted(enteredPin)
                                        if (!success) {
                                            isPinError = true
                                            enteredPin = ""
                                        }
                                    }
                                }
                            },
                            onDeleteClick = {
                                if (enteredPin.isNotEmpty()) {
                                    enteredPin = enteredPin.dropLast(1)
                                    isPinError = false
                                }
                            },
                            onCancelClick = {
                                showPinInput = false
                                enteredPin = ""
                                isPinError = false
                            }
                        )

                        if (isBiometricAvailable) {
                            Spacer(modifier = Modifier.height(8.dp))
                            TextButton(
                                onClick = onEmergencyBiometricClick
                            ) {
                                Text(
                                    text = "☝️ Emergency Fingerprint",
                                    color = Color(0xFF38BDF8),
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PinDots(
    pinLength: Int,
    maxDigits: Int = 4,
    isError: Boolean = false
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 6.dp)
    ) {
        for (i in 0 until maxDigits) {
            val isFilled = i < pinLength
            Box(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .size(18.dp)
                    .clip(CircleShape)
                    .background(
                        if (isError) Color(0xFFEF4444)
                        else if (isFilled) Color(0xFF6366F1)
                        else Color(0xFF374151)
                    )
            )
        }
    }
}

@Composable
fun NumpadView(
    onDigitClick: (String) -> Unit,
    onDeleteClick: () -> Unit,
    onCancelClick: (() -> Unit)? = null
) {
    val buttons = listOf(
        listOf("1", "2", "3"),
        listOf("4", "5", "6"),
        listOf("7", "8", "9"),
        listOf("✖", "0", "⌫")
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        for (row in buttons) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 3.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                for (btn in row) {
                    Box(
                        modifier = Modifier
                            .size(width = 80.dp, height = 50.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(
                                when (btn) {
                                    "✖" -> Color(0xFF374151)
                                    "⌫" -> Color(0xFF374151)
                                    else -> Color(0xFF212838)
                                }
                            )
                            .clickable {
                                when (btn) {
                                    "✖" -> onCancelClick?.invoke()
                                    "⌫" -> onDeleteClick()
                                    else -> onDigitClick(btn)
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = btn,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (btn == "✖" && onCancelClick == null) Color.Transparent else Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SleepLockOverlayContent(
    onUnlockRequested: () -> Unit
) {
    var currentTime by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        while (true) {
            currentTime = sdf.format(Date())
            delay(1000)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFA090D16))
            .statusBarsPadding()
            .clickable { onUnlockRequested() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(24.dp)
        ) {
            Text(
                text = currentTime,
                fontSize = 72.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "🌙", fontSize = 56.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Sleep Time",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Tap anywhere to unlock with PIN or Fingerprint",
                fontSize = 15.sp,
                color = Color(0xFF9CA3AF),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = onUnlockRequested,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6366F1)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.height(52.dp)
            ) {
                Text("🔓 Open SleepTouch Unlock", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
