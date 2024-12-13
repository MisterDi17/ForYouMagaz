package com.example.foryoumagaz.View

import JournalAdapter
import android.annotation.SuppressLint
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
import com.example.foryoumagaz.Entity.Journal
import com.example.foryoumagaz.Model.DatabaseManager
import com.example.foryoumagaz.R
class JournalFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var btnJournalProduct: Button
    private lateinit var searchEditText: EditText
    private lateinit var adapter: JournalAdapter
    private lateinit var dbHelper: DatabaseManager
    private var journalList: List<Journal> = emptyList()

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_journal, container, false)

        recyclerView = view.findViewById(R.id.RecyclerJournal)
        btnJournalProduct = view.findViewById(R.id.btnJournalProduct)
        btnJournalProduct.setOnClickListener{
            parentFragmentManager.beginTransaction()
                .replace(R.id.fram, JournalProductFragment())
                .addToBackStack(null)
                .commit()
        }
        dbHelper = DatabaseManager(requireContext())
        journalList = dbHelper.getJournals()  // Получаем журналы из базы данных

        adapter = JournalAdapter(requireContext(), journalList, dbHelper)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        searchEditText = view.findViewById(R.id.tvPoisk3)
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
            }
            override fun afterTextChanged(editable: Editable?) {
                journalList = dbHelper.getJournals()

                val query = editable.toString().trim().toLowerCase()
                val filteredList = journalList.filter { journal ->
                    val productName = dbHelper.getProductNameById(journal.product_id).toLowerCase()
                    productName.contains(query)
                }
                updateItemList(filteredList)
            }
        })
        return view
    }
    fun updateItemList(newItems: List<Journal>) {
        journalList = newItems
        adapter.updateData(journalList)
    }
}