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

    // crud methods

    fun getCars(position: Int): Cars {
        return cars[position]
    }

    fun addCar(car: Cars) {
        val id = database?.insertCar(car) ?: return

        if (id > 0) {
            car.id = id
            cars.add(car)
        }
    }

    fun editCar(car: Cars, position: Int) {
        car.id = cars[position].id
        val count = database?.updateCar(car) ?: return

        if (count > 0) {
            cars.set(position, car)
        }
    }

    fun deleteCar(position: Int) {
        val count = database?.deleteCar(cars[position]) ?: return

        if (count > 0) {
            cars.removeAt(position)
        }
    }

    fun clearCarList() {
        val count = database?.deleteAllCars() ?: return
        if (count > 0) {
            cars.clear()
        }
    }
}