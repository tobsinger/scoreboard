package com.mhp.scoreboard.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mhp.scoreboard.R
import com.mhp.scoreboard.databinding.FragmentListBinding
import kotlinx.android.synthetic.main.fragment_list.fab
import kotlinx.android.synthetic.main.fragment_list.recyclerview
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * [Fragment] that holds the list of players and navigates to the score of a selected player or to the create player form
 */
class PlayerListFragment : Fragment(), PlayerListAdapter.OnItemClickListener {

    private val model: PlayerListViewModel by viewModel()
    private lateinit var binding: FragmentListBinding
    private var windowTransitionTime: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        windowTransitionTime = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()
        val viewRoot = LayoutInflater.from(activity).inflate(R.layout.fragment_list, container, false)
        binding = DataBindingUtil.bind(viewRoot)!!
        binding.viewmodel = model
        return viewRoot
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fab.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_List_to_Create))
        val adapter = PlayerListAdapter(requireContext(), this)
        recyclerview.adapter = adapter
        recyclerview.layoutManager = LinearLayoutManager(requireContext())
        model.players.observe(this.viewLifecycleOwner, Observer { players ->
            players?.let { adapter.setPlayers(it) }
        })
    }

    override fun onResume() {
        fab.postDelayed(fab::show, windowTransitionTime)
        super.onResume()
    }

    override fun onPause() {
        fab.hide()
        super.onPause()
    }

    override fun onItemClicked(id: Int) {
        // Navigate to the player score fragment with the player id as a parameter
        findNavController().navigate(PlayerListFragmentDirections.actionListToScore(id))
    }
}
