package com.example.chatrealtime

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.chatrealtime.models.ChatMessage
import com.example.chatrealtime.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_chat_log.*
import kotlinx.android.synthetic.main.activity_new_messenge.*
import kotlinx.android.synthetic.main.chat_from_row.view.*
import kotlinx.android.synthetic.main.chat_to_row.view.*
import kotlinx.android.synthetic.main.user_row_newmessenge.view.*
import kotlin.math.log

class ChatLogActivity : AppCompatActivity() {
    val adapter=GroupAdapter<GroupieViewHolder>()
    var toUser:User?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)
        recyclerview_chat_log.adapter=adapter

         toUser=intent.getParcelableExtra<User>(NewMessengeActivity.USER_KEY)
        supportActionBar?.title=toUser?.username

//        setupDummyData()
listenForMessages()

        send_button_chat_log.setOnClickListener{
            performSendMessage()
        }

    }
    private fun listenForMessages(){
val fromId=LatestMessengesActivity.currentUser?.uid
        val toId=toUser?.uid
        val ref=FirebaseDatabase.getInstance().getReference("/messages/$fromId/$toId")
        ref.addChildEventListener(object :ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {

                val chatMessage=snapshot.getValue(ChatMessage::class.java)
                chatMessage?.text?.let { Log.d("Chatlog", it) }
                if(chatMessage!=null)
                {




                    if(chatMessage.fromId==fromId){
                        adapter.add(ChatToItem(chatMessage.text))
                    }
                    else
                    {
                        adapter.add(ChatFromItem(chatMessage.text, toUser!!))
                    }
                    recyclerview_chat_log.scrollToPosition(adapter.itemCount-1)
                }


            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildRemoved(snapshot: DataSnapshot) {

            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })


    }
    private fun performSendMessage(){
        val text=edittext_chat_log.text.toString()
        val fromId=FirebaseAuth.getInstance().uid
        val user=intent.getParcelableExtra<User>(NewMessengeActivity.USER_KEY)
        val toId=user?.uid
        if(fromId==null||toId==null) return
        val reference=FirebaseDatabase.getInstance().getReference("/messages/$fromId/$toId").push()
        val toReference=FirebaseDatabase.getInstance().getReference("/messages/$toId/$fromId").push()

        val chatMessage=ChatMessage(reference.key!!,text,fromId,toId,System.currentTimeMillis()/1000)

        reference.setValue(chatMessage).addOnSuccessListener {
            Log.d("Chatlog","Success")

        }
        toReference.setValue(chatMessage).addOnSuccessListener {

            edittext_chat_log.text.clear()
            recyclerview_chat_log.scrollToPosition(adapter.itemCount-1)
        }
        val latestReferenceRef=FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId/$toId")
        val latestReferenceToRef=FirebaseDatabase.getInstance().getReference("/latest-messages/$toId/$fromId")
        latestReferenceRef.setValue(chatMessage)
        latestReferenceToRef.setValue(chatMessage)

    }


}
class ChatFromItem(val text:String,val user: User): Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

viewHolder.itemView.textview_form_row.text=text
        Picasso.get().load(user.profileImageUrl).into(viewHolder.itemView.circlerview_from_row)
    }

    override fun getLayout(): Int {
        return R.layout.chat_from_row
    }

}
class ChatToItem(val text:String): Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
viewHolder.itemView.textview_to_row.text=text


    }

    override fun getLayout(): Int {
        return R.layout.chat_to_row
    }

}
