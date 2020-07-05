package com.eatwithme.toori.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.eatwithme.toori.R
import com.eatwithme.toori.fragments.ChatListFragment
import com.eatwithme.toori.fragments.ChatSearchFragment
import com.eatwithme.toori.fragments.ChatSettingsFragment
import com.google.android.material.tabs.TabLayout

class ChatActivty : AppCompatActivity() {
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
        val fragmentManager = supportFragmentManager
        val viewPagerAdapter = ChatViewPagerAdapter(fragmentManager)
        viewPagerAdapter.setFragmentAndTitle(ChatListFragment(),"Chat")
        viewPagerAdapter.setFragmentAndTitle(ChatSearchFragment(),"Search")
        viewPagerAdapter.setFragmentAndTitle(ChatSettingsFragment(),"Settings")

        chat_VP.adapter = viewPagerAdapter
        chat_TL.setupWithViewPager(chat_VP)
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

        override fun getPageTitle(pstion: Int): CharSequence? {
            return super.getPageTitle(pstion)
        }
    }
}