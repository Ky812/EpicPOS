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
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import my.edu.tarc.epicpos.data.Order
import my.edu.tarc.epicpos.data.OrderAdapter
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class OrderListFragment : Fragment() {
    private lateinit var db: DatabaseReference
    private lateinit var orderRecyclerView: RecyclerView
    private lateinit var orderArrayList: ArrayList<Order>
    private val currentUser = FirebaseAuth.getInstance().currentUser!!.uid
    val SST = 0.10
    //private val database = Firebase.database("https://fypproject-bdcb3-default-rtdb.asia-southeast1.firebasedatabase.app/")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_order_list, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        orderRecyclerView = view.findViewById(R.id.orderRecyclerView)
        orderRecyclerView.layoutManager = LinearLayoutManager(this.context)
        orderRecyclerView.setHasFixedSize(true)
        orderArrayList = arrayListOf<Order>()

        val btnCalAmount = view.findViewById<Button>(R.id.btnCalAmount)
        val tvTotalPrice = view.findViewById<TextView>(R.id.tvTotalPrice)
        val cal = Calendar.getInstance()
        var year = cal.get(Calendar.YEAR)
        val month = SimpleDateFormat("MMMM")
        val month_name : String = month.format(cal.time)
        val date = SimpleDateFormat("dd-MM-yyyy")
        val date_name : String = date.format(Date())
        val time = SimpleDateFormat("ddMMyyyyhhmmssa")
        val time_name : String = "O" + time.format(Date())
        var totalAmount = 0.0
        var taxAmount = 0.0
        var total = 0.0
        var strTotal = ""
        var testingAmount = 0.0
        var trial = 0.0
        var trialTotal = 0.0

        val bundle = Bundle()
        val args = this.arguments
        val getTableNoData = args?.get("tableNumber")
        val getName = args?.get("name")
        val getMember = args?.get("member")

        db = FirebaseDatabase.getInstance("https://fypproject-bdcb3-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Customers").child("$currentUser").child("TempOrder").child("OrderDetails")

//        db.addListenerForSingleValueEvent(object : ValueEventListener{
//            override fun onDataChange(snapshot: DataSnapshot) {
//                snapshot.children.forEach {
//                    val totalPrice : String = it.child("price").value.toString()
//                    testingAmount += totalPrice.toDouble()
//                    trial = testingAmount.toDouble() * SST
//                    tvTotalPrice.text = "Total Amount : RM $testingAmount"
//                    trialTotal = trial + testingAmount
//                    Log.i("trial","$trialTotal")
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                TODO("Not yet implemented")
//            }
//
//        })

        btnCalAmount.setOnClickListener(){
            bundle.putString("tableNumber",getTableNoData.toString())
            bundle.putString("name",getName.toString())
            bundle.putString("member",getMember.toString())
            Navigation.findNavController(it).navigate(R.id.action_orderListFragment_to_confirmOrderFragment,bundle)

//            db.addListenerForSingleValueEvent(object : ValueEventListener{
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    snapshot.children.forEach {
//                        val key : String = it.key.toString()
//                        val itemName : String = it.child("itemName").value.toString()
//                        val itemPrice : String = it.child("price").value.toString()
//                        val itemQty : String = it.child("quantity").value.toString()
//                        totalAmount += itemPrice.toDouble()
//                        Log.i("total","$totalAmount")
//
//                        val dbRef = Firebase.database("https://fypproject-bdcb3-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Orders").child("$year").child("$month_name").child("${date_name}").child("$time_name")
//                        dbRef.addListenerForSingleValueEvent(object : ValueEventListener{
//                            override fun onDataChange(snapshot: DataSnapshot) {
////                            testdb.child("$key").child("itemName").setValue("$data")
//                                dbRef.child("OrderDetails").child("$key").setValue(Order("$itemName","$itemPrice","$itemQty"))
//                                dbRef.child("total").setValue("$totalAmount")
//
//                            }
//
//                            override fun onCancelled(error: DatabaseError) {
//                                TODO("Not yet implemented")
//                            }
//
//                        })
//
//                    }
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                    TODO("Not yet implemented")
//                }
//
//            })

//            val dbAmountRef = Firebase.database("https://fypproject-bdcb3-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Orders").child("$year").child("$month_name").child("${date_name}").child("$time_name").child("total")
//            dbAmountRef.addListenerForSingleValueEvent(object : ValueEventListener{
//                override fun onDataChange(snapshot: DataSnapshot) {
////                    if(snapshot.exists()){
//                        strTotal = snapshot.value.toString()
////                        taxAmount = strTotal.toDouble() * SST
////                        total = strTotal.toDouble() + taxAmount
//                        Log.i("data","$strTotal")
//
////                    }
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                    TODO("Not yet implemented")
//                }
//
//            })
        }
        getOrder()
    }

    private fun getOrder() {
        //need chg according to id
        db = FirebaseDatabase.getInstance("https://fypproject-bdcb3-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Customers").child("$currentUser").child("TempOrder").child("OrderDetails")

        db.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                orderArrayList.clear()
                if (snapshot.exists()) {
                    for (snap in snapshot.children) {
                        val eve = snap.getValue(Order::class.java)
                        orderArrayList.add(eve!!)
                    }
                    var adapter = OrderAdapter(orderArrayList)
                    orderRecyclerView.adapter = adapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.i("Error", "Not Found")
            }

        })
        //get key value
        //db = FirebaseDatabase.getInstance("https://fypproject-bdcb3-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Customers").child("$currentUser").child("TempOrder")

//        db.orderByChild("itemName").equalTo("Salmon Sushi").addListenerForSingleValueEvent(object : ValueEventListener{

    }
}