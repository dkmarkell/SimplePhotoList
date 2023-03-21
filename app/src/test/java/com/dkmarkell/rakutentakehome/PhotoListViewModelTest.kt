package com.dkmarkell.rakutentakehome

import com.dkmarkell.rakutentakehome.photo.PhotoRepository
import com.dkmarkell.rakutentakehome.photo.list.PhotoListViewModel
import com.dkmarkell.rakutentakehome.photo.list.PhotoPreview
import com.google.common.truth.Truth
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class PhotoListViewModelTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    @MockK
    lateinit var repo: PhotoRepository

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testGetPhotosEmpty() = runTest {

        coEvery { repo.photoPreviews } returns flowOf(emptyList())

        val viewModel = PhotoListViewModel(repo)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.photoPreviews.collect()
        }

        Truth.assertThat(viewModel.photoPreviews.value).isEmpty()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testGetPhotosNotEmpty() = runTest {

        coEvery { repo.photoPreviews } returns flowOf (listOf(PhotoPreview(1, "title", "url")) )

        val viewModel = PhotoListViewModel(repo)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.photoPreviews.collect()
        }

        Truth.assertThat(viewModel.photoPreviews.value).hasSize(1)
    }

}