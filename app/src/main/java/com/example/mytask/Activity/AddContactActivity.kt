package com.example.mytask.Activity

import android.content.ContentUris
import android.content.ContentValues
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatSpinner
import com.example.mytask.R

class AddContactActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_contact)
        initView()

    }
    private fun initView() {
        val btn_save: AppCompatButton = findViewById(R.id.btn_save)

        btn_save.setOnClickListener {
            CheckValidation()
        }
    }

    private fun CheckValidation() {
        var spin_address: AppCompatSpinner = findViewById(R.id.spin_address);
        val phoneTypeArr = arrayOf("Mobile", "Home", "Work")

        val phoneTypeSpinnerAdaptor = ArrayAdapter(this, android.R.layout.simple_spinner_item, phoneTypeArr)
        spin_address.setAdapter(phoneTypeSpinnerAdaptor)

        var et_name: AppCompatEditText = findViewById(R.id.et_name)
        var et_phone: AppCompatEditText = findViewById(R.id.et_phone)

        if (!et_name.text.toString().equals("")) {
            if (!et_phone.text.toString().equals("")) {

                val addContactsUri: Uri = ContactsContract.Data.CONTENT_URI

                val rowContactId: Long = getRawContactId()
                val displayName: String = et_name.text.toString()
                insertContactDisplayName(addContactsUri, rowContactId, displayName)
                val phoneNumber: String = et_phone.text.toString()
                val phoneTypeStr = spin_address.getSelectedItem()
                insertContactPhoneNumber(addContactsUri, rowContactId, phoneNumber, phoneTypeStr);
                Toast.makeText(applicationContext, "Contact Saved Successfully", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(applicationContext, "Please enter Contact No.", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(applicationContext, "Please enter your Name", Toast.LENGTH_SHORT).show()
        }
    }


    private fun insertContactPhoneNumber(addContactsUri: Uri, rowContactId: Long, phoneNumber: String, phoneTypeStr: Any?) {
        val contentValues = ContentValues()
        contentValues.put(ContactsContract.Data.RAW_CONTACT_ID, rowContactId)
        contentValues.put(
            ContactsContract.Data.MIMETYPE,
            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE
        )
        contentValues.put(ContactsContract.CommonDataKinds.Phone.NUMBER, phoneNumber)
        var phoneContactType = ContactsContract.CommonDataKinds.Phone.TYPE_HOME
        if ("home".equals(phoneTypeStr == true)) {
            phoneContactType = ContactsContract.CommonDataKinds.Phone.TYPE_HOME
        } else if ("mobile".equals(phoneTypeStr == true)) {
            phoneContactType = ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE
        } else if ("work".equals(phoneTypeStr == true)) {
            phoneContactType = ContactsContract.CommonDataKinds.Phone.TYPE_WORK
        }
        contentValues.put(ContactsContract.CommonDataKinds.Phone.TYPE, phoneContactType)
        contentResolver.insert(addContactsUri, contentValues)
    }

    private fun getRawContactId(): Long {
        val contentValues = ContentValues()
        val rawContactUri =
            contentResolver.insert(ContactsContract.RawContacts.CONTENT_URI, contentValues)
        return ContentUris.parseId(rawContactUri!!)
    }

    private fun insertContactDisplayName(
        addContactsUri: Uri,
        rowContactId: Long,
        displayName: String
    ) {
        val contentValues = ContentValues()
        contentValues.put(ContactsContract.Data.RAW_CONTACT_ID, rowContactId)
        contentValues.put(
            ContactsContract.Data.MIMETYPE,
            ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE
        )
        contentValues.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, displayName)
        contentResolver.insert(addContactsUri, contentValues)
    }
}