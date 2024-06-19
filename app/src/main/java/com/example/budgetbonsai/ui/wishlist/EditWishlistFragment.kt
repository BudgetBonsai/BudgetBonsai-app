package com.example.budgetbonsai.ui.wishlist

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.budgetbonsai.data.local.UserPreference
import com.example.budgetbonsai.data.local.dataStore
import com.example.budgetbonsai.data.remote.ApiConfig
import com.example.budgetbonsai.data.remote.response.WishlistItem
import com.example.budgetbonsai.databinding.FragmentEditWishlistBinding
import com.example.budgetbonsai.repository.WishlistRepository
import com.google.android.material.snackbar.Snackbar
import com.example.budgetbonsai.utils.Result
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class EditWishlistFragment : Fragment() {

    private var _binding: FragmentEditWishlistBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: WishlistViewModel
    private lateinit var userPreference: UserPreference

    private val pickMediaLauncher = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        uri?.let {
            // Use the URI to load the image into the ImageView
            binding.savingImage.setImageURI(it)
            viewModel.imageUri = it
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditWishlistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Ambil data dari Bundle
        val wishlistItem: WishlistItem? = arguments?.getParcelable("wishlist_item")

        wishlistItem?.let {
            Log.d("EditWishlistFragment", "Received item for edit: ${it.id}")
            binding.editTextName.setText(it.name)
            binding.editTextAmount.setText(it.amount.toString())
            binding.editTextSavingPlan.setText(it.savingPlan)
        }

        userPreference = UserPreference.getInstance(requireContext().dataStore)

        val apiService = ApiConfig.getApiService(userPreference)
        val repository = WishlistRepository(apiService)
        val viewModelFactory = WishlistViewModel.WishlistViewModelFactory(repository, requireContext())
        viewModel = ViewModelProvider(this, viewModelFactory).get(WishlistViewModel::class.java)

        binding.btnEditwishlist.setOnClickListener {
            editWishlist(wishlistItem)
        }

        binding.savingImage.setOnClickListener {
            pickMediaLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        viewModel.editWishlistResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    Snackbar.make(requireView(), "Wishlist updated successfully", Snackbar.LENGTH_SHORT).show()
                    // Optionally, navigate back to previous fragment or perform other actions
                }
                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Snackbar.make(requireView(), "Error: ${result.error}", Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun editWishlist(item: WishlistItem?) {
        item?.let {
            val name = binding.editTextName.text.toString().trim()
            val amount = binding.editTextAmount.text.toString().toIntOrNull() ?: 0
            val savingPlan = binding.editTextSavingPlan.text.toString().trim()

            if (name.isNotEmpty() && amount != null && savingPlan.isNotEmpty() && viewModel.imageUri != null) {
                val imageFile = uriToFile(viewModel.imageUri!!, requireContext())
                val requestFile = RequestBody.create("image/jpeg".toMediaTypeOrNull(), imageFile)
                val body = MultipartBody.Part.createFormData("file", imageFile.name, requestFile)

                Log.d("EditWishlistFragment", "Sending edit request for item ID: ${item.id}")

                viewModel.editWishlist(
                    item.id.toString(),
                    name,
                    amount,
                    savingPlan,
                    "Weekly",
                    viewModel.imageUri.toString()
                )
            } else {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun uriToFile(uri: Uri, context: Context): File {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context.contentResolver.query(uri, projection, null, null, null)
        val columnIndex = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor?.moveToFirst()
        val filePath = cursor?.getString(columnIndex ?: 0)
        cursor?.close()
        return File(filePath)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
