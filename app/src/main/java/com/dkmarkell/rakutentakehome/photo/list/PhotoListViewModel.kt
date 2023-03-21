package com.dkmarkell.rakutentakehome.photo.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dkmarkell.rakutentakehome.photo.PhotoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class PhotoListViewModel @Inject constructor(
    photoRepository: PhotoRepository
) : ViewModel() {

    val photoPreviews: StateFlow<List<PhotoPreview>> = photoRepository.photoPreviews.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList()
    )

    private val _title: MutableStateFlow<String?> = MutableStateFlow(null)
    val title: StateFlow<String?> = _title

    fun setTitle(title: String) {
        _title.value = title
    }

}