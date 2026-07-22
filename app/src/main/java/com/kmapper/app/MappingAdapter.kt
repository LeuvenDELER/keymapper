package com.kmapper.app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView

class MappingAdapter(
    private val items: MutableList<KeyMapping>,
    private val onToggle: (KeyMapping, Boolean) -> Unit,
    private val onEdit: (KeyMapping) -> Unit,
    private val onDelete: (KeyMapping) -> Unit
) : RecyclerView.Adapter<MappingAdapter.VH>() {

    class VH(view: View) : RecyclerView.ViewHolder(view) {
        val title: android.widget.TextView = view.findViewById(R.id.textTitle)
        val subtitle: android.widget.TextView = view.findViewById(R.id.textSubtitle)
        val toggle: android.widget.Switch = view.findViewById(R.id.switchEnabled)
        val editBtn: android.widget.ImageButton = view.findViewById(R.id.btnEdit)
        val deleteBtn: android.widget.ImageButton = view.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_mapping, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
        holder.title.text = "${item.keyName}  →  ${item.actionType.label}"
        holder.subtitle.text = if (item.actionExtra.isNotBlank()) item.actionExtra else "Ek parametre yok"

        holder.toggle.setOnCheckedChangeListener(null)
        holder.toggle.isChecked = item.enabled
        holder.toggle.setOnCheckedChangeListener { _: CompoundButton, checked: Boolean ->
            onToggle(item, checked)
        }

        holder.editBtn.setOnClickListener { onEdit(item) }
        holder.deleteBtn.setOnClickListener { onDelete(item) }
    }

    override fun getItemCount(): Int = items.size

    fun updateData(newItems: List<KeyMapping>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}
