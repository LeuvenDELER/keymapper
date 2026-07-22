package com.kmapper.app

import android.os.Bundle
import android.view.KeyEvent
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kmapper.app.databinding.ActivityAddEditMappingBinding

class AddEditMappingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEditMappingBinding
    private var editingId: String? = null
    private var capturedKeyCode: Int = -1
    private var capturedKeyName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditMappingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            ActionType.values().map { it.label }
        )
        binding.spinnerAction.adapter = actionAdapter

        editingId = intent.getStringExtra(EXTRA_ID)
        if (editingId != null) {
            val existing = ConfigStorage.loadAll(this).firstOrNull { it.id == editingId }
            if (existing != null) {
                capturedKeyCode = existing.keyCode
                capturedKeyName = existing.keyName
                binding.textCapturedKey.text = "Yakalanan tuş: $capturedKeyName"
                binding.spinnerAction.setSelection(existing.actionType.ordinal)
                binding.editExtra.setText(existing.actionExtra)
            }
        }

        // Kullanıcı bir tuşa bassın diye bu alan focus'ta tuş event'i yakalar
        binding.textCapturedKey.isFocusableInTouchMode = true
        binding.textCapturedKey.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN) {
                capturedKeyCode = keyCode
                capturedKeyName = KeyEvent.keyCodeToString(keyCode)
                binding.textCapturedKey.text = "Yakalanan tuş: $capturedKeyName"
                true
            } else false
        }

        binding.btnCaptureKey.setOnClickListener {
            binding.textCapturedKey.requestFocus()
            Toast.makeText(this, "Şimdi eşlemek istediğin fiziksel tuşa bas", Toast.LENGTH_LONG).show()
        }

        binding.btnSave.setOnClickListener { save() }
    }

    private fun save() {
        if (capturedKeyCode == -1) {
            Toast.makeText(this, "Önce bir tuş yakala", Toast.LENGTH_SHORT).show()
            return
        }
        val actionType = ActionType.values()[binding.spinnerAction.selectedItemPosition]
        val extra = binding.editExtra.text.toString().trim()

        if (actionType.needsExtra && extra.isBlank()) {
            Toast.makeText(this, "${actionType.label} için ek parametre gerekli", Toast.LENGTH_SHORT).show()
            return
        }

        val mapping = KeyMapping(
            id = editingId ?: ConfigStorage.newId(),
            keyCode = capturedKeyCode,
            keyName = capturedKeyName,
            actionType = actionType,
            actionExtra = extra,
            enabled = true
        )
        ConfigStorage.addOrUpdate(this, mapping)
        Toast.makeText(this, "Config kaydedildi", Toast.LENGTH_SHORT).show()
        finish()
    }

    companion object {
        const val EXTRA_ID = "extra_id"
    }
}
