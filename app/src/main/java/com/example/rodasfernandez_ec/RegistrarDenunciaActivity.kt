package com.example.rodasfernandez_ec

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.rodasfernandez_ec.databinding.ActivityRegistrarDenunciaBinding
import com.example.rodasfernandez_ec.model.Denuncia
import com.example.rodasfernandez_ec.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistrarDenunciaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrarDenunciaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrarDenunciaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        binding.btnGuardar.setOnClickListener {
            registrarDenuncia()
        }
    }

    private fun registrarDenuncia() {
        val dni = binding.etDni.text.toString().trim()
        val tipo = binding.etTipo.text.toString().trim()
        val direccion = binding.etDireccion.text.toString().trim()
        val descripcion = binding.etDescripcion.text.toString().trim()

        if (dni.isEmpty() || tipo.isEmpty() || direccion.isEmpty() || descripcion.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        if (dni.length != 8) {
            Toast.makeText(this, "El DNI debe contener exactamente 8 dígitos", Toast.LENGTH_SHORT).show()
            return
        }

        val nuevaDenuncia = Denuncia(
            dni = dni,
            tipo = tipo.uppercase(),
            direccion = direccion,
            descripcion = descripcion
        )

        binding.progressBar.visibility = View.VISIBLE
        binding.btnGuardar.isEnabled = false

        RetrofitClient.apiService.registrarDenuncia(nuevaDenuncia)
            .enqueue(object : Callback<Denuncia> {
                override fun onResponse(call: Call<Denuncia>, response: Response<Denuncia>) {
                    binding.progressBar.visibility = View.GONE
                    binding.btnGuardar.isEnabled = true

                    if (response.isSuccessful && response.body() != null) {
                        val denunciaRegistrada = response.body()!!
                        Toast.makeText(
                            this@RegistrarDenunciaActivity,
                            "¡Denuncia #${denunciaRegistrada.id} registrada con éxito!",
                            Toast.LENGTH_LONG
                        ).show()
                        finish()
                    } else {
                        Toast.makeText(
                            this@RegistrarDenunciaActivity,
                            "Error al registrar denuncia (${response.code()})",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<Denuncia>, t: Throwable) {
                    binding.progressBar.visibility = View.GONE
                    binding.btnGuardar.isEnabled = true
                    Toast.makeText(
                        this@RegistrarDenunciaActivity,
                        "Error de conexión: ${t.localizedMessage}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
}
