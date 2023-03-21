package com.dkmarkell.rakutentakehome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dkmarkell.rakutentakehome.photo.PhotoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val photoRepository: PhotoRepository
) : ViewModel() {

    private val _error = MutableStateFlow<Int?>(null)
    val error: StateFlow<Int?> = _error

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    fun onAppStarted() {
        viewModelScope.launch {
            _loading.value = true
            photoRepository.deleteAllPhotos()
            val result = photoRepository.getPhotos()
            if (!result) {
                _error.value = R.string.network_error
            }
            _loading.value = false
        }
    }

    fun onErrorProcessed() {
        _error.value = null
    }
}