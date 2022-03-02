package my.edu.tarc.epicpos.data

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.database.PropertyName

//data class Menu(
//    var categoryType : String = "",
//    var description : String = "",
//    var itemName : String = "",
//    var price : String = "",
//    @get:PropertyName("imageUrl") @set:PropertyName("imageUrl") var imageUrl : String = ""
//    )

@IgnoreExtraProperties
data class Menu(
    @get:Exclude var itemName : String = "",
    var categoryType : String = "",
    var description : String = "",
    var price : String = "",
    @get:PropertyName("imageUrl") @set:PropertyName("imageUrl") var imageUrl : String = ""
)
