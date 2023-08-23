package com.theberdakh.carrierapp.ui.tax

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.gms.location.LocationRequest
import com.theberdakh.carrierapp.R
import com.theberdakh.carrierapp.data.local.SharedPrefStorage
import com.theberdakh.carrierapp.data.model.response.violation.PostViolation
import com.theberdakh.carrierapp.databinding.FragmentTaxFormBinding
import com.theberdakh.carrierapp.presentation.SellerViewModel
import com.theberdakh.carrierapp.presentation.TaxViewModel
import com.theberdakh.carrierapp.util.getNotNullText
import com.theberdakh.carrierapp.util.makeToast
import com.theberdakh.carrierapp.util.setCustomAdapter
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.ldralighieri.corbind.view.clicks
import java.io.ByteArrayOutputStream

class TaxFormFragment : Fragment(R.layout.fragment_tax_form) {
    lateinit var binding: FragmentTaxFormBinding

    private var _encoded: String? = null
    private val encoded get() = _encoded!!
    private val viewModel by viewModel<TaxViewModel>()
    private val sellerViewModel by viewModel<SellerViewModel>()
    private val args: TaxFormFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (args.id !=-1){
            insertValues(args.id)
        }

    }

    private fun insertValues(id: Int) {

        lifecycleScope.launch {
            sellerViewModel.getOrderById(id)
        }

        sellerViewModel.orderSuccessFlow.onEach {

            binding.etAutoNumber.setText(it.car_number)
            binding.etAutoNumber.isEnabled = false
            binding.etCarrierAutoBrand.setText(it.car_brand)
            binding.etCarrierAutoBrand.isEnabled = false
            binding.etCarrierName.setText(it.driver_name)
            binding.etCarrierName.isEnabled = false
            binding.etCarrierPhone.setText(it.driver_phone_number)
            binding.etCarrierPhone.isEnabled =false
            binding.atvSellerName.setText(it.driver_name)
            binding.atvSellerName.isEnabled = false
            binding.atvDocumentType.setText(if(it.driver_passport_or_id_number != "passport") "ID" else "passport")
            binding.atvDocumentType.isEnabled = false
            binding.etPassportSeries.setText(it.driver_passport_or_id_number)
            binding.etPassportSeries.isEnabled = false
            binding.atvCargoType.setText(it.cargo_type)
            binding.atvCargoType.isEnabled = false
            binding.etCargoDate.setText(it.date)
            binding.etCargoDate.isEnabled = false


        }.launchIn(lifecycleScope)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTaxFormBinding.bind(view)

        initViews()
        initListeners()
        initObservers()


    }

    private fun initObservers() {

        viewModel.postViolationSuccessFlow.onEach {
            Log.d("Violation", "Created ${it.car_number}")
            findNavController().popBackStack()
            makeToast("Violation created")
        }.launchIn(lifecycleScope)

        viewModel.postViolationMessageFlow.onEach {
            Log.d("Violation", "Message $it")
            makeToast(it)
        }.launchIn(lifecycleScope)

        viewModel.postViolationErrorFlow.onEach {
            Log.d("Violation", "Error ${it.printStackTrace()}")
            makeToast(it.message.toString())
        }.launchIn(lifecycleScope)







    }

    private fun initViews() {

        binding.atvDocumentType.setCustomAdapter(listOf("Passport", "ID"))
        binding.atViolationType.setCustomAdapter(listOf("Mag'liwmat kiritilmegen", "Mag'liwmat toliq emes"))
        binding.atvCargoType.setCustomAdapter(listOf("Sheben", "Shege qum"))

        val request = LocationRequest.Builder(1000).build()
        Intent(requireContext(), requireActivity().javaClass).apply {
            putExtra("request", request)
        }

    }


    private fun initListeners() {
        binding.tbForm.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

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



        binding.atvSellerName.setOnClickListener {
            findNavController().navigate(TaxFormFragmentDirections.actionTaxFormFragmentToTaxSearchSellers())
        }

        binding.btnSendForm.clicks().debounce(200).onEach {
          viewModel.postViolation(
                PostViolation(
                    car_brand = binding.etCarrierAutoBrand.getNotNullText(),
                    car_photo =encoded,
                    car_number = binding.etAutoNumber.getNotNullText(),
                    cargo_date = "2023-08-18T00:50:46+05:00",
                    cargo_type = binding.atvCargoType.text.toString(),
                    driver_name = binding.etCarrierName.getNotNullText(),
                    driver_passport_or_id=  "passport",
                    driver_passport_or_id_number = binding.etPassportSeries.getNotNullText(),
                    driver_phone_number = binding.etCarrierPhone.getNotNullText(),
                    karer_name = binding.atvSellerName.text.toString(),
                    location = "139349, 3403445" ,
                    reason_violation = "not_entered",
                    is_updated = false,
                    tax_officer = SharedPrefStorage().id
                )
            )

            makeToast("Clicked")
        }.launchIn(lifecycleScope)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 42 && resultCode == Activity.RESULT_OK) {
            val takenImage = data?.extras?.get("data") as Bitmap
            binding.ivFormImage.setImageBitmap(takenImage)
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

    override fun onResume() {
        binding.atvSellerName.setText(SharedPrefStorage().lastSellerName)
        super.onResume()
    }
}