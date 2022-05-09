package com.example.chatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapp.databinding.ActivityChatBinding
import com.example.chatapp.databinding.ActivitySignupactivityBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import java.util.ArrayList

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var messageList:ArrayList<Message>
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var dbRef:DatabaseReference
    var recieverRoom:String?= null
    var senderRoom :String?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val username = intent.getStringExtra("name")
        val recieverUid = intent.getStringExtra("uid")
         val senderUid = FirebaseAuth.getInstance().currentUser?.uid
       supportActionBar?.title=username
        Toast.makeText(this, username, Toast.LENGTH_LONG).show()
        senderRoom=recieverUid+senderUid
        recieverRoom= senderUid+recieverUid
        dbRef= FirebaseDatabase.getInstance().getReference()

        chatRecyclerView=binding.charRecyclerView
        val messageBox= binding.messageUp
        messageList= ArrayList()
        messageAdapter= MessageAdapter(this, messageList)
        chatRecyclerView.layoutManager= LinearLayoutManager(this)
        chatRecyclerView.adapter =  messageAdapter
        //logic for add data to recyclerview
        dbRef.child("chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()
                    for(postSnap in snapshot.children){

                        val message = postSnap.getValue(Message::class.java)

                        messageList.add(message!!)
                    }
                    messageAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

        //logic for adding adding data to db
        binding.sendBtn.setOnClickListener {
           val message = messageBox.text.toString()
            val messageObject = Message(message,senderUid)
            dbRef.child("chats").child(senderRoom!!).child("messages").push()
                .setValue(messageObject).addOnSuccessListener {
                   dbRef.child("chats").child(recieverRoom!!).child("messages").push()
                       .setValue(messageObject)
                }
            messageBox.setText("")
        }
    }
}