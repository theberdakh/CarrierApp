package com.theberdakh.carrierapp.ui.tax

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.theberdakh.carrierapp.R
import com.theberdakh.carrierapp.databinding.FragmentTaxFormBinding
import com.theberdakh.carrierapp.util.makeToast
import java.io.ByteArrayOutputStream

class TaxFormFragment : Fragment(R.layout.fragment_tax_form) {
    lateinit var binding: FragmentTaxFormBinding

    private var _encoded: String? = null
    private val encoded get() = _encoded!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTaxFormBinding.bind(view)

        initViews()
        initListeners()

    }

    private fun initViews() {

        val documentTypeAdapter = ArrayAdapter(
            requireActivity(),
            android.R.layout.simple_spinner_dropdown_item,
            arrayOf("Passport", "ID")
        )
        binding.atvDocumentType.setAdapter(documentTypeAdapter)
        binding.atvDocumentType.setOnItemClickListener { parent, view, position, id ->
            makeToast(parent.getItemAtPosition(position).toString())
        }

        val unitsAdapter = ArrayAdapter(
            requireActivity(),
            android.R.layout.simple_spinner_dropdown_item,
            arrayOf("Mag'liwmat kiritilmegen", "Mag'liwmat toliq emes")
        )
        binding.atViolationType.setAdapter(unitsAdapter)
        binding.atViolationType.setOnItemClickListener { parent, view, position, id ->
            makeToast(parent.getItemAtPosition(position).toString())
        }
        val cargoTypeAdapter = ArrayAdapter(
            requireActivity(),
            android.R.layout.simple_spinner_dropdown_item,
            arrayOf("Sheben", "Shege qum")
        )
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

            val byteArrayOutputStream = ByteArrayOutputStream()
            takenImage.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
            val byteArray = byteArrayOutputStream.toByteArray()
            _encoded = Base64.encodeToString(byteArray, Base64.DEFAULT)
            Log.d("Image", encoded)


        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}