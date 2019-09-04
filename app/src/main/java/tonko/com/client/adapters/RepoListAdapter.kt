package tonko.com.client.adapters

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_repo.view.*
import tonko.com.client.R
import tonko.com.client.model.Repos


class RepoListAdapter(var list: ArrayList<Repos>,
                      var listener: RepoListener) : RecyclerView.Adapter<RepoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder {
        return RepoViewHolder(
                LayoutInflater
                        .from(parent.context)
                        .inflate(
                                R.layout.item_repo,
                                parent,
                                false

                        ), listener)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
        holder.bind(list[position])
    }
}