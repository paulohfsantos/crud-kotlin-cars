package com.example.androidexampleapp.model

import android.content.Context
import android.util.Log

object DataStore {
    var cars: MutableList<Cars> = arrayListOf()
        private set

    private var database: DBHelper? = null

    fun setContext(context: Context) {
        database = DBHelper(context)
        database?.let {
            cars = it.getAllCars()
        }
    }

    //init {
    //  addCar(Cars(1,"Audi", "2022", "black", 154000))
    //  addCar(Cars(1,"BMW", "2018", "silver", 85000))
    //  addCar(Cars(1,"Mercedes", "2020", "red", 90000))
    //}

    // crud methods

    fun getCars(position: Int): Cars {
        return cars[position]
    }

    fun addCar(car: Cars) {
        val id = database?.insertCar(car) ?: return

        if (id > 0) {
            car.id = id
            cars.add(car)

            Log.d("SQLite!", "Car successfully added")
        }
    }

    fun editCar(car: Cars, position: Int) {
        car.id = cars[position].id
        val count = database?.updateCar(car) ?: return

        if (count > 0) {
            cars[position] = car
            Log.d("SQLite!", "Car successfully updated")
        }
    }

    fun deleteCar(position: Int) {
        cars.removeAt(position)
    }
}