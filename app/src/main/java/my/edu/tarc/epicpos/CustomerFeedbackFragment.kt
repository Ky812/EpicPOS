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
import com.google.firebase.database.*
import my.edu.tarc.epicpos.data.Feedback
import my.edu.tarc.epicpos.databinding.FragmentCustomerFeedbackBinding


class CustomerFeedbackFragment : Fragment() {
    private lateinit var binding : FragmentCustomerFeedbackBinding
    private lateinit var db: DatabaseReference
    val rating = 0.0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_customer_feedback,container,false)
        db = FirebaseDatabase.getInstance("https://fypproject-bdcb3-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Feedback")

        binding.btnSubmit.setOnClickListener {

            if(binding.ratingBar.rating == rating.toFloat() && binding.editTextFeedback.text.toString() == ""){
                Navigation.findNavController(it).navigate(R.id.action_customerFeedbackFragment_to_customerHomepageFragment)
            }else{
                    val feedback = binding.editTextFeedback.text.toString()
                    db.addListenerForSingleValueEvent(object : ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            db.push().setValue(Feedback("$feedback","${binding.ratingBar.rating}"))
                        Toast.makeText(context,"Feedback Send Successful.", Toast.LENGTH_SHORT).show()
                        }
                        override fun onCancelled(error: DatabaseError) {
                            Log.w("TAG", "Add Feedback Failed")
                        }

                    })
                Navigation.findNavController(it).navigate(R.id.action_customerFeedbackFragment_to_customerHomepageFragment)
            }
        }

        binding.btnCancel.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_customerFeedbackFragment_to_customerHomepageFragment)
        }

        return binding.root
    }

}