package my.edu.tarc.epicpos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import my.edu.tarc.epicpos.databinding.FragmentCategoriseMenuBinding


class CategoriseMenuFragment : Fragment() {
    private lateinit var binding: FragmentCategoriseMenuBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_categorise_menu,container,false)
        val args = this.arguments
        val bundle = Bundle()
        val getTableNoData = args?.get("tableNumber")
        val getName = args?.get("name")
        val getMember = args?.get("member")
        val getUserType = args?.get("usertype")
        var categorise = ""

        binding.foodCardView.setOnClickListener {
            categorise = "Food"
            bundle.putString("tableNumber",getTableNoData.toString())
            bundle.putString("name",getName.toString())
            bundle.putString("member",getMember.toString())
            bundle.putString("usertype",getUserType.toString())
            bundle.putString("categoriseType",categorise)
            Navigation.findNavController(it).navigate(R.id.action_categoriseMenuFragment_to_menuFragment,bundle)
        }

        binding.beverageCardView.setOnClickListener {
            categorise = "Drink"
            bundle.putString("tableNumber",getTableNoData.toString())
            bundle.putString("name",getName.toString())
            bundle.putString("member",getMember.toString())
            bundle.putString("usertype",getUserType.toString())
            bundle.putString("categoriseType",categorise)
            Navigation.findNavController(it).navigate(R.id.action_categoriseMenuFragment_to_menuFragment,bundle)

        }
        binding.dessertCardView.setOnClickListener {
            categorise = "Dessert"
            bundle.putString("tableNumber",getTableNoData.toString())
            bundle.putString("name",getName.toString())
            bundle.putString("member",getMember.toString())
            bundle.putString("usertype",getUserType.toString())
            bundle.putString("categoriseType",categorise)
            Navigation.findNavController(it).navigate(R.id.action_categoriseMenuFragment_to_menuFragment,bundle)
        }

        return binding.root
    }

}