package com.example.lunchwallet.common.confirmMail.presentation.ui

import android.os.Bundle
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
import com.example.lunchwallet.databinding.FragmentConfirmEmailBeneficiaryBinding
import com.example.lunchwallet.util.Resource
import com.example.lunchwallet.util.snackbar
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*
import kotlin.concurrent.schedule

private const val TAG = "ConfirmEmailBeneficiary"

@AndroidEntryPoint
class ConfirmEmailBeneficiary : Fragment() {
    private var _binding: FragmentConfirmEmailBeneficiaryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ConfirmEmailViewModel by viewModels()
    private val args: ConfirmEmailBeneficiaryArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentConfirmEmailBeneficiaryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.confirmEmailFragmentConfirmEmailBeneficiaryButton.setOnClickListener {
            confirmEmail()
        }

    }

    private fun confirmEmail() {
        val token = args.token
        if (token != null) {
            viewModel.verifyBeneficiaryEmail(token)
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
                            binding.confirmEmailFragmentConfirmEmailBeneficiaryButton.isEnabled = true
                        }
                        is Resource.Loading -> {
                            showProgressIndicator(true)
                            binding.confirmEmailFragmentConfirmEmailBeneficiaryButton.isEnabled = false
                        }
                    }
                }
            }
        }
    }

    private fun showProgressIndicator(isLoading: Boolean) {
        if (isLoading) {
            binding.confirmEmailBeneficiaryProgressBar.visibility = View.VISIBLE
        } else {
            binding.confirmEmailBeneficiaryProgressBar.visibility = View.GONE
        }
    }


}