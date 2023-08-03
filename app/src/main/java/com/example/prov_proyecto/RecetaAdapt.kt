package com.example.prov_proyecto

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class RecetaAdapt(
    private val context: Activity, private val list: List<ListaRecetas>): ArrayAdapter<ListaRecetas>(context, R.layout.list_item, list) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.list_item, null)

        val imageView: ImageView = view.findViewById(R.id.imgReceta)
        val nombreReceta: TextView = view.findViewById(R.id.lblRecetaNombre)
        val categoriaReceta: TextView = view.findViewById(R.id.lblCategoriaReceta)

        //imageView.setImageResource(list[position].)
        nombreReceta.setText(list[position].Nombre)
        categoriaReceta.setText("Categoria: " + list[position].Categoria)

        return view
    }
}