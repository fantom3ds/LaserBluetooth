package com.example.laserbluetooth

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import app.akexorcist.bluetotohspp.library.BluetoothSPP
import app.akexorcist.bluetotohspp.library.BluetoothState
import app.akexorcist.bluetotohspp.library.DeviceList
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    lateinit var bt: BluetoothSPP

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bt = BluetoothSPP(applicationContext)
        bt.startService(BluetoothState.DEVICE_OTHER)

        val intent = Intent(applicationContext , DeviceList::class.java)
        startActivityForResult(intent , BluetoothState.REQUEST_CONNECT_DEVICE)

        btn_get.setOnClickListener {
            //C04000EE - старт замера
            val bytes = arrayListOf<Byte>()
            bytes.addAll(byteArrayOf(12 , 0 , 4 , 0 , 0 , 0 , 0xE , 0xE , 32).toList())
            bt.send(bytes.toByteArray() , false)
            Toast.makeText(this@MainActivity , bytes.toString() , Toast.LENGTH_LONG).show()
        }

        bt.setOnDataReceivedListener { data , message ->
            Toast.makeText(this , "Что-то передалось" , Toast.LENGTH_LONG).show()
            //tv_log.text = data[0].toString()
            data.forEach {
                tv_log.append(it.toString())
            }
            tv_log2.append(message)
        }
    }

    override fun onActivityResult(requestCode: Int , resultCode: Int , data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            bt.setupService();
            bt.startService(BluetoothState.DEVICE_OTHER);
            bt.connect(data)
        }
    }

    override fun onDestroy() {
        bt.disconnect()
        super.onDestroy()
    }
}
