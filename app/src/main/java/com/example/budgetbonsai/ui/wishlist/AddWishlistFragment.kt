package com.example.budgetbonsai.ui.wishlist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.example.budgetbonsai.Category
import com.example.budgetbonsai.R
import com.example.budgetbonsai.databinding.FragmentAddWishlistBinding

class AddWishlistFragment : Fragment() {

    private var _binding: FragmentAddWishlistBinding? = null
    private val binding get() = _binding!!

    private val pickMediaLauncher = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        uri?.let {
            // Use the URI to load the image into the ImageView
            binding.savingImage.setImageURI(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_add_wishlist, container, false)
        _binding = FragmentAddWishlistBinding.inflate(inflater, container, false)


        val btnBack = binding.backImageButton
        btnBack.setOnClickListener {
            // Use NavController to navigate back to the previous fragment
            findNavController().navigateUp()
        }
        return binding.root
//        toggleButton.addOnButtonCheckedListener { toggleButton, checkedId, isChecked ->
//            // Respond to button selection
//        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.savingImage.setOnClickListener {
            pickMediaLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

}