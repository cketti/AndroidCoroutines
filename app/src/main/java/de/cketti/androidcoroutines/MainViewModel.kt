package de.cketti.androidcoroutines

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import java.util.concurrent.TimeUnit

class MainViewModel : ViewModel() {
    var text: String = "Hello World"
        private set
    var buttonEnabled: Boolean = true
        private set

    private val uiUpdates = MutableLiveData<MainViewModel>()


    fun uiUpdates(): LiveData<MainViewModel> = uiUpdates

    fun doSomething() {
        launch(UI) {
            updateView("Doing something…", buttonEnabled = false)
            delay(1, TimeUnit.SECONDS)

            repeat(5) {
                updateView("${it + 1}")
                delay(1, TimeUnit.SECONDS)
            }

            updateView("Done!", buttonEnabled = true)
        }
    }

    private fun updateView(text: String, buttonEnabled: Boolean? = null) {
        this.text = text
        buttonEnabled?.let { this.buttonEnabled = buttonEnabled }
        uiUpdates.value = this
    }
}
