package my.edu.tarc.epicpos

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import my.edu.tarc.epicpos.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {
    private lateinit var binding : FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_login,container,false)

        binding.btnLogin.setOnClickListener(){
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()


        }

        return binding.root
    }

}