package com.example.androidcrudcarsapp.view

import android.view.LayoutInflater
import com.example.androidcrudcarsapp.R
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidcrudcarsapp.model.Cars


class CarsListAdapter(var carsList: MutableList<Cars>) : RecyclerView.Adapter<CarsListAdapter.CarsHolder>() {

    inner class CarsHolder(view: View): RecyclerView.ViewHolder(view) {
        var carName: TextView
        var carPrice: TextView
        var carYear: TextView
        var carColor: TextView

        var carList: LinearLayout

        init {
            carName = view.findViewById(R.id.car_name)
            carPrice = view.findViewById(R.id.car_price)
            carColor = view.findViewById(R.id.car_color)
            carYear = view.findViewById(R.id.car_year)
            carList = view.findViewById(R.id.carListLayout)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarsHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.rcv_cars, parent, false)

        return CarsHolder(view)
    }

    override fun onBindViewHolder(holder: CarsHolder, position: Int) {
        val carName = carsList[position]

        holder.carName.text = "Name: ${carName.car_name}"
        holder.carPrice.text = "Price: ${carName.price}"
        holder.carYear.text = "Year: ${carName.year}"
        holder.carColor.text = "Color: ${carName.color}"
    }

    override fun getItemCount(): Int {
        return carsList.size
    }
}