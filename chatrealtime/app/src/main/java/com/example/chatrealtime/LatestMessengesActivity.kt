package com.example.chatrealtime

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.chatrealtime.models.ChatMessage
import com.example.chatrealtime.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_latest_messenges.*
import kotlinx.android.synthetic.main.latest_message_row.view.*

class LatestMessengesActivity : AppCompatActivity() {
    companion object {
        var currentUser: User? = null
    }
    var adapter=GroupAdapter<GroupieViewHolder>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_latest_messenges)
        recyclerview_latest_message.addItemDecoration(DividerItemDecoration(this,DividerItemDecoration.VERTICAL))
        listenForLatestMessages()
        fecthCurrentUser()
        verifyUserIsLoggedIn()
        recyclerview_latest_message.adapter=adapter
        adapter.setOnItemClickListener{
            item,view->
            val row=item as LastestMessageRow
            val intent=Intent(this,ChatLogActivity::class.java)
            intent.putExtra(NewMessengeActivity.USER_KEY,row.chatPartnerUser)
            startActivity(intent)
        }
    }
    class LastestMessageRow(val chatMessage: ChatMessage):Item<GroupieViewHolder>(){
        var chatPartnerUser:User?=null
        override fun bind(viewHolder: GroupieViewHolder, position: Int) {
            var text=chatMessage.text
            if(chatMessage.fromId== currentUser?.uid) text="You: "+text
            viewHolder.itemView.latest_message_textview.text=text
            val chatPartnerId:String
            if(chatMessage.fromId==FirebaseAuth.getInstance().uid)
            {
                chatPartnerId=chatMessage.toId
            }
            else{
                chatPartnerId=chatMessage.fromId
            }
            val ref=FirebaseDatabase.getInstance().getReference("/users/$chatPartnerId")
            ref.addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    chatPartnerUser=snapshot.getValue(User::class.java)
                    viewHolder.itemView.name_textview.text=chatPartnerUser?.username
                    Picasso.get().load(chatPartnerUser?.profileImageUrl).into(viewHolder.itemView.circleview_latest_message_row)
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
        }

        override fun getLayout(): Int {
            return R.layout.latest_message_row
        }

    }
val latestMessageMap=HashMap<String,ChatMessage>()
    private fun refreshRecyclerViewMessages(){
        adapter.clear()
        latestMessageMap.values.forEach{
            adapter.add(LastestMessageRow(it))
        }
    }
    private fun listenForLatestMessages(){
        val fromId=FirebaseAuth.getInstance().uid
        val ref=FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId")
        ref.addChildEventListener(object :ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
val chatMessage=snapshot.getValue(ChatMessage::class.java)?:return
                latestMessageMap[snapshot.key!!]=chatMessage
                refreshRecyclerViewMessages()
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage=snapshot.getValue(ChatMessage::class.java)?:return
                latestMessageMap[snapshot.key!!]=chatMessage
                refreshRecyclerViewMessages()
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {

            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
    private fun fecthCurrentUser() {
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                currentUser = snapshot.getValue(User::class.java)


            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    private fun verifyUserIsLoggedIn() {
        val uid = FirebaseAuth.getInstance().uid
        if (uid == null) {
            val intent = Intent(this, RegisterActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.menu_new_messenge -> {
                val intent = Intent(this, NewMessengeActivity::class.java)
                startActivity(intent)
            }
            R.id.menu_sign_out -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, RegisterActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
}