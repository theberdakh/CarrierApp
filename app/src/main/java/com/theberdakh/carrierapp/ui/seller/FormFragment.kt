package com.theberdakh.carrierapp.ui.seller

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.LocationServices
import com.theberdakh.carrierapp.R
import com.theberdakh.carrierapp.data.local.SharedPrefStorage
import com.theberdakh.carrierapp.data.model.response.order.PostOrder
import com.theberdakh.carrierapp.databinding.FragmentSellerFormBinding
import com.theberdakh.carrierapp.presentation.SellerViewModel
import com.theberdakh.carrierapp.util.checkLocationPermissions
import com.theberdakh.carrierapp.util.getCurrentDate
import com.theberdakh.carrierapp.util.getNotNullText
import com.theberdakh.carrierapp.util.makeToast
import com.theberdakh.carrierapp.util.setCustomAdapter
import com.theberdakh.carrierapp.util.setErrorText
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.ByteArrayOutputStream
import java.time.LocalDateTime


class FormFragment : Fragment(R.layout.fragment_seller_form) {
    private lateinit var binding: FragmentSellerFormBinding
    private val viewModel by viewModel<SellerViewModel>()
    private var _encoded: String? = null
    private var location = ""
    private val encoded get() = _encoded!!

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSellerFormBinding.bind(view)


        initViews()
        initListeners()
        initObservers()

