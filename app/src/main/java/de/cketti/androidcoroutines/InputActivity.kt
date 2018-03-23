package de.cketti.androidcoroutines

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_input.*

class InputActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input)

        val args = InputActivityArgs.decode(intent)
        description.text = args.description

        doneButton.setOnClickListener {
            val value = input.text.toString()
            val data = InputActivityResult(value).buildIntent()
            setResult(Activity.RESULT_OK, data)
            finish()
        }
    }

    class InputActivityArgs(val description: String) {
        fun buildIntent(context: Context) = Intent(context, InputActivity::class.java).apply {
            putExtra(EXTRA_DESCRIPTION, description)
        }

        companion object {
            private const val EXTRA_DESCRIPTION = "description"

            fun decode(intent: Intent): InputActivityArgs {
                val description = intent.getStringExtra(EXTRA_DESCRIPTION)!!
                return InputActivityArgs(description)
            }
        }
    }

    class InputActivityResult(val value: String) {
        fun buildIntent() = Intent().apply {
            putExtra(RESULT_EXTRA_VALUE, value)
        }

        companion object {
            private const val RESULT_EXTRA_VALUE = "value"

            fun decode(intent: Intent): InputActivityResult {
                val value = intent.getStringExtra(RESULT_EXTRA_VALUE)!!
                return InputActivityResult(value)
            }
        }
    }
}
