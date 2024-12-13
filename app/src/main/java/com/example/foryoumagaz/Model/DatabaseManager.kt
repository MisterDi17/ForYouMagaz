package com.example.foryoumagaz.Model

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.foryoumagaz.Entity.Journal
import com.example.foryoumagaz.Entity.Product

class DatabaseManager(context: Context) {
    private val dbHelper = DatabaseHelper(context)

    fun addProduct(product: Product): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_NAME, product.name)
            put(DatabaseHelper.COLUMN_COUNT, product.count)
            put(DatabaseHelper.COLUMN_IMG, product.img)
        }
        return db.insert(DatabaseHelper.TABLE_PRODUCT, null, values)
    }

    fun getProducts(): List<Product> {
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.query(DatabaseHelper.TABLE_PRODUCT, null, null, null, null, null, null)
        val products = mutableListOf<Product>()
        while (cursor.moveToNext()) {
            products.add(
                Product(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID)),
                    name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAME)),
                    count = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_COUNT)),
                    isVisible = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_VISIBLE)),
                    img = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_IMG)))
            )
        }
        cursor.close()
        return products
    }

    fun addJournal(journal: Journal): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_PRODUCT_ID, journal.product_id)
            put(DatabaseHelper.COLUMN_PRODUCT_COUNT, journal.product_count)
            put(DatabaseHelper.COLUMN_ORDER_DATE, journal.order_data)
        }
        return db.insert(DatabaseHelper.TABLE_JOURNAL, null, values)
    }

    fun getJournals(): List<Journal> {
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.query(DatabaseHelper.TABLE_JOURNAL, null, null, null, null, null, null)
        val journal = mutableListOf<Journal>()
        while (cursor.moveToNext()) {
            journal.add(
                Journal(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID)),
                    product_id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_ID)),
                    order_data = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ORDER_DATE)),
                    chek = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CHEK)),
                    product_count = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_COUNT)))
            )
        }
        cursor.close()
        return journal
    }
    fun getProductById(productId: Int): Product {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM Product WHERE id = ?", arrayOf(productId.toString()))
        cursor.moveToFirst()
        val product = Product(
            id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
            name = cursor.getString(cursor.getColumnIndexOrThrow("name")),
            img = cursor.getInt(cursor.getColumnIndexOrThrow("img")),
            count = cursor.getInt(cursor.getColumnIndexOrThrow("count")),
            isVisible = cursor.getInt(cursor.getColumnIndexOrThrow("visible"))
        )
        cursor.close()
        return product
    }
    fun updateProductQuantity(productId: Int, newQuantity: Int) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("count", newQuantity)
        }
        db.update("Product", values, "id = ?", arrayOf(productId.toString()))
    }
    fun updateJournalCheck(journalId: Int, checkValue: Int) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("chek", checkValue)
        }
        db.update("Journal", values, "id = ?", arrayOf(journalId.toString()))
    }
    fun updateProductCheck(productId: Int, checkValue: Int) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("visible", checkValue)
        }
        db.update("Product", values, "id = ?", arrayOf(productId.toString()))
    }
    fun getProductNameById(productId: Int): String {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT name FROM Product WHERE id = ?", arrayOf(productId.toString()))
        var productName = ""
        if (cursor.moveToFirst()) {
            productName = cursor.getString(cursor.getColumnIndexOrThrow("name"))
        }
        cursor.close()
        return productName
    }
}