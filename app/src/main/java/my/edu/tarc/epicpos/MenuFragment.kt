package my.edu.tarc.epicpos

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import my.edu.tarc.epicpos.data.Menu
import my.edu.tarc.epicpos.data.MenuAdapter

class MenuFragment : Fragment() {
    private lateinit var db: DatabaseReference
    private lateinit var menuRecyclerView: RecyclerView
    private lateinit var menuArrayList: ArrayList<Menu>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        menuRecyclerView = view.findViewById(R.id.menuRecyclerView)
        menuRecyclerView.layoutManager = LinearLayoutManager(this.context)
        menuRecyclerView.setHasFixedSize(true)
        menuArrayList = arrayListOf<Menu>()
        val btnMyOrder = view.findViewById<Button>(R.id.btnMyOrder)
        val bundle = Bundle()
        val args = this.arguments
        val getTableNoData = args?.get("tableNumber")
        val getName = args?.get("name")
        val getMember = args?.get("member")
        val getUserType = args?.get("usertype")

//        val btnTableNo = view.findViewById<Button>(R.id.btnTableNoConfirm)
//        val editTableNo = view.findViewById<EditText>(R.id.editTableNo)
//        val tvTableNo = view.findViewById<TextView>(R.id.tvTableNo)
//        val tvOrder = view.findViewById<TextView>(R.id.tvOrder)
//        val searchView = view.findViewById<SearchView>(R.id.search)

        btnMyOrder.setOnClickListener(){
            bundle.putString("tableNumber",getTableNoData.toString())
            bundle.putString("name",getName.toString())
            bundle.putString("member",getMember.toString())
            bundle.putString("usertype",getUserType.toString())
            Navigation.findNavController(it).navigate(R.id.action_menuFragment_to_orderListFragment,bundle)
        }

//        btnTableNo.setOnClickListener(){
//            when{
//                TextUtils.isEmpty(editTableNo.text.toString().trim { it <= ' ' }) ->{
//                    Toast.makeText(context,"Please enter Table No.", Toast.LENGTH_SHORT).show()
//                }else -> {
//                val tableNo = editTableNo.text.toString().trim { it <= ' ' }
//                    tvTableNo.visibility = View.GONE
//                    editTableNo.visibility = View.GONE
//                    btnTableNo.visibility = View.GONE
//                    tvOrder.visibility = View.VISIBLE
//                    searchView.visibility = View.VISIBLE
//                    btnMyOrder.visibility = View.VISIBLE
//                    menuRecyclerView.visibility = View.VISIBLE
//
//                }
//            }
//        }

        getMenu()

    }

    private fun getMenu() {
        db = FirebaseDatabase.getInstance("https://fypproject-bdcb3-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Menu")

        db.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                menuArrayList.clear()
                if (snapshot.exists()) {
                    for (snap in snapshot.children) {
                        val eve = snap.getValue(Menu::class.java)

                        menuArrayList.add(eve!!)
                    }
                    var adapter = MenuAdapter(menuArrayList)
                    menuRecyclerView.adapter = adapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.i("Error", "No Menu Found")
            }

        })
    }

}