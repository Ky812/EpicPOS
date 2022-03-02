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
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import my.edu.tarc.epicpos.data.Users
import my.edu.tarc.epicpos.databinding.FragmentAddStaffBinding

class AddStaffFragment : Fragment() {
    private lateinit var binding : FragmentAddStaffBinding
    private lateinit var auth : FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_add_staff,container,false)
        auth = Firebase.auth
        val database = Firebase.database("https://fypproject-bdcb3-default-rtdb.asia-southeast1.firebasedatabase.app/")
        val ref = database.getReference("Users")

        binding.btnAddStaff.setOnClickListener {
            when{
                TextUtils.isEmpty(binding.editTextName.text.toString().trim { it <= ' ' }) ->{
                    Toast.makeText(context,"Please enter name.", Toast.LENGTH_SHORT).show()
                }
                TextUtils.isEmpty(binding.editTextTel.text.toString().trim { it <= ' ' }) ->{
                    Toast.makeText(context,"Please enter telephone number.", Toast.LENGTH_SHORT).show()
                }
//                TextUtils.isEmpty(binding.editTextGender.text.toString().trim { it <= ' ' }) ->{
//                    Toast.makeText(context,"Please enter gender.", Toast.LENGTH_SHORT).show()
//                }
                TextUtils.isEmpty(binding.editTextEmail.text.toString().trim { it <= ' ' }) ->{
                    Toast.makeText(context,"Please enter email.", Toast.LENGTH_SHORT).show()
                }
                TextUtils.isEmpty(binding.editTextPassword.text.toString().trim { it <= ' ' }) ->{
                    Toast.makeText(context,"Please enter password.", Toast.LENGTH_SHORT).show()
                }
                TextUtils.isEmpty(binding.editTextConfPassword.text.toString().trim { it <= ' ' }) ->{
                    Toast.makeText(context,"Please enter confirm password.", Toast.LENGTH_SHORT).show()
                }
                binding.editTextPassword.text.toString().trim { it <= ' ' } != binding.editTextConfPassword.text.toString().trim { it <= ' ' } ->{
                    Toast.makeText(context,"Please enter the same password with confirm password.",
                        Toast.LENGTH_LONG).show()
                }

                else -> {
                    val email = binding.editTextEmail.text.toString().trim { it <= ' ' }
                    val password = binding.editTextPassword.text.toString().trim { it <= ' ' }
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener(){
                            task ->
                        if (task.isSuccessful){
                            val userName : String = binding.editTextName.text.toString().trim() { it <= ' ' }
                            val userTel : String = binding.editTextTel.text.toString().trim { it <= ' ' }
//                            val customerGender : String = binding.editTextGender.text.toString().trim { it <= ' ' }
                            val firebaseUser: FirebaseUser = task.result!!.user!!
                            ref.addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    if(binding.rdBtnMale.isChecked){
                                        ref.child(firebaseUser.uid).setValue(Users("$userName","$email","$userTel","Male","Non-Membership","Staff"))
                                    }else if(binding.rdBtnFemale.isChecked){
                                        ref.child(firebaseUser.uid).setValue(Users("$userName","$email","$userTel","Female","Non-Membership","Staff"))
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    Log.w("TAG", "Error adding document")
                                }

                            })
                            Toast.makeText(context,"Registered Successful",Toast.LENGTH_SHORT).show()
                            //Navigation.findNavController(it).navigate(R.id.action_userRegisterFragment_to_loginFragment)

                        }else{
                            Toast.makeText(context,task.exception!!.message.toString(),Toast.LENGTH_SHORT).show()

                        }
                    }
                }
            }
        }
        return binding.root
    }

}