package com.kmapper.app

import android.content.Context
import org.json.JSONArray
import java.util.UUID

/**
 * Tüm tuş eşleme configlerini cihazda saklar (SharedPreferences + JSON).
 * Admin panel buradan okuyup yazıyor.
 */
object ConfigStorage {
    private const val PREFS = "kmapper_prefs"
    private const val KEY_MAPPINGS = "mappings_json"

    fun loadAll(context: Context): MutableList<KeyMapping> {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val raw = prefs.getString(KEY_MAPPINGS, null) ?: return mutableListOf()
        val arr = JSONArray(raw)
        val list = mutableListOf<KeyMapping>()
        for (i in 0 until arr.length()) {
            list.add(KeyMapping.fromJson(arr.getJSONObject(i)))
        }
        return list
    }

    fun saveAll(context: Context, mappings: List<KeyMapping>) {
        val arr = JSONArray()
        mappings.forEach { arr.put(it.toJson()) }
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_MAPPINGS, arr.toString())
            .apply()
    }

    fun addOrUpdate(context: Context, mapping: KeyMapping) {
        val all = loadAll(context)
        val idx = all.indexOfFirst { it.id == mapping.id }
        if (idx >= 0) all[idx] = mapping else all.add(mapping)
        saveAll(context, all)
    }

    fun delete(context: Context, id: String) {
        val all = loadAll(context).filterNot { it.id == id }
        saveAll(context, all)
    }

    fun newId(): String = UUID.randomUUID().toString()
}
