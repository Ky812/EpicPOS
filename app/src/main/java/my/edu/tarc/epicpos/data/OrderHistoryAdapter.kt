package my.edu.tarc.epicpos.data

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import my.edu.tarc.epicpos.R

class OrderHistoryAdapter (private val orderHistoryList : List<OrderHistory>) : RecyclerView.Adapter<OrderHistoryAdapter.orderHistoryViewHolder> (){
    class orderHistoryViewHolder (itemview : View) : RecyclerView.ViewHolder(itemview){
        val orderHistoryId : TextView = itemview.findViewById(R.id.tvOrderHistoryID)
        val btnView : Button = itemview.findViewById(R.id.btnView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): orderHistoryViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.orderhistory,parent,false)

        return orderHistoryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: orderHistoryViewHolder, position: Int) {
        val currentPosition = orderHistoryList[position]
        val bundle = Bundle()
        holder.orderHistoryId.text = currentPosition.orderHistoryID

        holder.btnView.setOnClickListener {
            bundle.putString("orderHistoryId",currentPosition.orderHistoryID)
            Navigation.findNavController(it).navigate(R.id.action_orderHistoryListFragment_to_orderHistoryListDetailsFragment,bundle)
        }
    }

    override fun getItemCount(): Int {
        return orderHistoryList.size
    }
}