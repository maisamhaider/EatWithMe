package com.eatwithme.toori.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.eatwithme.toori.annotations.MyAnnotation
import com.eatwithme.toori.R
import com.eatwithme.toori.fragments.ChatListFragment
import com.eatwithme.toori.fragments.ChatSearchFragment
import com.eatwithme.toori.fragments.ChatSettingsFragment
import com.eatwithme.toori.models.UserModel
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_chat_activty.*

class ChatActivty : AppCompatActivity() {

    var userDatabaseReference: DatabaseReference? = null
    var firebaseUser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_activty)
        //chat main activity view,that i am finding. ❤👍✌😍🤦‍♂️
        val chat_VP: ViewPager = findViewById(R.id.chat_VP)
        val chat_TB: Toolbar = findViewById(R.id.chat_TB)
        val chat_TL: TabLayout = findViewById(R.id.chat_TL)

        //setting chat tab layout as chat activity ToolBar
        setSupportActionBar(chat_TB)
        supportActionBar!!.title = "" //setting no value if user name in not set //!! not-null assertion..(!!)converts any value to a non-null type and throws an exception if the value is null

        //set fragments in ViewPagerAdapter
        val viewPagerAdapter = ChatViewPagerAdapter(supportFragmentManager)
        viewPagerAdapter.setFragmentAndTitle(ChatListFragment(),"Chat")
        viewPagerAdapter.setFragmentAndTitle(ChatSearchFragment(),"Search")
        viewPagerAdapter.setFragmentAndTitle(ChatSettingsFragment(),"Settings")

        chat_VP.adapter = viewPagerAdapter
        chat_TL.setupWithViewPager(chat_VP)

// set user appbar small icon and user name
        userSmallNameAndProfile()
    }
    internal class ChatViewPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager,FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT){

       private val fragments: ArrayList<Fragment>
       private val fragmentTitle: ArrayList<String>

        init {
            fragments = ArrayList<Fragment>()
            fragmentTitle = ArrayList<String>()
        }

        override fun getItem(position: Int): Fragment {
            return fragments[position]
        }

        override fun getCount(): Int {
            return fragments.size
        }

        fun setFragmentAndTitle(fragment: Fragment, title:String)
        {
            fragments.add(fragment)
            fragmentTitle.add(title)
        }

        override fun getPageTitle(i: Int): CharSequence? {
            return fragmentTitle[i]
        }
    }

   private fun  userSmallNameAndProfile()
    {
        firebaseUser = FirebaseAuth.getInstance().currentUser
      userDatabaseReference = FirebaseDatabase.getInstance().reference.child(MyAnnotation.USER).child(firebaseUser!!.uid)

        // retrieve and display user profile👩‍👨 and name ..
        userDatabaseReference!!.addValueEventListener(object : ValueEventListener
        {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists())
                {
                    val user: UserModel? = snapshot.getValue(UserModel::class.java)
                    chat_user_name_small_TV.text = user!!.username
                    Picasso.get()
                        .load(user!!.profile)
                        .placeholder(R.drawable.ic_user)
                        .into(chat_user_profile_CIV)
                }
            }


        })



    }
}