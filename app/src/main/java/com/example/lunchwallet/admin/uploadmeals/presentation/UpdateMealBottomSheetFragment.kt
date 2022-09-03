package com.example.lunchwallet.admin.uploadmeals.presentation

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.room.util.StringUtil
import com.example.lunchwallet.R
import com.example.lunchwallet.admin.uploadmeals.adapter.OnMealSelectedForUpdate
import com.example.lunchwallet.admin.uploadmeals.model.Meals
import com.example.lunchwallet.admin.uploadmeals.model.UpdateMeal
import com.example.lunchwallet.databinding.FragmentUpdateMealBinding
import com.example.lunchwallet.util.Resource
import com.example.lunchwallet.util.UploadMealValidation
import com.example.lunchwallet.util.snackbar
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.zxing.common.StringUtils
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import kotlin.reflect.jvm.internal.impl.descriptors.Visibilities

class UpdateMealBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentUpdateMealBinding? = null
    private val binding get() = _binding!!
    private lateinit var uploadMealsViewModel: UploadMealsViewModel
    private val args: UpdateMealBottomSheetFragmentArgs by navArgs()
    private val date: LocalDate? = null
    private val name: String?= null
    private val type: String?= null
    private val kitchen: String?= null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentUpdateMealBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        uploadMealsViewModel = ViewModelProvider(requireActivity())[UploadMealsViewModel::class.java]

        val mealUpdate = args.mealUpdate
        val servingTime =  mealUpdate.timeServing?.lowercase()?.capitalize()
        val type = servingTime?.first()?.uppercase()
        Log.d("TAG", "onViewCreated:type $type")

        binding.dateAutoView.setText(getString(R.string.date_serving, mealUpdate.date?.toInt(), mealUpdate.month?.toInt(), mealUpdate.year?.toInt()))
        binding.uploadMealFragmentNameOfMealET.setText(mealUpdate.name)
        binding.servingTimeAutoView.setText(servingTime)
        binding.kitchenAutoView.setText(mealUpdate.kitchen)

        kitchenDropDown()
        servingTimeDropDown()
        dateDropDownList()
        namedMealFocusListener()
        serveTimeFocusListener()
        kitchenFocusListener()
        dateFocusListener()

        binding.optionMealFragmentAddMealButton.setOnClickListener {
            submitForm()
        }
    }

    private fun observeUpdateMeal() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                uploadMealsViewModel.updateMealState.collect { updateMealUIState ->
                    when (updateMealUIState) {
                        is Resource.Success -> {
                            showProgressIndicator(false)
                            try {
                                findNavController().navigate(R.id.action_updateMealBottomSheetFragment_to_uploadMealFragment)
                                Log.d("TAG", "deleteMeal: $updateMealUIState")
                                Log.d("TAG", "deleteMeal: ${updateMealUIState.data}")
                                Log.d("TAG", "deleteMeal: ${updateMealUIState.message}")
                            } catch (e: Exception) {
                                println(e.message)
                            }


                        }
                        is Resource.Error -> {
                            showProgressIndicator(false)
                            requireView().snackbar(
                                updateMealUIState.message.toString(),
                                requireContext(),
                                null
                            )
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
            val name = binding.uploadMealFragmentNameOfMealET.text.toString()
            val timeServing = binding.servingTimeAutoView.text.toString()
            val id = args.mealId
            val type = binding.servingTimeAutoView.text.toString()
            val date = binding.dateAutoView.text.toString()
            val dateArr = date.split("-")
              val  kitchen = binding.kitchenAutoView.text.toString()
              val  day = dateArr[2].toInt()
               val  month = dateArr[1].toInt()
               val year = dateArr[0].toInt()
            val updateMeal = UpdateMeal(image = null, name, timeServing, kitchen, day, month, year)

            uploadMealsViewModel.updateMeal(mealId = id, updateMeal)
            observeUpdateMeal()
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
    private fun getNextDaysOnIntervalOf(times: Int, interval: Long): List<LocalDate> {
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
}