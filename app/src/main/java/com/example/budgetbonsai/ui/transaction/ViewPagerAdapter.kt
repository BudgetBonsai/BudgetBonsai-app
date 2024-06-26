package com.example.budgetbonsai.ui.transaction

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.budgetbonsai.data.remote.response.DataItem
import com.example.budgetbonsai.ui.transaction.DailyFragment
import com.example.budgetbonsai.ui.transaction.MonthlyFragment
import com.example.budgetbonsai.ui.transaction.WeeklyFragment

class ViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private val fragmentList = listOf(
        DailyFragment(),
        WeeklyFragment(),
        MonthlyFragment()
    )

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }
}