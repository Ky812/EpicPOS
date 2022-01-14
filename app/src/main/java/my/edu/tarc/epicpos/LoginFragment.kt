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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import my.edu.tarc.epicpos.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {
    private lateinit var binding : FragmentLoginBinding
    private lateinit var auth : FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_login,container,false)
        auth = Firebase.auth
        binding.btnLogin.setOnClickListener(){
            when {
                TextUtils.isEmpty(binding.editTextEmail.text.toString().trim { it <= ' ' }) ->{
                    Toast.makeText(context,"Please enter email.",Toast.LENGTH_SHORT).show()
                }
                TextUtils.isEmpty(binding.editTextPassword.text.toString().trim { it <= ' ' }) ->{
                    Toast.makeText(context,"Please enter password.",Toast.LENGTH_SHORT).show()
                }
                else -> {
                    val email = binding.editTextEmail.text.toString().trim { it <= ' ' }
                    val password = binding.editTextPassword.text.toString().trim { it <= ' ' }
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener(){
                            task ->
                        if (task.isSuccessful){
                            Toast.makeText(context,"Login Successful",Toast.LENGTH_SHORT).show()

                        }else{
                            Toast.makeText(context,task.exception!!.message.toString(),Toast.LENGTH_SHORT).show()

                        }
                    }
                }
            }

        }
        binding.tvForgetPass.setOnClickListener(){
            Navigation.findNavController(it).navigate(R.id.action_loginFragment_to_forgetPasswordFragment)

        }
        binding.tvSignUp.setOnClickListener(){
            Navigation.findNavController(it).navigate(R.id.action_loginFragment_to_userRegisterFragment)
        }
        return binding.root
    }

}