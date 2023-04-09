package com.example.oqueue

import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase

class History : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var backButton: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        backButton = findViewById(R.id.back_arrow)
        backButton.setOnClickListener {
            finish()
        }

        recyclerView = findViewById(R.id.recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = HistoryAdapter(mutableListOf(), this)

        val database = FirebaseDatabase.getInstance().getReference("services")
        database.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                val service = dataSnapshot.getValue(Service::class.java)
                service?.let {
                    (recyclerView.adapter as? HistoryAdapter)?.addService(it)
                }
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
                val service = dataSnapshot.getValue(Service::class.java)
                service?.let {
                    (recyclerView.adapter as? HistoryAdapter)?.updateService(it)
                }
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                val service = dataSnapshot.getValue(Service::class.java)
                service?.let {
                    (recyclerView.adapter as? HistoryAdapter)?.removeService(it)
                }
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {
                // Not implemented
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(
                    applicationContext,
                    "Fatal error during retrieval of data",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}

