package com.example.prov_proyecto

import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.prov_proyecto.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private lateinit var binding: ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val baseUrl = "https://megapredator360.github.io/API_PRUEBA/"
    private val opcionesFiltrar = listOf("Mostrar Todos", "Entrada", "Plato Fuerte", "Postre", "Bebidas")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Se llama al metodo para cargar las recetas
        cargarRecetas()
    }

    private fun cargarRecetas() {
        // Definimos la otra mitad del URL que será usado para obtener los datos
        val url = "recetario.json"

        // Creamos el request
        val api = Retrofit.Builder()
            // Invocamos al url base
            .baseUrl(baseUrl)
            // Conversion de tipo JSON
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            // Asociamos el create con el interface
            .create(RecetasAPI::class.java)

        // Llamamos a la funcion de getRecetas de la interface y que queremos datos de tipo RecetasResponse
        // que serán llenados con la respuesta del API
        api.getRecetas(url).enqueue(object : Callback<RecetasResponse> {

            // Si la respuesta del API es satisfactoria, se llama a la clase RecetasResponse para
            // llenar los datos de la respuesta del API
            override fun onResponse(
                call: Call<RecetasResponse>,
                response: Response<RecetasResponse>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { recetasResponse ->

                        val spinnerAdapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_spinner_item, opcionesFiltrar)
                        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        binding.filtrarCategorias.adapter = spinnerAdapter

                        binding.filtrarCategorias.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                                // Get the selected filter option
                                val selectedOption = opcionesFiltrar[position]

                                // Apply the filter and update the ListView
                                val listaFiltrada = if (selectedOption == "Mostrar Todos") {
                                    recetasResponse.ListaRecetas
                                } else {
                                    recetasResponse.ListaRecetas.filter { item -> item.Categoria.contains(selectedOption) } // Filter based on some condition
                                }

                                val adaptadorListaRecetas =
                                    RecetaAdapt(this@MainActivity, listaFiltrada)
                                binding.lstRecetas.isClickable = true
                                binding.lstRecetas.adapter = adaptadorListaRecetas

                                binding.lstRecetas.setOnItemClickListener { parent, view, position, id ->
                                    val infoRecetaIntent =
                                        Intent(this@MainActivity, InfoRecetaActivity::class.java)

                                    // Obtenemos la posicion de la receta en la lista
                                    val posicionReceta = listaFiltrada[position]

                                    infoRecetaIntent.putExtra("nombreReceta", posicionReceta.Nombre)
                                    infoRecetaIntent.putExtra("categoriaReceta", posicionReceta.Categoria)
                                    infoRecetaIntent.putExtra(
                                        "ingredientesReceta",
                                        posicionReceta.Ingredientes
                                    )
                                    infoRecetaIntent.putExtra(
                                        "preparacionReceta",
                                        posicionReceta.Preparacion
                                    )
                                    infoRecetaIntent.putStringArrayListExtra(
                                        "imagenesReceta",
                                        posicionReceta.Imagenes
                                    )
                                    startActivity(infoRecetaIntent)
                                }
                            }

                            override fun onNothingSelected(parent: AdapterView<*>?) {
                                TODO("Not yet implemented")
                            }
                        }
                    }
                }
            }

            // En caso de que ocurra un fallo de respuesta, el usuario recibira un mensaje en su pantalla
            // diciendo que hubo un error y se imprimirá en el Logcat el tipo de error
            override fun onFailure(call: Call<RecetasResponse>, t: Throwable) {
                Toast.makeText(
                    applicationContext,
                    "Hubo un error al obtener los datos, por favor intenta más tarde",
                    Toast.LENGTH_SHORT
                ).show()
                // Mostrará en el Logcat el error
                Log.i(TAG, "onFailure: ${t.message}")
            }

        })
    }
}