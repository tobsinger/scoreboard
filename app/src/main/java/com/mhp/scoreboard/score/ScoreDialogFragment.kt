package com.mhp.scoreboard.score

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.mhp.scoreboard.R
import com.mhp.scoreboard.databinding.FragmentDialogScoreBinding
import kotlinx.android.synthetic.main.fragment_dialog_score.actions
import kotlinx.android.synthetic.main.fragment_dialog_score.input_score


class ScoreDialogFragment : DialogFragment() {

    private lateinit var model: ScoreViewModel
    private lateinit var binding: FragmentDialogScoreBinding
    private val args: ScoreDialogFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        model = BaseViewModelFactory { activity?.application?.let { ScoreViewModel(it, args.id) } }.create(ScoreViewModel::class.java)
        model.closeDialog.observe(this.viewLifecycleOwner, Observer { dismiss() })
        val viewRoot = LayoutInflater.from(activity).inflate(R.layout.fragment_dialog_score, container, false)
        binding = DataBindingUtil.bind(viewRoot)!!
        binding.lifecycleOwner = this
        binding.viewmodel = model
        return viewRoot
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        input_score.setOnFocusChangeListener { _, _ ->
            input_score.post {
                val inputMethodManager: InputMethodManager? =
                    activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
                inputMethodManager?.showSoftInput(input_score, InputMethodManager.SHOW_IMPLICIT)
            }
        }
        input_score.requestFocus()

        prepareRecyclerView()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun prepareRecyclerView() {
        val adapter = ActionListAdapter(requireContext())
        actions.adapter = adapter
        actions.addItemDecoration(DividerItemDecoration(actions.context, DividerItemDecoration.VERTICAL))

        actions.layoutManager = LinearLayoutManager(requireContext())
        model.actions.observe(this.viewLifecycleOwner, Observer { actions ->
            // Update the cached copy of the words in the adapter.
            actions?.let { adapter.setPlayers(it) }
        })
    }


    class BaseViewModelFactory<T>(val creator: () -> T) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return creator() as T
        }
    }
}
