package com.example.se2_ue1

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.se2_ue1.databinding.ActivityMainBinding
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.Socket

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.run {
            button.setOnClickListener{ sendInput() }
        }
    }

    private fun sendInput(){
        Thread{
            try {
                val matrikelnummer = binding.matrikelnummer.text.toString()
                val socket = Socket("se2-isys.aau.at",53212)
                val toServer = socket.getOutputStream()
                val fromServer = socket.getInputStream()

                toServer.write((matrikelnummer + "\n").toByteArray())

                val responseBuffer = ByteArray(1024)
                val bytesRead = fromServer.read(responseBuffer)
                val response = responseBuffer.copyOfRange(0, bytesRead).toString(Charsets.UTF_8)

                socket.close()

                runOnUiThread {
                    binding.textView2.text = response
                }
            }catch (e: IOException) {
                Log.d("MainActivity", "Error")
                e.printStackTrace()
            }
        }.start()

    }
}
