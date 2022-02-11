package my.edu.tarc.epicpos.data

import android.content.DialogInterface
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import my.edu.tarc.epicpos.R

class MenuAdapter (private val menuList : List<Menu>) : RecyclerView.Adapter<MenuAdapter.menuViewHolder>(){
    val currentUser = FirebaseAuth.getInstance().currentUser!!.uid
    val database = Firebase.database("https://fypproject-bdcb3-default-rtdb.asia-southeast1.firebasedatabase.app/")
//    var strTempOrder = ""

    class menuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val itemName : TextView = itemView.findViewById(R.id.tvOrderDetailsName)
        val price : TextView = itemView.findViewById(R.id.textView5)
        val add : Button = itemView.findViewById(R.id.button5)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuAdapter.menuViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.menu,parent,false)

        return menuViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MenuAdapter.menuViewHolder, position: Int) {
        val currentPosition = menuList[position]
        holder.itemName.text = currentPosition.itemName
        holder.price.text = "RM" + currentPosition.price
        val ref = database.getReference("Customers").child("$currentUser").child("TempOrder").child("OrderDetails").child("${currentPosition.itemName}")
        val db = database.getReference("Customers").child("$currentUser").child("TempOrder").child("OrderDetails")
        val price = currentPosition.price
        var calPrice = 0.0
        var existCalPrice = 0.0
        var strExistQty = ""
        var totalExistQty = 0
        var total = 0.0

        holder.add.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                var itemQty = ""
                val builder: AlertDialog.Builder =
                    AlertDialog.Builder(view!!.context)
                builder.setTitle("Enter Quantity for " + "${holder.itemName.text}")
                builder.setMessage("How Many Quantity would you want?")
                val input = EditText(view.context)
                input.inputType = InputType.TYPE_CLASS_TEXT
                input.setHint("Enter Quantity")
                builder.setView(input)

                builder.setPositiveButton(
                    "Add to Order",
                    DialogInterface.OnClickListener { dialog, which ->
                        itemQty = input.text.toString()
                        if(itemQty == ""){
                            dialog.dismiss()
                        }else{
                        calPrice = price.toDouble() * itemQty.toInt()

                        ref.addListenerForSingleValueEvent(object : ValueEventListener{
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if(snapshot.exists()){
                                    strExistQty = snapshot.child("quantity").value.toString()
                                    totalExistQty = strExistQty.toInt() + itemQty.toInt()
                                    existCalPrice = price.toDouble() * totalExistQty
                                    ref.setValue(Order("${holder.itemName.text}","$existCalPrice","${totalExistQty.toString()}"))
                                    Toast.makeText(view.context,"${holder.itemName.text} add to order successful.", Toast.LENGTH_SHORT).show()
                                }else{
//                                ref.push().setValue(Order("${holder.itemName.text}","$calPrice","$itemQty"))
                                    ref.setValue(Order("${holder.itemName.text}","$calPrice","$itemQty"))
                                    Toast.makeText(view.context,"${holder.itemName.text} add to order successful.", Toast.LENGTH_SHORT).show()
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                Log.w("TAG", "Error adding document")
                            }
                        })
                            //got bug
//                            db.addListenerForSingleValueEvent(object : ValueEventListener{
//                                override fun onDataChange(snapshot: DataSnapshot) {
//                                    snapshot.children.forEach {
//                                        //val key : String = it.key.toString()
//                                        val itemPrice : String = it.child("price").value.toString()
//                                        total += itemPrice.toDouble()
//                                        Log.i("price","$total")
//                                    }
//                                }
//
//                                override fun onCancelled(error: DatabaseError) {
//                                    TODO("Not yet implemented")
//                                }
//
//                            })
                        }
                        dialog.cancel()
                    })

                builder.setNegativeButton(
                    "Cancel",
                    DialogInterface.OnClickListener { dialog, which ->
                        dialog.dismiss()
                    })
                val alertDialog: AlertDialog = builder.create()

                alertDialog.show()
            }


        })
//        holder.add.setOnClickListener(){
//            ref.addListenerForSingleValueEvent(object : ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    ref.push().setValue("${holder.price.text}")
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                    Log.w("TAG", "Error adding document")
//                }
//
//            })
//        }
    }

    override fun getItemCount(): Int {
        return menuList.size
    }

}