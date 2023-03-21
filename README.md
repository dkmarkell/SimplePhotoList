# SimplePhotoList

Thank you for checking out my SimplePhotoList!

The project uses the following versions:

**Compiled and Target SDK Version:** 33 (minimum support for 26) <br />
**Kotlin Plugin Version:** 1.8.0 <br />
**Android Gradle Plugin Version:** 7.4.1 <br />
**Gradle Version:** 7.5 <br />
**JVM Target:** 1.8 <br />


The project can be cloned and built in Android Studio.

My goals in developing this app were to hit every requirement and showcase my knowledge as an Android developer, by using some Jetpack and third-party libraries, all while writing clean code, with a solid backing arhictecture with Unit Tests.

On opening the app, you'll be presented with a list of photos and their respective title. Clicking a list item opens a new screen and displays the photo in a higher resolution and displays some details.

The photo details are all stored in the Room database which is treated like a local cache. When the app is first launched all data in Room is cleared before making a GET request which then populates Room with the new data.

I added instrumented unit tests to cover the various Room operations and I also added unit tests for the 3 ViewModels.

The libraries used:

**Retrofit:** for the HTTP client to do the GET request.<br />
**Moshi:** for parsing and converting the JSON objects to Kotlin objects.<br />
**Picasso:** for loading/caching images from the web.<br />
**Room:** for offline persistent storage.<br />
**Jetpack Navigation:** for fragment transitions and passing arguments between fragments.<br />
**Coroutines and Flow:** for asynchronous IO and to reactively emit data to the UI layer.<br />
**Hilt:** for dependency injection.<br />
**MockK:** to create mocks to help unit test the ViewModels<br />

