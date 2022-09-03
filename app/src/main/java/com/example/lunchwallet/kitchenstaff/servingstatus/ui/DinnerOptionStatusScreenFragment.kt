package com.example.lunchwallet.kitchenstaff.servingstatus.ui

import android.os.Bundle
import android.util.Log
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.lunchwallet.R
import com.example.lunchwallet.kitchenstaff.servingstatus.model.DinnerStatusRequest
import com.example.lunchwallet.kitchenstaff.servingstatus.viewmodel.DinnerStatusViewModel
import com.example.lunchwallet.util.Resource
import com.example.lunchwallet.util.visibility
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

private const val TAG ="DinnerOptionStatusScreenFragment"

@AndroidEntryPoint

class DinnerOptionStatusScreenFragment : BottomSheetDialogFragment() {
    private val dinnerViewModel: DinnerStatusViewModel by viewModels()
    private var dinnerServingStatus: String? = null
    private lateinit var radioGroup: RadioGroup
    private lateinit var radioButtonServing: RadioButton
    private lateinit var radioButtonNotServing: RadioButton
    private lateinit var radioButtonServed: RadioButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dinner_option_status_screen, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        radioGroup = requireView().findViewById(R.id.dinner_option_status_radio_group)
        radioButtonServing = requireView().findViewById(R.id.dinner_option_status_serving_TV)
        radioButtonNotServing = requireView().findViewById(R.id.dinner_option_status_Not_serving_TV)
        radioButtonServed = requireView().findViewById(R.id.dinner_option_status_served_TV)

        radioGroup.setOnCheckedChangeListener { radioGroup, i -> }

        radioButtonServing.setOnClickListener {
            dinnerServingStatus = radioButtonServing.text.toString().uppercase()
            observeDinnerStatusResponse()
            Log.d(TAG, "onViewCreated: radioButtonServing ${dinnerServingStatus}")
        }
        radioButtonNotServing.setOnClickListener {
            dinnerServingStatus = radioButtonNotServing.text.toString().uppercase()
            observeDinnerStatusResponse()
            Log.d(TAG, "onViewCreated: radioButtonNotServing ${dinnerServingStatus}")
        }
        radioButtonServed.setOnClickListener {
            dinnerServingStatus = radioButtonServed.text.toString().uppercase()
            observeDinnerStatusResponse()
            Log.d(TAG, "onViewCreated:radioButtonServed ${dinnerServingStatus}")
        }
    }


    private fun observeDinnerStatusResponse() {
        dinnerViewModel.getDinnerStatus(
            dinnerStatusRequest = DinnerStatusRequest(
                dinnerServingStatus
            )
        )
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                dinnerViewModel.dinnerStatus.collect {
                    when (it) {
                        is Resource.Success -> {
                            requireView().findViewById<ProgressBar>(R.id.dinner_status_progress_bar).visibility(false)
                            d(TAG, "observeDinnerStatusResponse: $it")
                            d(TAG, "observeDinnerStatusResponse: ${it.data}")
                            d(TAG, "observeDinnerStatusResponse: ${it.data?.status}")
                            d(TAG, "observeDinnerStatusResponse: ${it.data?.data}")
                            d(TAG, "observeDinnerStatusResponse: ${it.message}")
                            findNavController().previousBackStackEntry?.savedStateHandle?.set(
                                "dinner_key",
                                dinnerServingStatus
                            )
                            dismiss()
                        }
                        is Resource.Error -> {
                            requireView().findViewById<ProgressBar>(R.id.dinner_status_progress_bar).visibility(false)
                            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT)
                                .show()
                        }
                        is Resource.Loading -> {
                            requireView().findViewById<ProgressBar>(R.id.dinner_status_progress_bar).visibility(true)
                        }
                    }
                }
            }
        }
    }
}








