package com.example.androidexampleapp.controller

import android.content.Intent
import io.github.cdimascio.dotenv.dotenv
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.ui.AppBarConfiguration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidexampleapp.R
import com.example.androidexampleapp.model.Cars
import com.example.androidexampleapp.model.DataStore
import com.example.androidexampleapp.view.CarsListAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

val dotenv = dotenv {
    directory = "../../../../.env"
    ignoreIfMissing = true
}

class MainActivity : AppCompatActivity() {

    private var adapter: CarsListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)


        DataStore.setContext(this)

        val fabBtn = findViewById<FloatingActionButton>(R.id.fab)
        fabBtn.setOnClickListener {
            val intent = Intent(this@MainActivity, CarsActivity::class.java)
            startActivity(intent)
        }


        val count = DataStore.cars.size // length of the array

        for (car: Cars in DataStore.cars) {
            println(car.car_name)
        }

        for (i in 0 until count) {
            // println(DataStore.cars[i].car_name)
            val car = DataStore.getCars(i)
            // Log.d()  displays on Logcat
            Log.d("Car", "Car: ${car.car_name}")
        }


        val rcvCars = findViewById<RecyclerView>(R.id.recyclerViewCars)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        rcvCars.layoutManager = layoutManager
        adapter = CarsListAdapter(DataStore.cars)
        rcvCars.adapter = adapter
    }
}