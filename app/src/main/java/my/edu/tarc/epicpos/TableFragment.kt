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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import my.edu.tarc.epicpos.databinding.FragmentTableBinding

class TableFragment : Fragment() {
    private lateinit var binding : FragmentTableBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_table,container,false)
        val currentUser = FirebaseAuth.getInstance().currentUser!!.uid
        val database = FirebaseDatabase.getInstance("https://fypproject-bdcb3-default-rtdb.asia-southeast1.firebasedatabase.app/")
//        val ref = database.getReference("Customers").child("$currentUser")
        val ref = database.getReference("Users").child("$currentUser")
        var membership = ""
        var name = ""
        var userType = ""
        val bundle = Bundle()

        binding.btnTableNoConfirm.setOnClickListener(){

            when{
                TextUtils.isEmpty(binding.editTableNo.text.toString().trim { it <= ' ' }) ->{
                    Toast.makeText(context,"Please enter Table No.", Toast.LENGTH_SHORT).show()
                }else -> {
                val tableNo = binding.editTableNo.text.toString().trim { it <= ' ' }

                ref.addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        name = snapshot.child("customerName").value.toString()
                        membership = snapshot.child("membership").value.toString()
                        userType = snapshot.child("userType").value.toString()
                        bundle.putString("tableNumber",tableNo)
                        bundle.putString("name",name)
                        bundle.putString("member",membership)
                        bundle.putString("usertype",userType)
                        Navigation.findNavController(it).navigate(R.id.action_tableFragment_to_menuFragment,bundle)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.w("TAG", "Read document Fail")
                    }

                })
                }
            }
        }

        return binding.root
    }

}