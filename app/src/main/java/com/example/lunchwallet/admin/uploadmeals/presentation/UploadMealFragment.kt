package com.example.lunchwallet.admin.uploadmeals.presentation

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lunchwallet.R
import com.example.lunchwallet.admin.uploadmeals.adapter.AllMealsAdapter
import com.example.lunchwallet.admin.uploadmeals.adapter.OnMealSelected
import com.example.lunchwallet.admin.uploadmeals.adapter.OnMealSelectedForUpdate
import com.example.lunchwallet.admin.uploadmeals.model.Meals
import com.example.lunchwallet.common.mealtimetable.model.Data
import com.example.lunchwallet.util.Resource
import com.example.lunchwallet.util.snackbar
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

private const val TAG = "UploadMealFragment"

@AndroidEntryPoint
class UploadMealFragment : Fragment(), OnMealSelected, OnMealSelectedForUpdate {

    private lateinit var allMealsAdapter: AllMealsAdapter
    private lateinit var allMealsRV: RecyclerView
    private lateinit var uploadMealFilledLayout: LinearLayout
    private lateinit var uploadMealEmptyLayout: LinearLayout
    private lateinit var addMealButtonForEmptyLayout: MaterialButton
    private lateinit var addMealButtonForFilledLayout: MaterialButton
    private lateinit var progressBar: ProgressBar
    private val uploadMealsViewModel: UploadMealsViewModel by viewModels()
    private var id: String? = null


//    private val listOfMeals: ArrayList<Data> = arrayListOf()

//    private var _binding: FragmentUploadMealBinding? = null
//    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
//        _binding = FragmentUploadMealBinding.inflate(inflater, container, false)
        return inflater.inflate(R.layout.fragment_admin_upload_meal, container, false)


    }

    // Initialising Binding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBackPressed()

        observeGetAllMeals()


        uploadMealEmptyLayout = requireView().findViewById(R.id.admin_meal_screen_empty)
        uploadMealFilledLayout = requireView().findViewById(R.id.admin_upload_meal_screen_filled)
        addMealButtonForEmptyLayout = requireView().findViewById(R.id.uploadMealFragment_add_meal_Button)
        addMealButtonForFilledLayout = requireView().findViewById(R.id.uploadMealFragment_Add_meal_button_Filled)
        progressBar = requireView().findViewById(R.id.upload_meals_filled_progress_bar)

//        uploadMealAdapter = UploadMealAdapter()
//        uploadMealsRV = requireView().findViewById(R.id.uploadMealFragment_recyclerView)
//        uploadMealsRV.layoutManager = LinearLayoutManager(requireContext())

        allMealsAdapter= AllMealsAdapter(this, this, requireContext())
        allMealsRV = requireView().findViewById(R.id.uploadMealFragment_recyclerView)
        allMealsRV.adapter = allMealsAdapter
        allMealsRV.layoutManager = LinearLayoutManager(requireContext())


        if (allMealsAdapter.meals.isEmpty()) {
            uploadMealEmptyLayout.visibility = View.GONE
            uploadMealFilledLayout.visibility = View.VISIBLE
        } else {
            uploadMealEmptyLayout.visibility = View.VISIBLE
            uploadMealFilledLayout.visibility = View.GONE
        }

        addMealButtonForEmptyLayout.setOnClickListener {
            addMealButtonForEmptyLayout.isEnabled = false
            findNavController().navigate(R.id.action_uploadMealFragment_to_optionMealFragment)
        }

        addMealButtonForFilledLayout.setOnClickListener {
            addMealButtonForFilledLayout.isEnabled = false
            findNavController().navigate(R.id.action_uploadMealFragment_to_optionMealFragment)
        }
    }

    private fun  observeGetAllMeals(){
        uploadMealsViewModel.getAllMeals()
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                uploadMealsViewModel.getMealState.collect{ allMealsUIState ->
                    when(allMealsUIState) {
                        is Resource.Success -> {
                            showProgressIndicator(false)
                            try {
                                val mealList = allMealsUIState.data?.data?.sortedByDescending { it.created_at }
                                allMealsAdapter.setAllMealsList(mealList as MutableList<Data>)
                            }catch (e: Exception) {
                               println( e.message)
                                uploadMealEmptyLayout.visibility = View.VISIBLE
                                uploadMealFilledLayout.visibility = View.GONE
                            }
                        }
                        is Resource.Error -> {
                            showProgressIndicator(false)
                            requireView().snackbar( allMealsUIState.message.toString(), requireContext(), null)
                        }
                        is Resource.Loading -> {
                            showProgressIndicator(true)
                        }
                    }
                }
            }
        }
    }


    private fun deleteMeal(){
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                uploadMealsViewModel.deleteState.collect { deletedUIState ->
                    when (deletedUIState) {
                        is Resource.Success -> {
                            showProgressIndicator(false)
                            try {
                                Log.d("TAG", "deleteMeal: $deletedUIState")
                                Log.d("TAG", "deleteMeal: ${deletedUIState.data}")
                                Log.d("TAG", "deleteMeal: ${deletedUIState.message}")
                            } catch (e: Exception) {
                                println(e.message)
                            }


                        }
                        is Resource.Error -> {
                            showProgressIndicator(false)
                            requireView().snackbar(
                                deletedUIState.message.toString(),
                                requireContext(),
                                null
                            )
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
           progressBar.visibility = View.VISIBLE
        }else {
            progressBar.visibility = View.GONE
        }
    }

    override fun getMealId(mealId: String){
        id= mealId
       inflateLogoutViewX()
    }

    override fun getMealDetails(
        mealId: String,
        mealName: String,
        mealType: String,
        kitchen: String,
        mealDate: String,
        mealImage: Int?
    ) {
        Log.d(TAG, "getMealDetails:meal date is $mealDate")
        val date = mealDate.split("-")
        Log.d(TAG, "getMealDetails:date[0] ${date[0]}")
        Log.d(TAG, "getMealDetails:date[1] ${date[1]}")
        Log.d(TAG, "getMealDetails:date[2] ${date[2]}")

        val updateMeal = Meals(null, mealName, mealType, kitchen, date[2], date[1], date[0] )
        val direction =
            UploadMealFragmentDirections.actionUploadMealFragmentToUpdateMealBottomSheetFragment(mealId, updateMeal)
        findNavController().navigate(direction)
    }

    private fun onBackPressed() {
        //Overriding onBack press to finish activity and exit app
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)
    }

    private fun inflateLogoutViewX() {
        val view = View.inflate(requireContext(), R.layout.delete_confirmation, null)
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(view)

        val dialog = builder.create()

        dialog.apply {
            show()
            window?.attributes?.apply {
                gravity = Gravity.TOP
            }
            window?.setBackgroundDrawableResource(android.R.color.transparent)

            val dismissBtn = findViewById<Button>(R.id.dismiss_delete_btn)
            val logoutBtn = findViewById<Button>(R.id.delete_btn)

            logoutBtn?.apply {
                setOnClickListener {
                    try {
                        uploadMealsViewModel.deleteMeal(id.toString())
                        deleteMeal()
                    }catch (e: java.lang.Exception){
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


}
