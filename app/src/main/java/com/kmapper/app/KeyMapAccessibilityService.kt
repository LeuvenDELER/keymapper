package com.kmapper.app

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.content.Intent
import android.graphics.Path
import android.net.Uri
import android.view.KeyEvent
import android.view.accessibility.AccessibilityEvent

class KeyMapAccessibilityService : AccessibilityService() {

    override fun onServiceConnected() {
        super.onServiceConnected()
        instance = this
    }

    override fun onDestroy() {
        super.onDestroy()
        instance = null
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // Bu servis içerik olaylarını değil, tuş olaylarını dinliyor.
    }

    override fun onInterrupt() {}

    override fun onKeyEvent(event: KeyEvent?): Boolean {
        if (event == null) return false
        // Sadece tuşa BASILDIĞINDA tetikle (ACTION_DOWN), tekrar tetiklemeyi önle
        if (event.action != KeyEvent.ACTION_DOWN) return false

        val mappings = ConfigStorage.loadAll(this)
        val match = mappings.firstOrNull { it.enabled && it.keyCode == event.keyCode }
            ?: return false

        performAction(match)
        return true // orijinal tuş davranışını iptal et, bizim aksiyonumuzu çalıştır
    }

    private fun performAction(mapping: KeyMapping) {
        when (mapping.actionType) {
            ActionType.GO_BACK -> performGlobalAction(GLOBAL_ACTION_BACK)
            ActionType.GO_HOME -> performGlobalAction(GLOBAL_ACTION_HOME)
            ActionType.RECENT_APPS -> performGlobalAction(GLOBAL_ACTION_RECENTS)
            ActionType.NOTIFICATIONS -> performGlobalAction(GLOBAL_ACTION_NOTIFICATIONS)
            ActionType.QUICK_SETTINGS -> performGlobalAction(GLOBAL_ACTION_QUICK_SETTINGS)
            ActionType.LOCK_SCREEN -> performGlobalAction(GLOBAL_ACTION_LOCK_SCREEN)
            ActionType.VOLUME_UP -> adjustVolume(true)
            ActionType.VOLUME_DOWN -> adjustVolume(false)
            ActionType.OPEN_APP -> openApp(mapping.actionExtra)
            ActionType.OPEN_URL -> openUrl(mapping.actionExtra)
            ActionType.TAP_SCREEN -> tapAt(mapping.actionExtra, longPress = false, doubleTap = false)
            ActionType.LONG_PRESS_SCREEN -> tapAt(mapping.actionExtra, longPress = true, doubleTap = false)
            ActionType.DOUBLE_TAP_SCREEN -> tapAt(mapping.actionExtra, longPress = false, doubleTap = true)
        }
    }

    /**
     * "X,Y" formatındaki koordinatı okuyup o noktaya sanal dokunma gönderir.
     * longPress = true ise basılı tutma süresini uzatır (sağ tık muadili).
     * doubleTap = true ise iki hızlı tıklama gönderir.
     */
    private fun tapAt(coords: String, longPress: Boolean, doubleTap: Boolean) {
        val parts = coords.split(",").map { it.trim() }
        if (parts.size != 2) return
        val x = parts[0].toFloatOrNull() ?: return
        val y = parts[1].toFloatOrNull() ?: return

        val path = Path().apply { moveTo(x, y) }
        val duration = if (longPress) 600L else 50L

        val stroke = GestureDescription.StrokeDescription(path, 0, duration)
        val builder = GestureDescription.Builder().addStroke(stroke)
        dispatchGesture(builder.build(), null, null)

        if (doubleTap) {
            val secondStroke = GestureDescription.StrokeDescription(path, 120, duration)
            val secondBuilder = GestureDescription.Builder().addStroke(secondStroke)
            dispatchGesture(secondBuilder.build(), null, null)
        }
    }

    private fun adjustVolume(up: Boolean) {
        val am = getSystemService(AUDIO_SERVICE) as android.media.AudioManager
        am.adjustStreamVolume(
            android.media.AudioManager.STREAM_MUSIC,
            if (up) android.media.AudioManager.ADJUST_RAISE else android.media.AudioManager.ADJUST_LOWER,
            android.media.AudioManager.FLAG_SHOW_UI
        )
    }

    private fun openApp(packageName: String) {
        if (packageName.isBlank()) return
        val intent = packageManager.getLaunchIntentForPackage(packageName) ?: return
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    private fun openUrl(url: String) {
        if (url.isBlank()) return
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    companion object {
        var instance: KeyMapAccessibilityService? = null
        fun isRunning(): Boolean = instance != null
    }
}
