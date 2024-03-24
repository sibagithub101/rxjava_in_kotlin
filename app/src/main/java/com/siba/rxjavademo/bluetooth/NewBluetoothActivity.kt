package com.siba.rxjavademo.bluetooth

import android.Manifest
import android.annotation.SuppressLint
import android.app.Service
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Binder
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.siba.rxjavademo.databinding.ActivityNewBluetoothBinding

class NewBluetoothActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewBluetoothBinding
    private lateinit var bluetoothAdapter: BluetoothAdapter
    private var bluetoothGatt: BluetoothGatt? = null
    private var count = 0


    // checking result code if it is bluetooth turn on or not
    private val mActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
            // Bluetooth was successfully turned on
            Toast.makeText(this,"Bluetooth Turned on",Toast.LENGTH_SHORT).show()
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

        // Bind to the BleService
        val serviceIntent = Intent(this, BluetoothLeService::class.java)
        bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE)

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
        //iniialize the bluetooth manager and bluetooth adatpter
        val bluetoothManager = getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter

        if(bluetoothAdapter==null){
            Toast.makeText(this, "Bluetooth is not supported on this device", Toast.LENGTH_SHORT).show()
        }else if(!bluetoothAdapter.isEnabled){
            turnOnBluetooth()
        }else{
          //  startScan()
            discoverBlueToothDevice()
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

    /**
     * Discover bluetooth device and showing which is already pair with mobile
     *  or we use scan bluetooth device for getting
     *
     */
    @RequiresApi(Build.VERSION_CODES.S)
    @SuppressLint("MissingPermission")
    private fun discoverBlueToothDevice() {
        val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter.bondedDevices
        val adapter = DeviceAdapter(pairedDevices?.toList() ?: emptyList()){ getDevice->
            connectToDevice(getDevice)
        }
        binding.devicesRecyclerView.adapter = adapter
    }

    /**
     * we can use above discoverBlueToothDevice() for getting paired device or we use
     * below startScan()
     */
    @SuppressLint("MissingPermission")
    private fun startScan() {
        val scanner = bluetoothAdapter.bluetoothLeScanner
        val scanSettings = ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            .build()
        scanner.startScan(null, scanSettings, scanCallback)
    }
    private val scanCallback = object : ScanCallback() {
        @RequiresApi(Build.VERSION_CODES.S)
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            val device = result.device
            connectToDevice(device)
        }
    }

    /**
     * After scan device or discoverBlueToothDevice() connect to gatt server
     */

    @RequiresApi(Build.VERSION_CODES.S)
    @SuppressLint("MissingPermission")
    private fun connectToDevice(device: BluetoothDevice) {
        val getDevice = bluetoothAdapter.getRemoteDevice(device.address)
            bluetoothGatt = getDevice.connectGatt(this, false, gattCallback)
            if (bluetoothGatt == null) {
                binding.tvCheckCOnnection.text = "Failed to connect to the Bluetooth device"
            }
    }

    /**
     * after connect with gatt server create a bound service to connect with bluetooth device
     */

    class BluetoothLeService : Service() {
        private lateinit var bluetoothAdapter: BluetoothAdapter
        private var bluetoothGatt: BluetoothGatt? = null

        private val binder = LocalBinder()
        override fun onBind(intent: Intent): IBinder {
            return binder
        }

        @SuppressLint("MissingPermission")
        fun connectToDevice(device: BluetoothDevice) {
           // bluetoothGatt = device.connectGatt(this, false, gattCallback)
        }

        inner class LocalBinder : Binder() {
            fun getService() : BluetoothLeService {
                return this@BluetoothLeService
            }
        }
    }

    /**
     *  after boundService(LocalBinder) create a serviceConnection for manage Service lifecycle
     *   ServiceConnection object to handle service binding
     */

    private val serviceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(
            componentName: ComponentName,
            service: IBinder
        ) {
            val bluetoothService = (service as BluetoothLeService.LocalBinder).getService()
            bluetoothService.let { bluetooth ->
                // call functions on service to check connection and connect to devices
            }
        }

        override fun onServiceDisconnected(componentName: ComponentName) {
           // bluetoothService = null
        }
    }


     val gattCallback: BluetoothGattCallback = object : BluetoothGattCallback() {
        @SuppressLint("MissingPermission")
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            when (newState) {
                BluetoothProfile.STATE_CONNECTED -> {
                    binding.tvCheckCOnnection.text = "Connected to device"
                    bluetoothGatt?.discoverServices()
                }
                BluetoothProfile.STATE_DISCONNECTED -> {
                    // Disconnected from the device
                    binding.tvCheckCOnnection.text = "Disconnected from device"
                }
                BluetoothProfile.STATE_CONNECTING -> {
                    // Device is connecting
                    binding.tvCheckCOnnection.text = "Device is connecting"
                }
                BluetoothProfile.STATE_DISCONNECTING -> {
                    // Device is disconnecting
                    binding.tvCheckCOnnection.text = "Device is disconnecting"
                }
                else -> {
                    // Handle other states if needed
                    binding.tvCheckCOnnection.text = "Other state: $newState"
                }
            }
        }


        // Method to read a characteristic from the connected BLE device
        @SuppressLint("MissingPermission")
        fun readCharacteristic(characteristic: BluetoothGattCharacteristic) {
            bluetoothGatt?.readCharacteristic(characteristic)
        }

        // Method to write a characteristic to the connected BLE device
        @SuppressLint("MissingPermission")
        fun writeCharacteristic(characteristic: BluetoothGattCharacteristic, data: ByteArray) {
            characteristic.value = data
            bluetoothGatt?.writeCharacteristic(characteristic)
        }

        // Override the onCharacteristicRead method to handle read responses from the device
        override fun onCharacteristicRead(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            status: Int
        ) {
            // Handle read responses here
        }

        // Override the onCharacteristicWrite method to handle write responses from the device
        override fun onCharacteristicWrite(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            status: Int
        ) {
            // Handle write responses here
        }


        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            if(status==BluetoothGatt.GATT_SUCCESS){
                val services = gatt?.services
                for (service in services!!) {
                    val characteristics = service.characteristics
                    for (characteristic in characteristics) {
                        if (characteristic.uuid.equals("D4:36:3D:3C:42:1F")) {
                            readCharacteristic(characteristic)
                        }
                        if (characteristic.uuid.equals("D4:36:3D:3C:42:1F")) {
                            val data = "Your data to be written".toByteArray()
                            writeCharacteristic(characteristic, data)
                        }
                    }
                }
            }
        }
        }

    @SuppressLint("MissingPermission")
    fun readCharacteristic(characteristic: BluetoothGattCharacteristic) {
        bluetoothGatt?.readCharacteristic(characteristic)
    }

    @SuppressLint("MissingPermission")
    fun writeCharacteristic(characteristic: BluetoothGattCharacteristic, data: ByteArray) {
        characteristic.value = data
        bluetoothGatt?.writeCharacteristic(characteristic)
    }

    @SuppressLint("MissingPermission")
    override fun onDestroy() {
        super.onDestroy()

        // Stop Bluetooth device discovery
        bluetoothAdapter.bluetoothLeScanner.stopScan(scanCallback)
        bluetoothAdapter.cancelDiscovery()
        // Disconnect and close BluetoothGatt
        bluetoothGatt?.disconnect()
        bluetoothGatt?.close()
    }
}