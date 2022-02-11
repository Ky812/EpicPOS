package my.edu.tarc.epicpos.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import my.edu.tarc.epicpos.R

class PaymentOrderAdapter (private val paymentOrderList : List<Order>) : RecyclerView.Adapter<PaymentOrderAdapter.paymentOrderViewHolder> () {
    class paymentOrderViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val paymentOrderDetailsName : TextView = itemView.findViewById(R.id.tvPaymentOrderDetailsName)
        val paymentOrderDetailsQty : TextView = itemView.findViewById(R.id.tvPaymentOrderDetailsQty)
        val paymentOrderDetailsPrice : TextView = itemView.findViewById(R.id.tvPaymentOrderDetailsPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): paymentOrderViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.order_payment,parent,false)

        return paymentOrderViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: paymentOrderViewHolder, position: Int) {
        val currentPosition = paymentOrderList[position]
        holder.paymentOrderDetailsName.text = currentPosition.itemName
        holder.paymentOrderDetailsQty.text = currentPosition.quantity
        holder.paymentOrderDetailsPrice.text = currentPosition.price

    }

    override fun getItemCount(): Int {
        return paymentOrderList.size
    }
}