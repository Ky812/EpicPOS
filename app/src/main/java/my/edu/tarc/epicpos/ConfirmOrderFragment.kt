package my.edu.tarc.epicpos

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
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
import my.edu.tarc.epicpos.data.PaymentOrderAdapter
import java.text.SimpleDateFormat
import java.util.*

class ConfirmOrderFragment : Fragment() {
    private lateinit var db: DatabaseReference
    private lateinit var paymentOrderRecyclerView: RecyclerView
    private lateinit var orderArrayList: ArrayList<Order>
    private val currentUser = FirebaseAuth.getInstance().currentUser!!.uid
    val SST = 0.10
    var MembershipDiscount = 0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_confirm_order, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        paymentOrderRecyclerView = view.findViewById(R.id.paymentOrderRecyclerView)
        paymentOrderRecyclerView.layoutManager = LinearLayoutManager(this.context)
        paymentOrderRecyclerView.setHasFixedSize(true)
        orderArrayList = arrayListOf<Order>()

        val tvOriginAmount = view.findViewById<TextView>(R.id.tvOriginAmount)
        val tvAmountWithTax = view.findViewById<TextView>(R.id.tvAmountWithTax)
        val btnConfirmOrder = view.findViewById<Button>(R.id.btnConfirmOrder)
        val membershipDiscountLayout = view.findViewById<LinearLayout>(R.id.memberLinearLayout)
        val tvMembershipDiscount = view.findViewById<TextView>(R.id.tvMembershipDiscount)
        val tvSST = view.findViewById<TextView>(R.id.tvSST)

        db = FirebaseDatabase.getInstance("https://fypproject-bdcb3-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users").child("$currentUser").child("TempOrder").child("OrderDetails")

        val cal = Calendar.getInstance()
        var year = cal.get(Calendar.YEAR)
        val month = SimpleDateFormat("MMMM")
        val month_name : String = month.format(cal.time)
        val date = SimpleDateFormat("dd-MM-yyyy")
        val date_name : String = date.format(Date())
        val time = SimpleDateFormat("ddMMyyyyhhmmssa")
        val time_name : String = "O" + time.format(Date())
        val dbStoreDateTime = SimpleDateFormat("ddMMyyhhmm")
        val dbDateTime : String = dbStoreDateTime.format(Date())
        var caltotalAmount = 0.0
        var calTaxAmount = 0.0
        var calSubtotal = 0.0
        var calMembershipDiscount = 0.0

        var subTotal = 0.0
        var taxAmount = 0.0
        var totalAmount = 0.0
        var membershipDiscount = 0.0

        val bundle = Bundle()
        val args = this.arguments
        val getTableNoData = args?.get("tableNumber")
        val getName = args?.get("name")
        val getMember = args?.get("member")
        val getUserType = args?.get("usertype")

        val dbRef = Firebase.database("https://fypproject-bdcb3-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Orders").child("$year").child("$month_name").child("${date_name}").child("$time_name")
        bundle.putString("year", year.toString())
        bundle.putString("month",month_name)
        bundle.putString("date",date_name)
        bundle.putString("time",time_name)
        bundle.putString("usertype",getUserType.toString())


        getOrderPayment()

        db.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    val totalPrice : String = it.child("price").value.toString()
                    calSubtotal += totalPrice.toDouble()
                    if(getMember == "Non-Membership"){
                        calTaxAmount = calSubtotal * SST
                        caltotalAmount = calSubtotal  + calTaxAmount
                        tvSST.text = "RM ${calTaxAmount.toFloat()}"
                        tvOriginAmount.text = "RM $calSubtotal"
                        tvAmountWithTax.text = "RM $caltotalAmount"
                    }else{
                        if(calSubtotal >= 150.00){
                            MembershipDiscount = 0.05
                            calMembershipDiscount = calSubtotal * MembershipDiscount
                            calTaxAmount = calSubtotal * SST
                            caltotalAmount = calSubtotal - calMembershipDiscount + calTaxAmount
                            tvSST.text = "RM ${calTaxAmount.toFloat()}"
                            tvOriginAmount.text = "RM $calSubtotal"
                            tvAmountWithTax.text = "RM $caltotalAmount"
                            membershipDiscountLayout.visibility = View.VISIBLE
                            tvMembershipDiscount.text = "RM $calMembershipDiscount"
                        }else{
                            MembershipDiscount = 0.0
                            calTaxAmount = calSubtotal * SST
                            caltotalAmount = calSubtotal  + calTaxAmount
                            tvSST.text = "RM ${calTaxAmount.toFloat()}"
                            tvOriginAmount.text = "RM $calSubtotal"
                            tvAmountWithTax.text = "RM $caltotalAmount"
                            membershipDiscountLayout.visibility = View.VISIBLE
                            tvMembershipDiscount.text = "RM $calMembershipDiscount"
                        }
                    }
//                    Log.i("trial","$totalAmount")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        btnConfirmOrder.setOnClickListener(){

