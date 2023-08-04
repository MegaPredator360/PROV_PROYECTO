package com.example.prov_proyecto

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.prov_proyecto.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private lateinit var binding: ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val baseUrl = "https://megapredator360.github.io/API_PRUEBA/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Se llama al metodo para cargar las recetas
        cargarRecetas()
        /*
        Aplicación de recetas
        - Que muestre una coleccion de recetas
        - Ordenar las recetas por categoria
        - Poder guardar las recetas en una lista de favoritos
        - Mostrar procedimientos de recetas con algunas imagenes

        Primer pantalla
        Muestra la lista de recetas
        Filtrar recetas por categorias como lista desplegable

        Pantalla info de recetas
         */

        /* PRUEBA 2
        * */

        // Prueba 2
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
        api.getRecetas(url).enqueue(object: Callback<RecetasResponse> {

            // Si la respuesta del API es satisfactoria, se llama a la clase RecetasResponse para
            // llenar los datos de la respuesta del API
            override fun onResponse(
                call: Call<RecetasResponse>,
                response: Response<RecetasResponse>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {recetasResponse ->
                        // Se crea un adaptador para las lista de recetas y mostrarlas en el ListView
                        val adaptadorListaRecetas = RecetaAdapt(this@MainActivity, recetasResponse.ListaRecetas)

                        // Se llama al ListView y se le asignan los datos que recibimos del API
                        binding.lstRecetas.isClickable = true
                        binding.lstRecetas.adapter = adaptadorListaRecetas
                        binding.lstRecetas.setOnItemClickListener { parent, view, position, id ->
                            val infoRecetaIntent = Intent(this@MainActivity, InfoRecetaActivity::class.java)

                            // Obtenemos la posicion de la receta en la lista
                            val posicionReceta = recetasResponse.ListaRecetas[position]

                            infoRecetaIntent.putExtra("nombreReceta", posicionReceta.Nombre)
                            infoRecetaIntent.putExtra("categoriaReceta", posicionReceta.Categoria)
                            infoRecetaIntent.putExtra("ingredientesReceta", posicionReceta.Ingredientes)
                            infoRecetaIntent.putExtra("preparacionReceta", posicionReceta.Preparacion)
                            startActivity(infoRecetaIntent)
                        }
                    }
                }
            }

            // En caso de que ocurra un fallo de respuesta, el usuario recibira un mensaje en su pantalla
            // diciendo que hubo un error y se imprimirá en el Logcat el tipo de error
            override fun onFailure(call: Call<RecetasResponse>, t: Throwable) {
                Toast.makeText(applicationContext, "Hubo un error al obtener los datos, por favor intenta más tarde", Toast.LENGTH_SHORT).show()
                // Mostrará en el Logcat el error
                Log.i(TAG, "onFailure: ${t.message}")
            }

        })
    }
}