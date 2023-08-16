package com.theberdakh.carrierapp.ui.user

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.theberdakh.carrierapp.R
import com.theberdakh.carrierapp.databinding.FragmentSellerFormBinding
import com.theberdakh.carrierapp.util.makeToast

class FormFragment: Fragment(R.layout.fragment_seller_form) {
    private lateinit var binding: FragmentSellerFormBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSellerFormBinding.bind(view)

        initViews()
        initListeners()

    }

    private fun initViews() {
        val documentTypeAdapter = ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_dropdown_item, arrayOf("Passport", "ID"))
        binding.atvDocumentType.setAdapter(documentTypeAdapter)
        binding.atvDocumentType.setOnItemClickListener { parent, view, position, id ->
            makeToast(parent.getItemAtPosition(position).toString())
        }

        val unitsAdapter = ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_dropdown_item, arrayOf("KG", "m3"))
        binding.atvCargoUnit.setAdapter(unitsAdapter)
        binding.atvCargoUnit.setOnItemClickListener { parent, view, position, id ->
            makeToast(parent.getItemAtPosition(position).toString())
        }
        val cargoTypeAdapter = ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_dropdown_item, arrayOf("Sheben", "Shege qum"))
        binding.atvCargoType.setAdapter(cargoTypeAdapter)
        binding.atvCargoType.setOnItemClickListener { parent, view, position, id ->
            makeToast(parent.getItemAtPosition(position).toString())
        }
    }

    private fun initListeners() {
        binding.tbForm.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.ivFormImage.setOnClickListener {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (takePictureIntent.resolveActivity(requireActivity().packageManager) != null) {
                startActivityForResult(takePictureIntent, 42)
            } else {
                makeToast("Unable to open camera")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 42 && resultCode == Activity.RESULT_OK) {
            val takenImage = data?.extras?.get("data") as Bitmap
            binding.ivFormImage.setImageBitmap(takenImage)
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}