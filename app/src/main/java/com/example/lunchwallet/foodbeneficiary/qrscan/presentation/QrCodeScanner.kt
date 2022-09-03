package com.example.lunchwallet.foodbeneficiary.qrscan.presentation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.lunchwallet.R
import com.example.lunchwallet.common.authentication.Code
import com.example.lunchwallet.common.authentication.UserDatastore
import com.example.lunchwallet.databinding.FragmentQrCodeScannerBinding
import com.example.lunchwallet.foodbeneficiary.qrscan.utils.makeStatusNotification
import com.example.lunchwallet.util.Resource
import com.example.lunchwallet.util.snackbar
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject

private const val TAG = "QrCodeScanner"

class QrCodeScanner: Fragment() {

    private var _binding: FragmentQrCodeScannerBinding? = null
    private val binding get() = _binding!!
    private lateinit var qrScanIntegrator: IntentIntegrator
    private val scanViewModel: ScanViewModel by viewModels()
    private lateinit var datastore: UserDatastore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQrCodeScannerBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        datastore = UserDatastore(requireContext())

        setupScanner()
        performAction()
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
                    // Converting the data to json format
                        onScanSuccess()
//                    val obj = JSONObject(result.contents)

                    // Show values in UI.
//                    binding.name.text = result.contents
//                    binding.siteName.text = obj.getString("site_name")


//                    binding.name.text = obj.getString("name")
//                    binding.siteName.text = obj.getString("site_name")

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
        scanViewModel.onScanSuccess(datastore.brunchCode.toString(), datastore.userId.toString())
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                scanViewModel.qrScanState.collect { code ->
                    when (code) {
                        is Resource.Success -> {
                            Log.d(TAG, "getCodeResponse: ${code.data?.data}")
                            Log.d(TAG, "getCodeResponse: ${code.data}")
                            makeStatusNotification("${code.data?.message}", requireContext())

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
}
