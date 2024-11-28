package com.motorro.resources1

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.motorro.resources1.databinding.ActivityMainBinding
import java.time.LocalDate

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setCatsInHouse()
        setAnimalName()
        setCatWeight()
        setCatBd()
    }

    private fun setCatsInHouse() = with(binding) {
        catsInHouse.text = getString(R.string.cats_in_house, 25)
    }

    private fun setAnimalName() = with(binding) {
        animalName.text = getString(R.string.animal_name, "Cat", "Tom")
    }

    private fun setCatWeight() = with(binding) {
        weightOfCat.text = getString(R.string.cat_weight, 7.546534F)
    }

    private fun setCatBd() = with(binding) {
        bdOfCat.text = getString(
            R.string.cats_birthday,
            LocalDate.of(2007, 10, 17)
        )
    }
}