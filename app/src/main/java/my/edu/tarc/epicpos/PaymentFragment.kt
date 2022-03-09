package my.edu.tarc.epicpos

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import my.edu.tarc.epicpos.databinding.FragmentPaymentBinding
import java.text.SimpleDateFormat
import java.util.*

class PaymentFragment : Fragment() {
    private lateinit var db: DatabaseReference
    private val currentUser = FirebaseAuth.getInstance().currentUser!!.uid
    private lateinit var binding : FragmentPaymentBinding

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
            binding.tvPayment.visibility = View.GONE
            binding.orderPaymentLayout.visibility = View.GONE
            binding.btnPay.visibility = View.GONE
            binding.PaymentMethodLayout.visibility = View.VISIBLE
        }

        binding.button2.setOnClickListener {
            binding.PaymentMethodLayout.visibility = View.GONE
            binding.PaymentDetailsLayout.visibility = View.VISIBLE
            when {
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


}