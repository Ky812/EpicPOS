package my.edu.tarc.epicpos

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import my.edu.tarc.epicpos.databinding.FragmentMembershipBinding
import java.text.SimpleDateFormat
import java.util.*

class MembershipFragment : Fragment() {
    private lateinit var binding : FragmentMembershipBinding
    private lateinit var db: DatabaseReference
    val currentUser = FirebaseAuth.getInstance().currentUser!!.uid

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_membership,container,false)
//        db = FirebaseDatabase.getInstance("https://fypproject-bdcb3-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Customers").child("$currentUser")
        db = FirebaseDatabase.getInstance("https://fypproject-bdcb3-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users").child("$currentUser")

        var membership = ""
        val cal = Calendar.getInstance()
        val month = SimpleDateFormat("ddMMMyyyy")
        cal.add(Calendar.MONTH,3)
        val membershipExpire : String = month.format(cal.time)

        db.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    membership = snapshot.child("membership").value.toString()
                    when(snapshot.child("membership").value.toString()){
                        "Non-Membership" ->{
                            binding.textView4.text = "Memebership Status : Non Membership"
                            binding.btnSubscribe.setOnClickListener {

                                Navigation.findNavController(it).navigate(R.id.action_membershipFragment_to_membershipPaymentFragment)
                            }
                        }else -> {
                            binding.textView4.text = "Memebership Status : Valid Until $membership"
                            binding.btnSubscribe.text = "Subscribed"
                            binding.btnSubscribe.isClickable = false
                        }
                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("TAG", "Read Failed")
            }

        })

        binding.button8.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_membershipFragment_to_customerHomepageFragment)

        }


        return binding.root
    }


}