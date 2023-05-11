package com.ilkeruzer.recyclerviewpaging

import Person
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewAdapter(
    private var itemList: ArrayList<Person?>?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_LOADING = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_ITEM) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_row, parent, false)
            ItemViewHolder(view)
        } else {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_loading, parent, false)
            LoadingViewHolder(view)
        }
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        if (viewHolder is ItemViewHolder) {
            populateItemRows(viewHolder, position)
        } else if (viewHolder is LoadingViewHolder) {
            showLoadingView(viewHolder, position)
        }
    }

    override fun getItemCount(): Int {
        return if (itemList == null) 0 else itemList!!.size
    }

    /**
     * The following method decides the type of ViewHolder to display in the RecyclerView
     *
     * @param position
     * @return
     */
    override fun getItemViewType(position: Int): Int {
        return if (itemList!![position] == null) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

    private inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvItem: TextView

        init {
            tvItem = itemView.findViewById(R.id.tvItem)
        }
    }

    private inner class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var progressBar: ProgressBar

        init {
            progressBar = itemView.findViewById(R.id.progressBar)
        }
    }

    private fun showLoadingView(viewHolder: LoadingViewHolder, position: Int) {
        //ProgressBar would be displayed
    }

    @SuppressLint("SetTextI18n")
    private fun populateItemRows(viewHolder: ItemViewHolder, position: Int) {
        val item = itemList!![position]
        item?.let { viewHolder.tvItem.text = "${it.id} - ${it.fullName}" }

    }

    fun clear() {
        val size = itemList!!.size
        itemList!!.clear()
        notifyItemRangeRemoved(0, size);
    }
}