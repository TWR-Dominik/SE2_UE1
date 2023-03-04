package com.example.se2_ue1

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.se2_ue1.databinding.ActivityMainBinding
import java.io.IOException
import java.net.Socket

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.run {
            button.setOnClickListener{ sendInput() }
            button3.setOnClickListener{ findPrimeDigits() }
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
    private fun findPrimeDigits(){
        val matrikelnummer = binding.matrikelnummer.text.toString().toInt()
        var temp = matrikelnummer // Create a variable to store the temporary copy of the matrikelnummer.
        var primeDigits = "" // Create a variable to store the prime digits found in the matrikelnummer.
        while (temp > 0) { // Loop through each digit in the matrikelnummer.
            val digit = temp % 10 // Get the last digit of the matrikelnummer.
            if (isPrime(digit)) {  // Check if the digit is prime.
                primeDigits += "$digit "   // Add the prime digit to the primeDigits string.
            }
            temp /= 10  // Remove the last digit from the matrikelnummer.
        }
        runOnUiThread {
            binding.textView3.text = primeDigits
        }
    }
    private fun isPrime(n: Int): Boolean{
        if (n <= 1) return false // Check if the integer is less than or equal to 1.  1 is not considered a prime number.
        for (i in 2..n/2) { // Loop through each integer from 2 to half of the given integer. If the integer is divisible by any number other than 1 and itself, then it is not a prime number.
            if (n % i == 0) {
                return false
            }
        }
        return true // If the integer is not divisible by any number other than 1 and itself,then it is a prime number.
    }
}


