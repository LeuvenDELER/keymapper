package com.kmapper.app

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.kmapper.app.databinding.ActivityMainBinding

/**
 * ADMIN PANEL ekranı.
 * Buradan tüm tuş eşleme configlerini görebilir, ekleyebilir, düzenleyebilir, silebilirsin.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: MappingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = MappingAdapter(
            items = ConfigStorage.loadAll(this),
            onToggle = { mapping, checked ->
                mapping.enabled = checked
                ConfigStorage.addOrUpdate(this, mapping)
            },
            onEdit = { mapping ->
                val intent = Intent(this, AddEditMappingActivity::class.java)
                intent.putExtra(AddEditMappingActivity.EXTRA_ID, mapping.id)
                startActivity(intent)
            },
            onDelete = { mapping ->
                AlertDialog.Builder(this)
                    .setTitle("Silinsin mi?")
                    .setMessage("${mapping.keyName} eşlemesi silinecek.")
                    .setPositiveButton("Sil") { _, _ ->
                        ConfigStorage.delete(this, mapping.id)
                        refreshList()
                    }
                    .setNegativeButton("Vazgeç", null)
                    .show()
            }
        )

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        binding.fabAdd.setOnClickListener {
            startActivity(Intent(this, AddEditMappingActivity::class.java))
        }

        binding.btnAccessibilitySettings.setOnClickListener {
            startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
        }
    }

    override fun onResume() {
        super.onResume()
        refreshList()
        updateServiceStatus()
    }

    private fun refreshList() {
        adapter.updateData(ConfigStorage.loadAll(this))
        binding.textEmpty.visibility =
            if (adapter.itemCount == 0) android.view.View.VISIBLE else android.view.View.GONE
    }

    private fun updateServiceStatus() {
        val running = KeyMapAccessibilityService.isRunning()
        binding.textServiceStatus.text = if (running) {
            "Servis durumu: ✅ ÇALIŞIYOR"
        } else {
            "Servis durumu: ❌ KAPALI — Erişilebilirlik ayarlarından aç"
        }
    }
}
