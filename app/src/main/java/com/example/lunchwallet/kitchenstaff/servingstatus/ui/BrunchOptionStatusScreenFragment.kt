package com.example.lunchwallet.kitchenstaff.servingstatus.ui

import android.icu.text.DateTimePatternGenerator.PatternInfo.OK
import android.os.Bundle
import android.util.Log
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
import com.example.lunchwallet.util.Resource
import com.example.lunchwallet.kitchenstaff.servingstatus.model.BrunchStatusRequest
import com.example.lunchwallet.kitchenstaff.servingstatus.model.BrunchStatusResponse
import com.example.lunchwallet.kitchenstaff.servingstatus.viewmodel.BrunchStatusViewModel
import com.example.lunchwallet.util.visibility
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

private const val TAG ="OptionStatusScreenFragment "

@AndroidEntryPoint

class BrunchOptionStatusScreenFragment : BottomSheetDialogFragment() {
    private var status:String?= null
    private val brunchViewModel: BrunchStatusViewModel by viewModels()
    private var servingStatus: String? = null
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
        return inflater.inflate(R.layout.fragment_option_status_screen, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        radioGroup = requireView().findViewById(R.id.option_status_radio_group)
        radioButtonServing = requireView().findViewById(R.id.option_status_serving_TV)
        radioButtonNotServing = requireView().findViewById(R.id.option_status_Not_serving_TV)
        radioButtonServed = requireView().findViewById(R.id.option_status_served_TV)


        radioButtonServing.setOnClickListener {
            servingStatus = radioButtonServing.text.toString()
            status = servingStatus!!.uppercase()
            observeBrunchStatusResponse()
            Log.d(TAG, "onViewCreated: radioButtonServing ${servingStatus}")
        }
        radioButtonNotServing.setOnClickListener {
            servingStatus = radioButtonNotServing.text.toString()
            status = servingStatus!!.uppercase()
            observeBrunchStatusResponse()
            Log.d(TAG, "onViewCreated: radioButtonNotServing ${servingStatus}")
        }
        radioButtonServed.setOnClickListener {
            servingStatus = radioButtonServed.text.toString()
            status = servingStatus!!.uppercase()
            observeBrunchStatusResponse()
            Log.d(TAG, "onViewCreated:radioButtonServed ${servingStatus}")
        }
    }


    private fun observeBrunchStatusResponse() {
        brunchViewModel.getBrunchStatus(BrunchStatusRequest(status))
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                brunchViewModel.brunchStatus.collect {
                    when (it) {
                        is Resource.Success -> {
                            requireView().findViewById<ProgressBar>(R.id.brunch_status_progress_bar).visibility(false)
                            findNavController().previousBackStackEntry?.savedStateHandle?.set(
                                "brunch_key",
                                status
                            )
                            dismiss()
                        }
                        is Resource.Error -> {
                            requireView().findViewById<ProgressBar>(R.id.brunch_status_progress_bar).visibility(false)
                            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT)
                                .show()
                        }
                        is Resource.Loading -> {
                            requireView().findViewById<ProgressBar>(R.id.brunch_status_progress_bar).visibility(true)
                        }
                    }
                }
            }
        }
    }

}

