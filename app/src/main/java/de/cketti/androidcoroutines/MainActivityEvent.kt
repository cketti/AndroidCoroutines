package de.cketti.androidcoroutines

sealed class MainActivityEvent {
    data class GetUserInput(val description: String) : MainActivityEvent()
}
