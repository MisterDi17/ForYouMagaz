package com.example.aftomaster.Model

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        // Создание таблиц
        db.execSQL(
            "CREATE TABLE $TABLE_PRODUCT (" +
                    "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$COLUMN_NAME TEXT, " +
                    "$COLUMN_COUNT INTEGER, " +
                    "$COLUMN_VISIBLE INTEGER, " +
                    "$COLUMN_IMG INTEGER)"
        )
        db.execSQL(
            "CREATE TABLE $TABLE_JOURNAL (" +
                    "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$COLUMN_PRODUCT_ID INTEGER, " +
                    "$COLUMN_PRODUCT_COUNT INTEGER, " +
                    "$COLUMN_ORDER_DATE TEXT, " +
                    "$COLUMN_CHEK INTEGER, " +
                    "FOREIGN KEY($COLUMN_PRODUCT_ID) REFERENCES $TABLE_PRODUCT($COLUMN_ID))"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_JOURNAL")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PRODUCT")
        onCreate(db)
    }

    companion object {
        const val DATABASE_NAME = "products.db"
        const val DATABASE_VERSION = 6

        const val TABLE_PRODUCT = "Product"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_IMG = "img"
        const val COLUMN_COUNT = "count"
        const val COLUMN_VISIBLE = "visible"

        const val TABLE_JOURNAL = "Journal"
        const val COLUMN_PRODUCT_ID = "product_id"
        const val COLUMN_ORDER_DATE = "order_data"
        const val COLUMN_PRODUCT_COUNT = "product_count"
        const val COLUMN_CHEK = "chek"
    }
}