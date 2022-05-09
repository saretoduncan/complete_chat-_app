package com.example.chatapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapp.databinding.RecieveBinding
import com.google.firebase.auth.FirebaseAuth

class MessageAdapter(val context:Context, val messageList:ArrayList<Message>):RecyclerView.Adapter<RecyclerView.ViewHolder>()  {

    private val ITEM_RECIEVE=1
    private val ITEM_SENT=2
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
     if(viewType==1){
        val view = LayoutInflater.from(context).inflate(R.layout.recieve,parent, false)
        return ReceiveViewHolder(view)
     }else{
         val view = LayoutInflater.from(context).inflate(R.layout.sent,parent, false)
         return SentViewHolder(view)
     }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMessage = messageList[position]
        if(holder.javaClass == SentViewHolder::class.java){
        //do staff for sent view holder

         val viewHolder = holder as SentViewHolder
         holder.sentMessage.text= currentMessage.message
     }else{
           val viewHolder = holder as ReceiveViewHolder
         holder.receive.text= currentMessage.message
     }
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage= messageList[position]
        if(FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.senderId)){
            return ITEM_SENT
        }else {
            return ITEM_RECIEVE

        }
    }
    override fun getItemCount(): Int {
        return messageList.size
    }
    class SentViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val sentMessage = itemView.findViewById<TextView>(R.id.tvSentMessage)
    }
    class ReceiveViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val receive = itemView.findViewById<TextView>(R.id.tvrecieveMessage)
    }
}