package com.example.rodasfernandez_ec

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rodasfernandez_ec.adapter.DenunciaAdapter
import com.example.rodasfernandez_ec.databinding.ActivityListaDenunciasBinding
import com.example.rodasfernandez_ec.model.DenunciaPage
import com.example.rodasfernandez_ec.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListaDenunciasActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListaDenunciasBinding
    private lateinit var adapter: DenunciaAdapter

    private var offset = 0
    private val limit = 5
    private var paginaActual = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListaDenunciasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        setupRecyclerView()
        setupListeners()
        cargarDenuncias()
    }

    override fun onResume() {
        super.onResume()
        cargarDenuncias()
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
        binding.rvDenuncias.layoutManager = LinearLayoutManager(this)
        binding.rvDenuncias.adapter = adapter
    }

    private fun setupListeners() {
        binding.btnAnterior.setOnClickListener {
            if (offset >= limit) {
                offset -= limit
                paginaActual--
                cargarDenuncias()
            }
        }

        binding.btnSiguiente.setOnClickListener {
            offset += limit
            paginaActual++
            cargarDenuncias()
        }
    }

    private fun cargarDenuncias() {
        binding.progressBar.visibility = View.VISIBLE
        binding.tvEmptyState.visibility = View.GONE
        binding.rvDenuncias.visibility = View.GONE

        RetrofitClient.apiService.listarDenuncias(offset, limit)
            .enqueue(object : Callback<DenunciaPage> {
                override fun onResponse(
                    call: Call<DenunciaPage>,
                    response: Response<DenunciaPage>
                ) {
                    binding.progressBar.visibility = View.GONE

                    if (response.isSuccessful && response.body() != null) {
                        val pagina = response.body()!!
                        val lista = pagina.content

                        adapter.actualizarLista(lista)
                        binding.tvPagina.text = "Página $paginaActual"

                        if (lista.isEmpty()) {
                            binding.tvEmptyState.visibility = View.VISIBLE
                            binding.rvDenuncias.visibility = View.GONE
                        } else {
                            binding.tvEmptyState.visibility = View.GONE
                            binding.rvDenuncias.visibility = View.VISIBLE
                        }

                        binding.btnAnterior.isEnabled = offset > 0
                        binding.btnSiguiente.isEnabled = lista.size == limit && pagina.last != true
                    } else {
                        binding.tvEmptyState.visibility = View.VISIBLE
                        binding.tvEmptyState.text = "Error al obtener datos (${response.code()})"
                        Toast.makeText(
                            this@ListaDenunciasActivity,
                            "Error en la consulta",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<DenunciaPage>, t: Throwable) {
                    binding.progressBar.visibility = View.GONE
                    binding.tvEmptyState.visibility = View.VISIBLE
                    binding.tvEmptyState.text = "Error de conexión: ${t.localizedMessage}"
                    Toast.makeText(
                        this@ListaDenunciasActivity,
                        "No se pudo conectar a la API",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
}
