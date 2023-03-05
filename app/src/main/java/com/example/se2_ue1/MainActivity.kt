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
                val matrikelnummer = binding.matrikelnummer.text.toString() // Gets the text from a UI element with the ID "matrikelnummer" (a text field where the user enters their student ID number) and converting it to a String.
                val socket = Socket("se2-isys.aau.at",53212) // Create a socket connection to the server to the provided address
                val toServer = socket.getOutputStream() // creates an output stream for sending data to the server through the socket.
                val fromServer = socket.getInputStream() // creates an input stream for receiving data from the server through the socket.

                toServer.write((matrikelnummer + "\n").toByteArray()) // sends the student ID number (with a newline character appended to the end) to the server through the output stream.

                val responseBuffer = ByteArray(1024) // create a Bytearray to store data from the input stream
                val bytesRead = fromServer.read(responseBuffer) // reads the response from the server and saves it to the bytearray
                val response = responseBuffer.copyOfRange(0, bytesRead).toString(Charsets.UTF_8) // creates a copy of the bytearray that contains only the bytes that have been read from the input stream from range 0 to byetesread. the resulting bytearry is converted to UTF8

                socket.close() // closes the socket connection

                runOnUiThread {
                    binding.textView2.text = response // binds the UI to the response
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
            if (n % i == 0) { // checks whether n is evenly divisible by i (i.e., n % i is 0). If n is divisible by i, then n is not a prime number,
                return false
            }
        }
        return true // If the integer is not divisible by any number other than 1 and itself,then it is a prime number.
    }
}


