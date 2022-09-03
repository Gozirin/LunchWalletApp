package com.example.lunchwallet.foodbeneficiary.signup.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.lunchwallet.R
import com.example.lunchwallet.databinding.FragmentSignUpBinding
import com.example.lunchwallet.foodbeneficiary.signup.model.Beneficiary
import com.example.lunchwallet.util.*
import com.example.lunchwallet.util.validation.FieldValidations.validateConfirmPassword
import com.example.lunchwallet.util.validation.FieldValidations.verifyEmail
import com.example.lunchwallet.util.validation.FieldValidations.verifyLocation
import com.example.lunchwallet.util.validation.FieldValidations.verifyName
import com.example.lunchwallet.util.validation.FieldValidations.verifyPassword
import com.example.lunchwallet.util.validation.FieldValidations.verifyStack
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


private const val TAG = "SignUpFragment"
@AndroidEntryPoint
class SignUpFragment : Fragment(R.layout.fragment_sign_up) {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!
    private val viewModel: BeneficiarySignUpViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        stackDropDown()
        locationDropDown()
        nameFocusListener()
        emailFocusListener()
        stackFocusListener()
        locationFocusListener()
        passwordFocusListener()
        confirmPasswordFocusListener()
        Log.d(TAG, "onCreate:${binding.signUpFullNameTextView.text}," +
                " ${binding.signUpEmailTextView.text}, ${binding.signUpStackAutoView.text}" +
                ", ${binding.signUpLocationAutoView.text}, " +
                "${binding.signUpPasswordTextView.text}")

        binding.signUpBtn.setOnClickListener {

            submitForm()
        }

