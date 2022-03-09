package my.edu.tarc.epicpos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import my.edu.tarc.epicpos.databinding.FragmentEditProfileBinding

class EditProfileFragment : Fragment() {
    private lateinit var binding : FragmentEditProfileBinding
    private val currentUser = FirebaseAuth.getInstance().currentUser!!.uid

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_edit_profile,container,false)
//        val db  = FirebaseDatabase.getInstance("https://fypproject-bdcb3-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Customers").child("$currentUser")
        val db  = FirebaseDatabase.getInstance("https://fypproject-bdcb3-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users").child("$currentUser")


        db.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                binding.btnUpdateProfile.setOnClickListener(){
                    db.child("userName").setValue(binding.txtCustomerName.text.toString())
                    db.child("userTel").setValue(binding.txtCustomerTel.text.toString())
                    Toast.makeText(context, "Updated Successful", Toast.LENGTH_LONG).show()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                //  TODO("Not yet implemented")
            }
        })
        return binding.root
    }
}