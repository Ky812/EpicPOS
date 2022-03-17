package my.edu.tarc.epicpos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import my.edu.tarc.epicpos.R.layout.fragment_categorise_menu
import my.edu.tarc.epicpos.databinding.FragmentCategoriseMenuBinding
import my.edu.tarc.epicpos.databinding.FragmentDeleteCategorizeMenuBinding



class DeleteCategorizeMenu : Fragment() {
    private lateinit var binding: FragmentDeleteCategorizeMenuBinding



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_delete_categorize_menu,container,false)
        val bundle = Bundle()
        var categorise = ""

        binding.foodCardView.setOnClickListener {
            categorise = "Food"
            bundle.putString("categoriseType",categorise)
            Navigation.findNavController(it).navigate(R.id.action_deleteCategorizeMenu_to_managerDeleteMenuFragment,bundle)
        }

        binding.beverageCardView.setOnClickListener {
            categorise = "Drink"
            bundle.putString("categoriseType",categorise)
            Navigation.findNavController(it).navigate(R.id.action_deleteCategorizeMenu_to_managerDeleteMenuFragment,bundle)

        }
        binding.dessertCardView.setOnClickListener {
            categorise = "Dessert"
            bundle.putString("categoriseType",categorise)
            Navigation.findNavController(it).navigate(R.id.action_deleteCategorizeMenu_to_managerDeleteMenuFragment,bundle)
        }

        return binding.root
    }


}