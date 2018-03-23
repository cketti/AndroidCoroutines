package de.cketti.androidcoroutines

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.os.SystemClock
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import org.jetbrains.anko.coroutines.experimental.bg
import kotlin.coroutines.experimental.Continuation
import kotlin.coroutines.experimental.suspendCoroutine

class MainViewModel : ViewModel() {
    var text: String = "Hello World"
        private set
    var buttonEnabled: Boolean = true
        private set

    private val uiUpdates = MutableLiveData<MainViewModel>()
    private val events = SingleLiveEvent<MainActivityEvent>()
    private lateinit var continuation: Continuation<String>


    fun uiUpdates(): LiveData<MainViewModel> = uiUpdates

    fun events(): LiveData<MainActivityEvent> = events

    fun doSomething() {
        launch(UI) {
            updateView("Doing something…", buttonEnabled = false)

            val firstName = getUserInput("Please enter your first name")
            val lastName = getUserInput("Please enter your last name")

            val expensiveComputationResult = bg {
                // Sleep to actually block the thread
                SystemClock.sleep(2000L)
                "Hello $firstName $lastName"
            }.await()

            updateView(expensiveComputationResult, buttonEnabled = true)
        }
    }

    fun userInput(value: String) {
        continuation.resume(value)
    }

    private suspend fun getUserInput(text: String) = suspendCoroutine<String> {
        sendEvent(MainActivityEvent.GetUserInput(text))
        continuation = it
    }

    private fun updateView(text: String, buttonEnabled: Boolean? = null) {
        this.text = text
        buttonEnabled?.let { this.buttonEnabled = buttonEnabled }
        uiUpdates.value = this
    }

    private fun sendEvent(event: MainActivityEvent) {
        events.value = event
    }
}
