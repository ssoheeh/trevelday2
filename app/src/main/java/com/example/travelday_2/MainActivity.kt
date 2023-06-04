package com.example.travelday_2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.travelday_2.databinding.ActivityMainBinding
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
        setTabLayout()
        setFramgnet()
    }

    private fun setFramgnet() {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction().apply {
            val traveladdFragment = TraveladdFragment()
            val dateListFragment=DateListFragment()
            val dailyFragment=DailyFragment()
            val countryFragment=CountryFragment()
            add(R.id.frag_container, traveladdFragment)
            add(R.id.frag_container, dateListFragment)
            add(R.id.frag_container, dailyFragment)
            add(R.id.frag_container, countryFragment)
                .hide(dateListFragment)
                .hide(dailyFragment)
                .hide(countryFragment)
                .show(traveladdFragment)
        }


    }
    private fun switchFragment(currentFragment: Fragment?, newFragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()

        if (currentFragment != null) {
            fragmentTransaction.hide(currentFragment)
        }
        if (newFragment.isAdded) {
            fragmentTransaction.show(newFragment)
        } else {
            fragmentTransaction.add(R.id.frag_container, newFragment)
        }
        fragmentTransaction.addToBackStack(null).commit()
    }


    private fun setTabLayout() {
        val tabCount=3
        val viewPagerAdapter=ViewPagerAdapter(supportFragmentManager, lifecycle, tabCount)
        binding.viewPager2.adapter = viewPagerAdapter
        TabLayoutMediator(binding.tabLayout,binding.viewPager2){tab, position->
            when(position){
                0->tab.text="준비물"
                1->tab.text="일정"
                2->tab.text="여행기"

        }
    }.attach()
}}