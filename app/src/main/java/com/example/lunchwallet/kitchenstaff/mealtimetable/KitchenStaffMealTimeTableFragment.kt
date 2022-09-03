package com.example.lunchwallet.kitchenstaff.mealtimetable

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.lunchwallet.MainActivity
import com.example.lunchwallet.R
import com.example.lunchwallet.admin.uploadmeals.presentation.UploadMealsViewModel
import com.example.lunchwallet.common.authentication.Code
import com.example.lunchwallet.common.authentication.UserDatastore
import com.example.lunchwallet.common.mealtimetable.presentation.MealTimeTableViewModel
import com.example.lunchwallet.databinding.FragmentKitchenStaffMealTimeTableBinding
import com.example.lunchwallet.kitchenstaff.dashboard.GenerateCodeResponse
import com.example.lunchwallet.kitchenstaff.qr.presentation.GenerateCodeViewModel
import com.example.lunchwallet.util.*
import com.example.lunchwallet.util.adapter.CalenderAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalDate

private val TAG = "KitchenStaffMealTimeTable"

@AndroidEntryPoint
class KitchenStaffMealTimeTableFragment : Fragment() {
    private lateinit var binding: FragmentKitchenStaffMealTimeTableBinding
    private lateinit var selectedDate: LocalDate
    private val viewModel: MealTimeTableViewModel by viewModels()
    private val uploadMealsViewModel: UploadMealsViewModel by viewModels()
    private lateinit var datastore: UserDatastore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentKitchenStaffMealTimeTableBinding.inflate(layoutInflater)

        selectedDate = LocalDate.now()
        (requireActivity() as MainActivity).apply {
            setStatusBarColor(Color.WHITE)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        datastore = UserDatastore(requireContext())
        onBackPressed()
        setWeekView()
//        observeMealTimeTable()
        usingUploadMealsViewModel()

        when(binding.kitchenStaffMealTimeTableBrunchServingStatusTv.text) {
            SERVING  -> servingStatusColorBrunch()
            NOT_SERVING -> notServingStatusColorBrunch()
            SERVED -> servedStatusColorBrunch()
        }

        when(binding.kitchenStaffMealTimeTableDinnerServingStatusTv.text) {
            SERVING -> servingStatusColorDinner()
            NOT_SERVING -> notServingStatusColorDinner()
            SERVED -> servedStatusColorDinner()
        }

        binding.kitchenMealMenuBackButton.setOnClickListener {
            findNavController().navigate(R.id.kitchenStaffDashBoardFragment)
        }

        binding.mealTimeTableBrunchGenerateButton.setOnClickListener {
            datastore.brunchCode.asLiveData().observe(viewLifecycleOwner) {
                if (it != null) {
                    Log.d(TAG, "onViewCreated:brunchcode ")
                    findNavController().navigate(R.id.generateCodeBrunch)
                } else {
                    Toast.makeText(requireContext(), "Code is empty", Toast.LENGTH_SHORT).show()
                    Log.d("XYZ", "onViewCreated:brunchcode $it")
                }
            }
        }

        binding.mealTimeTableDinnerGenerateButton.setOnClickListener {
            datastore.dinnerCode.asLiveData().observe(viewLifecycleOwner) {
                if (it != null) {
                    Log.d(TAG, "onViewCreated: dinnercode ")
                    findNavController().navigate(R.id.generateCodeDinner)
                } else {
                    Toast.makeText(requireContext(), "Code is empty", Toast.LENGTH_SHORT).show()
                    Log.d("XYZ", "onViewCreated:dinnercode $it")
                }
            }
        }
    }

    private fun setWeekView() {
        binding.monthYearTV.text = monthYearFromDate(selectedDate)
        // val daysInMonth = daysInMonthArray(selectedDate)
        val daysInWeek = daysInWeekArray(selectedDate)
        val calendarAdapter = CalenderAdapter(daysInWeek)
        val layoutManager = GridLayoutManager(context, 7)
        binding.calendarRecyclerView.layoutManager = layoutManager
        binding.calendarRecyclerView.adapter = calendarAdapter
    }

    fun previousWeekAction(view: View?) {
        selectedDate = selectedDate.minusWeeks(1)
        setWeekView()
    }

