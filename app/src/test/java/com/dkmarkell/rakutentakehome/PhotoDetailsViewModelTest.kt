package com.dkmarkell.rakutentakehome

import androidx.lifecycle.SavedStateHandle
import com.dkmarkell.rakutentakehome.photo.PhotoRepository
import com.dkmarkell.rakutentakehome.photo.details.PhotoDetails
import com.dkmarkell.rakutentakehome.photo.details.PhotoDetailsViewModel
import com.google.common.truth.Truth
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class PhotoDetailsViewModelTest {
    @get:Rule
    val mockkRule = MockKRule(this)

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    @MockK
    lateinit var repo: PhotoRepository

    @MockK
    lateinit var savedStateHandle: SavedStateHandle

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testGetPhotoDetails() = runTest {

        coEvery { savedStateHandle.get<Long>("photoId") } returns 1
        coEvery { repo.getPhoto(any()) } returns PhotoDetails(1,"2","owner", "server", "secret", 123, "title", true, false, false, "www.url.com")

        val viewModel = PhotoDetailsViewModel(repo, savedStateHandle)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.farm.collect()
        }

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.photoId.collect()
        }

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.isFamily.collect()
        }

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.owner.collect()
        }

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.isFriend.collect()
        }

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.isPublic.collect()
        }

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.title.collect()
        }

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.server.collect()
        }

        Truth.assertThat(viewModel.farm.value).isEqualTo("123")
        Truth.assertThat(viewModel.photoId.value).isEqualTo("2")
        Truth.assertThat(viewModel.owner.value).isEqualTo("owner")
        Truth.assertThat(viewModel.server.value).isEqualTo("server")
        Truth.assertThat(viewModel.title.value).isEqualTo("title")
        Truth.assertThat(viewModel.isPublic.value).isEqualTo(R.string.yes)
        Truth.assertThat(viewModel.isFamily.value).isEqualTo(R.string.no)
        Truth.assertThat(viewModel.isFriend.value).isEqualTo(R.string.no)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testUrlNoWidth() = runTest {

        coEvery { savedStateHandle.get<Long>("photoId") } returns 1
        coEvery { repo.getPhoto(any()) } returns PhotoDetails(
            1,
            "2",
            "owner",
            "server",
            "secret",
            123,
            "title",
            true,
            false,
            false,
            "www.url.com"
        )

        val viewModel = PhotoDetailsViewModel(repo, savedStateHandle)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.url.collect()
        }

        Truth.assertThat(viewModel.url.value).isEqualTo(Pair("www.url.com", 0))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testUrlWithWidth() = runTest {
        val imgWidth = 256

        coEvery { savedStateHandle.get<Long>("photoId") } returns 1
        coEvery { repo.getPhoto(any()) } returns PhotoDetails(
            1,
            "2",
            "owner",
            "server",
            "secret",
            123,
            "title",
            true,
            false,
            false,
            "www.url.com"
        )

        val viewModel = PhotoDetailsViewModel(repo, savedStateHandle)
        viewModel.onImageWidthMeasured(imgWidth)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.url.collect()
        }

        Truth.assertThat(viewModel.url.value).isEqualTo(Pair("www.url.com", imgWidth))
    }
}