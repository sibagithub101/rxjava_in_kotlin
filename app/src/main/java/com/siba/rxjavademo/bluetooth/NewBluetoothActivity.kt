package com.siba.rxjavademo.bluetooth

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.siba.rxjavademo.databinding.ActivityNewBluetoothBinding
import java.util.UUID

class NewBluetoothActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewBluetoothBinding
    private lateinit var bluetoothAdapter: BluetoothAdapter
    private lateinit var bluetoothSocket: BluetoothSocket
    private var count = 0


    // checking result code if it is bluetooth turn on or not
    private val mActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
            // Bluetooth was successfully turned on
            Toast.makeText(this,"Bluetooth turned on",Toast.LENGTH_SHORT).show()
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
                initlizeBluetooth()
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
            if(!hasBluetoothPermissions()){
                requestBluetoothPermissions()
            }else{
                initlizeBluetooth()
            }

        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun initlizeBluetooth() {
        val bluetoothManager = getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter

        if(bluetoothAdapter==null){
            Toast.makeText(this, "Bluetooth is not supported on this device", Toast.LENGTH_SHORT).show()
        }else if(!bluetoothAdapter.isEnabled){
            turnOnBluetooth()
        }else{
            discoverBlueToothDevice()
        }

    }

    /**
     * Discover bluetooth device and showing which is already pair with mobile
     *
     */
    @SuppressLint("MissingPermission")
    private fun discoverBlueToothDevice() {
        val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter.bondedDevices
        val adapter = DeviceAdapter(pairedDevices?.toList() ?: emptyList()){ getDevice->
            connectToDevice(getDevice,pairedDevices?.toList()?: emptyList())
        }
        binding.devicesRecyclerView.adapter = adapter
    }

    /**
     * Connect to device through UUIDS
     * @param device
     */
    @SuppressLint("MissingPermission")
    private fun connectToDevice(device: BluetoothDevice, bluetoothDevices: List<BluetoothDevice>) {
        var macAddressMatched = false
        bluetoothDevices.forEach {getBluetoothDevice->
            if(device.address.equals(getBluetoothDevice.address)){
                // Create a Handler to handle BluetoothService messages
                val handler = object : Handler(Looper.getMainLooper()) {
                    override fun handleMessage(msg: Message) {
                        // Handle BluetoothService messages
                        when (msg.what) {
                            BluetoothService.MESSAGE_STATE_CHANGE -> {
                                Log.e("NewBlueToothTAG", "getting_message${msg.arg1}")
                            }
                        }
                    }
                }
                // Create an instance of BluetoothService
                val bluetoothService = BluetoothService(handler)
                // Start the BluetoothService
                bluetoothService.start()
                // Connect to the Bluetooth device
                bluetoothService.connect(device)
                val data = "Hello, Bluetooth!"
                val byteData = data.toByteArray()

                 bluetoothService.sendMessage("Hellow bluetooth","UTF-8")
                bluetoothSocket = device.createRfcommSocketToServiceRecord(UUID.fromString("00001124-0000-1000-8000-00805F9B34FB"))
                bluetoothService.connected(bluetoothSocket)

                val currentState = bluetoothService.state
                val isConnected = currentState == BluetoothService.STATE_CONNECTED
                if (isConnected) {
                    // Device is connected
                    Toast.makeText(this, "Device Connected", Toast.LENGTH_SHORT).show()
                } else {
                    // Device is not connected
                    Toast.makeText(this, "Device is not connected $currentState", Toast.LENGTH_SHORT).show()
                }
                macAddressMatched = true
                return@forEach
            }
        }

        if (!macAddressMatched) {
            Toast.makeText(this, "MAC address did not match", Toast.LENGTH_SHORT).show()
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
}