    fun nextWeekAction(view: View) {
        selectedDate = selectedDate.plusWeeks(1)
        setWeekView()
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

    private fun observeMealTimeTable(){
        viewModel.getBrunch()
        viewModel.getDinner()
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.mealState.collect{ mealTimeTableUIState ->
                    when(mealTimeTableUIState) {
                        is Resource.Success -> {
                            showProgressIndicator(false)
                            val mealTimeTableData = mealTimeTableUIState.data?.data?.get(0)
                            if (mealTimeTableUIState.data?.data?.get(0)?.type.toString() == BRUNCH) {
                                binding.kitchenStaffMealTimeTableBrunchMealIdTv.text = mealTimeTableData?.ID.toString()
                                binding.kitchenStaffMealTimeTableBrunchKitchenTv.text = mealTimeTableData?.kitchen.toString()
                                binding.kitchenStaffMealTimeTableBrunchMealTv.text = mealTimeTableData?.name.toString()
                                binding.kitchenStaffMealTimeTableBrunchServingStatusTv.text = mealTimeTableData?.status.toString()
                            }


                            if (mealTimeTableUIState.data?.data?.get(0)?.type.toString() == DINNER) {
                                binding.kitchenStaffMealTimeTableDinnerMealIdTv.text = mealTimeTableData?.ID.toString()
                                binding.kitchenStaffMealTimeTableDinnerKitchenTv.text = mealTimeTableData?.kitchen.toString()
                                binding.kitchenStaffMealTimeTableDinnerMealTv.text = mealTimeTableData?.name.toString()
                                binding.kitchenStaffMealTimeTableDinnerServingStatusTv.text = mealTimeTableData?.status.toString()

                            }

                            Log.d(TAG, "observeMealTimeTable: mealTimeTableUIState is $mealTimeTableUIState")
                            Log.d(TAG, "observeMealTimeTable: mealTimeTableUIState.message is ${mealTimeTableUIState.message}")
                            Log.d(TAG, "observeMealTimeTable: mealTimeTableUIState.data is ${mealTimeTableUIState.data}")
                            Log.d(TAG, "observeMealTimeTable: mealTimeTableUIState is ${mealTimeTableUIState.data?.data?.get(0)}")

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

    private fun showProgressIndicator(isLoading: Boolean) {
        if(isLoading) {
            binding.kitchenStaffMealTimeTableProgressBar.visibility = View.VISIBLE
        }else {
            binding.kitchenStaffMealTimeTableProgressBar.visibility = View.GONE
        }
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
                                        binding.kitchenStaffMealTimeTableBrunchMealIdTv.text = mealTimeTableData[meal].ID
//                                        datastore.saveCode(Code(brunchCode = mealTimeTableData[meal].ID.toString() ))
                                        binding.kitchenStaffMealTimeTableBrunchKitchenTv.text = mealTimeTableData[meal].kitchen.toString()
                                        binding.kitchenStaffMealTimeTableBrunchMealTv.text = mealTimeTableData[meal].name.toString()
                                        binding.kitchenStaffMealTimeTableBrunchServingStatusTv.text = mealTimeTableData[meal].status.toString()
                                        when(mealTimeTableData[meal].status) {
                                            SERVING -> servingStatusColorBrunch()
                                            NOT_SERVING -> notServingStatusColorBrunch()
                                            SERVED -> servedStatusColorBrunch()
                                        }
                                    }

                                    if (mealTimeTableData[meal].type == DINNER) {
                                        binding.kitchenStaffMealTimeTableDinnerMealIdTv.text = mealTimeTableData[meal].ID
//                                        datastore.saveCode(Code(dinnerCode = mealTimeTableData[meal].ID))
                                        binding.kitchenStaffMealTimeTableDinnerKitchenTv.text = mealTimeTableData[meal].kitchen.toString()
                                        binding.kitchenStaffMealTimeTableDinnerMealTv.text = mealTimeTableData[meal].name.toString()
                                        binding.kitchenStaffMealTimeTableDinnerServingStatusTv.text = mealTimeTableData[meal].status.toString()
                                        when(mealTimeTableData[meal].status) {
                                            SERVING -> servingStatusColorDinner()
                                            NOT_SERVING -> notServingStatusColorDinner()
                                            SERVED -> servedStatusColorDinner()
                                        }

                                    }

                                }
                            }
                            Log.d(TAG, "observeMealTimeTable: mealTimeTableUIState is $mealTimeTableUIState")
                            Log.d(TAG, "observeMealTimeTable: mealTimeTableUIState.message is ${mealTimeTableUIState.message}")
                            Log.d(TAG, "observeMealTimeTable: mealTimeTableUIState.data is ${mealTimeTableUIState.data}")
                            Log.d(TAG, "observeMealTimeTable: mealTimeTableUIState is ${mealTimeTableUIState.data?.data?.get(0)}")

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
        binding.kitchenStaffMealTimeTableBrunchServingStatusTv.setTextColor(
            resources.getColor(
                R.color.serving_radio_button_color, requireActivity().theme
            )
        )
        binding.kitchenStaffMealTimeTableBrunchRadioButton.setImageResource(R.drawable.ic_greendot)
    }
    private fun servingStatusColorDinner(){

        binding.kitchenStaffMealTimeTableDinnerServingStatusTv.setTextColor(
            resources.getColor(
                R.color.serving_radio_button_color, requireActivity().theme
            )
        )
        binding.kitchenStaffMealTimeTableDinnerRadioButton.setImageResource(R.drawable.ic_greendot)
    }

    private fun notServingStatusColorBrunch() {
        binding.kitchenStaffMealTimeTableBrunchServingStatusTv.setTextColor(
            resources.getColor(
                R.color.not_serving_radio_button_color, requireActivity().theme
            )
        )
        binding.kitchenStaffMealTimeTableBrunchRadioButton.setImageResource(R.drawable.ic_reddot)
    }
    private fun notServingStatusColorDinner() {

        binding.kitchenStaffMealTimeTableDinnerServingStatusTv.setTextColor(
            resources.getColor(
                R.color.not_serving_radio_button_color, requireActivity().theme
            )
        )
        binding.kitchenStaffMealTimeTableDinnerRadioButton.setImageResource(R.drawable.ic_reddot)
    }


    private fun servedStatusColorBrunch() {
        binding.kitchenStaffMealTimeTableBrunchServingStatusTv.setTextColor(
            resources.getColor(
                R.color.loginbutton, requireActivity().theme
            )
        )
        binding.kitchenStaffMealTimeTableBrunchRadioButton.setImageResource(R.drawable.ic_orangedot)
    }
    private fun servedStatusColorDinner(){

        binding.kitchenStaffMealTimeTableDinnerServingStatusTv.setTextColor(
            resources.getColor(
                R.color.loginbutton, requireActivity().theme
            )
        )
        binding.kitchenStaffMealTimeTableDinnerRadioButton.setImageResource(R.drawable.ic_orangedot)
    }



}