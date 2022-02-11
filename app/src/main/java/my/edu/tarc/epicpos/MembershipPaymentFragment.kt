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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import my.edu.tarc.epicpos.databinding.FragmentMembershipPaymentBinding
import java.text.SimpleDateFormat
import java.util.*


class MembershipPaymentFragment : Fragment() {
    private lateinit var binding : FragmentMembershipPaymentBinding
    private lateinit var db: DatabaseReference
    val currentUser = FirebaseAuth.getInstance().currentUser!!.uid
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_membership_payment, container, false)

        binding.button.setOnClickListener {
            binding.textView3.visibility = View.GONE
            binding.MembershipPaymentLayout.visibility = View.GONE
            binding.button.visibility = View.GONE
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
                                Toast.makeText(context,"Please enter credit card number.",Toast.LENGTH_SHORT).show()
                            }
                            TextUtils.isEmpty(binding.editExpiryDate.text.toString()) -> {
                                Toast.makeText(context,"Please enter credit card expiry date.",Toast.LENGTH_SHORT).show()
                            }
                            TextUtils.isEmpty(binding.editCVC.text.toString()) -> {
                                Toast.makeText(context,"Please enter credit card CVC.",Toast.LENGTH_SHORT).show()
                            }else -> {
                            updateMembershipStatus()
                            Navigation.findNavController(it).navigate(R.id.action_membershipPaymentFragment_to_membershipFragment)
                            Toast.makeText(context,"Subscribed Successful",Toast.LENGTH_SHORT).show()
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
                                Toast.makeText(context,"Please enter credit card number.",Toast.LENGTH_SHORT).show()
                            }
                            TextUtils.isEmpty(binding.editExpiryDate.text.toString()) -> {
                                Toast.makeText(context,"Please enter credit card expiry date.",Toast.LENGTH_SHORT).show()
                            }
                            TextUtils.isEmpty(binding.editCVC.text.toString()) -> {
                                Toast.makeText(context,"Please enter credit card CVC.",Toast.LENGTH_SHORT).show()
                            }else -> {
                            updateMembershipStatus()
                            Navigation.findNavController(it).navigate(R.id.action_membershipPaymentFragment_to_membershipFragment)
                            Toast.makeText(context,"Subscribed Successful",Toast.LENGTH_SHORT).show()
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
                                Toast.makeText(context,"Please enter credit card number.",Toast.LENGTH_SHORT).show()
                            }
                            TextUtils.isEmpty(binding.editExpiryDate.text.toString()) -> {
                                Toast.makeText(context,"Please enter credit card expiry date.",Toast.LENGTH_SHORT).show()
                            }
                            TextUtils.isEmpty(binding.editCVC.text.toString()) -> {
                                Toast.makeText(context,"Please enter credit card CVC.",Toast.LENGTH_SHORT).show()
                            }else -> {
                            updateMembershipStatus()
                            Navigation.findNavController(it).navigate(R.id.action_membershipPaymentFragment_to_membershipFragment)
                            Toast.makeText(context,"Subscribed Successful",Toast.LENGTH_SHORT).show()
                        }
                        }
                    }
                }
            }

        }
        return binding.root
    }

    private fun updateMembershipStatus(){
        db = FirebaseDatabase.getInstance("https://fypproject-bdcb3-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Customers").child("$currentUser")

        val cal = Calendar.getInstance()
        val month = SimpleDateFormat("ddMMMyyyy")
        cal.add(Calendar.MONTH,3)
        val membershipExpire : String = month.format(cal.time)

        db.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                db.child("membership").setValue("$membershipExpire")
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w("TAG", "Update Failed")
            }
        })
    }
}