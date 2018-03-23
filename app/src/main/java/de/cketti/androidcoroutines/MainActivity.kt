package de.cketti.androidcoroutines

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
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

        button.setOnClickListener {
            model.doSomething()
        }
    }

    private fun render(viewModel: MainViewModel) {
        textView.text = viewModel.text
        button.isEnabled = viewModel.buttonEnabled
    }
}
