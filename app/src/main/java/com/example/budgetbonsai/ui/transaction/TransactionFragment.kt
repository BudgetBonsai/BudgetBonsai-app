package com.example.budgetbonsai.ui.transaction

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.budgetbonsai.R
import com.example.budgetbonsai.TransactionAdapter
import com.example.budgetbonsai.ViewPagerAdapter
import com.example.budgetbonsai.data.Repository
import com.example.budgetbonsai.data.local.UserPreference
import com.example.budgetbonsai.data.local.dataStore
import com.example.budgetbonsai.data.remote.ApiConfig
import com.example.budgetbonsai.data.remote.ApiService
import com.example.budgetbonsai.databinding.FragmentTransactionBinding
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import retrofit2.Retrofit

class TransactionFragment : Fragment() {

    private lateinit var binding: FragmentTransactionBinding
    private lateinit var pieChart: PieChart
    private lateinit var viewModel: TransactionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentTransactionBinding.inflate(layoutInflater)

        val userPreference = UserPreference.getInstance(requireContext().dataStore)
        val apiService = ApiConfig.getApiService(userPreference)
        val repository = TransactionRepository(apiService, userPreference)
        viewModel = ViewModelProvider(this, TransactionViewModelFactory(repository)).get(TransactionViewModel::class.java)

//        pieChart = binding.piechart

//        val list: ArrayList<PieEntry> = ArrayList()
//
//        list.add(PieEntry(30f, "Food"))
//        list.add(PieEntry(20f, "Transport"))
//        list.add(PieEntry(10f, "Entertainment"))
//        list.add(PieEntry(40f, "Others"))
//
//        val pieDataset = PieDataSet(list, "Expenses")
//        pieDataset.setColors(ColorTemplate.MATERIAL_COLORS, 255)
//        pieDataset.valueTextSize = 15f
//        pieDataset.valueTextColor = Color.BLACK
//
//        val pieData = PieData(pieDataset)
//
//        pieChart.data = pieData
//
//        pieChart.animateY(2000)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_transaction, container, false)
        binding = FragmentTransactionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTabLayoutAndViewPager()

        val pieChart = view.findViewById<PieChart>(R.id.piechart)
        val tabLayout = view.findViewById<TabLayout>(R.id.tabLayout)
        val viewPager = view.findViewById<ViewPager2>(R.id.viewPager)

        val list: ArrayList<PieEntry> = ArrayList()

        list.add(PieEntry(30f, "Food"))
        list.add(PieEntry(20f, "Transport"))
        list.add(PieEntry(10f, "Entertainment"))
        list.add(PieEntry(40f, "Others"))

        val pieDataset = PieDataSet(list, "Expenses")
        pieDataset.setColors(ColorTemplate.MATERIAL_COLORS, 255)
        pieDataset.valueTextSize = 15f
        pieDataset.valueTextColor = Color.BLACK

        val pieData = PieData(pieDataset)

        pieChart.data = pieData

        pieChart.description.text = "Expenses Pie Chart"
        pieChart.animateY(1000)

        val adapter = ViewPagerAdapter(this)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "Daily"
                }
                1 -> {
                    tab.text = "Weekly"
                }
                2 -> {
                    tab.text = "Monthly"
                }
            }
        }.attach()

//        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
//
//            override fun onTabSelected(tab: TabLayout.Tab?) {
//                // Handle tab select
//            }
//
//            override fun onTabReselected(tab: TabLayout.Tab?) {
//                // Handle tab reselect
//            }
//
//            override fun onTabUnselected(tab: TabLayout.Tab?) {
//                // Handle tab unselect
//            }
//        })

    }

    private fun setupTabLayoutAndViewPager() {
        val tabLayout = binding.tabLayout
        val viewPager = binding.viewPager

        val adapter = ViewPagerAdapter(this)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Daily"
                1 -> "Weekly"
                2 -> "Monthly"
                else -> null
            }
        }.attach()
    }
}