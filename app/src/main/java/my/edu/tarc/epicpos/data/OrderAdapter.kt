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
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import my.edu.tarc.epicpos.R

class OrderAdapter (private val orderList : List<Order>) : RecyclerView.Adapter<OrderAdapter.orderViewHolder>() {
    val currentUser = FirebaseAuth.getInstance().currentUser!!.uid
    val database = Firebase.database("https://fypproject-bdcb3-default-rtdb.asia-southeast1.firebasedatabase.app/")
    val ref = FirebaseDatabase.getInstance("https://fypproject-bdcb3-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users").child("$currentUser").child("TempOrder").child("OrderDetails")
    var totalAmount = 0.0

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
//
//        ref.addListenerForSingleValueEvent(object : ValueEventListener{
//            override fun onDataChange(snapshot: DataSnapshot) {
//                snapshot.children.forEach {
//                    val itemPrice : String = it.child("price").value.toString()
//                    totalAmount += itemPrice.toDouble()
//                    Log.i("testing","$totalAmount")
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                TODO("Not yet implemented")
//            }
//
//        })

        holder.remove.setOnClickListener(){
            val getKey = database.getReference("Users").child("$currentUser").child("TempOrder").child("OrderDetails")

            getKey.orderByChild("itemName").equalTo("${currentPosition.itemName}").addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach {
                        val key : String = it.key.toString()
                        val removeRef = database.getReference("Users").child("$currentUser").child("TempOrder").child("OrderDetails").child("$key")

                        Log.i("key", "$key")

                        removeRef.addListenerForSingleValueEvent(object : ValueEventListener{
                            override fun onDataChange(snapshot: DataSnapshot) {
                                var rmv = snapshot.ref.removeValue()
                            }
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    //for (snap in snapshot.children){
//                        var rmv = snapshot.ref.key
//                        Log.i("rmv","$rmv")
//                    //}
//                }

                            override fun onCancelled(error: DatabaseError) {
                                Log.w("TAG", "Error Delete document")
                            }

                        })

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

        }

    }

    override fun getItemCount(): Int {
        return orderList.size
    }

}