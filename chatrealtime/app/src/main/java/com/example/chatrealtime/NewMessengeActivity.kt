package com.example.chatrealtime


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import com.example.chatrealtime.models.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.Group
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_new_messenge.*
import kotlinx.android.synthetic.main.activity_register.view.*
import kotlinx.android.synthetic.main.user_row_newmessenge.view.*

class NewMessengeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_messenge)

        supportActionBar?.title="Select User"

        val adapter=GroupAdapter<GroupieViewHolder>()

        recyclerview_newmessenge.adapter=adapter

        fecthUsers()
    }
    companion object{
        val USER_KEY="USER_KEY"
    }
    private fun fecthUsers(){
        val ref=FirebaseDatabase.getInstance().getReference("/users")
        ref.addListenerForSingleValueEvent(object :ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
                val adapter=GroupAdapter<GroupieViewHolder>()

                snapshot.children.forEach{
                    val user=it.getValue(User::class.java)
                    if(user!=null){
                        adapter.add(UserItem(user))
                    }

                }
                adapter.setOnItemClickListener{

                    item,view->

                    val itemUser=item as UserItem
                    val intent=Intent(view.context,ChatLogActivity::class.java)
                    intent.putExtra(USER_KEY,itemUser.user)
                    startActivity(intent)

                    finish()
                }
                recyclerview_newmessenge.adapter=adapter
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}
class UserItem(val user: User): Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.username_textview_new_messenge.text=user.username

        Picasso.get().load(user.profileImageUrl).into(viewHolder.itemView.imageView_new_messenge)

    }

    override fun getLayout(): Int {
        return R.layout.user_row_newmessenge
    }

}