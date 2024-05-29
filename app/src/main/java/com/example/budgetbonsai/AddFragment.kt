package com.example.budgetbonsai

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.budgetbonsai.databinding.FragmentAddBinding

class AddFragment : Fragment() {

    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAddexpenses.setOnClickListener {
            onAddExpensesClicked()
        }
    }
    private fun onAddExpensesClicked() {
        findNavController().navigate(R.id.action_addFragment_to_addExpensesFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}