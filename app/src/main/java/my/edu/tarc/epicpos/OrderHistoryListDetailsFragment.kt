package my.edu.tarc.epicpos

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import my.edu.tarc.epicpos.data.Order
import my.edu.tarc.epicpos.data.PaymentOrderAdapter
import java.util.ArrayList


class OrderHistoryListDetailsFragment : Fragment() {
    private lateinit var db : DatabaseReference
    private val currentUser = FirebaseAuth.getInstance().currentUser!!.uid
    private lateinit var paymentOrderRecyclerView: RecyclerView
    private lateinit var orderArrayList: ArrayList<Order>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order_history_list_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        paymentOrderRecyclerView = view.findViewById(R.id.OrderHistoryDetailsRecyclerView)
        paymentOrderRecyclerView.layoutManager = LinearLayoutManager(this.context)
        paymentOrderRecyclerView.setHasFixedSize(true)
        orderArrayList = arrayListOf<Order>()

        val args = this.arguments
        val getOrderHistoryID = args?.get("orderHistoryId")
        val dbref = FirebaseDatabase.getInstance("https://fypproject-bdcb3-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users").child("$currentUser").child("OrderHistory").child("$getOrderHistoryID")
        val tvOrderID = view.findViewById<TextView>(R.id.tvOrderID)
        val tvDateTime = view.findViewById<TextView>(R.id.tvDateTime)
        val tvOrderTableNo = view.findViewById<TextView>(R.id.tvOrderTableNo)
        val tvOrderTotal = view.findViewById<TextView>(R.id.tvOrderTotal)


        dbref.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                tvOrderID.text = "Order ID : "+ snapshot.child("orderHistoryID").value.toString()
                tvDateTime.text = "DateTime : " +snapshot.child("orderDateTime").value.toString()
                tvOrderTableNo.text = "Table No : " + snapshot.child("tableNo").value.toString()
                tvOrderTotal.text= "Total : RM" + snapshot.child("total").value.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        db = FirebaseDatabase.getInstance("https://fypproject-bdcb3-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users").child("$currentUser").child("OrderHistory").child("$getOrderHistoryID").child("OrderDetails")

        db.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (snap in snapshot.children) {
                        val eve = snap.getValue(Order::class.java)
                        orderArrayList.add(eve!!)
                    }
                    var adapter = PaymentOrderAdapter(orderArrayList)
                    paymentOrderRecyclerView.adapter = adapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.i("Error", "No Order History Found")
            }

        })

    }


}