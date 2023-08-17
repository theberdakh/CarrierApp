package com.theberdakh.carrierapp.ui.user

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
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
import okhttp3.MultipartBody
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
    private var _imageUri : Uri? = null
    private var _file: File? = null
    private val file get() = _file!!
    private val imageUri get() = _imageUri!!
    private var _part: MultipartBody.Part? = null
    private val part get() = _part!!

    private val contact = registerForActivityResult(ActivityResultContracts.GetContent()){
        _imageUri = it!!
    }

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
                driver_name = binding.etCarrierName.text.toString(),
                driver_phone_number = binding.etCarrierPhone.text.toString(),
                driver_passport_or_id = "passport",
                driver_passport_or_id_number = binding.etPassportSeries.text.toString(),
                car_number = binding.etAutoNumber.text.toString(),
                car_photo = file,
                location = location,
                karer = 10,
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
            val encoded: String = Base64.encodeToString(byteArray, Base64.DEFAULT)
            Log.d("Image", encoded)

            //making file from taken image
           _file = saveImageFile(takenImage)

        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun makeBitmapImageFile(bitmap: Bitmap) {
        /*try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                val resolver = requireActivity().contentResolver
                val contentValues = ContentValues()
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "Image.jpg")
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                 _photo = Objects.requireNonNull(uri)?.let { resolver.openOutputStream(it) }
                if (_photo != null) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, _photo!!)
                }
                Objects.requireNonNull(_photo)

                val image =  File(requireActivity().applicationContext.filesDir, "camera_photo.jpg")
            }
        } catch (e: Exception){
            Log.d("Image making", e.toString())
        }
    }*/

    /*    val filesDir = requireActivity().applicationContext.filesDir
        val file  = File(filesDir, "image.png")
        val input = requireActivity().contentResolver.openInputStream(imageUri)
        val output = FileOutputStream(file)
         input!!.copyTo(output)

        val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        _part = MultipartBody.Part.createFormData("profile", file.name, requestBody)*/

     */

        saveImageFile(bitmap)
    }

    fun saveImageFile(bitmap: Bitmap):File{
            val  filesDir = requireActivity().applicationContext.filesDir
            val  imageFile = File(filesDir, "image" + ".jpg");

            try {
                val os =  FileOutputStream(imageFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
                os.flush();
                os.close();
            } catch (e: Exception) {
                Log.e(javaClass.simpleName, "Error writing bitmap", e);
            }

        return imageFile
    }
}