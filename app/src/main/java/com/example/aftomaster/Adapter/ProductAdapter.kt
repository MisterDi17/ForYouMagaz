package com.example.aftomaster.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.example.aftomaster.Entity.Product
import com.example.aftomaster.R

class ProductAdapter(
    private val context: Context,
    private var itemList: List<Product>,
    private val onItemClick: (Product) -> Unit
    ) : BaseAdapter() {

    inner class ViewHolder(view: View) {
        val tvTitle: TextView = view.findViewById(R.id.tvName)
        val tvCount: TextView = view.findViewById(R.id.tvCount)
        val img: ImageView = view.findViewById(R.id.img)
        val ViewImg: View = view.findViewById(R.id.ViewImg)

        fun bind(product: Product,view: View) {
            tvTitle.text = product.name
            img.setImageResource(product.img)
            view.setOnClickListener { onItemClick(product) }
            if (product.count == 0) {
                tvCount.text = "Отсутствует"
                tvCount.typeface = ResourcesCompat.getFont(context, R.font.kunstlerscript)
                ViewImg.setBackgroundResource(R.drawable.protuct_background_off) // Фон для отсутствующего товара
            } else {
                tvCount.text = (product.count).toString()
                tvCount.typeface = ResourcesCompat.getFont(context, R.font.kumar_one_cyrillic)
                ViewImg.setBackgroundResource(R.drawable.protuct_background_on) // Обычный фон
            }
        }
    }
    override fun getCount(): Int = itemList.size

    override fun getItem(position: Int): Any = itemList[position]

    override fun getItemId(position: Int): Long = itemList[position].id.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val viewHolder: ViewHolder
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_warehouse, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        viewHolder.bind(itemList[position],view)
        return view
    }

    fun updateData(newItems: List<Product>) {
        itemList = newItems
        notifyDataSetChanged()
    }
}