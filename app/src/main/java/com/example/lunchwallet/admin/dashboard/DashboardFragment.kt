package com.example.lunchwallet.admin.dashboard

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.lunchwallet.R
import com.example.lunchwallet.common.authentication.UserDatastore
import com.example.lunchwallet.common.logout.presentation.LogoutViewModel
import com.example.lunchwallet.databinding.FragmentDashboardBinding
import com.example.lunchwallet.util.ADMIN
import com.example.lunchwallet.util.BENEFICIARY
import com.example.lunchwallet.util.KITCHENSTAFF
import com.example.lunchwallet.util.Resource
import com.example.lunchwallet.util.validation.checkUserType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.lang.Exception

@AndroidEntryPoint
class DashboardFragment : Fragment() {
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private val logoutViewModel: LogoutViewModel by viewModels()
    private lateinit var userDatastore: UserDatastore
    private var email: String?= null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBackPressed()
        userDatastore = UserDatastore(requireContext())

        runBlocking { email = userDatastore.userEmail.first() }

        setHasOptionsMenu(true)

        binding.mealTimeTableToolbar.inflateMenu(R.menu.user_menu)
        binding.mealTimeTableToolbar.setOnMenuItemClickListener {
            if (it.itemId == R.id.logout) inflateLogoutViewX()
            true
        }

        binding.adminDashboardUploadBtn.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_uploadMealFragment)
        }

        binding.adminDashboardTimetableBtn.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_mealTimeTableFragment)
        }

    }

    private fun onBackPressed(){
        //Overriding onBack press to finish activity and exit app
        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                requireActivity().finish()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)
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
