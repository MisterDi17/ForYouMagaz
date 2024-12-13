package com.example.foryoumagaz.View

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.GridView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.foryoumagaz.Adapter.ProductAdapter
import com.example.foryoumagaz.Entity.Journal
import com.example.foryoumagaz.Entity.Product
import com.example.foryoumagaz.Model.DatabaseManager
import com.example.foryoumagaz.R
import com.google.android.material.button.MaterialButton
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ProductFragment : Fragment() {

    private lateinit var gridView: GridView
    private lateinit var dbHelper: DatabaseManager
    private lateinit var adapter: ProductAdapter
    private lateinit var searchEditText: EditText
    private var itemList: List<Product> = emptyList() // Список товаров

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_product, container, false)
        searchEditText = view.findViewById(R.id.tvPoisk)
        gridView = view.findViewById(R.id.gvProtuct)
        dbHelper = DatabaseManager(requireContext())
        /*
        dbHelper.addProduct(Product(0,"Хлеб Бородинский",R.drawable.img_borodinsky_bread,35))
        dbHelper.addProduct(Product(1,"Органические яйца",R.drawable.img_organic_eggs,0))
        dbHelper.addProduct(Product(2,"Молоко",R.drawable.img_milk,546))
        dbHelper.addProduct(Product(3,"Твердый сыр",R.drawable.img_hard_cheese,546))
        dbHelper.addProduct(Product(4,"Куриные грудки",R.drawable.img_chicken_breasts,346))
        dbHelper.addProduct(Product(5,"Картофель",R.drawable.img_potato,12))
        dbHelper.addProduct(Product(6,"Паста",R.drawable.img_pasta,435))
        dbHelper.addProduct(Product(7,"Овощной микс",R.drawable.img_vegetable_mix,564))
        dbHelper.addProduct(Product(8,"Ягодый микс",R.drawable.img_berries_packaging,35))
        dbHelper.addProduct(Product(9,"Яблоки",R.drawable.img_apples,435))
        dbHelper.addProduct(Product(10,"Орехи",R.drawable.img_nuts,346))
        dbHelper.addProduct(Product(11,"Кофе в зернах",R.drawable.img_coffee_beans,346))
        dbHelper.addProduct(Product(12,"Оливковое масло",R.drawable.img_olive_oil,743))
        dbHelper.addProduct(Product(13,"Кефир",R.drawable.img_kefir,435))
        dbHelper.addProduct(Product(14,"Шоколад",R.drawable.img_milka_chocolate,345))
        */
        itemList = dbHelper.getProducts().filter{it.isVisible == 0}

        adapter = ProductAdapter(requireContext(), itemList) { item ->
            showProductDialog(item)
        }
        gridView.adapter = adapter

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
            }
            override fun afterTextChanged(editable: Editable?) {
                itemList = dbHelper.getProducts()
                val query = editable.toString().trim().toLowerCase()
                val filteredList = itemList.filter { it.name.toLowerCase().contains(query) }
                updateItemList(filteredList)
            }
        })
        return view
    }


    fun updateItemList(newItems: List<Product>) {
        itemList = newItems
        adapter.updateData(itemList)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingInflatedId")
    private fun showProductDialog(product: Product) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_order, null)
        val dialog = AlertDialog.Builder(requireContext(), R.style.TransparentDialog)
            .setView(dialogView)
            .create()

        val tvProduct = dialogView.findViewById<TextView>(R.id.tvProduct)
        val imgProduct = dialogView.findViewById<ImageView>(R.id.img)
        val edCount = dialogView.findViewById<EditText>(R.id.edCount)
        val btnOrder = dialogView.findViewById<MaterialButton>(R.id.btnOrder)
        val btnCancel = dialogView.findViewById<Button>(R.id.btnCancel)

        tvProduct.text = product.name
        imgProduct.setImageResource(product.img)
        btnOrder.setOnClickListener {
            val count = edCount.text.toString().toIntOrNull() ?: 1
            Toast.makeText(requireContext(), "Заказано $count шт. ${product.name}", Toast.LENGTH_SHORT).show()
            dbHelper.addJournal(Journal(1,product.id, count, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))) )
            dialog.dismiss()
        }

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }


}