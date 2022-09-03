package com.example.lunchwallet.common.confirmMail.presentation.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.lunchwallet.R
import com.example.lunchwallet.common.confirmMail.presentation.ConfirmEmailViewModel
import com.example.lunchwallet.databinding.FragmentConfirmEmailKitchenStaffBinding
import com.example.lunchwallet.util.Resource
import com.example.lunchwallet.util.snackbar
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.concurrent.schedule

private const val TAG = "ConfirmEmailKitchenStaff"

@AndroidEntryPoint
class ConfirmEmailKitchenStaff : Fragment() {

    private var _binding: FragmentConfirmEmailKitchenStaffBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ConfirmEmailViewModel by viewModels()
    private val args: ConfirmEmailBeneficiaryArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentConfirmEmailKitchenStaffBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        binding.confirmEmailFragmentConfirmEmailKitchenStaffButton.setOnClickListener {
            confirmEmail()
        }

    }

    private fun confirmEmail() {
        val token = args.token
        if (token != null) {
            viewModel.verifyKitchenStaffEmail(token)
        }

        // Start a coroutine in the lifecycle scope
        viewLifecycleOwner.lifecycleScope.launch {
            // repeatOnLifecycle launches the block in a new coroutine every time the
            // lifecycle is in the STARTED state (or above) and cancels it when it's STOPPED.
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                // Trigger the flow and start listening for values.
                // Note that this happens when lifecycle is STARTED and stops
                // collecting when the lifecycle is STOPPED
                viewModel.confirmEmailState.collect { confirmEmailUIState ->
                    // New value received
                    when (confirmEmailUIState) {
                        is Resource.Success -> {
                            showProgressIndicator(false)
                            findNavController().navigate(R.id.loginFragment)

                        }
                        is Resource.Error -> {
                            showProgressIndicator(false)
                            requireView().snackbar(confirmEmailUIState.message.toString(), requireContext(), null)
                            binding.confirmEmailFragmentConfirmEmailKitchenStaffButton.isEnabled = true
                        }
                        is Resource.Loading -> {
                            showProgressIndicator(true)
                            binding.confirmEmailFragmentConfirmEmailKitchenStaffButton.isEnabled = false
                        }
                    }
                }
            }
        }
    }

    private fun showProgressIndicator(isLoading: Boolean) {
        if(isLoading) {
            binding.confirmEmailKitchenStaffProgressBar.visibility = View.VISIBLE
        }else {
            binding.confirmEmailKitchenStaffProgressBar.visibility = View.GONE
        }
    }
}