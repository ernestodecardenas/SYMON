package com.example.symon.ui.devices

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.symon.R
import com.example.symon.data.model.DeviceItem
import com.example.symon.databinding.ItemDeviceBinding

/**
 * Adapter con ViewBinding para mostrar la lista de dispositivos IoT ESP32.
 */
class DeviceAdapter(
    private val onItemClick: (DeviceItem) -> Unit
) : ListAdapter<DeviceItem, DeviceAdapter.DeviceViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
        val binding = ItemDeviceBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return DeviceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class DeviceViewHolder(
        private val binding: ItemDeviceBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DeviceItem) {
            binding.tvDeviceName.text = item.name
            binding.tvDeviceId.text = "ID: ${item.deviceId}"
            binding.tvDeviceBattery.text = "${item.batteryPercent}%"
            binding.tvDeviceRssi.text = "${item.signalStrengthDbm} dBm"
            binding.tvDeviceLastSeen.text = "Sincro: ${item.lastSeen}"
            binding.tvDeviceAssignedBatch.text = "Lote: ${item.assignedBatch}"

            if (item.isOnline) {
                binding.tvDeviceOnlineStatus.text = "En Línea"
                binding.tvDeviceOnlineStatus.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(itemView.context, R.color.status_green_bg)
                )
                binding.tvDeviceOnlineStatus.setTextColor(
                    ContextCompat.getColor(itemView.context, R.color.status_green)
                )
            } else {
                binding.tvDeviceOnlineStatus.text = "Offline"
                binding.tvDeviceOnlineStatus.backgroundTintList = ColorStateList.valueOf(
                    Color.parseColor("#EEEEEE")
                )
                binding.tvDeviceOnlineStatus.setTextColor(
                    Color.parseColor("#757575")
                )
            }

            itemView.setOnClickListener { onItemClick(item) }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<DeviceItem>() {
        override fun areItemsTheSame(oldItem: DeviceItem, newItem: DeviceItem): Boolean {
            return oldItem.deviceId == newItem.deviceId
        }

        override fun areContentsTheSame(oldItem: DeviceItem, newItem: DeviceItem): Boolean {
            return oldItem == newItem
        }
    }
}
