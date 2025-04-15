package com.example.aftomaster.View

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
import com.example.aftomaster.Adapter.ProductAdapter
import com.example.aftomaster.Entity.Journal
import com.example.aftomaster.Entity.Product
import com.example.aftomaster.Model.DatabaseManager
import com.example.aftomaster.R
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
        //dbHelper.addProduct(Product(0,"Моторное масло 5W-30",R.drawable.img_motoroil_5w30,20))
        dbHelper.addProduct(Product(1,"Тормозные колодки",R.drawable.img_brake_pads,15))
        dbHelper.addProduct(Product(2,"Воздушный фильтр",R.drawable.img_brake_pads,15))

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