package com.example.lunchwallet.admin.mealtimetable

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.lunchwallet.R
import com.example.lunchwallet.admin.uploadmeals.presentation.UploadMealsViewModel
import com.example.lunchwallet.common.mealtimetable.presentation.MealTimeTableViewModel
import com.example.lunchwallet.databinding.FragmentMealTimeTableBinding
import com.example.lunchwallet.util.*
import com.example.lunchwallet.util.adapter.CalenderAdapter
import com.example.lunchwallet.util.adapter.OnItemListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalDate

private const val TAG= "AdminMealTimeTable"

@AndroidEntryPoint
class MealTimeTableFragment : Fragment(), OnItemListener {

    private lateinit var binding: FragmentMealTimeTableBinding
    private lateinit var selectedDate: LocalDate
    private val viewModel: MealTimeTableViewModel by viewModels()
    private val uploadMealsViewModel: UploadMealsViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMealTimeTableBinding.inflate(layoutInflater)
        selectedDate = LocalDate.now()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setWeekView()
        onBackPressed()
//        observeMealTimeTable()
        usingUploadMealsViewModel()

        binding.backButton.setOnClickListener {
            findNavController().navigate(R.id.dashboardFragment)
        }

        when(binding.adminMealTimeTableDinnerServingStatusTv.text) {
            SERVING -> servingStatusColorDinner()
            NOT_SERVING -> notServingStatusColorDinner()
            SERVED -> servedStatusColorDinner()
        }

        when(binding.adminMealTimeTableDinnerServingStatusTv.text) {
            SERVING -> servingStatusColorDinner()
            NOT_SERVING -> notServingStatusColorDinner()
            SERVED -> servedStatusColorDinner()
        }

    }

    private fun setWeekView() {
        binding.monthYearTV.text = monthYearFromDate(selectedDate)
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

    override fun onItemClick(position: Int, date: LocalDate) {
        selectedDate = date
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
                            val mealTimeTableData = mealTimeTableUIState.data?.data

                            for(meal in mealTimeTableData?.indices!!) {
                                if (mealTimeTableData[meal].day == LocalDate.now().dayOfMonth &&
                                    mealTimeTableData[meal].month == LocalDate.now().monthValue &&
                                    mealTimeTableData[meal].year == LocalDate.now().year) {

                                    if (mealTimeTableData[meal].type == BRUNCH) {
                                        binding.adminMealTimeTableBrunchMealIdTv.text = mealTimeTableData[meal].ID.toString()
                                        binding.adminMealTimeTableBrunchKitchenTv.text = mealTimeTableData[meal].kitchen.toString()
                                        binding.adminMealTimeTableBrunchMealTv.text = mealTimeTableData[meal].name.toString()
                                        binding.adminMealTimeTableBrunchServingStatusTv.text = mealTimeTableData[meal].status.toString()
                                    }

                                    if (mealTimeTableData[meal].type == DINNER) {
                                        binding.adminMealTimeTableDinnerMealIdTv.text = mealTimeTableData[meal].ID.toString()
                                        binding.adminMealTimeTableDinnerKitchenTv.text = mealTimeTableData[meal].kitchen.toString()
                                        binding.adminMealTimeTableDinnerMealTv.text = mealTimeTableData[meal].name.toString()
                                        binding.adminMealTimeTableDinnerServingStatusTv.text = mealTimeTableData[meal].status.toString()

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



    private fun showProgressIndicator(isLoading: Boolean) {
        if(isLoading) {
            binding.adminMealTimeTableProgressBar.visibility = View.VISIBLE
        }else {
            binding.adminMealTimeTableProgressBar.visibility = View.GONE
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
                                        binding.adminMealTimeTableBrunchMealIdTv.text = mealTimeTableData[meal].ID.toString()
                                        binding.adminMealTimeTableBrunchKitchenTv.text = mealTimeTableData[meal].kitchen.toString()
                                        binding.adminMealTimeTableBrunchMealTv.text = mealTimeTableData[meal].name.toString()
                                        binding.adminMealTimeTableBrunchServingStatusTv.text = mealTimeTableData[meal].status.toString()
                                        when(mealTimeTableData[meal].status) {
                                            SERVING -> servingStatusColorBrunch()
                                            NOT_SERVING -> notServingStatusColorBrunch()
                                            SERVED -> servedStatusColorBrunch()
                                        }

                                    }

                                    if (mealTimeTableData[meal].type == DINNER) {
                                        binding.adminMealTimeTableDinnerMealIdTv.text = mealTimeTableData[meal].ID.toString()
                                        binding.adminMealTimeTableDinnerKitchenTv.text = mealTimeTableData[meal].kitchen.toString()
                                        binding.adminMealTimeTableDinnerMealTv.text = mealTimeTableData[meal].name.toString()
                                        binding.adminMealTimeTableDinnerServingStatusTv.text = mealTimeTableData[meal].status.toString()
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
                            Log.d(TAG, "observeMealTimeTable: mealTimeTableUIState is ${mealTimeTableUIState.data.data.get(0)}")

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
        binding.adminMealTimeTableBrunchServingStatusTv.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.serving_radio_button_color
            )
        )
        binding.adminMealTimeTableBrunchRadioButton.setImageResource(R.drawable.ic_greendot)
    }
    private fun servingStatusColorDinner(){

        binding.adminMealTimeTableDinnerServingStatusTv.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.serving_radio_button_color
            )
        )
        binding.adminMealTimeTableDinnerRadioButton.setImageResource(R.drawable.ic_greendot)
    }

    private fun notServingStatusColorBrunch() {
        binding.adminMealTimeTableBrunchServingStatusTv.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.not_serving_radio_button_color
            )
        )
        binding.adminMealTimeTableBrunchRadioButton.setImageResource(R.drawable.ic_reddot)
    }
    private fun notServingStatusColorDinner() {

        binding.adminMealTimeTableDinnerServingStatusTv.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.not_serving_radio_button_color
            )
        )
        binding.adminMealTimeTableDinnerRadioButton.setImageResource(R.drawable.ic_reddot)
    }


    private fun servedStatusColorBrunch() {
        binding.adminMealTimeTableBrunchServingStatusTv.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.loginbutton
            )
        )
        binding.adminMealTimeTableBrunchRadioButton.setImageResource(R.drawable.ic_orangedot)
    }
    private fun servedStatusColorDinner(){

        binding.adminMealTimeTableDinnerServingStatusTv.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.loginbutton
            )
        )
        binding.adminMealTimeTableDinnerRadioButton.setImageResource(R.drawable.ic_orangedot)
    }
}
