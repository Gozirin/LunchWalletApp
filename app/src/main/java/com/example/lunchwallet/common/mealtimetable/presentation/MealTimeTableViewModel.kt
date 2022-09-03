package com.example.lunchwallet.common.mealtimetable.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lunchwallet.common.mealtimetable.model.MealTimeTableResponse
import com.example.lunchwallet.common.mealtimetable.use_cases.MealTimeTableUseCases
import com.example.lunchwallet.util.Resource
import com.example.lunchwallet.util.connectivity.ConnectivityObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MealTimeTableViewModel @Inject constructor(
    private val useCase: MealTimeTableUseCases,
    private val connectivityObserver: ConnectivityObserver
): ViewModel() {

   private val intent = Channel<MealTimeTableIntent>(Channel.UNLIMITED)

    private var _mealState =
        MutableStateFlow<Resource<MealTimeTableResponse>>(Resource.Loading())
    val mealState: StateFlow<Resource<MealTimeTableResponse>> get() = _mealState

    init {
        handleIntent()
    }

    private fun handleIntent() {
        connectivityObserver.observeNetworkAccess().onEach {
            intent.consumeAsFlow().collect{ intent ->
                when(intent) {
                    is MealTimeTableIntent.BrunchTimeTableIntent -> getBrunch()
                    is MealTimeTableIntent.DinnerTimeTableIntent -> getDinner()
                }

            }

        }.launchIn(viewModelScope)
    }

    fun getBrunch() {
        viewModelScope.launch {
          useCase.getBrunchTimeTableUseCase.invoke().collect{ brunch ->
              _mealState.value = brunch
          }
        }
    }

    fun getDinner() {
        viewModelScope.launch {
            useCase.getDinnerTimeTableUseCase.invoke().collect{ dinner->
                _mealState.value = dinner
            }
        }

    }
}