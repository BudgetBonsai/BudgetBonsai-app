package com.example.budgetbonsai.ui.wishlist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.budgetbonsai.databinding.FragmentAddWishlistBinding
import com.example.budgetbonsai.databinding.FragmentWishlistBinding
import com.example.budgetbonsai.databinding.FragmentWishlistDetailBinding

class WishlistDetailFragment : Fragment() {

    private var _binding: FragmentWishlistDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentWishlistDetailBinding.inflate(inflater, container, false)


        val btnBack = binding.backImageButton
        btnBack.setOnClickListener {
            // Use NavController to navigate back to the previous fragment
            findNavController().navigateUp()
        }
        return binding.root
    }

}