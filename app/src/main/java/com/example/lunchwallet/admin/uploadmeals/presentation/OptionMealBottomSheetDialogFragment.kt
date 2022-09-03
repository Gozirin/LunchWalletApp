package com.example.lunchwallet.admin.uploadmeals.presentation

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.lunchwallet.R
import com.example.lunchwallet.databinding.FragmentOptionMealBinding
import com.example.lunchwallet.util.*
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch
import java.io.IOException
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import kotlin.collections.ArrayList

private const val TAG = "OptionMealBottomSheetDialogFragment"
class OptionMealBottomSheetDialogFragment : BottomSheetDialogFragment(){

    private var _binding: FragmentOptionMealBinding? = null
    private val binding get() = _binding!!
    private lateinit var uploadMealsViewModel: UploadMealsViewModel
    private var selectedImageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOptionMealBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        uploadMealsViewModel = ViewModelProvider(requireActivity())[UploadMealsViewModel::class.java]

        kitchenDropDown()
        servingTimeDropDown()
        dateDropDownList()
        namedMealFocusListener()
        serveTimeFocusListener()
        kitchenFocusListener()
        dateFocusListener()

        binding.optionMealFragmentUploadImageIv.setOnClickListener {
            imageChooser()
        }

        binding.optionMealFragmentAddMealButton.setOnClickListener {
//            findNavController().navigate(R.id.action_optionMealFragment_to_messageAlertFragment)
            submitForm()
            Toast.makeText(requireContext(), "Clicked!", Toast.LENGTH_SHORT).show()
        }

    }

    private fun imageChooser() {
        val i = Intent()
        i.type = "image/*"
        i.action = Intent.ACTION_GET_CONTENT
        launchSomeActivity.launch(i)
    }

    private var launchSomeActivity =
       registerForActivityResult<Intent, ActivityResult>(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            // do your operation from here....
            if (data != null
                && data.data != null
            ) {
                 selectedImageUri = data.data
                try { Picasso
                        .get()
                        .load(selectedImageUri)
                        .resize(300, 200)
                        .centerCrop()
                        .into(binding.optionMealFragmentUploadImageIv)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun submitForm() {
        binding.optionMealFragmentDateContainer.helperText =
            UploadMealValidation().verifyDate(LocalDate.parse(binding.dateAutoView.text))
        binding.uploadMealFragmentNameOfMealContainer.helperText =
            UploadMealValidation().verifyName(binding.uploadMealFragmentNameOfMealET.text.toString())
        binding.optionMealFragmentServingTimeContainer.helperText =
            UploadMealValidation().verifyServingTime(binding.servingTimeAutoView.text.toString())
        binding.optionMealFragmentKitchenContainer.helperText =
            UploadMealValidation().verifyKitchen(binding.kitchenAutoView.text.toString())

        val validDate = binding.optionMealFragmentDateContainer.helperText == null
        val validName = binding.uploadMealFragmentNameOfMealContainer.helperText == null
        val validServingTime = binding.optionMealFragmentServingTimeContainer.helperText == null
        val validKitchen = binding.optionMealFragmentKitchenContainer.helperText == null


        if (validDate && validName && validServingTime && validKitchen) {
            uploadMeal()
            observeMealUploadResponse()
        } else { invalidForm() }
    }

    private fun kitchenDropDown() {
        val kitchen = resources.getStringArray(R.array.kitchen)
        val adapter = ArrayAdapter(requireContext(), R.layout.kitchen_menu_list, kitchen)
        with(binding.kitchenAutoView) {
            setAdapter(adapter)
        }
    }

    private fun servingTimeDropDown() {
        val servingTime = resources.getStringArray(R.array.serving_time)
        val adapter = ArrayAdapter(requireContext(), R.layout.serving_time_list, servingTime)
        with(binding.servingTimeAutoView) {
            setAdapter(adapter)
        }
    }

    private fun dateDropDownList() {
        val dates = getNextDaysOnIntervalOf(30, 1)
        val adapter = ArrayAdapter(requireContext(), R.layout.serving_time_list, dates)
        with(binding.dateAutoView) {
            setAdapter(adapter)
        }
    }

    private fun namedMealFocusListener() {
        binding.uploadMealFragmentNameOfMealET.setOnFocusChangeListener { _, focused ->
            if (!focused)
                binding.uploadMealFragmentNameOfMealContainer.helperText
        }
    }

    private fun serveTimeFocusListener() {
        binding.servingTimeAutoView.setOnFocusChangeListener { _, focused ->
            if (!focused)
                binding.optionMealFragmentServingTimeContainer.helperText
        }
    }
    private fun kitchenFocusListener() {
        binding.kitchenAutoView.setOnFocusChangeListener { _, focused ->
            if (!focused)
                binding.optionMealFragmentKitchenContainer.helperText
        }
    }

    private fun dateFocusListener() {
        binding.dateAutoView.setOnFocusChangeListener { _, focused ->
            if (!focused)
                binding.optionMealFragmentDateContainer.helperText
        }
    }

     fun getNextDaysOnIntervalOf(times: Int, interval: Long): List<LocalDate> {
        // The list to be populated with the desired dates
        val list: MutableList<LocalDate> = ArrayList()

        // Today
         val zonedDateTime = ZonedDateTime.now(ZoneId.of("Africa/Lagos"))
        var date: LocalDate = zonedDateTime.toLocalDate()
        for (i in 1..times) {
            list.add(date)
            date = date.plusDays(interval)
        }
         for(i in list) {
             val parsedDt = LocalDate.parse( i.toString(), DateTimeFormatter.ofPattern( "yyyy-MM-dd" ))
             println(parsedDt)
         }


        // Return the populated list
        return list
    }

    private fun invalidForm() {
        var message = ""
        if (binding.optionMealFragmentDateContainer.helperText != null)
            message = "\n\nName: " + binding.optionMealFragmentDateContainer.helperText
        if (binding.uploadMealFragmentNameOfMealContainer.helperText != null)
            message += "\n\nEmail: " + binding.uploadMealFragmentNameOfMealContainer.helperText
        if (binding.optionMealFragmentServingTimeContainer.helperText != null)
            message += "\n\nStack: " + binding.optionMealFragmentServingTimeContainer.helperText
        if (binding.optionMealFragmentKitchenContainer.helperText != null)
            message += "\n\nLocation: " + binding.optionMealFragmentKitchenContainer.helperText
    }

    private fun uploadMeal() {
        val date = LocalDate.parse(binding.dateAutoView.text)
        val dateString = date.toString()
        val dateArr = dateString.split("-")
        val year = dateArr[0]
        val month = dateArr[1]
        val day = dateArr[2]
        Log.d(TAG, "uploadMeal: $dateString")
        Log.d(TAG, "uploadMeal: $year")
        Log.d(TAG, "uploadMeal: $month")
        Log.d(TAG, "uploadMeal: $day")
//        val day = date.dayOfMonth
//        Log.d(TAG, "uploadMeal: $day")
//        val month = date.monthValue
//        Log.d(TAG, "uploadMeal: $month")
//        val year = date.year
//        Log.d(TAG, "uploadMeal: $year")
        val name = binding.uploadMealFragmentNameOfMealET.text.toString()
        Log.d(TAG, "uploadMeal: $name")
        val servingTime = binding.servingTimeAutoView.text.toString()
        Log.d(TAG, "uploadMeal: $servingTime")
        val kitchen = binding.kitchenAutoView.text.toString()
        Log.d(TAG, "uploadMeal: $kitchen")

        uploadMealsViewModel.createMeal( name, servingTime.uppercase(), kitchen, year, month, day, selectedImageUri )

    }

    private fun observeMealUploadResponse() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                uploadMealsViewModel.mealState.collect{ mealUIState ->
                    when(mealUIState) {
                        is Resource.Success -> {
                            showProgressIndicator(false)
                            findNavController().navigate(R.id.action_optionMealFragment_to_uploadMealFragment)
                        }
                        is Resource.Error -> {
                            showProgressIndicator(false)
                            requireView().snackbar( mealUIState.message.toString(), requireContext(), null)
                        }
                        is Resource.Loading -> {
                            showProgressIndicator(true)
                        }
                    }
                }
            }
        }
    }

    private fun showProgressIndicator(isLoading: Boolean) {
        if(isLoading) {
            binding.optionMealProgressBar.visibility = View.VISIBLE
        }else {
            binding.optionMealProgressBar.visibility = View.GONE
        }
    }
}
