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
import my.edu.tarc.epicpos.databinding.FragmentManagerHomepageBinding


class ManagerHomepageFragment : Fragment() {
    private lateinit var binding : FragmentManagerHomepageBinding
    private val currentUser = FirebaseAuth.getInstance().currentUser!!.uid

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_manager_homepage,container,false)

        binding.tvWelcome.text = "Welcome back,Manager!"


        // Inflate the layout for this fragment
        binding.editMenuCardView.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_managerHomepageFragment_to_editMenuPageFragment)

        }

        binding.addStaffCardView.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_managerHomepageFragment_to_addStaffFragment)

        }

        binding.salesReportCardView.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_managerHomepageFragment_to_salesReportFragment)

        }

        binding.viewFeedbackCardView.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_managerHomepageFragment_to_viewFeedbackFragment)

        }

        binding.signOutCardView.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_managerHomepageFragment_to_loginFragment)
            FirebaseAuth.getInstance().signOut()
        }

        return binding.root
    }

}