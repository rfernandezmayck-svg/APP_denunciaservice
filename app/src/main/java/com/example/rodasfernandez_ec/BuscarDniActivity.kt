package com.example.rodasfernandez_ec

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rodasfernandez_ec.adapter.DenunciaAdapter
import com.example.rodasfernandez_ec.databinding.ActivityBuscarDniBinding
import com.example.rodasfernandez_ec.model.Denuncia
import com.example.rodasfernandez_ec.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BuscarDniActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBuscarDniBinding
    private lateinit var adapter: DenunciaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBuscarDniBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        setupRecyclerView()

        binding.btnBuscar.setOnClickListener {
            buscarPorDni()
        }
    }

    private fun setupRecyclerView() {
        adapter = DenunciaAdapter { denuncia ->
            denuncia.id?.let { id ->
                val intent = Intent(this, DetalleDenunciaActivity::class.java).apply {
                    putExtra("DENUNCIA_ID", id)
                }
                startActivity(intent)
            }
        }
        binding.rvResultadosDni.layoutManager = LinearLayoutManager(this)
        binding.rvResultadosDni.adapter = adapter
    }

    private fun buscarPorDni() {
        val dni = binding.etBuscarDni.text.toString().trim()

        if (dni.isEmpty()) {
            Toast.makeText(this, "Ingrese el número de DNI", Toast.LENGTH_SHORT).show()
            return
        }

        if (dni.length != 8) {
            Toast.makeText(this, "El DNI debe tener 8 dígitos", Toast.LENGTH_SHORT).show()
            return
        }

        binding.progressBar.visibility = View.VISIBLE
        binding.tvMensajeResultado.visibility = View.GONE
        binding.rvResultadosDni.visibility = View.GONE

        RetrofitClient.apiService.obtenerDenunciasPorDni(dni)
            .enqueue(object : Callback<List<Denuncia>> {
                override fun onResponse(
                    call: Call<List<Denuncia>>,
                    response: Response<List<Denuncia>>
                ) {
                    binding.progressBar.visibility = View.GONE

                    if (response.isSuccessful && response.body() != null) {
                        val lista = response.body()!!

                        if (lista.isEmpty()) {
                            binding.tvMensajeResultado.text = "No se encontraron denuncias registradas para el DNI $dni"
                            binding.tvMensajeResultado.visibility = View.VISIBLE
                            binding.rvResultadosDni.visibility = View.GONE
                        } else {
                            adapter.actualizarLista(lista)
                            binding.tvMensajeResultado.visibility = View.GONE
                            binding.rvResultadosDni.visibility = View.VISIBLE
                        }
                    } else {
                        binding.tvMensajeResultado.text = "Error al consultar las denuncias (${response.code()})"
                        binding.tvMensajeResultado.visibility = View.VISIBLE
                        binding.rvResultadosDni.visibility = View.GONE
                    }
                }

                override fun onFailure(call: Call<List<Denuncia>>, t: Throwable) {
                    binding.progressBar.visibility = View.GONE
                    binding.tvMensajeResultado.text = "Error de conexión: ${t.localizedMessage}"
                    binding.tvMensajeResultado.visibility = View.VISIBLE
                    binding.rvResultadosDni.visibility = View.GONE
                }
            })
    }
}
