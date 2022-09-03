package com.example.lunchwallet.kitchenstaff.signup.presentation

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.example.lunchwallet.R
import com.example.lunchwallet.databinding.FragmentKitchenStaffSignUpBinding
import com.example.lunchwallet.kitchenstaff.signup.model.KitchenStaff
import com.example.lunchwallet.util.*
import com.example.lunchwallet.util.loginvalidation.LoginInputValidation
import com.example.lunchwallet.util.validation.FieldValidations
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

private const val TAG = "KitchenStaffFragment"

@AndroidEntryPoint
class KitchenStaffSignUpFragment : Fragment() {
    private lateinit var binding: FragmentKitchenStaffSignUpBinding
    private val viewModel: KitchenStaffSignUpViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentKitchenStaffSignUpBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nameFocusListener()
        emailFocusListener()
        locationDropDown()
        locationFocusListener()
        passwordFocusListener()
        confirmPasswordFocusListener()


        binding.signUpBtn.setOnClickListener {
            signUpButtonStatus(false)
            submitForm()
        }

        binding.signUpToLogin.setOnClickListener {
            findNavController().navigate(R.id.action_kitchenStaffSignUpFragment_to_loginFragment)
        }
    }

    private fun locationDropDown() {
        val location = resources.getStringArray(R.array.location)
        val adapter = ArrayAdapter(requireContext(), R.layout.location_list, location)
        adapter.setNotifyOnChange(true)
        with(binding.signUpLocationDropdown) {
            setAdapter(adapter)
//            setOnItemClickListener { adapterView, view, i, l ->
//
//            }
        }
    }

    private fun nameFocusListener() {
        binding.signUpFullNameTextView.setOnFocusChangeListener { _, focused ->
            if (!focused)
                binding.fullNameContainer.helperText =
                    FieldValidations.verifyName(binding.signUpFullNameTextView.text.toString())
        }
    }

    private fun emailFocusListener() {
        binding.signUpEmailTextView.setOnFocusChangeListener { _, focused ->
            if (!focused)
                binding.emailContainer.helperText =
                    LoginInputValidation.validateEmail(binding.signUpEmailTextView.text.toString())
        }
    }

    private fun locationFocusListener() {
        binding.signUpLocationDropdown.setOnFocusChangeListener { _, focused ->
            if (!focused)
                binding.locationContainer.helperText =
                    FieldValidations.verifyLocation(binding.signUpLocationDropdown.text.toString())
        }
    }

    private fun passwordFocusListener() {
        binding.signUpPasswordTextView.setOnFocusChangeListener { _, focused ->
            if (!focused)
                binding.passwordContainer.helperText =
                    FieldValidations.verifyPassword(binding.signUpPasswordTextView.text.toString())
        }
    }

    private fun confirmPasswordFocusListener() {
        binding.signUpConfirmPasswordTextView.setOnFocusChangeListener { _, focused ->
            if (!focused)
                binding.confirmPasswordContainer.helperText =
                    FieldValidations.validateConfirmPassword(
                        binding.signUpPasswordTextView.text.toString(),
                        binding.signUpConfirmPasswordTextView.text.toString()
                    )
        }
    }

    private fun submitForm() {
        binding.fullNameContainer.helperText =
            FieldValidations.verifyName(binding.signUpFullNameTextView.text.toString())
        binding.emailContainer.helperText =
            LoginInputValidation.validateEmail(binding.signUpEmailTextView.text.toString())
        binding.locationContainer.helperText =
            FieldValidations.verifyLocation(binding.signUpLocationDropdown.text.toString())
        binding.passwordContainer.helperText =
            FieldValidations.verifyPassword(binding.signUpPasswordTextView.text.toString())
        binding.confirmPasswordContainer.helperText = FieldValidations.validateConfirmPassword(
            binding.signUpPasswordTextView.text.toString(),
            binding.signUpConfirmPasswordTextView.text.toString()
        )

        val validName = binding.fullNameContainer.helperText == null
        val validEmail = binding.emailContainer.helperText == null
        val validLocation = binding.locationContainer.helperText == null
        val validPassword = binding.passwordContainer.helperText == null
        val validConfirmPassword = binding.confirmPasswordContainer.helperText == null

        if (validEmail && validName && validLocation && validPassword && validConfirmPassword) {
            showProgressIndicator(true)
            binding.signUpBtn.text = getString(R.string.button_registering)
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
        val location = binding.signUpLocationDropdown.text.toString()
        val password = binding.signUpPasswordTextView.text.toString()

        val newKitchenStaff = KitchenStaff(fullName, email, location, password)
        Log.d(TAG, "newBeneficiary: $newKitchenStaff")

        viewModel.signUpKitchenStaff(newKitchenStaff)
        // Start a coroutine in the lifecycle scope
        viewLifecycleOwner.lifecycleScope.launch {
            // repeatOnLifecycle launches the block in a new coroutine every time the
            // lifecycle is in the STARTED state (or above) and cancels it when it's STOPPED.
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                // Trigger the flow and start listening for values.
                // Note that this happens when lifecycle is STARTED and stops
                // collecting when the lifecycle is STOPPED
                viewModel.kitchenStaffSignUpState.collect { beneficiarySignUpState ->
                    // New value received
                    when (beneficiarySignUpState) {
                        is Resource.Success -> {
                            clearInputFields()
                            findNavController().safeNavigate(KitchenStaffSignUpFragmentDirections.actionKitchenStaffSignUpFragmentToCheckMailFragment())
//                            findNavController().navigate(R.id.action_signUpFragment_to_checkMailFragment)
                        }
                        is Resource.Error -> {
                            clearPasswordFields()
                            binding.signUpBtn.text = getString(R.string.button_sign_up)
                            showProgressIndicator(false)
                            requireView().snackbar(beneficiarySignUpState.message.toString(),requireContext(),binding.signUpBtn)
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
            binding.kitchenstaffSignupProgressbar.visibility = View.VISIBLE
        }else {
            binding.kitchenstaffSignupProgressbar.visibility = View.GONE
        }
    }

    private fun clearPasswordFields(){
        binding.signUpPasswordTextView.text?.clear()
        binding.signUpConfirmPasswordTextView.text?.clear()
    }

    private fun signUpButtonStatus(isEnabled: Boolean) {
        if (isEnabled) {
            binding.signUpBtn.isEnabled
        } else {
            !binding.signUpBtn.isEnabled
        }
    }

    private fun clearInputFields() {
        binding.signUpFullNameTextView.text?.clear()
        binding.signUpEmailTextView.text?.clear()
        binding.signUpLocationDropdown.text.clear()
        binding.signUpPasswordTextView.text?.clear()
    }

    fun NavController.safeNavigate(direction: NavDirections) {
        Log.d("clickTag", "Click happened")
        currentDestination?.getAction(direction.actionId)?.run {
            Log.d("clickTag", "Click Propagated")
            navigate(direction)
        }
    }

}