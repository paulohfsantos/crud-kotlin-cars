package com.example.androidexampleapp.model

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.androidexampleapp.controller.dotenv
import kotlin.math.log

class DBHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_VERSION = 2
        const val DATABASE_NAME = "cars.db"
        const val TABLE_NAME = "cars"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "car_name"
        const val COLUMN_YEAR = "year"
        const val COLUMN_PRICE = "price"
        const val COLUMN_COLOR = "color"

        const val sqlCreateCars = (
            "CREATE TABLE IF NOT EXISTS " +
            TABLE_NAME +
            "($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
            "$COLUMN_NAME TEXT," +
            "$COLUMN_YEAR INTEGER," +
            "$COLUMN_PRICE INTEGER," +
            "$COLUMN_COLOR TEXT)"
        )
        const val sqlDrop = "DROP TABLE IF EXISTS $TABLE_NAME"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val db = db ?: return

        db.beginTransaction()
        try {
            db.execSQL(sqlCreateCars)
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

        val db = db ?: return

        if (oldVersion < newVersion) {
            db.beginTransaction()

            try {
                db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
                onCreate(db)
            } finally {
                db.endTransaction()
            }
        }
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < newVersion) {
            db.beginTransaction()

            try {
                db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
            } finally {
                db.endTransaction()
            }
        }
    }

    fun getAllCars(): MutableList<Cars> {
        val cars = mutableListOf<Cars>()
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            COLUMN_NAME
        )

        with(cursor) {
            while (moveToNext()) {
                val id = getInt(getColumnIndex(COLUMN_ID))
                val name = getString(getColumnIndex(COLUMN_NAME))
                val year = getString(getColumnIndex(COLUMN_YEAR))
                val price = getInt(getColumnIndex(COLUMN_PRICE))
                val color = getString(getColumnIndex(COLUMN_COLOR))

                val car = Cars(id, name, year, color, price) // on Cars class model order

                car.id = id.toLong().toInt()
                cars.add(car)
            }
        }

        return cars
    }

    fun insertCar(car: Cars): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_NAME, car.car_name)
            put(COLUMN_YEAR, car.year)
            put(COLUMN_PRICE, car.price)
            put(COLUMN_COLOR, car.color)
        }
        var id: Long = 0
        db.beginTransaction()
        try {
            id = db.insert(TABLE_NAME, "", contentValues)
            db.setTransactionSuccessful()
        } catch (e: Exception) {
            Log.d("Error add =>", e.toString())
            e.printStackTrace()
        } finally {
            db.endTransaction()
        }

        db.close()
        return id
    }

    fun updateCar(car: Cars): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_NAME, car.car_name)
            put(COLUMN_YEAR, car.year)
            put(COLUMN_PRICE, car.price)
            put(COLUMN_COLOR, car.color)
        }

        val selection = "$COLUMN_ID = ?"
        val selectionArgs = arrayOf(car.id.toString())
        var count = 0

        db.beginTransaction()

        try {
            count = db.update(TABLE_NAME, contentValues, selection, selectionArgs)
            db.setTransactionSuccessful()
        } catch (e: Exception) {
            Log.d("Error edit =>", e.toString())
            e.printStackTrace()
        } finally {
            db.endTransaction()
        }

        db.close()
        return count
    }

    fun deleteCar(car: Cars): Int {
        val db = this.writableDatabase

        val sqlClearData = "DELETE FROM $TABLE_NAME WHERE $COLUMN_ID = ${car.id}"
        val selection = "$COLUMN_ID = ?"
        val selectionArgs = arrayOf(car.id.toString())
        var count = 0

        db.beginTransaction()

        try {
            count = db.delete(TABLE_NAME, selection, selectionArgs)
            db.setTransactionSuccessful()
        } catch (e: Exception) {
            Log.d("Error delete =>", e.toString())
            e.printStackTrace()
        } finally {
            db.endTransaction()
        }

        db.close()
        return count
    }

    fun deleteAllCars(): Int {
        val db = this.writableDatabase
        var count = 0

        db.beginTransaction()

        try {
            count = db.delete(TABLE_NAME, null, null)
            db.setTransactionSuccessful()
        } catch (e: Exception) {
            Log.d("Error delete all =>", e.toString())
            e.printStackTrace()
        } finally {
            db.endTransaction()
        }

        db.close()
        return count
    }
}