package com.example.androidexampleapp.controller

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import io.github.cdimascio.dotenv.dotenv
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.Menu
import android.view.MenuItem
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
            val intent = Intent(this, CarsActivity::class.java).apply {
                putExtra("car", true)
            }
            getAction.launch(intent)
        }

        val rcvCars = findViewById<RecyclerView>(R.id.recyclerViewCars)
        val layoutManager = LinearLayoutManager(this)

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
                            putExtra("type", 2)
                            putExtra("position", position)
                        }
                        getAction.launch(intent)
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

    private fun updateToolbarTitle() {
        val collapsingToolbar = findViewById<Toolbar>(R.id.toolbar)
        collapsingToolbar.title = "Cars (${DataStore.cars.size})"
    }

    private fun deleteDialogHandler(car: Cars, position: Int) {
        val dialog = AlertDialog.Builder(this@MainActivity)

        dialog.setTitle("Delete Car?")
        dialog.setMessage("Are you sure you want to delete ${car.car_name}?")
        dialog.setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, which ->
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

    fun confirmClearList() {
        val dialog = AlertDialog.Builder(this@MainActivity)

        dialog.setTitle("Clear List?")
        dialog.setMessage("Are you sure you want to clear the list?")
        dialog.setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, which ->
            DataStore.clearCarList()
            adapter!!.notifyDataSetChanged()

            val mainLayout = findViewById<CoordinatorLayout>(R.id.layoutMain)
            Snackbar.make(
                mainLayout,
                "List cleared",
                Snackbar.LENGTH_LONG
            ).show()

            updateToolbarTitle()
        })
        .setNegativeButton("No", DialogInterface.OnClickListener { dialog, _ ->
            dialog.dismiss()
        }).show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.clearList -> {
                confirmClearList()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    // startActivityForResult is deprecated, using this method instead
    val getAction = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            adapter!!.notifyDataSetChanged()
            updateToolbarTitle()
        }
    }

    // TODO: implement later
    private fun onActivityResult(requestCode: Int, result: ActivityResult) {
        if(result.resultCode == Activity.RESULT_OK) {
            adapter!!.notifyDataSetChanged()
            updateToolbarTitle()
        }
    }
}