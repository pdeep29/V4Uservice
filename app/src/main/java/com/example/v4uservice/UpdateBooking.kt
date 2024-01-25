package com.example.v4uservice

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.servicebooking.*
import kotlinx.android.synthetic.main.updatebooking.*
import java.util.*

class UpdateBooking : AppCompatActivity() {
    // private var DB: FirebaseFirestore? = null
    // internal var id: String = ""
    lateinit var option: Spinner
    var current=""
    var currDate:String=""
    var currID:String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.updatebooking)
        val db = FirebaseFirestore.getInstance()

        //val options=arrayOf("1","20","3")
        //   var ssl= arrayOf("Please Select Service center","Honda Address:Old Chhani Rd,Vadodara, Gujarat 390024","C-14-15, Sama Rd, Laxmikunj Society, Near Bus Stand, Laxmikunj Society Part 1, Anandvan Society, New Sama, Vadodara, Gujarat 390024","Blue")

        getsp()
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        ubtnpickdate.setOnClickListener {

            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                // Display Selected date in TextView
                utxtdate.setText("" + dayOfMonth + " /" + (monthOfYear+1) + "/ " + year)
            }, year, month, day)
            dpd.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis());

            //   dpd.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
            dpd.show()

        }

        uspinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                // Toast.makeText( this@BookService,parent!!.selectedItem.toString()Toast.LENGTH_LONG).show()
                val sptext: String = uspinner.getSelectedItem().toString()
                uspaddress.setText(sptext)



            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        // val user = FirebaseAuth.getInstance().currentUser
        current = intent.getStringExtra("EXTRA_SESSION_ID");
        currDate=intent.getStringExtra("EXTRA_SESSION_DATE");

        val DATE: StringBuffer = StringBuffer()
        val SLOTTIME: StringBuffer = StringBuffer()
        val TYPE: StringBuffer = StringBuffer()
        val PADDRESS: StringBuffer = StringBuffer()
        val NAME: StringBuffer = StringBuffer()
        val PHONE: StringBuffer = StringBuffer()
        val ADDRESS: StringBuffer = StringBuffer()
        val VEHICLE: StringBuffer = StringBuffer()
        val PROVIDER: StringBuffer = StringBuffer()
        val spuid:StringBuffer =StringBuffer()
        db.collection("Bookings")//.document(current)
            .whereEqualTo("uid", current)
            .get()
            .addOnCompleteListener {


                if (it.isSuccessful) {
                    for (document in it.result!!) {

                        DATE.append(document.data.getValue("Date")).append()
                        SLOTTIME.append(document.data.getValue("SlotTime")).append()
                        TYPE.append(document.data.getValue("ServiceType")).append()
                        NAME.append(document.data.getValue("Name")).append()
                        PHONE.append(document.data.getValue("Phone")).append()
                        ADDRESS.append(document.data.getValue("Address")).append()
                        VEHICLE.append(document.data.getValue("Vehicleno")).append()
                        PROVIDER.append(document.data.getValue("ServiceCenter")).append()
                        spuid.append(document.data.getValue("spid")).append()
                        currID=document.id
                    }
                    textView7.setText(spuid)
                     utxtdate.setText(DATE)
                    if (TYPE.equals("9AM - 11AM{")) {
                        urbtnslot1.isChecked = true
                    } else if(TYPE.equals("12PM - 2PM{")){
                        urbtnslot2.isChecked = true
                    }
                    else{
                        urbtnslot3.isChecked = true
                    }
                    var pos = 0;
                    for (i in 0 until uspinner.count) {
                        if (uspinner.getItemAtPosition(i).toString()
                                .equals(TYPE.toString(), true)
                        ) {
                            uspinner.setSelection(i);
                            break
                        }
                    }

                    if (TYPE.equals("VISIT CENTER{")) {
                        urbtnvisit.isChecked = true
                    } else {
                        urbtnpickup.isChecked = true
                    }
                    uname.setText(NAME)
                    uphone.setText(PHONE)
                    uaddress.setText(ADDRESS)
                    uvehicalno.setText(VEHICLE)
                    uspaddress.setText(PROVIDER)
                }
            }
        //db.collection("Bookings").document(current).update(booking)

        ubtnok.setOnClickListener{
            var selRadioId=uSlottime.checkedRadioButtonId
            var selRadio: RadioButton =findViewById(selRadioId)
            var radText=selRadio.text.toString()
            // Toast.makeText(this, radText, Toast.LENGTH_LONG).show()
            var seleRadioId=urbtnpicktype.checkedRadioButtonId
            var seleRadio: RadioButton =findViewById(seleRadioId)
            var radiText=seleRadio.text.toString()
            // Toast.makeText(this, radiText, Toast.LENGTH_LONG).show()

            val Name = uname.text.toString()
            val Phone = uphone.text.toString()
            val Address = uaddress.text.toString()
            var date =utxtdate.text.toString()
            var vehicle=uvehicalno.text.toString()
            val sss: String = uspinner.getSelectedItem().toString()
            val spid=textView7.text.toString()
            saveFireStore(Name,Phone,Address,date,vehicle,sss,radText,radiText,spid)

        }

    }

    fun saveFireStore(
        Name: String,
        Phone: String,
        Address: String,
        date: String,
        vehicle: String,
        sss: String,
        radText: String,
        radiText: String,
       spid:String
    ) {
        if (uname.text.toString().isEmpty()) {
            uname.error = "Please Enter Name"
            uname.requestFocus()
            return
        }
        if (uphone.text.toString().isEmpty()) {
            uphone.error = "Please Enter Phone Number"
            uphone.requestFocus()
            return
        }
        if (uaddress.text.toString().isEmpty()) {
            uaddress.error = "Please Enter Address"
            uaddress.requestFocus()
            return
        }
        if (utxtdate.text.toString().isEmpty()) {
            utxtdate.error = "Please Select Date"
            utxtdate.requestFocus()
            return
        }
        if (uvehicalno.text.toString().isEmpty()) {
            uvehicalno.error = "Please Vehicle number"
            uvehicalno.requestFocus()
            return
        }


        val db = FirebaseFirestore.getInstance()
        val testUser =
            FirebaseAuth.getInstance().currentUser //getting the current logged in users id
        val userUid = testUser!!.uid
        val uidInput = userUid
        val booking: MutableMap<String, Any> = HashMap()
        booking["Name"] = Name
        booking["Phone"] = Phone
        booking["Address"] = Address
        booking["Date"] = date
        booking["Vehicleno"] = vehicle
        booking["ServiceCenter"] = sss
        booking["SlotTime"] = radText
        booking["ServiceType"] = radiText
        booking["spid"] = textView7
        booking["uid"] = uidInput

        if (booking != null) {
            db.collection("Bookings").document(currID).set(booking)
                .addOnSuccessListener() {
                    Toast.makeText(this, "Booking Updated Successfully ", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, HomeScreen::class.java))
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Please Try Again After Some Time", Toast.LENGTH_SHORT).show()
                }
        }
    }
    fun getsp() {
        val db = FirebaseFirestore.getInstance()
        db.collection("users")//.document(current)
            .whereEqualTo("userType", "serviceprovider")
            .whereEqualTo("verified","verified")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val list = ArrayList<String>()
                    for (document in task.result!!) {
                        val addr = document.data["Address"].toString()
                        list.add(addr)
                        option = findViewById<Spinner>(R.id.uspinner)
                        var ssdp = ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, list)
                        uspinner.adapter = ssdp
                    }
                }
            }
    }
}