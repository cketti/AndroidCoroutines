package de.cketti.androidcoroutines

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import de.cketti.androidcoroutines.InputActivity.InputActivityArgs
import de.cketti.androidcoroutines.InputActivity.InputActivityResult
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var model: MainViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        model = ViewModelProviders.of(this).get(MainViewModel::class.java)

        model.uiUpdates().observe(this, Observer<MainViewModel> {
            render(it!!)
        })

        model.events().observe(this, Observer<MainActivityEvent> { event ->
            when (event) {
                is MainActivityEvent.GetUserInput -> startUserInputActivity(event.description)
            }
        })

        button.setOnClickListener {
            model.doSomething()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_USER_INPUT && resultCode == Activity.RESULT_OK && data != null) {
            val result = InputActivityResult.decode(data)
            model.userInput(result.value)
        }
    }

    private fun startUserInputActivity(description: String) {
        val intent = InputActivityArgs(description).buildIntent(this)
        startActivityForResult(intent, REQUEST_CODE_USER_INPUT)
    }

    private fun render(viewModel: MainViewModel) {
        textView.text = viewModel.text
        button.isEnabled = viewModel.buttonEnabled
    }


    companion object {
        const val REQUEST_CODE_USER_INPUT = 1
    }
}
