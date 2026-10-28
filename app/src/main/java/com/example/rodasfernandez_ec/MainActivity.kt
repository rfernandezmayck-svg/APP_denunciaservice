package com.example.rodasfernandez_ec

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.rodasfernandez_ec.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegistrar.setOnClickListener {
            val intent = Intent(this, RegistrarDenunciaActivity::class.java)
            startActivity(intent)
        }

        binding.btnVerTodas.setOnClickListener {
            val intent = Intent(this, ListaDenunciasActivity::class.java)
            startActivity(intent)
        }

        binding.btnBuscarDni.setOnClickListener {
            val intent = Intent(this, BuscarDniActivity::class.java)
            startActivity(intent)
        }
    }
}
