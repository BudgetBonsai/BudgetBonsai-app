package com.example.budgetbonsai.ui.add

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.example.budgetbonsai.R
import com.example.budgetbonsai.databinding.FragmentAddBinding
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import java.io.FileNotFoundException

class AddFragment : Fragment() {

    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!

    private val UPLOAD_ACTION = 2001
    private val CAMERA_ACTION = 2003
    private val PERMISSION_ACTION = 2002

    private lateinit var photoImage: Bitmap
    private lateinit var firebaseImage: FirebaseVisionImage

    private val receiptsViewModel = ReceiptsViewModel()

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

        binding.btnAddwithcamera.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    PERMISSION_ACTION
                )
            } else {
                startActivityForResult(
                    receiptsViewModel.cameraIntent(requireContext()),
                    CAMERA_ACTION
                )
            }
        }

        binding.btnAddwithimage.setOnClickListener {
            startActivityForResult(receiptsViewModel.uploadIntent(), UPLOAD_ACTION)
        }
    }

    private fun uploadAction(data: Intent) {
        try {
            val stream = requireActivity().contentResolver.openInputStream(data.data!!)
            photoImage = BitmapFactory.decodeStream(stream)
            firebaseImage = FirebaseVisionImage.fromBitmap(photoImage)
            processImage()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
    }

    private fun cameraAction() {
        try {
            firebaseImage = FirebaseVisionImage.fromFilePath(requireContext(), receiptsViewModel.imageURI)
            processImage()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
    }

    private fun processImage() {
        receiptsViewModel.textDeviceDetector.processImage(firebaseImage)
            .addOnSuccessListener { result ->
                var text = ""
                for (block in result.textBlocks) text += block.text + "\n"
                val receipts = receiptsViewModel.getReceipts(text)
                val bundle = Bundle()
                bundle.putString("total", receipts.total)
                bundle.putString("name", receipts.name)
                findNavController().navigate(R.id.action_addFragment_to_addExpensesFragment, bundle)
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                UPLOAD_ACTION -> uploadAction(data!!)
                CAMERA_ACTION -> cameraAction()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_ACTION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED
            ) {
                binding.btnAddwithcamera.isEnabled = true
            }
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