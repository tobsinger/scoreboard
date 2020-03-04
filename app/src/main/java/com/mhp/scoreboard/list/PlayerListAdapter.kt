package com.mhp.scoreboard.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mhp.scoreboard.R
import com.mhp.scoreboard.db.Player

/**
 * Implementation of [RecyclerView.Adapter] for the entity type [Player]
 */
class PlayerListAdapter internal constructor(
    context: Context,
    val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<PlayerListAdapter.PlayerViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var players = emptyList<Player>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_item_player, parent, false)
        return PlayerViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        val currentPlayer = players[position]
        holder.playItemNameView.text = currentPlayer.name
        holder.playItemScoreView.text = currentPlayer.actions.sum().toString()
    }

    internal fun setPlayers(list: List<Player>) {
        this.players = list
        notifyDataSetChanged()
    }

    override fun getItemCount() = players.size

    inner class PlayerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        val playItemNameView: TextView = itemView.findViewById(R.id.name)
        val playItemScoreView: TextView = itemView.findViewById(R.id.score)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            players[adapterPosition].id?.let { onItemClickListener.onItemClicked(it) }
        }
    }

    interface OnItemClickListener {
        fun onItemClicked(id: Int)
    }
}
