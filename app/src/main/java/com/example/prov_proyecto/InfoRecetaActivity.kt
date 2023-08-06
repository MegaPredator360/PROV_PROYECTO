package com.example.prov_proyecto

import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.prov_proyecto.databinding.ActivityInfoRecetaBinding
import com.google.android.material.tabs.TabLayoutMediator

private lateinit var binding: ActivityInfoRecetaBinding

class InfoRecetaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoRecetaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configuramos el toolbar
        setSupportActionBar(binding.infoRecetaToolbar)

        // Agregamos el boton de regresar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Cambiar el color del icono del botón de navegación hacia atrás programáticamente
        val upArrow = ContextCompat.getDrawable(this, R.drawable.baseline_arrow_back_24)
        upArrow?.setColorFilter(resources.getColor(R.color.white), PorterDuff.Mode.SRC_ATOP)
        supportActionBar?.setHomeAsUpIndicator(upArrow)

        binding.viewPagerImages.adapter = intent.getStringArrayListExtra("imagenesReceta")
            ?.let { ImagePagerAdapt(it) }

        // Se vincula el TabLayout con el ViewPager
        TabLayoutMediator(binding.tabLayout, binding.viewPagerImages) { tab, position -> }.attach()

        // Se asigna el texto a los botones
        binding.lblinfoNombre.setText(intent.getStringExtra("nombreReceta"))
        binding.lblinfoCategoria.setText(intent.getStringExtra("categoriaReceta"))
        binding.lblinfoIngredientes.setText(intent.getStringExtra("ingredientesReceta"))
        binding.lblinfoPreparacion.setText(intent.getStringExtra("preparacionReceta"))
    }

    // Este metodo maneja el evento del botón de navegación hacia atrás
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}