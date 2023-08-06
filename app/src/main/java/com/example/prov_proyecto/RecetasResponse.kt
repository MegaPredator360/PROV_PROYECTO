package com.example.prov_proyecto

class RecetasResponse(
    val Version: String,
    val ListaRecetas: List<ListaRecetas>
)

data class ListaRecetas(
    val Id: String,
    val Nombre: String,
    val Categoria: String,
    val Ingredientes: String,
    val Preparacion: String,
    val Imagenes: ArrayList<String>
)
