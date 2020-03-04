package com.mhp.scoreboard.score

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.mhp.scoreboard.R
import com.mhp.scoreboard.databinding.FragmentScoreBinding
import kotlinx.android.synthetic.main.activity_main.toolbar
import kotlinx.android.synthetic.main.fragment_score.actions
import kotlinx.android.synthetic.main.fragment_score.input_score
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ScoreFragment : Fragment() {

    private val model: ScoreViewModel by viewModel {
        parametersOf(args.id)
    }
    private lateinit var binding: FragmentScoreBinding
    private val args: ScoreFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        observeViewModel()
        val viewRoot = LayoutInflater.from(activity).inflate(R.layout.fragment_score, container, false)
        binding = DataBindingUtil.bind(viewRoot)!!
        binding.lifecycleOwner = this
        binding.viewmodel = model
        return viewRoot
    }

    private fun observeViewModel() {
        model.back.observe(this.viewLifecycleOwner, Observer {
            findNavController().navigateUp()
        })
        model.title.observe(this.viewLifecycleOwner, Observer { title ->
            activity?.toolbar?.title = title

        })
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

    override fun onStop() {
        val inputMethodManager: InputMethodManager? =
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        inputMethodManager?.hideSoftInputFromWindow(view?.windowToken, 0)
        super.onStop()
    }

    private fun prepareRecyclerView() {
        val adapter = ActionListAdapter(requireContext())
        actions.adapter = adapter
        actions.addItemDecoration(DividerItemDecoration(actions.context, DividerItemDecoration.VERTICAL))
        actions.layoutManager = LinearLayoutManager(requireContext())
        model.actions.observe(this.viewLifecycleOwner, Observer { actionItems ->
            // Update the cached copy of the words in the adapter.
            actionItems?.let {
                adapter.setPlayers(it)
                val position = if (it.size > 0) {
                    it.size - 1
                } else {
                    0
                }
                actions.postDelayed({ actions.scrollToPosition(position) }, 100)
            }
        })
    }
}
