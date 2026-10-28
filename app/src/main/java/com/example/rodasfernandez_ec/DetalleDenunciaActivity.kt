package com.example.rodasfernandez_ec

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.rodasfernandez_ec.databinding.ActivityDetalleDenunciaBinding
import com.example.rodasfernandez_ec.model.Denuncia
import com.example.rodasfernandez_ec.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetalleDenunciaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetalleDenunciaBinding
    private var denunciaId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalleDenunciaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        denunciaId = intent.getLongExtra("DENUNCIA_ID", -1)

        if (denunciaId != -1L) {
            cargarDetalleDenuncia(denunciaId)
        } else {
            Toast.makeText(this, "ID de denuncia no válido", Toast.LENGTH_SHORT).show()
            finish()
        }

        binding.btnAtender.setOnClickListener {
            marcarComoAtendida(denunciaId)
        }
    }

    private fun cargarDetalleDenuncia(id: Long) {
        binding.progressBar.visibility = View.VISIBLE
        binding.cardDetalle.visibility = View.GONE

        RetrofitClient.apiService.obtenerDenunciaPorId(id)
            .enqueue(object : Callback<Denuncia> {
                override fun onResponse(call: Call<Denuncia>, response: Response<Denuncia>) {
                    binding.progressBar.visibility = View.GONE

                    if (response.isSuccessful && response.body() != null) {
                        val denuncia = response.body()!!
                        mostrarDatosDenuncia(denuncia)
                        binding.cardDetalle.visibility = View.VISIBLE
                    } else {
                        Toast.makeText(
                            this@DetalleDenunciaActivity,
                            "No se encontró la denuncia",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    }
                }

                override fun onFailure(call: Call<Denuncia>, t: Throwable) {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(
                        this@DetalleDenunciaActivity,
                        "Error de conexión: ${t.localizedMessage}",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                }
            })
    }

    private fun mostrarDatosDenuncia(denuncia: Denuncia) {
        with(binding) {
            tvDetalleId.text = "Denuncia #${denuncia.id ?: "-"}"
            tvDetalleDni.text = denuncia.dni
            tvDetalleTipo.text = denuncia.tipo
            tvDetalleDireccion.text = denuncia.direccion
            tvDetalleFecha.text = denuncia.fecha ?: "Sin fecha"
            tvDetalleDescripcion.text = denuncia.descripcion

            if (denuncia.estado == 1) {
                tvDetalleEstado.text = "PENDIENTE"
                tvDetalleEstado.setTextColor(ContextCompat.getColor(this@DetalleDenunciaActivity, R.color.status_pending_text))
                tvDetalleEstado.setBackgroundColor(ContextCompat.getColor(this@DetalleDenunciaActivity, R.color.status_pending_bg))
                btnAtender.visibility = View.VISIBLE
                btnAtender.isEnabled = true
            } else {
                tvDetalleEstado.text = "ATENDIDA"
                tvDetalleEstado.setTextColor(ContextCompat.getColor(this@DetalleDenunciaActivity, R.color.status_attended_text))
                tvDetalleEstado.setBackgroundColor(ContextCompat.getColor(this@DetalleDenunciaActivity, R.color.status_attended_bg))
                btnAtender.visibility = View.GONE
            }
        }
    }

    private fun marcarComoAtendida(id: Long) {
        binding.progressBar.visibility = View.VISIBLE
        binding.btnAtender.isEnabled = false

        RetrofitClient.apiService.atenderDenuncia(id)
            .enqueue(object : Callback<Denuncia> {
                override fun onResponse(call: Call<Denuncia>, response: Response<Denuncia>) {
                    binding.progressBar.visibility = View.GONE

                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@DetalleDenunciaActivity,
                            "¡Denuncia marcada como atendida exitosamente!",
                            Toast.LENGTH_SHORT
                        ).show()

                        val denunciaActualizada = response.body()
                        if (denunciaActualizada != null) {
                            mostrarDatosDenuncia(denunciaActualizada)
                        } else {
                            binding.tvDetalleEstado.text = "ATENDIDA"
                            binding.tvDetalleEstado.setTextColor(ContextCompat.getColor(this@DetalleDenunciaActivity, R.color.status_attended_text))
                            binding.tvDetalleEstado.setBackgroundColor(ContextCompat.getColor(this@DetalleDenunciaActivity, R.color.status_attended_bg))
                            binding.btnAtender.visibility = View.GONE
                        }
                    } else {
                        binding.btnAtender.isEnabled = true
                        Toast.makeText(
                            this@DetalleDenunciaActivity,
                            "Error al marcar como atendida (${response.code()})",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<Denuncia>, t: Throwable) {
                    binding.progressBar.visibility = View.GONE
                    binding.btnAtender.isEnabled = true
                    Toast.makeText(
                        this@DetalleDenunciaActivity,
                        "Error de conexión: ${t.localizedMessage}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
}
