package com.dkmarkell.rakutentakehome.photo.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dkmarkell.rakutentakehome.R
import com.dkmarkell.rakutentakehome.photo.PhotoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotoDetailsViewModel @Inject constructor(
    private val photoRepository: PhotoRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val imageWidth = MutableStateFlow(0)

    private val _url = MutableStateFlow("")
    val url: StateFlow<Pair<String, Int>> = combine(_url, imageWidth) { url, width ->
        Pair(url, width)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = Pair("", 0)
    )

    private val _title = MutableStateFlow("")
    val title: StateFlow<String> = _title

    private val _owner = MutableStateFlow("")
    val owner: StateFlow<String> = _owner

    private val _server = MutableStateFlow("")
    val server: StateFlow<String> = _server

    private val _farm = MutableStateFlow("")
    val farm: StateFlow<String> = _farm

    private val _photoId = MutableStateFlow("")
    val photoId: StateFlow<String> = _photoId

    private val _isPublic = MutableStateFlow(-1)
    val isPublic: StateFlow<Int> = _isPublic

    private val _isFriend = MutableStateFlow(-1)
    val isFriend: StateFlow<Int> = _isFriend

    private val _isFamily = MutableStateFlow(-1)
    val isFamily: StateFlow<Int> = _isFamily

    init {
        /**
         * photoId is the argument defined in the nav graph supplied to
         * the SavedStateHandle from the Safe Args
         */
        val photoId = savedStateHandle.get<Long>("photoId") ?: 0
        viewModelScope.launch {
            photoRepository.getPhoto(photoId)?.apply {
                _url.value = url
                _title.value = title
                _owner.value = owner
                _server.value = server
                _farm.value = farm.toString()
                _photoId.value = remoteId
                _isPublic.value = if (isPublic) R.string.yes else R.string.no
                _isFriend.value = if (isFriend) R.string.yes else R.string.no
                _isFamily.value = if (isFamily) R.string.yes else R.string.no
            }
        }
    }

    fun onImageWidthMeasured(width: Int) {
        imageWidth.value = width
    }
}