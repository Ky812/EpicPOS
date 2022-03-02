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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import my.edu.tarc.epicpos.databinding.FragmentStaffHomepageBinding


class StaffHomepageFragment : Fragment() {
    private lateinit var binding : FragmentStaffHomepageBinding
    private val currentUser = FirebaseAuth.getInstance().currentUser!!.uid

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_staff_homepage,container,false)
        val database = FirebaseDatabase.getInstance("https://fypproject-bdcb3-default-rtdb.asia-southeast1.firebasedatabase.app/")
//        val ref = database.getReference("Customers").child("$currentUser")
        val ref = database.getReference("Users").child("$currentUser")



        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
//                    membership = snapshot.child("membership").value.toString()
                    when (snapshot.child("customerGender").value.toString()) {
                        "Male" -> {
                            binding.tvWelcome.text = "Welcome back, Sir!"
                        }
                        "Female" -> {
                            binding.tvWelcome.text = "Welcome back, Madam!"
                        }
                        else -> {
                            binding.tvWelcome.text = "Welcome !"
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("TAG", "Read document Failed")
            }

        })

        binding.orderCardView.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_staffHomepageFragment_to_tableFragment)
        }
        binding.profileCardView.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_staffHomepageFragment_to_editProfileFragment)
        }
        binding.signOutCardView.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_staffHomepageFragment_to_loginFragment)
            FirebaseAuth.getInstance().signOut()
        }
        binding.viewFeedbackCardView.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_staffHomepageFragment_to_viewFeedbackFragment)
        }
        return binding.root
    }

}