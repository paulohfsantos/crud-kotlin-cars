package com.example.androidcrudcarsapp.controller

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.androidcrudcarsapp.R
import com.example.androidcrudcarsapp.model.Cars
import com.example.androidcrudcarsapp.model.DataStore
import com.google.android.material.textfield.TextInputEditText

class CarsActivity : AppCompatActivity() {
    var position = 0
    // receiving this type in main activity on PutExtra method
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.save_car_data, menu)
        return true
    }

    // save data
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d("type-1", type.toString())
        when (item.itemId) {
            R.id.saveData -> {
                Log.d("type-2", type.toString())
                val txtCarName = findViewById<EditText>(R.id.inputCarName)
                val txtCarColor = findViewById<EditText>(R.id.inputCarColor)
                val txtCarYear = findViewById<EditText>(R.id.inputCarYear)
                val txtCarPrice = findViewById<EditText>(R.id.inputCarPrice)

                val car = Cars(
                    txtCarName.text.toString(),
                    txtCarYear.text.toString(),
                    txtCarColor.text.toString(),
                    txtCarPrice.text.toString(),
                )

                Log.d("type-3", type.toString())

                if (type == 1) {
                    Log.d("type-4", type.toString())
                    DataStore.addCar(car)
                } else if (type == 2) {
                    Log.d("type-5", type.toString())
                    DataStore.editCar(car, position)
                }

                val intent = Intent().apply {
                    putExtra("carName", car.car_name)
                }

                setResult(Activity.RESULT_OK, intent)
                finish()
            }

            android.R.id.home -> {
                finish()
            }
        }

        return super.onOptionsItemSelected(item)
    }
}