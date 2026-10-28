package com.example.rodasfernandez_ec.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.rodasfernandez_ec.R
import com.example.rodasfernandez_ec.databinding.ItemDenunciaBinding
import com.example.rodasfernandez_ec.model.Denuncia

class DenunciaAdapter(
    private var listaDenuncias: List<Denuncia> = emptyList(),
    private val onItemClick: (Denuncia) -> Unit
) : RecyclerView.Adapter<DenunciaAdapter.DenunciaViewHolder>() {

    class DenunciaViewHolder(val binding: ItemDenunciaBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DenunciaViewHolder {
        val binding = ItemDenunciaBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DenunciaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DenunciaViewHolder, position: Int) {
        val denuncia = listaDenuncias[position]
        val context = holder.itemView.context

        with(holder.binding) {
            tvId.text = "Denuncia #${denuncia.id ?: "-"}"
            tvTipo.text = "Tipo: ${denuncia.tipo}"
            tvDni.text = "DNI: ${denuncia.dni}"
            tvDireccion.text = "Dirección: ${denuncia.direccion}"
            tvFecha.text = "Fecha: ${denuncia.fecha ?: "Sin fecha"}"

            if (denuncia.estado == 1) {
                tvEstado.text = "PENDIENTE"
                tvEstado.setTextColor(ContextCompat.getColor(context, R.color.status_pending_text))
                tvEstado.setBackgroundColor(ContextCompat.getColor(context, R.color.status_pending_bg))
            } else {
                tvEstado.text = "ATENDIDA"
                tvEstado.setTextColor(ContextCompat.getColor(context, R.color.status_attended_text))
                tvEstado.setBackgroundColor(ContextCompat.getColor(context, R.color.status_attended_bg))
            }

            root.setOnClickListener {
                onItemClick(denuncia)
            }
        }
    }

    override fun getItemCount(): Int = listaDenuncias.size

    fun actualizarLista(nuevaLista: List<Denuncia>) {
        listaDenuncias = nuevaLista
        notifyDataSetChanged()
    }
}
