package com.example.lunchwallet.kitchenstaff.servingstatus.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lunchwallet.kitchenstaff.servingstatus.model.BrunchStatusRequest
import com.example.lunchwallet.kitchenstaff.servingstatus.model.BrunchStatusResponse
import com.example.lunchwallet.kitchenstaff.servingstatus.model.DinnerStatusRequest
import com.example.lunchwallet.kitchenstaff.servingstatus.model.DinnerStatusResponse
import com.example.lunchwallet.kitchenstaff.servingstatus.repositories.IBrunchStatusRepositoryInterface
import com.example.lunchwallet.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class BrunchStatusViewModel @Inject constructor(private val brunchStatusRepositoryInterface: IBrunchStatusRepositoryInterface) : ViewModel(){

    private var _brunchStatus= MutableStateFlow<Resource<BrunchStatusResponse>>(Resource.Loading())
    val brunchStatus: StateFlow<Resource<BrunchStatusResponse>> get() = _brunchStatus


    fun getBrunchStatus(brunchStatusRequest: BrunchStatusRequest) {
        viewModelScope.launch {
            brunchStatusRepositoryInterface.brunchStatus(brunchStatusRequest)
                .collect{ brunchResponse ->
                    _brunchStatus.value = brunchResponse
                }
        }
    }

}


