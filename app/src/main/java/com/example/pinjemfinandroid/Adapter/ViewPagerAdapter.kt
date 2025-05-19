package com.example.pinjemfinandroid.Adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.pinjemfinandroid.Fragment.HistoryPengajuanFragment
import com.example.pinjemfinandroid.Fragment.HomeFragment
import com.example.pinjemfinandroid.Fragment.ProfileFragment
import com.example.pinjemfinandroid.Fragment.TransactionFragment

class ViewPagerAdapter(activity: AppCompatActivity): FragmentStateAdapter(activity) {
    private val fragments = listOf(
        HomeFragment(),
        ProfileFragment(),
        TransactionFragment(),
        HistoryPengajuanFragment()
    )

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]
}