package my.edu.tarc.epicpos

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.databinding.DataBindingUtil
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import my.edu.tarc.epicpos.data.Payment
import my.edu.tarc.epicpos.databinding.FragmentSalesReportBinding
import java.text.SimpleDateFormat
import java.util.*

class SalesReportFragment : Fragment() {
    private lateinit var binding: FragmentSalesReportBinding
    private lateinit var db : DatabaseReference
    val database = Firebase.database("https://fypproject-bdcb3-default-rtdb.asia-southeast1.firebasedatabase.app/")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =  DataBindingUtil.inflate(inflater,R.layout.fragment_sales_report, container, false)

        val c =  Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        var month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        var caltotalAmount = 0.0


        binding.editText1.setOnClickListener(){
            val dpd = DatePickerDialog(this.requireContext(),
                DatePickerDialog.OnDateSetListener { view, mYear, mMonth , mDay ->

                    var dy : String
                    var mt : String
                    var mTry = mMonth + 1

                    if(mTry < 10){
                        mt = "0" + mTry
                    }else{
                        mt = mTry.toString()
                    }

                    if(mDay < 10){
                        dy = "0" + mDay
                    }else{
                        dy = mDay.toString()
                    }


                    binding.editText1.setText("" + dy + "-" + mt + "-" + mYear)

                },
                year,
                month,
                day
            )

            dpd.show()

        }

        binding.editTextDate2.setOnClickListener(){
            val dpd = DatePickerDialog(this.requireContext(),
                DatePickerDialog.OnDateSetListener { view, mYear, mMonth, mDay ->

                    var dy : String
                    var mt : String
                    var mTry = mMonth + 1

                    if(mTry < 10){
                        mt = "0" + mTry
                    }else{
                        mt = mTry.toString()
                    }

                    if(mDay < 10){
                        dy = "0" + mDay
                    }else{
                        dy = mDay.toString()
                    }


                    binding.editTextDate2.setText("" + dy + "-" + mt + "-" + mYear)

                },
                year,
                month,
                day
            )

            dpd.show()

        }


        binding.btnProceed.setOnClickListener() {
            //first from date
            val fromDate : String = binding.editText1.text.toString()
            val delim = "-"
            val split = fromDate.split(delim).toTypedArray()
            val from_date_year = split[2]
            val month_date = SimpleDateFormat("MMMM", Locale.ENGLISH)
            val sdf = SimpleDateFormat("MM")
            val actualDate = split[1]
            val date = sdf.parse(actualDate)
            val beta = month_date.format(date)

            //to date
            val toDate : String = binding.editTextDate2.text.toString()
            val split1 = toDate.split(delim).toTypedArray()
            val to_date_year = split1[2]

            //between date





            val queryRef = FirebaseDatabase.getInstance("https://fypproject-bdcb3-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Payment").orderByChild("datePayment").startAt("$fromDate").endAt("$toDate")

            queryRef.get().addOnSuccessListener { it ->
                if(it.exists()){
                    for(record in it.children){
                        var data = record.getValue(Payment::class.java)

                        if(data != null){
                            val totalAmount = data.total
                            caltotalAmount += totalAmount.toDouble()


                        }
                    }

                    val builder: AlertDialog.Builder =
                        AlertDialog.Builder(requireContext())
                    builder.setTitle("This is the total sales report for the Restaurant!").setMessage("The total sales from " +fromDate+ " to " +toDate+ " is total RM" +caltotalAmount)

                    builder.setPositiveButton(
                        "Yes!",

                        DialogInterface.OnClickListener { dialog, which ->

//                            Navigation.findNavController(it).navigate(R.id.action_customerHomepageFragment_to_salesReport)

//

                            dialog.cancel()
                        })

                    builder.setNegativeButton(
                        "Back",
                        DialogInterface.OnClickListener { dialog, which ->
                            caltotalAmount = 0.0
                            dialog.dismiss()
                        })
                    val alertDialog: AlertDialog = builder.create()

                    alertDialog.show()
//                    Toast.makeText(requireContext(), "The total sales from " +fromDate+ " to " +toDate+ " is total RM" +caltotalAmount, Toast.LENGTH_SHORT).show()
                }
            }


        }

        binding.btnReset.setOnClickListener(){
            binding.editText1.setText("")
            binding.editTextDate2.setText("")
            caltotalAmount = 0.0
        }

        return binding.root

    }
    fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
        TODO("Not yet implemented")
    }
}