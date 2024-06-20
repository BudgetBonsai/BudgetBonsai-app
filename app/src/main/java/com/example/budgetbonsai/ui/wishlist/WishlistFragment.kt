package com.example.budgetbonsai.ui.wishlist

import android.app.AlarmManager
import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.budgetbonsai.R
import com.example.budgetbonsai.data.local.UserPreference
import com.example.budgetbonsai.data.local.dataStore
import com.example.budgetbonsai.data.remote.ApiConfig
import com.example.budgetbonsai.data.remote.response.WishlistItem
import com.example.budgetbonsai.databinding.FragmentWishlistBinding
import com.example.budgetbonsai.repository.WishlistRepository
import com.example.budgetbonsai.ui.NotificationScheduler
import com.example.budgetbonsai.utils.Result
import com.google.android.material.floatingactionbutton.FloatingActionButton

class WishlistFragment : Fragment() {

    private var _binding: FragmentWishlistBinding? = null
    private val binding get() = _binding!!
    private var recyclerView: RecyclerView? = null
    private lateinit var viewModel: WishlistViewModel
    private lateinit var userPreference: UserPreference
    private lateinit var adapter: WishlistAdapter

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWishlistBinding.inflate(inflater, container, false)
        val view = binding.root

        userPreference = UserPreference.getInstance(requireContext().dataStore)
        adapter = WishlistAdapter(requireContext(), emptyList(), { item ->
            showDeleteConfirmationDialog(item)
        }, { item ->
            showEditWishlistFragment(item)
        }, { item ->
            showDepositDialog(item)
        })
        binding.rvWishlist.layoutManager = LinearLayoutManager(requireContext())
        binding.rvWishlist.adapter = adapter

        val apiService = ApiConfig.getApiService(userPreference)
        val repository = WishlistRepository(apiService)
        viewModel = ViewModelProvider(this, WishlistViewModel.WishlistViewModelFactory(repository, requireContext())).get(WishlistViewModel::class.java)

        viewModel.wishlistLiveData.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    adapter.setItems(result.data)

                    result.data.forEach { item ->
                        item.type?.let {
                            NotificationWorker.scheduleNotification(requireContext(), it)
                        }
                    }
                }
                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(context, "No Wishlist Available", Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.deleteWishlistResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), result.data.message, Toast.LENGTH_SHORT).show()
                    viewModel.fetchWishlist()
                }
                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), "error", Toast.LENGTH_SHORT).show()
                }
            }
        }

//        viewModel.editWishlistResult.observe(viewLifecycleOwner) { result ->
//            when (result) {
//                is Result.Loading -> {
//                    // Show loading indicator
//                }
//                is Result.Success -> {
//                    // Wishlist edited successfully
//                    Toast.makeText(context, "Wishlist updated successfully", Toast.LENGTH_SHORT).show()
//                    viewModel.fetchWishlist() // Refresh the wishlist
//                }
//                is Result.Error -> {
//                    // Handle error
//                    Log.e("WishlistFragment", "Error editing wishlist: ${result.error}")
//                    Toast.makeText(context, "Failed to update wishlist", Toast.LENGTH_SHORT).show()
//                }
//            }
//        }

        viewModel.editWishlistResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), "Deposit successful", Toast.LENGTH_SHORT).show()
                    viewModel.fetchWishlist() // Refresh the wishlist
                }
                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    val errorMessage = "Failed to deposit amount: ${result.error}"
                    Log.e("WishlistFragment", errorMessage)
                    Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = binding.rvWishlist
        recyclerView?.layoutManager = LinearLayoutManager(context)

        val fab = binding.fabAdd
        fab.setOnClickListener {
            findNavController().navigate(R.id.action_wishlistFragment_to_addFragment)
        }
    }

    private fun showDeleteConfirmationDialog(item: WishlistItem) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Wishlist")
            .setMessage("Are you sure you want to delete this wishlist?")
            .setPositiveButton("Yes") { dialog, _ ->
                item.id?.let { viewModel.deleteWishlist(it) }
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showEditWishlistFragment(item: WishlistItem) {
        Log.d("WishlistFragment", "Sending item to edit: ${item.id}")
        val bundle = Bundle().apply {
            putParcelable("wishlist_item", item)
        }
        findNavController().navigate(R.id.action_wishlistFragment_to_editWishlistFragment, bundle)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showDepositDialog(item: WishlistItem) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_deposit, null)
        val editTextDepositAmount = dialogView.findViewById<EditText>(R.id.edit_text_deposit_amount)

        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Deposit Amount")
            .setView(dialogView)
            .setPositiveButton("Deposit") { _, _ ->
                val additionalAmount = editTextDepositAmount.text.toString().toIntOrNull()
                if (additionalAmount != null) {
                    viewModel.updateWishlistAmount(item.id!!, item, additionalAmount)
                } else {
                    Toast.makeText(requireContext(), "Invalid amount", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .create()

        dialog.show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun depositAmount(item: WishlistItem, amount: Int) {
        viewModel.depositWishlistAmount(item.id!!, amount)
        viewModel.depositResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    // Handle loading state if needed
                }
                is Result.Success -> {
                    // Handle success state if needed
                    Toast.makeText(requireContext(), "Deposit successful", Toast.LENGTH_SHORT).show()
                }
                is Result.Error -> {
                    // Handle error state
                    val errorMessage = "Failed to deposit amount: ${result.error}"
                    Log.e("WishlistFragment", errorMessage)
                    Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun calculateIntervalInMillis(type: String): Long {
        return when (type) {
            "Daily" -> AlarmManager.INTERVAL_DAY
            "Weekly" -> AlarmManager.INTERVAL_DAY * 7
            "Monthly" -> AlarmManager.INTERVAL_DAY * 30
            else -> AlarmManager.INTERVAL_DAY // Default, misalnya jika tidak ada nilai yang cocok
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchWishlist()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        recyclerView = null
    }
}
