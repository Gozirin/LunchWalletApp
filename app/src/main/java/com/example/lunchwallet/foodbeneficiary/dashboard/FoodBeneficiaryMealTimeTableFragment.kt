package com.example.lunchwallet.foodbeneficiary.dashboard

import android.content.Intent
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
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.lunchwallet.MainActivity
import com.example.lunchwallet.R
import com.example.lunchwallet.admin.uploadmeals.presentation.UploadMealsViewModel
import com.example.lunchwallet.common.authentication.UserDatastore
import com.example.lunchwallet.common.mealtimetable.model.Data
import com.example.lunchwallet.common.mealtimetable.presentation.MealTimeTableViewModel
import com.example.lunchwallet.databinding.FragmentFoodBeneficiaryMealTimeTableBinding
import com.example.lunchwallet.foodbeneficiary.qrscan.presentation.ScanViewModel
import com.example.lunchwallet.foodbeneficiary.qrscan.utils.makeStatusNotification
import com.example.lunchwallet.util.*
import com.example.lunchwallet.util.adapter.CalenderAdapter
import com.google.zxing.integration.android.IntentIntegrator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.json.JSONException
import java.time.LocalDate


private val TAG = "FoodBeneficiaryMealTimeTable"

@AndroidEntryPoint
class FoodBeneficiaryMealTimeTableFragment : Fragment() {
    private lateinit var binding: FragmentFoodBeneficiaryMealTimeTableBinding
    private lateinit var selectedDate: LocalDate
    private val viewModel: MealTimeTableViewModel by viewModels()
    private val uploadMealsViewModel: UploadMealsViewModel by viewModels()
    private lateinit var qrScanIntegrator: IntentIntegrator
    private val scanViewModel: ScanViewModel by viewModels()
    private var scanResult: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentFoodBeneficiaryMealTimeTableBinding.inflate(layoutInflater)

