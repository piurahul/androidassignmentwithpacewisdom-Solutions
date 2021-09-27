package com.example.mytask.Activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.mytask.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.kedar.contactfetch.ContactsAdapter
import com.kedar.contactfetch.ContactsViewModel
import com.kedar.contactfetch.hasPermission
import com.kedar.contactfetch.requestPermissionWithRationale

class MainActivity : AppCompatActivity() {
    private val contactsViewModel by viewModels<ContactsViewModel>()
    private val CONTACTS_READ_REQ_CODE = 100
    private val CONTACTSwritE_WRITE_REQ_CODE = 99

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
    }

    private fun init() {
        val add_fab :FloatingActionButton = findViewById(R.id.add_fab)
        add_fab.setOnClickListener(){
            startActivity(Intent(applicationContext,AddContactActivity::class.java))
        }

        val  rvContacts : RecyclerView = findViewById(R.id.rvContacts);
        val adapter = ContactsAdapter(this)
        rvContacts.adapter = adapter
        adapter.notifyDataSetChanged()


        contactsViewModel.contactsLiveData.observe(this, Observer {
            adapter.contacts = it
        })
        if (hasPermission(Manifest.permission.READ_CONTACTS) && hasPermission(Manifest.permission.WRITE_CONTACTS)) {
            contactsViewModel.fetchContacts()
        } else {
            requestPermissionWithRationale(Manifest.permission.READ_CONTACTS, CONTACTS_READ_REQ_CODE, getString(R.string.contact_permission_rationale))
        }
    }
    fun requestwritepermission(){
        requestPermissionWithRationale(Manifest.permission.WRITE_CONTACTS, CONTACTSwritE_WRITE_REQ_CODE, getString(R.string.contact_permission_rationale))


    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CONTACTS_READ_REQ_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            requestwritepermission()
        }else if (requestCode == CONTACTSwritE_WRITE_REQ_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            contactsViewModel.fetchContacts()
        }
    }
}
