package com.kmapper.app

import org.json.JSONObject

/**
 * Tek bir tuş eşleme kaydı.
 * keyCode: Android KeyEvent.KEYCODE_* değeri (örn. 25 = ses kısma)
 * actionType: ActionType enum ismi
 * actionExtra: aksiyona bağlı ek veri (örn. paket adı, url)
 * enabled: bu mapping aktif mi
 */
data class KeyMapping(
    var id: String,
    var keyCode: Int,
    var keyName: String,
    var actionType: ActionType,
    var actionExtra: String = "",
    var enabled: Boolean = true
) {
    fun toJson(): JSONObject {
        val o = JSONObject()
        o.put("id", id)
        o.put("keyCode", keyCode)
        o.put("keyName", keyName)
        o.put("actionType", actionType.name)
        o.put("actionExtra", actionExtra)
        o.put("enabled", enabled)
        return o
    }

    companion object {
        fun fromJson(o: JSONObject): KeyMapping {
            return KeyMapping(
                id = o.getString("id"),
                keyCode = o.getInt("keyCode"),
                keyName = o.getString("keyName"),
                actionType = ActionType.valueOf(o.getString("actionType")),
                actionExtra = o.optString("actionExtra", ""),
                enabled = o.optBoolean("enabled", true)
            )
        }
    }
}

enum class ActionType(val label: String, val needsExtra: Boolean) {
    OPEN_APP("Uygulama Aç (paket adı gerekir)", true),
    GO_BACK("Geri Git", false),
    GO_HOME("Ana Ekran", false),
    RECENT_APPS("Son Uygulamalar", false),
    NOTIFICATIONS("Bildirim Panelini Aç", false),
    QUICK_SETTINGS("Hızlı Ayarları Aç", false),
    LOCK_SCREEN("Ekranı Kilitle", false),
    VOLUME_UP("Sesi Aç", false),
    VOLUME_DOWN("Sesi Kıs", false),
    OPEN_URL("Bağlantı Aç (URL gerekir)", true),
    TAP_SCREEN("Ekrana Tıkla / Sol Tık (X,Y gerekir)", true),
    LONG_PRESS_SCREEN("Uzun Bas / Sağ Tık Muadili (X,Y gerekir)", true),
    DOUBLE_TAP_SCREEN("Çift Tıkla (X,Y gerekir)", true)
}
