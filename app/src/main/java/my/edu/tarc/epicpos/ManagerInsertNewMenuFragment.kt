package my.edu.tarc.epicpos

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.number.NumberFormatter.with
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import my.edu.tarc.epicpos.data.Menu
import my.edu.tarc.epicpos.databinding.FragmentManagerInsertNewMenuBinding
import java.util.*

class ManagerInsertNewMenuFragment : Fragment() {
    private lateinit var binding: FragmentManagerInsertNewMenuBinding
    lateinit var ImageUri: Uri
    private lateinit var database : DatabaseReference

    private val pickImages =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri?
            ->
            if (uri != null) {
                ImageUri = uri
            }
            uri?.let { it ->
                Picasso.with(context).load(it).resize(800, 800).into(binding.ivUpload)
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_manager_insert_new_menu,
            container,
            false
        )



        binding.btnUpload.setOnClickListener() {
            val cameraIntent = Intent(Intent.ACTION_GET_CONTENT)
            cameraIntent.type = "image/*"

            pickImages.launch("image/")
        }

        binding.btnRegister.setOnClickListener() {
            val itemName: String = binding.tvInputMenuName.text.toString()
            val description : String = binding.tvInputMenuDescription.text.toString()
            val price: String = binding.tvInputPrice.text.toString()
            var imageLink: String
            val img: ImageView = binding.ivUpload

            var category = ""

            if(binding.rdFood.isChecked){
                category = "Food"
            }else if(binding.rdDrink.isChecked){
                category = "Drink"
            }else{
                category = "Dessert"
            }

            val database =
                Firebase.database("https://fypproject-bdcb3-default-rtdb.asia-southeast1.firebasedatabase.app/")
            val refMenu = database.getReference("Menu").child(itemName)

            val tempString: String = "images/" + UUID.randomUUID().toString() + ".jpg"
            val storageReference =
                FirebaseStorage.getInstance()
                    .getReference(tempString)
            val getLink = FirebaseStorage.getInstance().reference


            if (itemName != "" && price != "" && img.drawable != null) {
                storageReference.putFile(ImageUri).addOnCompleteListener { task ->

                    if (task.isSuccessful) {

                        binding.ivUpload.setImageURI(null)
                        getLink.child("" + tempString + "").downloadUrl.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                imageLink = task.result.toString()
                                Toast.makeText(
                                    this.context,
                                    "Upload Success",
                                    Toast.LENGTH_LONG
                                ).show()

                                refMenu.addValueEventListener(object : ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        var itemName: String =
                                            snapshot.child("name").getValue().toString()
                                        val newMenu = Menu(
                                            itemName,
                                            category,
                                            description,
                                            price,
                                            imageLink
                                        )
                                        refMenu.setValue(newMenu)
                                        Log.i("tag",newMenu.toString())
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        TODO("Not yet implemented")
                                    }

                                })
                            } else {
                                Toast.makeText(context, "Please select a image.", Toast.LENGTH_LONG)
                                    .show()
                            }

                        }

                    } else {
                        Toast.makeText(context, "Upload Fail", Toast.LENGTH_LONG).show()
                    }
                    Toast.makeText(context, "Menu Insert Success", Toast.LENGTH_LONG)
                        .show()

                    Navigation.findNavController(it)
                        .navigate(R.id.action_managerInsertNewMenuFragment_to_managerHomepageFragment)
                }

            } else if (itemName == "" || description == "" ||price == "" || img.drawable == null) {
                Toast.makeText(context, "Please fill in all the fields.", Toast.LENGTH_LONG)
                    .show()

            }
        }
        return binding.root
    }
}