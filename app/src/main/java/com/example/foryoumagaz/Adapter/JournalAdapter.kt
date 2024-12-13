import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.foryoumagaz.Entity.Journal
import com.example.foryoumagaz.Entity.Product
import com.example.foryoumagaz.Model.DatabaseManager
import com.example.foryoumagaz.R

class JournalAdapter(
    private val context: Context,
    private var journalList: List<Journal>,
    private val dbHelper: DatabaseManager
) : RecyclerView.Adapter<JournalAdapter.JournalViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JournalViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_journal, parent, false)
        return JournalViewHolder(view)
    }

    override fun onBindViewHolder(holder: JournalViewHolder, position: Int) {
        val journal = journalList[position]
        holder.bind(journal)
    }

    override fun getItemCount(): Int = journalList.size

    inner class JournalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val checkBox: CheckBox = itemView.findViewById(R.id.checkBox)
        private val productCountTextView: TextView = itemView.findViewById(R.id.quantityTextView)
        private val productNameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        private val orderDateTextView: TextView = itemView.findViewById(R.id.orderDateTextView)
        private val productImageView: ImageView = itemView.findViewById(R.id.img)

        fun bind(journal: Journal) {
            val product = dbHelper.getProductById(journal.product_id)

            productCountTextView.text = journal.product_count.toString()
            orderDateTextView.text = journal.order_data
            productNameTextView.text = product.name
            productImageView.setImageResource(product.img)

            checkBox.setOnCheckedChangeListener(null) // Убираем слушатель для предотвращения "лишнего" вызова
            checkBox.isChecked = journal.chek == 1

            checkBox.setOnCheckedChangeListener { _, isChecked ->
                handleCheckBoxChange(journal, product, isChecked)
            }
        }

        private fun handleCheckBoxChange(journal: Journal, product: Product, isChecked: Boolean) {
            journal.chek = if (isChecked) 1 else 0
            dbHelper.updateJournalCheck(journal.id, journal.chek)

            val updatedQuantity = if (isChecked) {
                product.count + journal.product_count
            } else {
                maxOf(0, product.count - journal.product_count)
            }
            dbHelper.updateProductQuantity(journal.product_id, updatedQuantity)

            notifyItemChanged(adapterPosition)
        }
    }
    fun updateData(newItems: List<Journal>) {
        journalList = newItems
        notifyDataSetChanged()
    }
}