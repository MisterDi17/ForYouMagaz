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

class JournalProductAdapter(
    private val context: Context,
    private var productList: List<Product>,
    private val dbHelper: DatabaseManager
) : RecyclerView.Adapter<JournalProductAdapter.JournalProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JournalProductViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_journal_product, parent, false)
        return JournalProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: JournalProductViewHolder, position: Int) {
        val product = productList[position]
        holder.bind(product)
    }

    override fun getItemCount(): Int = productList.size

    inner class JournalProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val checkBox: CheckBox = itemView.findViewById(R.id.checkBox)
        private val productNameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        private val productImageView: ImageView = itemView.findViewById(R.id.img)

        fun bind(product: Product) {
            val product = dbHelper.getProductById(product.id)

            productNameTextView.text = product.name
            productImageView.setImageResource(product.img)
            checkBox.isChecked = product.isVisible == 1

            checkBox.setOnCheckedChangeListener { _, isChecked ->
                product.isVisible = if (isChecked) 1 else 0
            }
        }
    }
}