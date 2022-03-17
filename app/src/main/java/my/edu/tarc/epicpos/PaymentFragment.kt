package my.edu.tarc.epicpos

import android.content.DialogInterface
import android.os.Bundle
import android.text.InputType
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import my.edu.tarc.epicpos.data.Order
import my.edu.tarc.epicpos.data.Payment
import my.edu.tarc.epicpos.data.PaymentOrderAdapter
import my.edu.tarc.epicpos.databinding.FragmentPaymentBinding
import java.text.SimpleDateFormat
import java.util.*

class PaymentFragment : Fragment() {
    private lateinit var db: DatabaseReference
    private val currentUser = FirebaseAuth.getInstance().currentUser!!.uid
    private lateinit var binding : FragmentPaymentBinding
    private lateinit var paymentOrderRecyclerView: RecyclerView
    private lateinit var orderArrayList: ArrayList<Order>
    val SST = 0.10
    var MembershipDiscount = 0.0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_payment, container, false)


        val args = this.arguments
        val year = args?.get("year")
        val month = args?.get("month")
        val date = args?.get("date")
        val time = args?.get("time")
        val getUserType = args?.get("usertype")
        var price = ""

        val cal = Calendar.getInstance()
        var dbYear = cal.get(Calendar.YEAR)
        val dbMonth = SimpleDateFormat("MMMM")
        val month_name : String = dbMonth.format(cal.time)
        val dbDate = SimpleDateFormat("dd-MM-yyyy")
        val date_name : String = dbDate.format(Date())
        val dbTime = SimpleDateFormat("ddMMyyyyhhmmssa")
        val time_name : String = "P" + dbTime.format(Date())
        val dbStoreDateTime = SimpleDateFormat("ddMMyyhhmm")
        val dbDateTime : String = dbStoreDateTime.format(Date())


        val dbRef = Firebase.database("https://fypproject-bdcb3-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Orders").child("$year").child("$month").child("$date").child("$time").child("total")
        val dbPayment = Firebase.database("https://fypproject-bdcb3-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Payment")

        dbRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                price = snapshot.value.toString()
                binding.tvTotalNumber.text = "RM $price"
            }

            override fun onCancelled(error: DatabaseError) {
                Log.i("Error", "Not Found")
            }

        })

        binding.btnPay.setOnClickListener {
            binding.orderPaymentLayout.visibility = View.GONE
            binding.btnPay.visibility = View.GONE
            binding.PaymentMethodLayout.visibility = View.VISIBLE
        }

        binding.button2.setOnClickListener {
            binding.PaymentMethodLayout.visibility = View.GONE
            binding.PaymentDetailsLayout.visibility = View.VISIBLE
            when {
                binding.rdBtnCounter.isChecked -> {
                    binding.editCreditcardNumber.visibility = View.VISIBLE
                    binding.editCreditcardAmexNumber.visibility = View.GONE
                    binding.PaymentDetailsLayout.visibility = View.GONE
                    val cash = "Cash"
                    dbRef.addValueEventListener(object : ValueEventListener{
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    price = snapshot.value.toString()
                                    binding.tvTotalNumber.text = "RM $price"
                                    dbPayment.addListenerForSingleValueEvent(object : ValueEventListener{
                                        override fun onDataChange(snapshot: DataSnapshot) {
                                            dbPayment.push().setValue(Payment("$price","$cash","$dbDateTime","$time","$currentUser",date_name))
                                        }
                                        override fun onCancelled(error: DatabaseError) {
                                            TODO("Not yet implemented")
                                        }
                                    })
                                }
                                override fun onCancelled(error: DatabaseError) {
                                    Log.i("Error", "Not Found")
                                }
                            })
//                            Navigation.findNavController(it).navigate(R.id.action_membershipPaymentFragment_to_membershipFragment)
                    var payAmount = ""
                    val builder: AlertDialog.Builder =
                        AlertDialog.Builder(requireView().context)
                    builder.setTitle("Pay By Cash : " + "${binding.tvTotalNumber.text}")
                    builder.setMessage("Enter Pay Amount :")
                    val input = EditText(view?.context)
                    input.inputType = InputType.TYPE_CLASS_TEXT
                    input.setHint("Enter amount (By Staff)")
                    builder.setView(input)

                    builder.setPositiveButton(
                        "Pay the bill",
                        DialogInterface.OnClickListener { dialog, which ->

                                Toast.makeText(context, "Payment Successful", Toast.LENGTH_SHORT)
                                    .show()

                                dialog.cancel()

                        })

                    builder.setNegativeButton(
                        "Cancel",
                        DialogInterface.OnClickListener { dialog, which ->
                            dialog.dismiss()
                        })
                    val alertDialog: AlertDialog = builder.create()

                    alertDialog.show()


                        if(getUserType == "Customer"){
                            Navigation.findNavController(it).navigate(R.id.action_paymentFragment_to_customerFeedbackFragment)
                        }else if(getUserType == "Staff"){
                            Navigation.findNavController(it).navigate(R.id.action_paymentFragment_to_staffHomepageFragment)
                        }

                }
                binding.rdBtnVisa.isChecked -> {
                    binding.editCreditcardNumber.visibility = View.VISIBLE
                    binding.editCreditcardAmexNumber.visibility = View.GONE
                    val visa = "Visa"
                    binding.button3.setOnClickListener {
                        when{
                            TextUtils.isEmpty(binding.editCreditcardNumber.text.toString()) -> {
                                Toast.makeText(context,"Please enter credit card number.", Toast.LENGTH_SHORT).show()
                            }
                            TextUtils.isEmpty(binding.editExpiryDate.text.toString()) -> {
                                Toast.makeText(context,"Please enter credit card expiry date.",
                                    Toast.LENGTH_SHORT).show()
                            }
                            TextUtils.isEmpty(binding.editCVC.text.toString()) -> {
                                Toast.makeText(context,"Please enter credit card CVC.", Toast.LENGTH_SHORT).show()
                            }else -> {
                            dbRef.addValueEventListener(object : ValueEventListener{
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    price = snapshot.value.toString()
                                    binding.tvTotalNumber.text = "RM $price"
                            dbPayment.addListenerForSingleValueEvent(object : ValueEventListener{
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    dbPayment.push().setValue(Payment("$price","$visa","$dbDateTime","$time","$currentUser",date_name))
                                }
                                override fun onCancelled(error: DatabaseError) {
                                    TODO("Not yet implemented")
                                }
                            })
                                }
                                override fun onCancelled(error: DatabaseError) {
                                    Log.i("Error", "Not Found")
                                }
                            })
//                            Navigation.findNavController(it).navigate(R.id.action_membershipPaymentFragment_to_membershipFragment)

                            Toast.makeText(context,"Payment Successful", Toast.LENGTH_SHORT).show()
                        }
                        }
                        if(getUserType == "Customer"){
                            Navigation.findNavController(it).navigate(R.id.action_paymentFragment_to_customerFeedbackFragment)
                        }else if(getUserType == "Staff"){
                            Navigation.findNavController(it).navigate(R.id.action_paymentFragment_to_staffHomepageFragment)
                        }
                    }
                }
                binding.rdBtnMastercard.isChecked -> {
                    binding.editCreditcardNumber.visibility = View.VISIBLE
                    binding.editCreditcardAmexNumber.visibility = View.GONE
                    val mastercard = "Mastercard"
                    binding.button3.setOnClickListener {
                        when{
                            TextUtils.isEmpty(binding.editCreditcardNumber.text.toString()) -> {
                                Toast.makeText(context,"Please enter credit card number.", Toast.LENGTH_SHORT).show()
                            }
                            TextUtils.isEmpty(binding.editExpiryDate.text.toString()) -> {
                                Toast.makeText(context,"Please enter credit card expiry date.",
                                    Toast.LENGTH_SHORT).show()
                            }
                            TextUtils.isEmpty(binding.editCVC.text.toString()) -> {
                                Toast.makeText(context,"Please enter credit card CVC.", Toast.LENGTH_SHORT).show()
                            }else -> {
                            dbRef.addValueEventListener(object : ValueEventListener{
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    price = snapshot.value.toString()
                                    binding.tvTotalNumber.text = "RM $price"
                                    dbPayment.addListenerForSingleValueEvent(object : ValueEventListener{
                                        override fun onDataChange(snapshot: DataSnapshot) {
                                            dbPayment.push().setValue(Payment("$price","$mastercard","$dbDateTime","$time","$currentUser", date_name))
                                        }
                                        override fun onCancelled(error: DatabaseError) {
                                            TODO("Not yet implemented")
                                        }
                                    })
                                }
                                override fun onCancelled(error: DatabaseError) {
                                    Log.i("Error", "Not Found")
                                }
                            })
                            Toast.makeText(context,"Payment Successful", Toast.LENGTH_SHORT).show()
                        }
                        }
                        if(getUserType == "Customer"){
                            Navigation.findNavController(it).navigate(R.id.action_paymentFragment_to_customerFeedbackFragment)
                        }else if(getUserType == "Staff"){
                            Navigation.findNavController(it).navigate(R.id.action_paymentFragment_to_staffHomepageFragment)
                        }
                    }
                }
                binding.rdBtnAmex.isChecked -> {
                    binding.editCreditcardAmexNumber.visibility = View.VISIBLE
                    binding.editCreditcardNumber.visibility = View.GONE
                    val amex = "American Express"
                    binding.button3.setOnClickListener {
                        when{
                            TextUtils.isEmpty(binding.editCreditcardAmexNumber.text.toString()) -> {
                                Toast.makeText(context,"Please enter credit card number.", Toast.LENGTH_SHORT).show()
                            }
                            TextUtils.isEmpty(binding.editExpiryDate.text.toString()) -> {
                                Toast.makeText(context,"Please enter credit card expiry date.",
                                    Toast.LENGTH_SHORT).show()
                            }
                            TextUtils.isEmpty(binding.editCVC.text.toString()) -> {
                                Toast.makeText(context,"Please enter credit card CVC.", Toast.LENGTH_SHORT).show()
                            }else -> {
                            dbRef.addValueEventListener(object : ValueEventListener{
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    price = snapshot.value.toString()
                                    binding.tvTotalNumber.text = "RM $price"
                                    dbPayment.addListenerForSingleValueEvent(object : ValueEventListener{
                                        override fun onDataChange(snapshot: DataSnapshot) {
                                            dbPayment.push().setValue(Payment("$price","$amex","$dbDateTime","$time","$currentUser",date_name))
                                        }
                                        override fun onCancelled(error: DatabaseError) {
                                            TODO("Not yet implemented")
                                        }
                                    })
                                }
                                override fun onCancelled(error: DatabaseError) {
                                    Log.i("Error", "Not Found")
                                }
                            })
                            Toast.makeText(context,"Payment Successful", Toast.LENGTH_SHORT).show()
                        }
                        }
                        if(getUserType == "Customer"){
                            Navigation.findNavController(it).navigate(R.id.action_paymentFragment_to_customerFeedbackFragment)
                        }else if(getUserType == "Staff"){
                            Navigation.findNavController(it).navigate(R.id.action_paymentFragment_to_staffHomepageFragment)
                        }
                    }
                }
            }

        }

        return binding.root
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
//                            tvAmountWithTax.text = "RM $caltotalAmount"
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

        binding.btnPay.setOnClickListener {
            binding.tvContact.visibility =View.GONE
            binding.tvContact1.visibility = View.GONE
            binding.tvRestaurantName.visibility = View.GONE
            binding.tvRestaurantAddress.visibility = View.GONE
            binding.textView7.visibility = View.GONE
            binding.textView8.visibility = View.GONE
            binding.textView9.visibility = View.GONE
            binding.tvOriginAmount.visibility = View.GONE
            binding.tvSST.visibility = View.GONE
            binding.tvMembershipDiscount.visibility = View.GONE
            binding.orderPaymentLayout.visibility = View.GONE
            binding.btnPay.visibility = View.GONE
            binding.PaymentMethodLayout.visibility = View.VISIBLE
            binding.paymentOrderRecyclerView.visibility = View.GONE
            binding.tvSublinear.visibility = View.GONE
            binding.TVsstlinear.visibility = View.GONE
            binding.tvGone.visibility = View.GONE
            binding.memberLinearLayout.visibility = View.GONE
            binding.tvGone1.visibility = View.GONE
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