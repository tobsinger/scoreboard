package com.mhp.scoreboard.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mhp.scoreboard.R
import com.mhp.scoreboard.databinding.FragmentListBinding
import kotlinx.android.synthetic.main.fragment_list.recyclerview

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class PlayerListFragment : Fragment(), PlayerListAdapter.OnItemClickListener {
    private lateinit var model: PlayerListViewModel
    private lateinit var binding: FragmentListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        model = ViewModelProvider(this).get(PlayerListViewModel::class.java)
        val viewRoot = LayoutInflater.from(activity).inflate(R.layout.fragment_list, container, false)
        binding = DataBindingUtil.bind(viewRoot)!!
        binding.viewmodel = model
        return viewRoot
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            findNavController().navigate(R.id.action_List_to_Create)
        }

        val adapter = PlayerListAdapter(requireContext(), this)
        recyclerview.adapter = adapter
        recyclerview.addItemDecoration(DividerItemDecoration(recyclerview.context, DividerItemDecoration.VERTICAL))

        recyclerview.layoutManager = LinearLayoutManager(requireContext())
        model.players.observe(this.viewLifecycleOwner, Observer { words ->
            // Update the cached copy of the words in the adapter.
            words?.let { adapter.setPlayers(it) }
        })
    }

    override fun onItemClicked(id: Int) {
        val action = PlayerListFragmentDirections.actionListToScore(id)
        findNavController().navigate(action)
    }
}
