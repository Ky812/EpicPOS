package my.edu.tarc.epicpos.data

import android.content.DialogInterface
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import my.edu.tarc.epicpos.R


class DeleteMenuAdapter (private val menuList : List<Menu>) : RecyclerView.Adapter<DeleteMenuAdapter.menuViewHolder>(){
    val currentUser = FirebaseAuth.getInstance().currentUser!!.uid
    val database = Firebase.database("https://fypproject-bdcb3-default-rtdb.asia-southeast1.firebasedatabase.app/")

    class menuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val itemName : TextView = itemView.findViewById(R.id.tvOrderDetailsName)
        val price : TextView = itemView.findViewById(R.id.textView5)
        val delete : Button = itemView.findViewById(R.id.button5)
        val img : ImageView = itemView.findViewById(R.id.imageView)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeleteMenuAdapter.menuViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.menudelete,parent,false)

        return menuViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: menuViewHolder, position: Int) {
        val currentPosition = menuList[position]
        holder.itemName.text = currentPosition.itemName
        holder.price.text = "RM" + currentPosition.price
        Glide.with(holder.img.context).load(currentPosition.imageUrl).into(holder.img)


        val ref = database.getReference("Menu").child("${currentPosition.itemName}")


        holder.delete.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {

                val builder: AlertDialog.Builder =
                    AlertDialog.Builder(view!!.context)
                builder.setTitle("Are you sure want to remove " + "${holder.itemName.text} from the menu?")

                val button = Button(view.context)

                builder.setPositiveButton(
                    "Yes!",

                    DialogInterface.OnClickListener { dialog, which ->
                        ref.addListenerForSingleValueEvent(object : ValueEventListener{
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val removeRef = database.getReference("Menu").child("${currentPosition.itemName}")
                                Toast.makeText(view.context, "Menu Remove Success", Toast.LENGTH_LONG)
                                    .show()
                                removeRef.addListenerForSingleValueEvent(object : ValueEventListener{
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        var rmv = snapshot.ref.removeValue()
                                    }


//                override fun onDataChange(snapshot: DataSnapshot) {
//                    //for (snap in snapshot.children){
//                        var rmv = snapshot.ref.key
//                        Log.i("rmv","$rmv")
//                    //}
//                }

                                    override fun onCancelled(error: DatabaseError) {
                                        Log.w("TAG", "Error Delete document")
                                    }

                                })
                            }

                            override fun onCancelled(error: DatabaseError) {
                                Log.w("TAG", "Error adding document")
                            }
                        })
//

                        dialog.cancel()
                    })

                builder.setNegativeButton(
                    "Back",
                    DialogInterface.OnClickListener { dialog, which ->
                        dialog.dismiss()
                    })




                val alertDialog: AlertDialog = builder.create()

                alertDialog.show()
            }


        })
//
    }

    private fun removeItem(position: Int) {
//        val newPosition: Int = menuList[position]
//        menuList.remove(newPosition)
//        notifyItemRemoved(newPosition)
//        notifyItemRangeChanged(newPosition, menuList.size())
    }



    override fun getItemCount(): Int {
        return menuList.size
    }





}