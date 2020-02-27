package com.mhp.scoreboard.score

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mhp.scoreboard.R

class ActionListAdapter internal constructor(
    context: Context
) : RecyclerView.Adapter<ActionListAdapter.ActionViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var actions = emptyList<Int>() // Cached copy of players

    inner class ActionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val scoreView: TextView = itemView.findViewById(R.id.score)
        val roundNumberView: TextView = itemView.findViewById(R.id.roundNumber)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActionViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_item_action, parent, false)
        return ActionViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ActionViewHolder, position: Int) {
        holder.scoreView.text = actions[position].toString()
        holder.roundNumberView.text = (position + 1).toString()
    }

    internal fun setPlayers(list: List<Int>) {
        this.actions = list
        notifyDataSetChanged()
    }

    override fun getItemCount() = actions.size
}
