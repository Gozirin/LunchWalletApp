package com.example.lunchwallet.kitchenstaff.dashboard

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.lunchwallet.MainActivity
import com.example.lunchwallet.R
import com.example.lunchwallet.common.authentication.Code
import com.example.lunchwallet.common.authentication.UserDatastore
import com.example.lunchwallet.common.logout.presentation.LogoutViewModel
import com.example.lunchwallet.databinding.FragmentKitchenStaffDashboardBinding
import com.example.lunchwallet.kitchenstaff.qr.presentation.GenerateCodeViewModel
import com.example.lunchwallet.util.*
import com.example.lunchwallet.util.validation.checkUserType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.lang.Exception


private const val TAG = "KitchenStaffDashBoardFragment"

@AndroidEntryPoint
class KitchenStaffDashBoardFragment : Fragment() {

    private var _binding: FragmentKitchenStaffDashboardBinding? = null
    private val binding get() = _binding!!
    private lateinit var userDatastore: UserDatastore
    private val viewModel: GenerateCodeViewModel by viewModels()
    private val logoutViewModel: LogoutViewModel by viewModels()
    private var email: String?= null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentKitchenStaffDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userDatastore = UserDatastore(requireContext())

        runBlocking { email = userDatastore.userEmail.first() }

        setHasOptionsMenu(true)

        binding.kitchenStaffUsersCardView.setOnClickListener {
            findNavController().navigate(R.id.action_kitchenStaffDashBoardFragment_to_users)
        }

        binding.uploadMealFragmentToolbar.inflateMenu(R.menu.user_menu)
        binding.uploadMealFragmentToolbar.setOnMenuItemClickListener {
            if (it.itemId == R.id.logout) inflateLogoutViewX()
            true
        }
        onBackPressed()
        //getCodeResponseBrunch()
        getCodeResponseDinner()


//        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
//            userDatastore.getFromDataStore().catch { e->
//                e.printStackTrace()
//            }.collect{
//
//                    val id = it.userId
//                    val access = it.accessToken
//                    val refresh = it.refreshToken
//
//                    Log.d(TAG, "onViewCreated: datastore,id: $id")
//                    Log.d(TAG, "onViewCreated: datastore,accessToken: $access")
//                    Log.d(TAG, "onViewCreated: datastore,refreshToken: $refresh")
//            }
//        }

        binding.kitchenStaffMealTimeTableCardView.setOnClickListener {
            findNavController().navigate(R.id.action_kitchenStaffDashBoardFragment_to_kitchenStaffMealTimeTableFragment)

        }
        binding.changeStatusTv.setOnClickListener{
            findNavController().navigate(R.id.action_kitchenStaffDashBoardFragment_to_servingStatusFragment)
        }
        
        binding.kitchenStaffChangeStatusCardView.setOnClickListener {
            findNavController().navigate(R.id.servingStatusFragment)
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

    private fun getCodeResponseBrunch() {
        viewModel.getQrCode("brunch")
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.qrCodeState.collect { code ->
                    when (code) {
                        is Resource.Success -> {
                            Log.d(TAG, "getCodeResponse: ${code.data?.data?.ID}")
                            Log.d(TAG, "getCodeResponse: ${code.data}")
                            userDatastore.saveCode(Code(brunchCode = code.data?.data?.ID, null))

                        }
                        is Resource.Error -> {
                            requireView().snackbar(code.message.toString(), requireContext(), null)
                        }
                        is Resource.Loading -> {
                        }
                    }
                }

            }
        }
    }

    private fun getCodeResponseDinner() {
        viewModel.getQrCode("dinner")
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.qrCodeState.collect { code ->
                    when (code) {
                        is Resource.Success -> {
                            Log.d(TAG, "getCodeResponse: ${code.data?.data?.ID}")
                            Log.d(TAG, "getCodeResponse: ${code.data}")
                            userDatastore.saveCode(Code(null, dinnerCode = code.data?.data?.ID))

                        }
                        is Resource.Error -> {
                            requireView().snackbar(code.message.toString(), requireContext(), null)
                        }
                        is Resource.Loading -> {
                        }
                    }
                }

            }
        }
    }

   private fun inflateLogoutViewX() {
        val view = View.inflate(requireContext(), R.layout.logout_confirmation, null)
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(view)

        val dialog = builder.create()

        dialog.apply {
            show()
            window?.attributes?.apply {
                gravity = Gravity.TOP
            }
            window?.setBackgroundDrawableResource(android.R.color.transparent)
            Log.d(TAG, "inflateLogoutView: email is $email")

            val dismissBtn = findViewById<Button>(R.id.dismiss_logout_btn)
            val logoutBtn = findViewById<Button>(R.id.logout_btn)

            logoutBtn?.apply {
                setOnClickListener {
                    try {
                        logOutUser(email!!)
                    }catch (e: Exception){
                        e.printStackTrace()
                    }

                    dismiss()
//                    Toast.makeText(this@MainActivity, "Logged out!", Toast.LENGTH_SHORT).show()
                }
            }

            dismissBtn?.apply {
                setOnClickListener {
                    dismiss()
//                    Toast.makeText(this@MainActivity, "Dismissed!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun logOutUser(email: String){
        lifecycleScope.launch {
            when (checkUserType(email)) {
                BENEFICIARY -> logoutViewModel.logoutBeneficiary(email)
                ADMIN -> logoutViewModel.logoutAdmin(email)
                KITCHENSTAFF -> logoutViewModel.logoutKitchenStaff(email)
            }
            userDatastore.clearFromDataStore()
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                logoutViewModel.logoutState.collect { logoutUIState ->
                    when (logoutUIState) {
                        is Resource.Success -> {
                            findNavController().navigate(R.id.loginFragment)
                        }
                        is Resource.Error -> {
                            Toast.makeText(
                                requireContext(),
                                logoutUIState.message.toString(),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        is Resource.Loading -> {

                        }
                    }
                }
            }
        }
    }


}
