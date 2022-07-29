package com.example.androidexampleapp.model

import android.content.Context

object DataStore {
    var cars: MutableList<Cars> = arrayListOf()

    private var applicationContext: Context? = null

    fun setContext(context: Context) {
        applicationContext = context
    }

    init {
        addCar(Cars(1,"Audi", "2022", "black", 154000))
        addCar(Cars(1,"BMW", "2018", "silver", 85000))
        addCar(Cars(1,"Mercedes", "2020", "red", 90000))
    }

    // crud methods

    fun getCars(position: Int): Cars {
        return cars[position]
    }

    fun addCar(car: Cars) {
        cars.add(car)
    }

    fun editCar(car: Cars, position: Int) {
        cars[position] = car
    }

    fun deleteCar(position: Int) {
        cars.removeAt(position)
    }
}