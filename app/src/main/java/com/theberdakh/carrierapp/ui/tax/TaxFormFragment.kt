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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.theberdakh.carrierapp.R
import com.theberdakh.carrierapp.data.local.SharedPrefStorage
import com.theberdakh.carrierapp.data.model.response.order.Order
import com.theberdakh.carrierapp.data.model.response.seller.GetAllSellerResult
import com.theberdakh.carrierapp.data.model.response.violation.PostViolation
import com.theberdakh.carrierapp.databinding.FragmentTaxFormBinding
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTaxFormBinding.bind(view)

        initViews()
        initListeners()
        initObservers()

    }

    private fun initObservers() {

        viewModel.allSellersSuccessFlow.onEach {
            val names = mutableListOf<String>()
            for (seller in it.results){
                names.add(seller.karer_name)
            }
            binding.atvSellerName.setCustomAdapter(names)

            makeToast("Success ${it.results.size}")
        }

        viewModel.allSellersMessageFlow.onEach {
            makeToast(it)
        }

        viewModel.allSellersErrorFlow.onEach {
            makeToast(it.message.toString())
        }







    }

    private fun initViews() {

        binding.atvDocumentType.setCustomAdapter(listOf("Passport", "ID"))
        binding.atViolationType.setCustomAdapter(listOf("Mag'liwmat kiritilmegen", "Mag'liwmat toliq emes"))
        binding.atvCargoType.setCustomAdapter(listOf("Sheben", "Shege qum"))


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


        binding.atvSellerName.setOnClickListener {
            findNavController().navigate(TaxFormFragmentDirections.actionTaxFormFragmentToTaxSearchSellers())

        }


        binding.atvSellerName.setOnItemClickListener { parent, view, position, id ->

        }


        binding.btnSendForm.setOnClickListener {


          /* viewModel.postViolation(
                PostViolation(
                    car_brand = binding.etCarrierAutoBrand.getNotNullText(),
                    car_number = binding.etAutoNumber.getNotNullText(),
                    cargo_date = binding.etCargoDate.getNotNullText(),
                    cargo_type = binding.atvCargoType.text.toString(),
                    driver_name = binding.etCarrierName.getNotNullText(),
                    driver_passport_or_id=  binding.atvDocumentType.text.toString(),
                    driver_passport_or_id_number = binding.etPassportSeries.getNotNullText(),
                    driver_phone_number = binding.etCarrierPhone.getNotNullText(),
                    karer_name = binding.,
                    location = "139349, 3403445" ,
                    reason_violation = binding.atViolationType.text.toString(),
                    tax_officer = SharedPrefStorage().id
                )
            )*/
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