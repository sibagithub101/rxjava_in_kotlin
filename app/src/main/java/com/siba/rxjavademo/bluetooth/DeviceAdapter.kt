package com.siba.rxjavademo.bluetooth
import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.siba.rxjavademo.R

class DeviceAdapter(private val devices: List<BluetoothDevice>,
                    private val onDeviceClickListener: (BluetoothDevice) -> Unit
    ) :
    RecyclerView.Adapter<DeviceAdapter.DeviceViewHolder>() {

    inner class DeviceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val deviceNameTextView: TextView = itemView.findViewById(R.id.deviceNameTextView)
        val deviceAddressTextView: TextView = itemView.findViewById(R.id.deviceAddressTextView)
        val btLayout: LinearLayout = itemView.findViewById(R.id.btLayout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.bluetooth_layout, parent, false)
        return DeviceViewHolder(itemView)
    }

    @SuppressLint("MissingPermission")
    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
        val device = devices[position]
        holder.deviceNameTextView.text = device.name ?: "Unknown Device"
        holder.deviceAddressTextView.text = device.address?: "Unknown Error"
        holder.btLayout.setOnClickListener {
            onDeviceClickListener.invoke(device)
        }


    }

    override fun getItemCount(): Int {
        return devices.size
    }
}
