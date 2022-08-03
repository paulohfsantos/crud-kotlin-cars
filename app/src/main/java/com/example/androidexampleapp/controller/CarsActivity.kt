package com.example.androidexampleapp.controller

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.androidexampleapp.R
import com.example.androidexampleapp.model.DataStore
import com.google.android.material.textfield.TextInputEditText

class CarsActivity : AppCompatActivity() {
    val REQUEST_CODE_ADD = 1

    var position = 0
    var type = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cars_management)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        // TODO: detect if id exists in order to get edit or add
        supportActionBar?.title = "Car Add/Edit"

        type = intent.getIntExtra("type", 1)

        if (type == 2) {
            position = intent.getIntExtra("position", 0)

            val car = DataStore.getCars(position)

            val txtCarName = findViewById<TextInputEditText>(R.id.inputCarName)
            val txtCarColor = findViewById<TextInputEditText>(R.id.inputCarColor)
            val txtCarYear = findViewById<TextInputEditText>(R.id.inputCarYear)
            val txtCarPrice = findViewById<TextInputEditText>(R.id.inputCarPrice)

            txtCarName.setText(car.car_name)
            txtCarColor.setText(car.color)
            txtCarYear.setText(car.year)
            txtCarPrice.setText(car.price)
        }
    }
}