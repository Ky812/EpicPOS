package my.edu.tarc.epicpos

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import my.edu.tarc.epicpos.data.DeleteMenuAdapter
import my.edu.tarc.epicpos.data.Menu

class ManagerDeleteMenuFragment : Fragment() {
    private lateinit var db: DatabaseReference
    private lateinit var menuRecyclerView: RecyclerView
    private lateinit var menuArrayList: ArrayList<Menu>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_manager_delete_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        menuRecyclerView = view.findViewById(R.id.menuRecyclerView)
        menuRecyclerView.layoutManager = LinearLayoutManager(this.context)
        menuRecyclerView.setHasFixedSize(true)
        menuArrayList = arrayListOf<Menu>()

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
                        if (eve != null) {
                            eve.itemName = snap.key.toString()
                        }
                        menuArrayList.add(eve!!)
                    }
                    var adapter = DeleteMenuAdapter(menuArrayList)
                    menuRecyclerView.adapter = adapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.i("Error", "No Menu Found")
            }

        })
    }

}