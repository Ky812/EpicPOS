package my.edu.tarc.epicpos

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import my.edu.tarc.epicpos.databinding.FragmentForgetPasswordBinding

class ForgetPasswordFragment : Fragment() {
    private lateinit var binding : FragmentForgetPasswordBinding
    private lateinit var auth : FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_forget_password,container,false)
        auth = Firebase.auth

        binding.btnReset.setOnClickListener(){
            when{
                TextUtils.isEmpty(binding.editTextEmail.text.toString().trim { it <= ' ' }) ->{
                    Toast.makeText(context,"Please enter email.", Toast.LENGTH_SHORT).show()

                }
                else -> {
                    val email = binding.editTextEmail.text.toString().trim { it <= ' ' }
                    auth.sendPasswordResetEmail(email).addOnCompleteListener {
                        task->
                        if(task.isSuccessful){
                            Toast.makeText(context,"Reset Password has been send to your email.", Toast.LENGTH_LONG).show()
                            Navigation.findNavController(it).navigate(R.id.action_forgetPasswordFragment_to_loginFragment)

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