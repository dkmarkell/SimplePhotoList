package com.dkmarkell.rakutentakehome

import com.dkmarkell.rakutentakehome.photo.PhotoRepository
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import com.google.common.truth.Truth.assertThat

class AppViewModelTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    @MockK
    lateinit var repo: PhotoRepository

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testGetPhotosError() = runTest {

        val viewModel = AppViewModel(repo)

        coEvery { repo.deleteAllPhotos() } returns Unit
        coEvery { repo.getPhotos() } returns false

        viewModel.onAppStarted()

        assertThat(viewModel.error.value).isNotNull()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testGetPhotosSuccess() = runTest {

        val viewModel = AppViewModel(repo)

        coEvery { repo.deleteAllPhotos() } returns Unit
        coEvery { repo.getPhotos() } returns true

        viewModel.onAppStarted()

        assertThat(viewModel.error.value).isNull()
    }


}