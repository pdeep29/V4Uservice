package com.example.v4uservice

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.servicebooking.*
import kotlinx.android.synthetic.main.updateuserdetail.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.Calendar;
import javax.xml.datatype.DatatypeConstants.MONTHS


class BookService : AppCompatActivity() {
    lateinit var option: Spinner
    lateinit var result: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.servicebooking)
        //Celender selection of date


        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        btnpickdate.setOnClickListener {

            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                // Display Selected date in TextView
                txtdate.setText("" + dayOfMonth + " /" + (monthOfYear+1) + "/ " + year)
            }, year, month, day)
            dpd.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis());

         //   dpd.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
            dpd.show()

        }

        //selection of time slot
        Slottime.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.rbtnslot1) {
               // Toast.makeText(this, "Selected slot 1", Toast.LENGTH_LONG).show()

            } else if (checkedId == R.id.rbtnslot2) {
              //  Toast.makeText(this, "Selected slot 2", Toast.LENGTH_LONG).show()

            } else if (checkedId == R.id.rbtnslot3) {
              //  Toast.makeText(this, "Selected slot 3", Toast.LENGTH_LONG).show()
            } else {
              //  Toast.makeText(this, "Please select slot", Toast.LENGTH_LONG).show()
            }
        }
        //Selection of visit or pick and drop
        picktype.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.rbtnpickup) {
                //Toast.makeText(this, "Selected pick and drop service", Toast.LENGTH_LONG).show()

            } else if (checkedId == R.id.rbtnvisit) {
               // Toast.makeText(this, "Selected visit to service center", Toast.LENGTH_LONG).show()

            } else {
               // Toast.makeText(this, "Please select slot", Toast.LENGTH_LONG).show()
            }
        }
        //selection of service center
        getsp()
        
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
               // Toast.makeText( this@BookService,parent!!.selectedItem.toString()Toast.LENGTH_LONG).show()
                val sptext: String = spinner.getSelectedItem().toString()
                spaddress.setText(sptext)

                getuid()

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(this@BookService, "Please select service center", Toast.LENGTH_LONG)
                    .show()
            }
        }
        btnok.setOnClickListener {
            var selRadioId = Slottime.checkedRadioButtonId
            var selRadio: RadioButton = findViewById(selRadioId)
            var radText = selRadio.text.toString()
            // Toast.makeText(this, radText, Toast.LENGTH_LONG).show()
            var seleRadioId = picktype.checkedRadioButtonId
            var seleRadio: RadioButton = findViewById(seleRadioId)
            var radiText = seleRadio.text.toString()
            // Toast.makeText(this, radiText, Toast.LENGTH_LONG).show()

            val Name = name.text.toString()
            val Phone = phone.text.toString()
            val Address = address.text.toString()
            var date = txtdate.text.toString()
            var vehicle = vehicalno.text.toString()
            val sss: String = spinner.getSelectedItem().toString()
            val spuid: String = tvuspid.text.toString()
            saveFireStore(Name, Phone, Address, date, vehicle, sss, radText, radiText, spuid)
        }
        readFireStoreData()
    }
fun getuid(){
    val db = FirebaseFirestore.getInstance()
    db.collection("users")//.document(current)
        .whereEqualTo("Address",spaddress.text.toString())
        .get()
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (document in task.result!!) {
                    val uid = document.data["uid"].toString()
                    tvuspid.setText(uid)

                }
            }
        }
}
    fun readFireStoreData() {
        val db = FirebaseFirestore.getInstance()
        val user = FirebaseAuth.getInstance().currentUser
        val current = user!!.uid
        db.collection("users")//.document(current)
            .whereEqualTo("uid", current)
            .get()
            .addOnCompleteListener {

                val NAME: StringBuffer = StringBuffer()
                val PHONE: StringBuffer = StringBuffer()
                val ADDRESS: StringBuffer = StringBuffer()

                if (it.isSuccessful) {
                    for (document in it.result!!) {
                        NAME.append(document.data.getValue("Name")).append()
                        PHONE.append(document.data.getValue("Phone")).append()
                        ADDRESS.append(document.data.getValue("Address")).append()
                    }
                    name.setText(NAME)
                    phone.setText(PHONE)
                    address.setText(ADDRESS)
                }
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
        spuid: String
    ) {
        if (name.text.toString().isEmpty()) {
            name.error = "Please Enter Name"
            name.requestFocus()
            return
        }
        if (phone.text.toString().isEmpty()) {
            phone.error = "Please Enter Phone Number"
            phone.requestFocus()
            return
        }
        if (address.text.toString().isEmpty()) {
            address.error = "Please Enter Address"
            address.requestFocus()
            return
        }
        if (txtdate.text.toString().isEmpty()) {
            txtdate.error = "Please Enter Address"
            txtdate.requestFocus()
            return
        }
        if (vehicalno.text.toString().isEmpty()) {
            vehicalno.error = "Please Enter Address"
            vehicalno.requestFocus()
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
        booking["spid"] = spuid

        booking["uid"] = uidInput

        if (booking != null) {
            db.collection("Bookings").document()
                .set(booking)
                .addOnSuccessListener() {
                    Toast.makeText(this, "Booking done Successfully ", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, HomeScreen::class.java))
                }
                .addOnFailureListener {
                    Toast.makeText(
                        this,
                        "Error while Booking  Please Tye Againg After Some Time",
                        Toast.LENGTH_SHORT
                    ).show()
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
                        option = findViewById<Spinner>(R.id.spinner)
                        var ssdp = ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, list)
                        spinner.adapter = ssdp
                    }
                }
            }
    }
}

