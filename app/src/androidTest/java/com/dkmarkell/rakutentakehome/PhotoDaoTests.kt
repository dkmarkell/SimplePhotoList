package com.dkmarkell.rakutentakehome

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.dkmarkell.rakutentakehome.localstorage.LocalDatabase
import com.dkmarkell.rakutentakehome.photo.PhotoDao
import com.dkmarkell.rakutentakehome.photo.PhotoEntity
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class PhotoDaoTests {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var photoDao: PhotoDao
    private lateinit var db: LocalDatabase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(context, LocalDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        photoDao = db.photoDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testInsertEmpty() = runTest {
        val photos = listOf<PhotoEntity>()

        val ids = photoDao.insertPhotos(photos)

        assert(ids.isEmpty())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testInsertNotEmpty() = runTest {
        val photos = listOf(
            PhotoEntity(
                remoteId = "1",
                owner = "owner",
                secret = "secret",
                server = "server",
                farm = 23,
                title = "title",
                isPublic = 1,
                isFriend = 0,
                isFamily = 0
            ),
            PhotoEntity(
                remoteId = "2",
                owner = "owner",
                secret = "secret",
                server = "server",
                farm = 23,
                title = "title",
                isPublic = 1,
                isFriend = 0,
                isFamily = 0
            ),
        )

        val ids = photoDao.insertPhotos(photos)

        assert(ids.size == 2)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testGetNonExistent() = runTest {
        val photo = photoDao.getPhotoById(1)

        assertThat(photo).isNull()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testGetValidPhoto() = runTest {
        val photos = listOf(
            PhotoEntity(
                remoteId = "1",
                owner = "owner",
                secret = "secret",
                server = "server",
                farm = 23,
                title = "title",
                isPublic = 1,
                isFriend = 0,
                isFamily = 0
            ),
            PhotoEntity(
                remoteId = "2",
                owner = "owner",
                secret = "secret",
                server = "server",
                farm = 23,
                title = "title",
                isPublic = 1,
                isFriend = 0,
                isFamily = 0
            ),
        )

        val ids = photoDao.insertPhotos(photos)

        val getPhoto = photoDao.getPhotoById(ids[0])

        assertThat(getPhoto?.localId).isEqualTo(ids[0])
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testObserveAll() = runTest {
        val photos = listOf(
            PhotoEntity(
                remoteId = "1",
                owner = "owner",
                secret = "secret",
                server = "server",
                farm = 23,
                title = "title",
                isPublic = 1,
                isFriend = 0,
                isFamily = 0
            ),
            PhotoEntity(
                remoteId = "2",
                owner = "owner",
                secret = "secret",
                server = "server",
                farm = 23,
                title = "title",
                isPublic = 1,
                isFriend = 0,
                isFamily = 0
            ),
        )

        photoDao.insertPhotos(photos)

        val photosAsync = async { photoDao.observeAllPhotos().first() }

        assertThat(photosAsync.await()).hasSize(2)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testClearAll() = runTest {
        val photos = listOf(
            PhotoEntity(
                remoteId = "1",
                owner = "owner",
                secret = "secret",
                server = "server",
                farm = 23,
                title = "title",
                isPublic = 1,
                isFriend = 0,
                isFamily = 0
            ),
            PhotoEntity(
                remoteId = "2",
                owner = "owner",
                secret = "secret",
                server = "server",
                farm = 23,
                title = "title",
                isPublic = 1,
                isFriend = 0,
                isFamily = 0
            ),
        )

        val insertList = photoDao.insertPhotos(photos)
        photoDao.clearPhotos()

        for (i in insertList) {
            val p = photoDao.getPhotoById(i)
            assertThat(p).isNull()
        }
    }
}