            db.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach {
                        val key : String = it.key.toString()
                        val itemName : String = it.child("itemName").value.toString()
                        val itemPrice : String = it.child("price").value.toString()
                        val itemQty : String = it.child("quantity").value.toString()
                        subTotal += itemPrice.toDouble()
                        if(getMember == "Non-Membership"){
                            taxAmount = subTotal * SST
                            totalAmount = subTotal + taxAmount

                            //Log.i("total","$totalAmount")

                            dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
//                            testdb.child("$key").child("itemName").setValue("$data")
                                    dbRef.child("OrderDetails").child("$key").setValue(Order("$itemName","$itemPrice","$itemQty"))
                                    dbRef.child("total").setValue("$totalAmount")
                                    dbRef.child("orderDateTime").setValue("$dbDateTime")
                                    dbRef.child("tableNo").setValue("$getTableNoData")
                                    dbRef.child("customerID").setValue("$currentUser")
                                    dbRef.child("customerName").setValue("$getName")
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    TODO("Not yet implemented")
                                }

                            })

                        }else{
                            if(subTotal >= 150.00){
                                MembershipDiscount = 0.05
                                taxAmount = subTotal * SST
                                membershipDiscount = subTotal * MembershipDiscount
                                totalAmount = subTotal + taxAmount - membershipDiscount
                                //Log.i("total","$totalAmount")

                                dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
//                            testdb.child("$key").child("itemName").setValue("$data")
                                        dbRef.child("OrderDetails").child("$key").setValue(Order("$itemName","$itemPrice","$itemQty"))
                                        dbRef.child("total").setValue("$totalAmount")
                                        dbRef.child("orderDateTime").setValue("$dbDateTime")
                                        dbRef.child("tableNo").setValue("$getTableNoData")
                                        dbRef.child("customerID").setValue("$currentUser")
                                        dbRef.child("customerName").setValue("$getName")
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        TODO("Not yet implemented")
                                    }

                                })
                            }else{
                                MembershipDiscount = 0.0
                                taxAmount = subTotal * SST
                                totalAmount = subTotal + taxAmount

                                //Log.i("total","$totalAmount")

                                dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
//                            testdb.child("$key").child("itemName").setValue("$data")
                                        dbRef.child("OrderDetails").child("$key").setValue(Order("$itemName","$itemPrice","$itemQty"))
                                        dbRef.child("total").setValue("$totalAmount")
                                        dbRef.child("orderDateTime").setValue("$dbDateTime")
                                        dbRef.child("tableNo").setValue("$getTableNoData")
                                        dbRef.child("customerID").setValue("$currentUser")
                                        dbRef.child("customerName").setValue("$getName")
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        TODO("Not yet implemented")
                                    }

                                })
                            }
                        }

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
            val delRef = Firebase.database("https://fypproject-bdcb3-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users").child("$currentUser").child("TempOrder")

            delRef.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    var rmv = snapshot.ref.removeValue()
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w("TAG", "Error Delete document")
                }

            })

            Navigation.findNavController(it).navigate(R.id.action_confirmOrderFragment_to_paymentFragment,bundle)

        }
    }
    private fun getOrderPayment(){
        db = FirebaseDatabase.getInstance("https://fypproject-bdcb3-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users").child("$currentUser").child("TempOrder").child("OrderDetails")
        db.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                orderArrayList.clear()
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
                Log.i("Error", "Not Found")
            }

        })
    }
}