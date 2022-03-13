package my.edu.tarc.epicpos

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import my.edu.tarc.epicpos.data.OrderHistory
import my.edu.tarc.epicpos.data.OrderHistoryAdapter

class OrderHistoryListFragment : Fragment() {
    private lateinit var db : DatabaseReference
    private lateinit var orderHistoryRecyclerView : RecyclerView
    private lateinit var orderHistoryArrayList: ArrayList<OrderHistory>
    private val currentUser = FirebaseAuth.getInstance().currentUser!!.uid

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order_history_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        orderHistoryRecyclerView = view.findViewById(R.id.orderHistoryIdRecyclerView)
        orderHistoryRecyclerView.layoutManager = LinearLayoutManager(this.context)
        orderHistoryRecyclerView.setHasFixedSize(true)
        orderHistoryArrayList = arrayListOf<OrderHistory>()

        getOrderHistory()
    }

    private fun getOrderHistory() {
        db = FirebaseDatabase.getInstance("https://fypproject-bdcb3-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users").child("$currentUser").child("OrderHistory")

        db.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(snap in snapshot.children){
                        val eve = snap.getValue(OrderHistory::class.java)
                        orderHistoryArrayList.add(eve!!)
                    }
                    var adapter = OrderHistoryAdapter(orderHistoryArrayList)
                    orderHistoryRecyclerView.adapter = adapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.i("Error", "No Order History")
            }

        })

    }
}