package com.siba.rxjavademo.bluetooth


import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.siba.rxjavademo.R

// D4:36:3D:3C:42:1F
class BleBluetoothActivity : AppCompatActivity() {

    private var bluetoothAdapter: BluetoothAdapter? = null
    private var bluetoothGatt: BluetoothGatt? = null
    private var bluetoothReceiver: BroadcastReceiver? = null

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ble_bluetooth)

        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter

        if (bluetoothAdapter == null || !bluetoothAdapter!!.isEnabled) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Replace "00:00:00:00:00:00" with the MAC address of your BLE device
        val device: BluetoothDevice? = bluetoothAdapter!!.getRemoteDevice("D4:36:3D:3C:42:1F")
        device?.let { connectToDevice(it) }

        // Register the Bluetooth receiver for device discovery
        registerBluetoothReceiver()

        // Start Bluetooth device discovery
        bluetoothAdapter?.startDiscovery()
    }

    @SuppressLint("MissingPermission")
    private fun connectToDevice(device: BluetoothDevice) {
        bluetoothGatt = device.connectGatt(this, false, gattCallback)
    }

    private val gattCallback: BluetoothGattCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                // Connected to the device, perform further operations
                Toast.makeText(this@BleBluetoothActivity, "Connected to device", Toast.LENGTH_SHORT).show()
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                // Disconnected from the device
                Toast.makeText(this@BleBluetoothActivity, "Disconnected from device", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun registerBluetoothReceiver() {
        bluetoothReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val action = intent?.action
                if (BluetoothDevice.ACTION_FOUND == action) {
                    // A new Bluetooth device is found
                    val device: BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    device?.let {
                        val deviceName = it.name
                        val deviceAddress = it.address
                        // Here's the MAC address of the discovered device
                        // You can do further processing with the device
                    }
                }
            }
        }
        // Register the receiver for Bluetooth device discovery
        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        registerReceiver(bluetoothReceiver, filter)
    }

    override fun onDestroy() {
        super.onDestroy()
        // Unregister the Bluetooth receiver
        unregisterBluetoothReceiver()
        // Stop Bluetooth device discovery
        bluetoothAdapter?.cancelDiscovery()
        // Disconnect and close BluetoothGatt
        bluetoothGatt?.disconnect()
        bluetoothGatt?.close()
    }

    private fun unregisterBluetoothReceiver() {
        bluetoothReceiver?.let {
            unregisterReceiver(it)
        }
    }
}

