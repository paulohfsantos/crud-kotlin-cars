package com.example.androidexampleapp.controller

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import io.github.cdimascio.dotenv.dotenv
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.navigation.ui.AppBarConfiguration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidexampleapp.R
import com.example.androidexampleapp.model.Cars
import com.example.androidexampleapp.model.DataStore
import com.example.androidexampleapp.view.CarsListAdapter
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

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
            val intent = Intent(this@MainActivity, CarsActivity::class.java).apply {
                putExtra("isNew", 1)
            }
            //startActivityForResult(intent, 1)
            startActivity(intent)
        }

        updateToolbarTitle()

        val rcvCars = findViewById<RecyclerView>(R.id.recyclerViewCars)
        val layoutManager = LinearLayoutManager(this)

        layoutManager.orientation = LinearLayoutManager.VERTICAL
        rcvCars.layoutManager = layoutManager
        adapter = CarsListAdapter(DataStore.cars)
        rcvCars.adapter = adapter

        updateToolbarTitle()

        val gestureDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
                e?.let {
                    val view = rcvCars.findChildViewUnder(e.x, e.y)

                    view?.let {
                        val position = rcvCars.getChildAdapterPosition(view)
                        val intent = Intent(this@MainActivity, CarsActivity::class.java).apply {
                            putExtra("car", 2)
                            putExtra("position", position)
                        }
                        // startActivityForResult(intent, 2)
                        startActivity(intent)
                    }
                }

                return super.onSingleTapConfirmed(e)
            }

            override fun onLongPress(e: MotionEvent?) {
                super.onLongPress(e)

                e?.let {
                    val view = rcvCars.findChildViewUnder(e.x, e.y)
                    view?.let {
                        val position = rcvCars.getChildAdapterPosition(view)
                        val car = DataStore.getCars(position)

                        deleteDialogHandler(car, position)
                    }
                }
            }
        })

        rcvCars.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
                gestureDetector.onTouchEvent(e)
            }

            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                val child = rv.findChildViewUnder(e.x, e.y)
                return child != null && gestureDetector.onTouchEvent(e)
            }

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
        })
    }

    private fun deleteDialogHandler(car: Cars, position: Int) {
        val dialog = AlertDialog.Builder(this@MainActivity)

        dialog.setTitle("Delete Car?")
        dialog.setMessage("Are you sure you want to delete ${car.car_name}?")
        dialog.setPositiveButton("Yes", DialogInterface.OnClickListener { _, _ ->
            DataStore.deleteCar(position)
            adapter!!.notifyDataSetChanged()

            val mainLayout = findViewById<CoordinatorLayout>(R.id.layoutMain)
            Snackbar.make(
                mainLayout,
                "${car.car_name} deleted",
                Snackbar.LENGTH_LONG
            ).show()

            updateToolbarTitle()
        })
        .setNegativeButton("No", DialogInterface.OnClickListener { dialog, _ ->
            dialog.dismiss()
        })
        .show()
    }

    private fun updateToolbarTitle() {
        val collapsingToolbar = findViewById<Toolbar>(R.id.toolbar)

        collapsingToolbar.title = "Cars (${DataStore.cars.size})"
    }

    val getAction = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            // adapter!!.notifyDataSetChanged()
            // updateToolbarTitle()
        }
    }
}