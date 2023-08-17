package com.theberdakh.carrierapp.ui.user

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
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
import java.io.File
import java.io.FileOutputStream

class FormFragment : Fragment(R.layout.fragment_seller_form) {
    private lateinit var binding: FragmentSellerFormBinding
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val viewModel by viewModel<SellerViewModel>()
    private var location: String = ""
    private var _photo: File? = null
    private val photo get() = _photo!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSellerFormBinding.bind(view)

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        checkLocationPermission()


        initViews()
        initListeners()
        initObservers()

    }

    private fun initObservers() {
        viewModel.postOrderSuccessFlow.onEach {
            Log.d("Login Success", "Success ${it.driver_name}")

            findNavController().popBackStack()
        }.launchIn(lifecycleScope)

        viewModel.postOrderMessageFlow.onEach {
            makeToast(it)
        }.launchIn(lifecycleScope)

        viewModel.postOrderErrorFlow.onEach {
            makeToast("Error, check your Internet connection")
        }.launchIn(lifecycleScope)
    }


    private fun checkLocationPermission() {

        val task = fusedLocationProviderClient.lastLocation


        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireActivity(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                101
            )
            return
        }

        task.addOnSuccessListener {
            if (it != null) {
                makeToast("${it.latitude} / ${it.longitude} / ")
                location = "${it.latitude}, ${it.longitude}"
            }
        }

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

    private fun initListeners() {
        binding.tbForm.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        Log.d("Tag", "Inited")
        binding.btnSendForm.clicks().debounce(300).onEach {

            Log.d("Tag", "Clicked")

            viewModel.postOrder(SharedPrefStorage().token, PostOrder(
                car_number = binding.etAutoNumber.text.toString(),
                cargo_value = binding.etValue.text.toString(),
                cargo_type = 1,
                cargo_unit = 1,
                driver_name = binding.etCarrierName.text.toString(),
                driver_passport_or_id = "passport",
                driver_passport_or_id_number = binding.etPassportSeries.text.toString(),
                driver_phone_number = binding.etCarrierPhone.text.toString(),
                karer = SharedPrefStorage().id,
                location = location,
                weight =  binding.etValue.text.toString(),
                car_photo = photo
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


            //making file from taken image
            val bos = ByteArrayOutputStream()
            val a = takenImage.compress(Bitmap.CompressFormat.JPEG, 0, bos)
            _photo = File(requireContext().cacheDir, "photo")
            _photo!!.createNewFile()
            val fileOutputStream = FileOutputStream(_photo)
            fileOutputStream.write(bos.toByteArray())
            fileOutputStream.flush()
            fileOutputStream.close()

        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}