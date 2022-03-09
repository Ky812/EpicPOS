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
import my.edu.tarc.epicpos.databinding.FragmentEditMenuPageBinding


class EditMenuPageFragment : Fragment() {
    private lateinit var binding : FragmentEditMenuPageBinding

    private val currentUser = FirebaseAuth.getInstance().currentUser!!.uid
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_edit_menu_page,container,false)

        val database = FirebaseDatabase.getInstance("https://fypproject-bdcb3-default-rtdb.asia-southeast1.firebasedatabase.app/")
        val ref = database.getReference("Users").child("$currentUser")

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
//                    membership = snapshot.child("membership").value.toString()
                    when (snapshot.child("customerGender").value.toString()) {
                        "Male" -> {
                            binding.tvWelcome.text = "What Sir want to do?"
                        }
                        "Female" -> {
                            binding.tvWelcome.text = "What Madam want to do?!"
                        }
                        else -> {
                            binding.tvWelcome.text = "Welcome!"
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("TAG", "Read document Failed")
            }

        })

        binding.addMenuCardView.setOnClickListener{
            Navigation.findNavController(it).navigate(R.id.action_editMenuPageFragment_to_managerInsertNewMenuFragment)
        }
        binding.deleteMenuCardView.setOnClickListener{
            Navigation.findNavController(it).navigate(R.id.action_editMenuPageFragment_to_managerDeleteMenuFragment)
        }

        return binding.root

    }
}