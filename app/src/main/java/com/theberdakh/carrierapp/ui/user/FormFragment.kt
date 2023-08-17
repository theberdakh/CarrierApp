package com.theberdakh.carrierapp.ui.user

import android.Manifest
import android.R.attr.bitmap
import android.app.Activity
import android.app.Service
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.theberdakh.carrierapp.R
import com.theberdakh.carrierapp.data.local.SharedPrefStorage
import com.theberdakh.carrierapp.data.model.response.order.PostOrder
import com.theberdakh.carrierapp.databinding.FragmentSellerFormBinding
import com.theberdakh.carrierapp.presentation.SellerViewModel
import com.theberdakh.carrierapp.util.makeToast
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.ldralighieri.corbind.view.clicks
import java.io.ByteArrayOutputStream


class FormFragment : Fragment(R.layout.fragment_seller_form) {
    private lateinit var binding: FragmentSellerFormBinding
    private val viewModel by viewModel<SellerViewModel>()
    private var locationManager : LocationManager? = null
    private var _locationManager: LocationManager? = null
    private var _encoded: String? = null
    private val encoded get() = _encoded!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSellerFormBinding.bind(view)


        initViews()
        initListeners()
        initObservers()

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

        setLocation()

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
            arrayOf("KG", "m3")
        )
        binding.atvCargoUnit.setAdapter(unitsAdapter)
        binding.atvCargoUnit.setOnItemClickListener { parent, view, position, id ->
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

    private fun setLocation() {

        locationManager = requireActivity().getSystemService(Service.LOCATION_SERVICE) as LocationManager?


            try {
                // Request location updates
                locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f, locationListener)
            } catch(ex: SecurityException) {
                Log.d("myTag", "Security Exception, no location available")
            }

    }


    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            Log.d("Location", "${location.longitude}, ${location.latitude}")
        }
        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    private fun initListeners() {
        binding.tbForm.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        Log.d("Tag", "Inited")
        binding.btnSendForm.clicks().debounce(300).onEach {

            Log.d("Tag", "Clicked")


            viewModel.postOrder(PostOrder(
                driver_name = binding.etCarrierName.text.toString(),
                driver_phone_number = binding.etCarrierPhone.text.toString(),
                driver_passport_or_id = "passport",
                driver_passport_or_id_number = binding.etPassportSeries.text.toString(),
                car_number = binding.etAutoNumber.text.toString(),
                car_photo = encoded,
                location = "349355, 3489083",
                karer = SharedPrefStorage().id,
                cargo_type = 1,
            ))

        }.launchIn(lifecycleScope)

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