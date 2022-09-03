package com.example.lunchwallet.kitchenstaff.servingstatus.ui


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import androidx.navigation.fragment.findNavController
import com.example.lunchwallet.R
import com.example.lunchwallet.admin.uploadmeals.presentation.UploadMealsViewModel
import com.example.lunchwallet.databinding.FragmentServingStatusBinding
import com.example.lunchwallet.util.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalDate

private const val TAG = "ServingStatusFragment"

@AndroidEntryPoint
class ServingStatusFragment : Fragment() {

    private val uploadMealsViewModel: UploadMealsViewModel by viewModels()
    private var _binding: FragmentServingStatusBinding? = null
    private val binding get() = _binding!!
    private var mealIdBrunch: String? = null
    private var mealIdDinner: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentServingStatusBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        when(binding.servingStatusBrunchServingTV.text) {
            SERVING -> servingStatusColorBrunch()
            SERVED -> servedStatusColorBrunch()
            NOT_SERVING -> notServingStatusColorBrunch()
        }

        when(binding.servingStatusBrunchServingTV.text) {
            SERVING -> servingStatusColorBrunch()
            SERVED -> servedStatusColorBrunch()
            NOT_SERVING -> notServingStatusColorBrunch()
        }

        binding.servingStatusBackarrowIV.setOnClickListener {
            findNavController().navigate(R.id.kitchenStaffDashBoardFragment)
        }

        onBackPressed()
        usingUploadMealsViewModel()

        binding.servingStatusBackarrowIV.setOnClickListener {
            findNavController().navigate(R.id.kitchenStaffDashBoardFragment)
        }

        val navController = findNavController()
        // After a configuration change or process death, the currentBackStackEntry
        // points to the dialog destination, so you must use getBackStackEntry()
        // with the specific ID of your destination to ensure we always
        // get the right NavBackStackEntry
        val navBackStackEntry = navController.getBackStackEntry(R.id.servingStatusFragment)

