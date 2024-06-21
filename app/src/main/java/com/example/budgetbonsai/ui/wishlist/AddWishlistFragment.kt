package com.example.budgetbonsai.ui.wishlist

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.budgetbonsai.R
import com.example.budgetbonsai.data.local.UserPreference
import com.example.budgetbonsai.data.local.dataStore
import com.example.budgetbonsai.data.remote.ApiConfig
import com.example.budgetbonsai.databinding.FragmentAddWishlistBinding
import com.example.budgetbonsai.repository.WishlistRepository
import com.example.budgetbonsai.utils.Result
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class AddWishlistFragment : Fragment() {

    private var _binding: FragmentAddWishlistBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: WishlistViewModel
    private lateinit var userPreference: UserPreference

    private val pickMediaLauncher = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        uri?.let {
            binding.savingImage.setImageURI(it)
            viewModel.imageUri = it
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddWishlistBinding.inflate(inflater, container, false)

        userPreference = UserPreference.getInstance(requireContext().dataStore)
        val apiService = ApiConfig.getApiService(userPreference)
        val repository = WishlistRepository(apiService)
        viewModel = ViewModelProvider(this, WishlistViewModel.WishlistViewModelFactory(repository, requireContext())).get(WishlistViewModel::class.java)


        val btnBack = binding.backImageButton
        btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnAddwishlist.setOnClickListener {
            addWishlist()
        }

        viewModel.addWishlistResult.observe(viewLifecycleOwner) { result ->
            when(result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    findNavController().navigateUp()
                }
                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                }
            }
        }

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.savingImage.setOnClickListener {
            pickMediaLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.toggleButton.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.btn_daily -> {
                        viewModel.selectedWishlistType = "Daily"
                    }

                    R.id.btn_weekly -> {
                        viewModel.selectedWishlistType = "Weekly"
                    }

                    R.id.btn_monthly -> {
                        viewModel.selectedWishlistType = "Monthly"
                    }
                }
            }
        }
    }

    private fun addWishlist() {
        val name = binding.wishlistname.editText?.text.toString()
        val savingPlan = binding.wishlistsaving.editText?.text.toString()

        if (name.isNotEmpty()  && savingPlan.isNotEmpty() && viewModel.imageUri != null) {
            val imageFile = uriToFile(viewModel.imageUri!!, requireContext())
            val requestFile = RequestBody.create("image/jpeg".toMediaTypeOrNull(), imageFile)
            val body = MultipartBody.Part.createFormData("file", imageFile.name, requestFile)

            viewModel.addWishlist(name, 0, savingPlan, viewModel.selectedWishlistType, viewModel.imageUri.toString())
        } else {
            Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
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