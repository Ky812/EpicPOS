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
import my.edu.tarc.epicpos.databinding.FragmentPaymentBinding

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
        var price = ""

        val dbRef = Firebase.database("https://fypproject-bdcb3-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Orders").child("$year").child("$month").child("$date").child("$time").child("total")

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
//                            Navigation.findNavController(it).navigate(R.id.action_membershipPaymentFragment_to_membershipFragment)
                            Toast.makeText(context,"Subscribed Successful", Toast.LENGTH_SHORT).show()
                        }
                        }
                    }
                }
                binding.rdBtnMastercard.isChecked -> {
                    binding.editCreditcardNumber.visibility = View.VISIBLE
                    binding.editCreditcardAmexNumber.visibility = View.GONE
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
//                            Navigation.findNavController(it).navigate(R.id.action_membershipPaymentFragment_to_membershipFragment)
                            Toast.makeText(context,"Subscribed Successful", Toast.LENGTH_SHORT).show()
                        }
                        }
                    }
                }
                binding.rdBtnAmex.isChecked -> {
                    binding.editCreditcardAmexNumber.visibility = View.VISIBLE
                    binding.editCreditcardNumber.visibility = View.GONE
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
//                            Navigation.findNavController(it).navigate(R.id.action_membershipPaymentFragment_to_membershipFragment)
                            Toast.makeText(context,"Subscribed Successful", Toast.LENGTH_SHORT).show()
                        }
                        }
                    }
                }
            }

        }

        return binding.root
    }


}