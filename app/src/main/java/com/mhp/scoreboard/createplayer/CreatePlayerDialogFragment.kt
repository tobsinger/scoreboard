package com.mhp.scoreboard.createplayer

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.mhp.scoreboard.R
import com.mhp.scoreboard.databinding.FragmentDialogCreatePlayerBinding
import kotlinx.android.synthetic.main.fragment_dialog_create_player.input_name
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class CreatePlayerDialogFragment : DialogFragment() {

    private val model: CreatePlayerViewModel by viewModel()
    private lateinit var binding: FragmentDialogCreatePlayerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        model.closeDialog.observe(this.viewLifecycleOwner, Observer { dismiss() })
        val viewRoot = LayoutInflater.from(activity).inflate(R.layout.fragment_dialog_create_player, container, false)
        binding = DataBindingUtil.bind(viewRoot)!!
        binding.lifecycleOwner = this
        binding.viewmodel = model
        return viewRoot
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        input_name.setOnFocusChangeListener { _, _ ->
            input_name.post {
                val inputMethodManager: InputMethodManager? =
                    activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
                inputMethodManager?.showSoftInput(input_name, InputMethodManager.SHOW_IMPLICIT)
            }
        }
        input_name.requestFocus()
        input_name.setOnEditorActionListener { _: TextView, i: Int, _: KeyEvent? ->
            if (i == EditorInfo.IME_ACTION_DONE) {
                model.submit()
                true
            } else {
                false
            }
        }

        super.onViewCreated(view, savedInstanceState)
    }
}
