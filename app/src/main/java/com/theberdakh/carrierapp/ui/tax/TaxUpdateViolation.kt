package com.theberdakh.carrierapp.ui.tax

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
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.gms.location.LocationServices
import com.theberdakh.carrierapp.R
import com.theberdakh.carrierapp.data.local.SharedPrefStorage
import com.theberdakh.carrierapp.data.model.response.violation.PostUpdateViolation
import com.theberdakh.carrierapp.databinding.FragmentTaxUpdateViolationBinding
import com.theberdakh.carrierapp.presentation.TaxViewModel
import com.theberdakh.carrierapp.util.checkLocationPermissions
import com.theberdakh.carrierapp.util.getNotNullText
import com.theberdakh.carrierapp.util.makeToast
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.ldralighieri.corbind.view.clicks
import java.io.ByteArrayOutputStream
import java.time.LocalDateTime

class TaxUpdateViolation: Fragment(R.layout.fragment_tax_update_violation) {
    private lateinit var binding: FragmentTaxUpdateViolationBinding
    private val viewModel by viewModel<TaxViewModel>()
    private var unique_number: Int = 0
    private var _encoded: String? = null
    private val encoded get() = _encoded!!

    private var location: String = ""

    private val args: TaxUpdateViolationArgs by navArgs()

    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTaxUpdateViolationBinding.bind(view)

        initListeners()
        initObservers(args.id)

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

    private fun initObservers(id: Int) {

        lifecycleScope.launch {
            viewModel.getViolationByUnique(id)
        }

        viewModel.postViolationSuccessFlow.onEach {
            findNavController().popBackStack()
            makeToast("Updated successfully")
        }.launchIn(lifecycleScope)

        viewModel.uniqueViolationSuccessFlow.onEach {listOfUnique->
            listOfUnique.asReversed()[0].apply {
                binding.etAutoNumber.setText(this.car_number)
                binding.etCarrierAutoBrand.setText(this.car_brand)
                binding.etCarrierName.setText(this.driver_name)
                binding.etCarrierPhone.setText(this.driver_phone_number)
                binding.atvSellerName.setText(this.driver_name)
                binding.atvDocumentType.setText(if(this.driver_passport_or_id_number != "passport") "ID" else "passport")
                binding.etPassportSeries.setText(this.driver_passport_or_id_number)
                binding.atvCargoType.setText(this.cargo_type)
                binding.etCargoDate.setText(this.cargo_date)
            }

        }.launchIn(lifecycleScope)

        viewModel.singleViolationMessage.onEach {

        }.launchIn(lifecycleScope)

        viewModel.singleViolationError.onEach {

        }.launchIn(lifecycleScope)


    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initListeners(){

        binding.btnSendForm.clicks().debounce(300).onEach{
            viewModel.postUpdatedViolation(
                PostUpdateViolation(
                    created_at = LocalDateTime.now().toString(),
                    car_brand = binding.etCarrierAutoBrand.getNotNullText(),
                    car_photo =encoded,
                    car_number = binding.etAutoNumber.getNotNullText(),
                    cargo_date = LocalDateTime.now().toString(),
                    cargo_type = binding.atvCargoType.text.toString(),
                    driver_name = binding.etCarrierName.getNotNullText(),
                    driver_passport_or_id=  "passport",
                    driver_passport_or_id_number = binding.etPassportSeries.getNotNullText(),
                    driver_phone_number = binding.etCarrierPhone.getNotNullText(),
                    karer_name = binding.atvSellerName.text.toString(),
                    location = this.location ,
                    reason_violation =" ${binding.atViolationType.text.toString()} ${binding.etCustomReason.text.toString()}",
                    is_updated = true,
                    tax_officer = SharedPrefStorage().id,
                    unique_number = 193810
                )
            )
        }.launchIn(lifecycleScope)

        binding.ivFormImage.setOnClickListener {
            if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED){
                ActivityCompat.requestPermissions(requireActivity(),  arrayOf(android.Manifest.permission.CAMERA), 42)
            }
            else {
                val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                if (takePictureIntent.resolveActivity(requireActivity().packageManager) != null) {
                    startActivityForResult(takePictureIntent, 42)
                } else {
                    makeToast("Unable to open camera")
                }
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
            makeToast(encoded)
            Log.d("Image", encoded)

        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }


}