        // Create our observer and add it to the NavBackStackEntry's lifecycle
        val brunchObserver = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME
                && navBackStackEntry.savedStateHandle.contains("brunch_key")) {
                val result = navBackStackEntry.savedStateHandle.get<String>("brunch_key")
                // Do something with the result
                binding.servingStatusBrunchServingTV.text = result
                when(binding.servingStatusBrunchServingTV.text) {
                    SERVING -> servingStatusColorBrunch()
                    SERVED -> servedStatusColorBrunch()
                    NOT_SERVING -> notServingStatusColorBrunch()
                }

                Log.d(TAG, "onViewCreated: $result")
            }
        }
        navBackStackEntry.lifecycle.addObserver(brunchObserver)

        // As addObserver() does not automatically remove the observer, we
        // call removeObserver() manually when the view lifecycle is destroyed
        viewLifecycleOwner.lifecycle.addObserver(LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                navBackStackEntry.lifecycle.removeObserver(brunchObserver)
            }
        })

        // Create our observer and add it to the NavBackStackEntry's lifecycle
        val dinnerObserver = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME
                && navBackStackEntry.savedStateHandle.contains("dinner_key")) {
                val result = navBackStackEntry.savedStateHandle.get<String>("dinner_key")
                // Do something with the result
                binding.servingStatusDinnerServingTV.text = result
                when(binding.servingStatusDinnerServingTV.text) {
                    SERVING -> servingStatusColorDinner()
                    SERVED -> servedStatusColorDinner()
                    NOT_SERVING -> notServingStatusColorDinner()
                }
                Log.d(TAG, "onViewCreated: $result")
            }
        }
        navBackStackEntry.lifecycle.addObserver(dinnerObserver)

        // As addObserver() does not automatically remove the observer, we
        // call removeObserver() manually when the view lifecycle is destroyed
        viewLifecycleOwner.lifecycle.addObserver(LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                navBackStackEntry.lifecycle.removeObserver(dinnerObserver)
            }
        })

        binding.brunchServingStatusButton.setOnClickListener {
            if (mealIdBrunch != null) {
                findNavController().navigate(R.id.action_servingStatusFragment_to_optionStatusScreenFragment)
            }else {
                requireView().snackbar("Meal not available", requireContext(), null)
            }


        }
        binding.dinnerServingStatusButton.setOnClickListener {
            if (mealIdDinner != null) {
                findNavController().navigate(R.id.action_servingStatusFragment_to_dinnerOptionStatusScreenFragment)
            } else {
                requireView().snackbar("Meal not available", requireContext(), null)
            }
        }

        binding.servingStatusBackarrowIV.setOnClickListener {
            findNavController().navigate(R.id.kitchenStaffDashBoardFragment)
        }

    }

    private fun onBackPressed() {
        //Overriding onBack press to finish activity and exit app
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)
    }

    private fun usingUploadMealsViewModel() {
        uploadMealsViewModel.getAllMeals()
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                uploadMealsViewModel.getMealState.collect{ mealTimeTableUIState ->
                    when(mealTimeTableUIState) {
                        is Resource.Success -> {
                            showProgressIndicator(false)
                            val mealTimeTableData = mealTimeTableUIState.data?.data

                            for(meal in mealTimeTableData?.indices!!) {

                                if (mealTimeTableData[meal].day == LocalDate.now().dayOfMonth &&
                                    mealTimeTableData[meal].month == LocalDate.now().monthValue &&
                                    mealTimeTableData[meal].year == LocalDate.now().year) {

                                    if (mealTimeTableData[meal].type == BRUNCH) {
                                        binding.servingStatusBrunchKitchenTV.text = mealTimeTableData[meal].kitchen
                                        binding.servingStatusBrunchRiceChickenTV.text = mealTimeTableData[meal].name
                                        binding.servingStatusBrunchServingTV.text = mealTimeTableData[meal].status
                                        mealIdBrunch = mealTimeTableData[meal].ID
//                                        binding.adminMealTimeTableBrunchRadioButton.isChecked =
//                                            mealTimeTableData[meal].status.toString() == SERVING
                                    }

                                    if (mealTimeTableData[meal].type == DINNER) {
                                        binding.servingStatusDinnerKitchenTV.text = mealTimeTableData[meal].kitchen
                                        binding.servingStatusDinnerEfoSemoTV.text = mealTimeTableData[meal].name
                                        binding.servingStatusDinnerServingTV.text = mealTimeTableData[meal].status.uppercase()
                                        mealIdDinner = mealTimeTableData[meal].ID
//                                        binding.adminMealTimeTableBrunchRadioButton.isChecked =
//                                            mealTimeTableData[meal].status.toString() == SERVING

                                    }

                                }
                            }
                            Log.d(TAG, "observeMealTimeTable: mealTimeTableUIState is $mealTimeTableUIState")
                            Log.d(TAG, "observeMealTimeTable: mealTimeTableUIState.message is ${mealTimeTableUIState.message}")
                            Log.d(TAG, "observeMealTimeTable: mealTimeTableUIState.data is ${mealTimeTableUIState.data}")

                        }
                        is Resource.Error -> {
                            showProgressIndicator(false)
                            requireView().snackbar( mealTimeTableUIState.message.toString(), requireContext(), null)
                        }
                        is Resource.Loading -> {
                            showProgressIndicator(true)
                        }
                    }
                }
            }
        }

    }


    private fun servingStatusColorBrunch() {
        binding.servingStatusBrunchServingTV.setTextColor(resources.getColor(R.color.serving_radio_button_color, requireActivity().theme))
        binding.brunchRoundIcon.setImageResource(R.drawable.ic_greendot)
    }
    private fun servingStatusColorDinner(){

        binding.servingStatusDinnerServingTV.setTextColor(resources.getColor(R.color.not_serving_radio_button_color, requireActivity().theme))
        binding.dinnerRoundIcon.setImageResource(R.drawable.ic_greendot)
    }

    private fun notServingStatusColorBrunch() {
        binding.servingStatusBrunchServingTV.setTextColor(resources.getColor(R.color.not_serving_radio_button_color, requireActivity().theme))
        binding.brunchRoundIcon.setImageResource(R.drawable.ic_reddot)
    }
    private fun notServingStatusColorDinner() {

        binding.servingStatusDinnerServingTV.setTextColor(resources.getColor(R.color.not_serving_radio_button_color, requireActivity().theme))
        binding.dinnerRoundIcon.setImageResource(R.drawable.ic_reddot)
    }


    private fun servedStatusColorBrunch() {
        binding.servingStatusBrunchServingTV.setTextColor(resources.getColor(R.color.loginbutton, requireActivity().theme))
        binding.brunchRoundIcon.setImageResource(R.drawable.ic_orangedot)
    }
    private fun servedStatusColorDinner(){

        binding.servingStatusDinnerServingTV.setTextColor(resources.getColor(R.color.loginbutton, requireActivity().theme))
        binding.dinnerRoundIcon.setImageResource(R.drawable.ic_orangedot)
    }



    private fun showProgressIndicator(isLoading: Boolean) {
        if(isLoading) {
            binding.servingStatusProgressBar.visibility = View.VISIBLE
        }else {
            binding.servingStatusProgressBar.visibility = View.GONE
        }
    }

}