        if (requireActivity().checkLocationPermissions()){
            val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
            val location = fusedLocationProviderClient.lastLocation
            location.addOnSuccessListener {
                if (it != null){
                    val lat = it.latitude.toString()
                    val long = it.longitude.toString()
                    val locationText = "$lat, $long"
                    this.location = locationText
                }
            }
        }

    }


    private fun initObservers() {
        viewModel.postOrderSuccessFlow.onEach {
            makeToast("Posted successfully")
            Log.d("Post Success", "Success ${it.driver_name}")

            findNavController().popBackStack()
        }.launchIn(lifecycleScope)

        viewModel.postOrderMessageFlow.onEach {
            makeToast(it)
        }.launchIn(lifecycleScope)

        viewModel.postOrderErrorFlow.onEach {
            makeToast("Error, check your Internet connection")
        }.launchIn(lifecycleScope)
    }


    private fun initViews() {


        binding.tilCarrierTrailerWeight.isVisible = false

        binding.atvDocumentType.setCustomAdapter(listOf("ID", "Passport"))
        binding.atvCargoUnit.setCustomAdapter(listOf("Kg", "M3"))
        binding.atvCargoType.setCustomAdapter(listOf("Sheben", "Shege qum"))
        binding.atvCarrierTrailer.setCustomAdapter(listOf("Joq", "Bar"))


        binding.etAutoNumber.setErrorText(binding.tilAutoNumber, doAfter = true) {
            it.toString().isEmpty()
        }
        binding.etCarrierAutoBrand.setErrorText(binding.tilCarrierAutoBrand, doAfter = true) {
            it.toString().isEmpty()
        }
        binding.etCarrierName.setErrorText(binding.tilCarrierName, doAfter = true) {
            it.toString().isEmpty()
        }
        binding.etCarrierPhone.setErrorText(binding.tilCarrierPhone, doAfter = true) {
            it.toString().isEmpty()
        }
        binding.etPassportSeries.setErrorText(binding.tilPassportSeries, doAfter = true) {
            it.toString().isEmpty()
        }
        binding.etWeight.setErrorText(binding.tilWeight, doAfter = true) {
            it.toString().isEmpty()
        }
        binding.etCarrierTrailerWeight.setErrorText(
            binding.tilCarrierTrailerWeight,
            doAfter = true
        ) {
            it.toString().isEmpty()
        }
        binding.etDirection.setErrorText(binding.tilDirection, doAfter = true) {
            it.toString().isEmpty()
        }


    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun initListeners() {

        binding.atvCarrierTrailer.setOnItemClickListener { parent, view, position, id ->
            binding.tilCarrierTrailerWeight.isVisible = parent.getItemAtPosition(position) == "Bar"
            if (parent.getItemAtPosition(position) == "Joq") {
                binding.tilCarrierTrailerWeight.error = null
            }
        }

        binding.tbForm.setNavigationOnClickListener {
            findNavController().popBackStack()
        }


        binding.btnSendForm.setOnClickListener {
            if (_encoded == null) {
                makeToast("Su'wret qospadin'iz!", Toast.LENGTH_LONG)
            } else {
                val everythingOkay = checkEditTexts()
                if (everythingOkay) {
                    lifecycleScope.launch {
                        viewModel.postOrder(
                            PostOrder(
                                car_photo = encoded,
                                driver_name = binding.etCarrierName.getNotNullText(),
                                driver_phone_number = binding.etCarrierPhone.getNotNullText(),
                                driver_passport_or_id = if(binding.atvDocumentType.text.toString() == "Passport") "passport" else "document_id",
                                driver_passport_or_id_number = binding.etPassportSeries.getNotNullText(),
                                car_number = binding.etAutoNumber.getNotNullText(),
                                car_brand = binding.etCarrierAutoBrand.getNotNullText(),
                                trailer = binding.atvCarrierTrailer.text.toString(),
                                trailer_weight = if(binding.etCarrierTrailerWeight.text.toString().isNullOrEmpty()) "0" else binding.etCarrierTrailerWeight.text.toString(),
                                direction = binding.etDirection.getNotNullText(),
                                location = location,
                                cargo_type = 1,
                                cargo_value = binding.etWeight.getNotNullText(),
                                status = "waiting",
                                violated = false,
                                karer = SharedPrefStorage().id,
                                cargo_unit = if (binding.atvCargoUnit.text.toString() == "Kg" ) 2 else 1,
                                date = LocalDateTime.now().toString()
                            )
                        )
                    }
                }
            }
        }

        binding.ivFormImage.setOnClickListener {

            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.CAMERA
                )
                == PackageManager.PERMISSION_DENIED
            ) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(android.Manifest.permission.CAMERA),
                    42
                )
            } else {
                val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                if (takePictureIntent.resolveActivity(requireActivity().packageManager) != null) {
                    startActivityForResult(takePictureIntent, 42)
                } else {
                    makeToast("Unable to open camera")
                }
            }
        }
    }

    private fun checkEditTexts(): Boolean {

        var isEverythingOkay = false
        if (binding.tilAutoNumber.error != null || binding.etAutoNumber.text.toString()
                .isNullOrEmpty() ||binding.etAutoNumber.text.toString().trim().length < 8
        ) {
            binding.tilAutoNumber.error = "Toltiriw kerek!"
        } else if (binding.tilCarrierAutoBrand.error != null || binding.etCarrierAutoBrand.text.toString()
                .isNullOrEmpty()
        ) {
            binding.tilCarrierAutoBrand.error = "Toltiriw kerek!"
        } else if (binding.tilCarrierName.error != null || binding.etCarrierName.text.toString()
                .isNullOrEmpty()
        ) {
            binding.tilCarrierName.error = "Toltiriw kerek!"
        } else if (binding.tilCarrierPhone.error != null || binding.etCarrierPhone.text.toString()
                .isNullOrEmpty() || binding.etCarrierPhone.text.toString().trim().length < 9
        ) {
            binding.tilCarrierPhone.error = "Toltiriw kerek!"
        } else if (binding.tilPassportSeries.error != null || binding.etPassportSeries.text.toString()
                .isNullOrEmpty()
        ) {
            binding.tilPassportSeries.error = "Toltiriw kerek!"
        } else if (binding.tilDirection.error != null || binding.etDirection.text.toString()
                .isNullOrEmpty()
        ) {
            binding.tilDirection.error = "Toltiriw kerek!"
        } else if (binding.tilWeight.error != null || binding.etWeight.text.toString()
                .isNullOrEmpty()
        ) {
            binding.tilWeight.error = "Toltiriw kerek!"
        } else if (binding.tilCarrierTrailer.isVisible && binding.tilCarrierTrailerWeight.error != null || binding.tilCarrierTrailerWeight.isVisible && binding.etCarrierTrailerWeight.text.toString()
                .isNullOrEmpty()
        ) {
            binding.tilCarrierTrailerWeight.error = "Toltiriw kerek!"
        } else {
            isEverythingOkay = true
        }
        return isEverythingOkay
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