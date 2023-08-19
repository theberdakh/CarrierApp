package com.theberdakh.carrierapp.ui.seller

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.theberdakh.carrierapp.R
import com.theberdakh.carrierapp.data.local.SharedPrefStorage
import com.theberdakh.carrierapp.data.model.response.order.PostOrder
import com.theberdakh.carrierapp.databinding.FragmentSellerFormBinding
import com.theberdakh.carrierapp.presentation.SellerViewModel
import com.theberdakh.carrierapp.util.makeToast
import com.theberdakh.carrierapp.util.setCustomAdapter
import com.theberdakh.carrierapp.util.setErrorText
import com.theberdakh.carrierapp.util.showSnackBar
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.ldralighieri.corbind.view.clicks
import java.io.ByteArrayOutputStream


class FormFragment : Fragment(R.layout.fragment_seller_form) {
    private lateinit var binding: FragmentSellerFormBinding
    private val viewModel by viewModel<SellerViewModel>()
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


        binding.tilCarrierTrailerWeight.isVisible = false

        binding.atvDocumentType.setCustomAdapter("ID", "Passport")
        binding.atvCargoUnit.setCustomAdapter("KG", "m3")
        binding.atvCargoType.setCustomAdapter("Sheben", "Shege qum")
        binding.atvCarrierTrailer.setCustomAdapter("Joq", "Bar")


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


    }


    private fun initListeners() {

        binding.atvCarrierTrailer.setOnItemClickListener { parent, view, position, id ->
            binding.tilCarrierTrailerWeight.isVisible = parent.getItemAtPosition(position) == "Bar"
        }

        binding.tbForm.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnSendForm.setOnClickListener {

            if (binding.etAutoNumber.setErrorText(binding.tilAutoNumber) {
                    it.toString().isEmpty()
                } &&
                binding.etCarrierAutoBrand.setErrorText(binding.tilCarrierAutoBrand) {
                    it.toString().isEmpty()
                } &&
                binding.etCarrierName.setErrorText(binding.tilCarrierName) {
                    it.toString().isEmpty()
                } &&
                binding.etCarrierPhone.setErrorText(binding.tilCarrierPhone) {
                    it.toString().isEmpty()
                } &&
                binding.etPassportSeries.setErrorText(binding.tilPassportSeries) {
                    it.toString().isEmpty()
                } &&
                binding.etWeight.setErrorText(binding.tilWeight) {
                    it.toString().isEmpty()
                } &&
                binding.etCarrierTrailerWeight.setErrorText(binding.tilCarrierTrailerWeight) {
                    it.toString().isEmpty()
                }
            ) {
                findNavController().popBackStack()
            }
            else {
                showSnackBar(binding.btnSendForm, "Ha'mme kerek jerledi toltirin'!")
            }

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