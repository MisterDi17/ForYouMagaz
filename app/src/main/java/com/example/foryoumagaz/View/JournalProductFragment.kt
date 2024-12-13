package com.example.foryoumagaz.View

import JournalProductAdapter
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foryoumagaz.Adapter.ProductAdapter
import com.example.foryoumagaz.Entity.Product
import com.example.foryoumagaz.Model.DatabaseManager
import com.example.foryoumagaz.R

class JournalProductFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var btnProduct: Button
    private lateinit var searchEditText: EditText
    private lateinit var dbHelper: DatabaseManager
    private var itemList: List<Product> = emptyList()
    private lateinit var adapter: JournalProductAdapter

    private val databaseManager by lazy { DatabaseManager(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_journal_product, container, false)
        btnProduct = view.findViewById(R.id.btJournalOrder)
        btnProduct.setOnClickListener{
            parentFragmentManager.beginTransaction()
                .replace(R.id.fram, JournalFragment())
                .addToBackStack(null)
                .commit()
        }
        dbHelper = DatabaseManager(requireContext())
        recyclerView = view.findViewById(R.id.RecyclerJournal)
        itemList = dbHelper.getProducts()
        adapter = JournalProductAdapter(requireContext(), itemList, dbHelper)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        searchEditText = view.findViewById(R.id.tvPoisk2)
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
}