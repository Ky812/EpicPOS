package my.edu.tarc.epicpos.data

data class Payment(
    var total : String = "",
    var paymentType : String = "",
    var paymentDateTime : String = "",
    var orderID : String = "",
    var customerID : String = "",
    var datePayment : String =""

)
