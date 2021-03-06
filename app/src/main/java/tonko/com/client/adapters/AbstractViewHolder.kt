package tonko.com.client.adapters

import android.support.v7.widget.RecyclerView
import android.view.View

abstract class AbstractViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView)
{
    abstract fun bind(item: T)
}