        selectedDate = LocalDate.now()
        (requireActivity() as MainActivity).apply {
            setStatusBarColor(Color.WHITE)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupScanner()
        onBackPressed()
        setWeekView()
//        observeMealTimeTable()
        observeGetAllMeals()

        when(binding.beneficiaryMealTimeTableBrunchServingStatusTv.text) {
            SERVING -> servingStatusColorBrunch()
            NOT_SERVING -> notServingStatusColorBrunch()
            SERVED -> servedStatusColorBrunch()
        }

        when(binding.beneficiaryMealTimeTableDinnerServingStatusTv.text) {
            SERVING -> servingStatusColorDinner()
            NOT_SERVING -> notServingStatusColorDinner()
            SERVED -> servedStatusColorDinner()
        }


        binding.mealTimeTableBrunchScanButton.setOnClickListener {
            performAction()
        }

        binding.mealTimeTableDinnerScanButton.setOnClickListener {
            performAction()
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
                requireActivity().finish()
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
                            if (mealTimeTableData?.type.toString() == BRUNCH) {
                                binding.beneficiaryMealTimeTableBrunchMealIdTv.text = mealTimeTableData?.ID.toString()
                                binding.beneficiaryMealTimeTableBrunchKitchenTv.text = mealTimeTableData?.kitchen.toString()
                                binding.beneficiaryMealTimeTableBrunchMealTv.text = mealTimeTableData?.name.toString()
                                binding.beneficiaryMealTimeTableBrunchServingStatusTv.text = mealTimeTableData?.status.toString()

                            }


                            if (mealTimeTableData?.type.toString() == DINNER) {
                                binding.beneficiaryMealTimeTableDinnerMealIdTv.text = mealTimeTableData?.ID.toString()
                                binding.beneficiaryMealTimeTableDinnerKitchenTv.text = mealTimeTableData?.kitchen.toString()
                                binding.beneficiaryMealTimeTableDinnerMealTv.text = mealTimeTableData?.name.toString()
                                binding.beneficiaryMealTimeTableDinnerServingStatusTv.text = mealTimeTableData?.status.toString()


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
            binding.beneficiaryMealTimeTableProgressBar.visibility = View.VISIBLE
        }else {
            binding.beneficiaryMealTimeTableProgressBar.visibility = View.GONE
        }
    }

    private fun  observeGetAllMeals(){
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
                                    mealTimeTableData[meal].year == LocalDate.now().year
                                ) {

                                    if (mealTimeTableData[meal].type == BRUNCH) {
                                        binding.beneficiaryMealTimeTableBrunchMealIdTv.text =
                                            mealTimeTableData[meal].ID
                                        binding.beneficiaryMealTimeTableBrunchKitchenTv.text =
                                            mealTimeTableData[meal].kitchen
                                        binding.beneficiaryMealTimeTableBrunchMealTv.text =
                                            mealTimeTableData[meal].name
                                        binding.beneficiaryMealTimeTableBrunchServingStatusTv.text =
                                            mealTimeTableData[meal].status

                                        when(mealTimeTableData[meal].status) {
                                            SERVING -> servingStatusColorBrunch()
                                            NOT_SERVING -> notServingStatusColorBrunch()
                                            SERVED -> servedStatusColorBrunch()
                                        }
                                    }


                                    if (mealTimeTableData[meal].type == DINNER) {
                                        binding.beneficiaryMealTimeTableDinnerMealIdTv.text =
                                            mealTimeTableData[meal].ID
                                        binding.beneficiaryMealTimeTableDinnerKitchenTv.text =
                                            mealTimeTableData[meal].kitchen
                                        binding.beneficiaryMealTimeTableDinnerMealTv.text =
                                            mealTimeTableData[meal].name
                                        binding.beneficiaryMealTimeTableDinnerServingStatusTv.text =
                                            mealTimeTableData[meal].status

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
        binding.beneficiaryMealTimeTableBrunchServingStatusTv.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.serving_radio_button_color
            )
        )
        binding.beneficiaryMealTimeTableBrunchRadioButton.setImageResource(R.drawable.ic_greendot)
    }
    private fun servingStatusColorDinner(){

            binding.beneficiaryMealTimeTableDinnerServingStatusTv.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.serving_radio_button_color
                )
            )
            binding.beneficiaryMealTimeTableDinnerRadioButton.setImageResource(R.drawable.ic_greendot)
    }

    private fun notServingStatusColorBrunch() {
        binding.beneficiaryMealTimeTableBrunchServingStatusTv.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.not_serving_radio_button_color
            )
        )
        binding.beneficiaryMealTimeTableBrunchRadioButton.setImageResource(R.drawable.ic_reddot)
    }
    private fun notServingStatusColorDinner() {

                binding.beneficiaryMealTimeTableDinnerServingStatusTv.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.not_serving_radio_button_color
                    )
                )
                binding.beneficiaryMealTimeTableDinnerRadioButton.setImageResource(R.drawable.ic_reddot)
    }


    private fun servedStatusColorBrunch() {
        binding.beneficiaryMealTimeTableBrunchServingStatusTv.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.loginbutton
            )
        )
        binding.beneficiaryMealTimeTableBrunchRadioButton.setImageResource(R.drawable.ic_orangedot)
    }
    private fun servedStatusColorDinner(){

            binding.beneficiaryMealTimeTableDinnerServingStatusTv.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.loginbutton
                )
            )
            binding.beneficiaryMealTimeTableDinnerRadioButton.setImageResource(R.drawable.ic_orangedot)
    }

    private fun setupScanner() {
        qrScanIntegrator = IntentIntegrator.forSupportFragment(this)
        qrScanIntegrator.setOrientationLocked(false)
    }

    private fun performAction() {
        // Code to perform action when button is clicked.
        qrScanIntegrator.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            // If QRCode has no data.
            if (result.contents == null) {
                Toast.makeText(activity, R.string.result_not_found, Toast.LENGTH_LONG).show()
            } else {
                // If QRCode contains data.
                try {
                    //Get the data from the scan.
                        scanResult = result.contents
                        onScanSuccess()

                } catch (e: JSONException) {
                    e.printStackTrace()

                    // Data not in the expected format. So, whole object as toast message.
                    Toast.makeText(activity, result.contents, Toast.LENGTH_LONG).show()
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun onScanSuccess() {
        scanViewModel.onScanSuccess(scanResult!!, scanViewModel.getUserId()!!)
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                scanViewModel.qrScanState.collect { code ->
                    when (code) {
                        is Resource.Success -> {
                            Log.d(TAG, "getCodeResponse: ${code.data?.data}")
                            Log.d(TAG, "getCodeResponse: ${code.data}")
                            makeStatusNotification("${code.data?.message}", requireContext().applicationContext)

                        }
                        is Resource.Error -> {
                            code.data?.errors?.get(0)
                            requireView().snackbar(code.message.toString(), requireContext(), null)
                            code.data?.errors?.get(0)
                                ?.let { requireView().snackbar(it, requireContext(), null) }
                        }
                        is Resource.Loading -> {
                        }
                    }
                }

            }
        }
    }

}
