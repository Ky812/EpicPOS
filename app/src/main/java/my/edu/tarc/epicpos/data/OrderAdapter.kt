package my.edu.tarc.epicpos.data

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import my.edu.tarc.epicpos.R

class OrderAdapter (private val orderList : List<Order>) : RecyclerView.Adapter<OrderAdapter.orderViewHolder>() {
    val currentUser = FirebaseAuth.getInstance().currentUser!!.uid
    val database = Firebase.database("https://fypproject-bdcb3-default-rtdb.asia-southeast1.firebasedatabase.app/")
    val ref = database.getReference("Customers").child("$currentUser").child("TempOrder")

    class orderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val orderDetailsName : TextView = itemView.findViewById(R.id.tvOrderDetailsName)
        val orderDetailsQty : TextView = itemView.findViewById(R.id.tvOrderDetailsQty)
        val orderDetailsPrice : TextView = itemView.findViewById(R.id.textView6)
        val remove : Button = itemView.findViewById(R.id.button4)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): orderViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.order,parent,false)

        return orderViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: orderViewHolder, position: Int) {
        val currentPosition = orderList[position]
        holder.orderDetailsName.text = currentPosition.itemName
        holder.orderDetailsQty.text = currentPosition.quantity
        holder.orderDetailsPrice.text = "RM" + currentPosition.price
        //need to chg
        val removeRef = database.getReference("Customers").child("$currentUser").child("TempOrder").child("-MuRuYBhd_xqZ5-W9hUG")

        holder.remove.setOnClickListener(){
            removeRef.addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    var rmv = snapshot.ref.removeValue()
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w("TAG", "Error Delete document")
                }

            })
        }

    }

    override fun getItemCount(): Int {
        return orderList.size
    }

}