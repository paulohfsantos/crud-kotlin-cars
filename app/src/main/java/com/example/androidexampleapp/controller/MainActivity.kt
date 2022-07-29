package com.example.androidexampleapp.controller

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidexampleapp.R
import com.example.androidexampleapp.model.Cars
import com.example.androidexampleapp.model.DataStore
import com.example.androidexampleapp.view.CarsListAdapter

class MainActivity : AppCompatActivity() {

    private var adapter: CarsListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

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