package com.example.lunchwallet.common.login.presentation

import android.app.Activity
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.lunchwallet.R
import com.example.lunchwallet.common.authentication.UserAuth
import com.example.lunchwallet.common.authentication.UserDatastore
import com.example.lunchwallet.common.authentication.UserStore
import com.example.lunchwallet.common.login.model.User
import com.example.lunchwallet.databinding.FragmentLoginBinding
import com.example.lunchwallet.kitchenstaff.qr.presentation.GenerateCodeViewModel
import com.example.lunchwallet.util.*
import com.example.lunchwallet.util.loginvalidation.LoginInputValidation
import com.example.lunchwallet.util.validation.checkUserType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


private const val TAG = "LoginFragment"

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var errorMsg: TextView
    private val loginViewModel: LoginViewModel by viewModels()
    private lateinit var userDatastore: UserStore


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    // initialising Binding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBackPressed()
        userDatastore = UserDatastore(requireContext())
        errorMsg = binding.LoginErrorMsg
        emailFocusListener()
        passwordFocusListener()

        // LOGIN BUTTON
        binding.loginFragmentButton.setOnClickListener {
            requireActivity().hideSoftKeyboard()
            onClickLoginButton()
        }
        // Forgot Password
        binding.loginFragmentForgotpasswordTv.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_resetPasswordFragment)
        }
        // SIGN-UP
        binding.loginFragmentSignupTv.setOnClickListener {
             findNavController().navigate(R.id.action_loginFragment_to_selectTypeFragment)
        }
    }

    private fun emailFocusListener() {
        binding.loginFragmentEmailEt.setOnFocusChangeListener { _, focused ->
            if (!focused)
                binding.loginFragmentEmailTil.helperText =
                    LoginInputValidation.validateEmail(binding.loginFragmentEmailEt.text.toString())
        }
    }

    private fun passwordFocusListener() {
        binding.loginFragmentPasswordEt.setOnFocusChangeListener { _, focused ->
            if (!focused)
                binding.loginFragmentPasswordTil.helperText =
                    LoginInputValidation.validatePassword(binding.loginFragmentPasswordEt.text.toString())
        }
    }

    private fun onClickLoginButton() {
        binding.loginFragmentEmailTil.helperText = LoginInputValidation.validateEmail(binding.loginFragmentEmailEt.text.toString())

        binding.loginFragmentPasswordTil.helperText = LoginInputValidation.validatePassword(binding.loginFragmentPasswordEt.text.toString())

        val validEmailInput = LoginInputValidation.validateEmail(binding.loginFragmentEmailEt.text.toString()) == null
        val validPasswordInput = LoginInputValidation.validatePassword(binding.loginFragmentPasswordEt.text.toString()) == null

        if (validEmailInput && validPasswordInput) {
            loginUser()
//            if (binding.loginFragmentPasswordEt.text.toString() == "Beneficiary@123")
//            findNavController().navigate(R.id.action_loginFragment_to_foodBeneficiaryDashboardFragment)
//            if (binding.loginFragmentPasswordEt.text.toString() == "Kitchenstaff@123"){
//                findNavController().navigate(R.id.action_loginFragment_to_kitchenStaffDashBoardFragment2)
//            }
//            if (binding.loginFragmentPasswordEt.text.toString() == "Admin@123"){
//                findNavController().navigate(R.id.action_loginFragment_to_dashboardFragment)
//            }
        } else {
            invalidCredentials()
        }
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


    private fun invalidCredentials() {
        var message = ""
        if (binding.loginFragmentEmailTil.helperText != null) {
            message = "\n\nEmail: " + binding.loginFragmentEmailTil.helperText
        }
        if (binding.loginFragmentPasswordTil.helperText != null) {
            message = "\n\nEmail: " + binding.loginFragmentPasswordTil.helperText
        }
    }

    private fun loginUser() {
        val email = binding.loginFragmentEmailEt.text.toString()
        val password = binding.loginFragmentPasswordEt.text.toString()
        val user = User(email, password)
        when (checkUserType(email)) {
            BENEFICIARY -> loginViewModel.loginBeneficiary(user)
            ADMIN -> loginViewModel.loginAdmin(user)
            KITCHENSTAFF -> loginViewModel.loginKitchenStaff(user)
        }
        observeLoginResponse()
    }

    private fun observeLoginResponse() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                loginViewModel.loginState.collect{ loginUIState ->
                    when(loginUIState) {
                        is Resource.Success -> {
                            showProgressIndicator(false)
                            requireView().clear(binding.loginFragmentEmailEt)
                            requireView().clear(binding.loginFragmentPasswordEt)
                            val data = loginUIState.data?.data
                            val email = data?.user?.email
                            val userId = data?.user?.ID
                            val accessToken = data?.access_token
                            val refreshToken = data?.refresh_token
                            val userAuth = UserAuth(userId!!, email!!, accessToken!!, refreshToken!!)
                            loginViewModel.saveToDataStore(userAuth)

                            when(checkUserType(email)) {
                                BENEFICIARY ->  findNavController().navigate(R.id.foodBeneficiaryDashboardFragment)
                                ADMIN -> findNavController().navigate(R.id.dashboardFragment)
                                KITCHENSTAFF -> findNavController().navigate(R.id.kitchenStaffDashBoardFragment)

                        }
                    }
                    is Resource.Error -> {
                        showProgressIndicator(false)
                        requireView().snackbar( loginUIState.message.toString(), requireContext(), null)
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
            binding.loginProgressBar.visibility = View.VISIBLE
        }else {
            binding.loginProgressBar.visibility = View.GONE
        }
    }

    // extension function to hide soft keyboard programmatically
    fun Activity.hideSoftKeyboard(){
        (getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager).apply {
            hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }
    }

}
