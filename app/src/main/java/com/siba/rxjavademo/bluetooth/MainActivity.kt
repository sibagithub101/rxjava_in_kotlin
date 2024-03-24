package com.siba.rxjavademo.bluetooth

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.siba.rxjavademo.R
import com.siba.rxjavademo.databinding.ActivityMainBinding
import com.siba.rxjavademo.databinding.ActivityNewBluetoothBinding
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var bluetoothAdapter: BluetoothAdapter
    private var bluetoothGatt: BluetoothGatt? = null
    private var count = 0
    private lateinit var binding: ActivityNewBluetoothBinding

    // checking result code if it is bluetooth turn on or not
    @RequiresApi(Build.VERSION_CODES.S)
    private val mActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
            // Bluetooth was successfully turned on
            Toast.makeText(this,"Bluetooth Turned on",Toast.LENGTH_SHORT).show()
            initializeBluetooth()
        } else {
            // User either canceled the operation or Bluetooth could not be turned on
            Toast.makeText(this,"Bluetooth Activation failed",Toast.LENGTH_SHORT).show()
        }
    }

    // after getting run-time permission initlizeBluetooth
    @RequiresApi(Build.VERSION_CODES.S)
    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            val result = it.entries.all { entry ->
                entry.value
            }
            if (result) {
                initializeBluetooth()
            } else {
                if (count < 2) {
                    requestBluetoothPermissions()
                    count++
                } else {
                    Toast.makeText(this, "Exist bluetooth Process", Toast.LENGTH_SHORT).show()
                }
            }
        }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewBluetoothBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBluetooth.setOnClickListener {
            Toast.makeText(this@MainActivity, "Connected to device", Toast.LENGTH_SHORT).show()

            if(!hasBluetoothPermissions()){
                requestBluetoothPermissions()
            }else{
                initializeBluetooth()
            }

        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    @SuppressLint("MissingPermission")
    private fun initializeBluetooth() {

        //iniialize the bluetooth manager and bluetooth adatpter
        val bluetoothManager = getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter

        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not supported on this device", Toast.LENGTH_SHORT)
                .show()
        } else if (!bluetoothAdapter.isEnabled) {
            turnOnBluetooth()
        } else {
              discoverBlueToothDevice()
        }

    }

    @RequiresApi(Build.VERSION_CODES.S)
    @SuppressLint("MissingPermission")
    private fun discoverBlueToothDevice() {
        val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter.bondedDevices
        val adapter = DeviceAdapter(pairedDevices?.toList() ?: emptyList()){ getDevice->
            connectToDevice(getDevice)
        }
        binding.devicesRecyclerView.adapter = adapter
    }


    @RequiresApi(Build.VERSION_CODES.S)
    private fun turnOnBluetooth() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH
            ) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_ADMIN
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Bluetooth permissions are granted, proceed with Bluetooth activation
            val turnOn = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            mActivityResultLauncher.launch(turnOn)
        } else {
            // Request Bluetooth permissions if not granted
            //ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN), 1)
            requestBluetoothPermissions()
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun hasBluetoothPermissions(): Boolean {
        return (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH)
                == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN)
                == PackageManager.PERMISSION_GRANTED  && ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT)
                == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN)
                == PackageManager.PERMISSION_GRANTED
                )
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun requestBluetoothPermissions() {
        val permission = arrayOf(Manifest.permission.BLUETOOTH,Manifest.permission.BLUETOOTH_ADMIN,Manifest.permission.BLUETOOTH_CONNECT,Manifest.permission.BLUETOOTH_SCAN)
        permissionLauncher.launch(permission)
    }


    @SuppressLint("MissingPermission")
    override fun onDestroy() {
        super.onDestroy()
        bluetoothAdapter.cancelDiscovery()
        bluetoothGatt?.disconnect()
        bluetoothGatt?.close()
    }

    @SuppressLint("MissingPermission")
    private fun connectToDevice(device: BluetoothDevice) {
        val bluetoothGattCallback = object : BluetoothGattCallback() {
            override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
                when (newState) {
                    BluetoothProfile.STATE_CONNECTED -> {
                        // Connected to the device, perform further operations
                        Toast.makeText(this@MainActivity, "Connected to device", Toast.LENGTH_SHORT).show()
                    }
                    BluetoothProfile.STATE_DISCONNECTED -> {
                        // Disconnected from the device
                        Toast.makeText(this@MainActivity, "Disconnected from device", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        // Handle other states if needed
                    }
                }
            }

            override fun onCharacteristicRead(
                gatt: BluetoothGatt,
                characteristic: BluetoothGattCharacteristic,
                status: Int
            ) {
                // Handle characteristic read operations here
            }

            override fun onCharacteristicWrite(
                gatt: BluetoothGatt,
                characteristic: BluetoothGattCharacteristic,
                status: Int
            ) {
                // Handle characteristic read operations here
            }
        }

        bluetoothGatt = device.connectGatt(this, false, bluetoothGattCallback)
    }
}