        binding.signUpToLogin.setOnClickListener {
            findNavController().navigate(R.id.loginFragment)
        }
    }

    private fun stackDropDown() {
        val stack = resources.getStringArray(R.array.stack)
        val adapter = ArrayAdapter(requireContext(), R.layout.stack_list, stack)
        with(binding.signUpStackAutoView) {
            setAdapter(adapter)
        }
    }

    private fun locationDropDown() {
        val location = resources.getStringArray(R.array.location)
        val adapter = ArrayAdapter(requireContext(), R.layout.location_list, location)
        with(binding.signUpLocationAutoView) {
            setAdapter(adapter)
        }
    }
    private fun nameFocusListener() {
        binding.signUpFullNameTextView.setOnFocusChangeListener { _, focused ->
            if (!focused)
                binding.fullNameContainer.helperText = verifyName(binding.signUpFullNameTextView.text.toString())
        }
    }

    private fun emailFocusListener() {
        binding.signUpEmailTextView.setOnFocusChangeListener { _, focused ->
            if (!focused)
                binding.emailContainer.helperText = verifyEmail(binding.signUpEmailTextView.text.toString())
        }
    }

    private fun stackFocusListener() {
        binding.signUpStackAutoView.setOnFocusChangeListener { _, focused ->
            if (!focused)
                binding.stackContainer.helperText = verifyStack(binding.signUpStackAutoView.text.toString())
        }
    }

    private fun locationFocusListener() {
        binding.signUpLocationAutoView.setOnFocusChangeListener { _, focused ->
            if (!focused)
                binding.locationContainer.helperText = verifyLocation(binding.signUpLocationAutoView.text.toString())
        }
    }

    private fun passwordFocusListener() {
        binding.signUpPasswordTextView.setOnFocusChangeListener { _, focused ->
            if (!focused)
                binding.passwordContainer.helperText = verifyPassword(binding.signUpPasswordTextView.text.toString())
        }
    }

    private fun confirmPasswordFocusListener() {
        binding.signUpConfirmPasswordTextView.setOnFocusChangeListener { _, focused ->
            if (!focused)
                binding.confirmPasswordContainer.helperText = validateConfirmPassword(binding.signUpPasswordTextView.text.toString(), binding.signUpConfirmPasswordTextView.text.toString())
        }
    }

    private fun submitForm() {
        binding.fullNameContainer.helperText = verifyName(binding.signUpFullNameTextView.text.toString())
        binding.emailContainer.helperText = verifyEmail(binding.signUpEmailTextView.text.toString())
        binding.stackContainer.helperText = verifyStack(binding.signUpStackAutoView.text.toString())
        binding.locationContainer.helperText = verifyLocation(binding.signUpLocationAutoView.text.toString())
        binding.passwordContainer.helperText = verifyPassword(binding.signUpPasswordTextView.text.toString())
        binding.confirmPasswordContainer.helperText = validateConfirmPassword(binding.signUpPasswordTextView.text.toString(), binding.signUpConfirmPasswordTextView.text.toString())

        val validName = binding.fullNameContainer.helperText == null
        val validEmail = binding.emailContainer.helperText == null
        val validStack = binding.stackContainer.helperText == null
        val validLocation = binding.locationContainer.helperText == null
        val validPassword = binding.passwordContainer.helperText == null
        val validConfirmPassword = binding.confirmPasswordContainer.helperText == null

        if (validEmail && validName && validStack && validLocation && validPassword && validConfirmPassword) {
//            signUpButtonStatus(false)
//            binding.signUpBtn.text = getString(R.string.button_registering)
//            showProgressIndicator(true)
            observeSignUpState()
//            findNavController().navigate(R.id.checkMailFragment)
        } else { invalidForm() }
    }

    private fun invalidForm() {
        var message = ""
        if (binding.fullNameContainer.helperText != null)
            message = "\n\nName: " + binding.emailContainer.helperText
        if (binding.emailContainer.helperText != null)
            message += "\n\nEmail: " + binding.emailContainer.helperText
        if (binding.stackContainer.helperText != null)
            message += "\n\nStack: " + binding.stackContainer.helperText
        if (binding.locationContainer.helperText != null)
            message += "\n\nLocation: " + binding.locationContainer.helperText
        if (binding.passwordContainer.helperText != null)
            message += "\n\nPassword: " + binding.passwordContainer.helperText
        if (binding.fullNameContainer.helperText != null)
            message += "\n\nName: " + binding.confirmPasswordContainer.helperText
    }

    private fun observeSignUpState() {
        val fullName = binding.signUpFullNameTextView.text.toString()
        val email = binding.signUpEmailTextView.text.toString()
        val stack = binding.signUpStackAutoView.text.toString()
        val location = binding.signUpLocationAutoView.text.toString()
        val password = binding.signUpPasswordTextView.text.toString()

        val newBeneficiary = Beneficiary(fullName, email, stack, location, password)
        Log.d(TAG, "newBeneficiary: $newBeneficiary")

        viewModel.signUpBeneficiary(newBeneficiary)

        // Start a coroutine in the lifecycle scope
        viewLifecycleOwner.lifecycleScope.launch {
            // repeatOnLifecycle launches the block in a new coroutine every time the
            // lifecycle is in the STARTED state (or above) and cancels it when it's STOPPED.
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                // Trigger the flow and start listening for values.
                // Note that this happens when lifecycle is STARTED and stops
                // collecting when the lifecycle is STOPPED
                viewModel.beneficiarySignUpState.collect { beneficiarySignUpState ->
                    // New value received
                    when (beneficiarySignUpState) {
                       is Resource.Success -> {
                           clearInputFields()
                           findNavController().navigate(R.id.action_signUpFragment_to_checkMailFragment)
                        }
                        is Resource.Error -> {
                            clearPasswordFields()
                            binding.signUpBtn.text = getString(R.string.button_sign_up)
                            showProgressIndicator(false)
                            requireView().snackbar( beneficiarySignUpState.message.toString(), requireContext(), null)
                            signUpButtonStatus(true)
                        }
                        is Resource.Loading -> {
                            signUpButtonStatus(false)
                            binding.signUpBtn.text = getString(R.string.button_registering)
                            showProgressIndicator(true)
                        }
                    }
                }
            }
        }

    }


    private fun showProgressIndicator(isLoading: Boolean) {
        if(isLoading) {
        binding.beneficiarySignupProgressIndicator.visibility = View.VISIBLE
        }else {
            binding.beneficiarySignupProgressIndicator.visibility = View.GONE
        }
    }

    private fun clearPasswordFields(){
        binding.signUpPasswordTextView.text.clear()
        binding.signUpConfirmPasswordTextView.text.clear()
    }
    private fun signUpButtonStatus(isEnabled: Boolean) {
        if (isEnabled) {
            binding.signUpBtn.isEnabled
        } else {
            !binding.signUpBtn.isEnabled
        }
    }
    private fun clearInputFields() {
        binding.signUpFullNameTextView.text.clear()
        binding.signUpEmailTextView.text.clear()
        binding.signUpStackAutoView.text.clear()
        binding.signUpLocationAutoView.text.clear()
        binding.signUpPasswordTextView.text.clear()
    }
}
