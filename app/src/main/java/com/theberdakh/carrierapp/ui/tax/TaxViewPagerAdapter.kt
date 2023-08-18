package com.theberdakh.carrierapp.ui.tax

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class TaxViewPagerAdapter(private val list: ArrayList<Fragment>, fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount() = list.size
    override fun createFragment(position: Int) = list[position]
}