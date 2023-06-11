package com.example.travelday_2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils.replace
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.travelday_2.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding
    lateinit var tabLayout:TabLayout
    lateinit var viewPager:ViewPager2
    private val sharedViewModel: SharedViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initLayout()

    }
    fun initLayout(){
        val homeFragment = TravelListParentFragment()
        val checkListFragment=CheckListFragment()
        val communityLoginFragment=CommunityLoginFragment()
        replaceFragment(homeFragment)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.calendar -> replaceFragment(homeFragment)
                R.id.checkList -> replaceFragment(checkListFragment)
                R.id.community -> replaceFragment(communityLoginFragment)
            }
            true
        }
    }


    fun replaceFragment(fragment: Fragment) {
    // 현 Activity 에 연결된 Fragment 관리하는 supportFragmentManager 를 통해 Fragment 전환
    supportFragmentManager.beginTransaction().apply {
        replace(R.id.fragmentMainContainer, fragment)
        commit()
            }
        }
    }
