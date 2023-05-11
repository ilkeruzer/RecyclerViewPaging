package com.ilkeruzer.recyclerviewpaging

import Person
import android.annotation.SuppressLint
import android.content.AttributionSource
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.time.format.DecimalStyle

class PagingRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : RecyclerView(context, attrs, defStyle) {

    private var recyclerViewAdapter: RecyclerViewAdapter? = null
    private var rowsArrayList = ArrayList<Person?>()
    private var isLoading = false
    private var listener: PagingRecyclerViewListener? = null
    private var endScroll = false

    init {
        initAdapter()
        initScrollListener()
    }

    private fun initAdapter() {
        rowsArrayList.clear()
        recyclerViewAdapter = RecyclerViewAdapter(rowsArrayList)
        this.adapter = recyclerViewAdapter
    }

    fun clearAdapterData() {
        recyclerViewAdapter?.clear()
    }


    private fun initScrollListener() {
        this.addOnScrollListener(object : OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?
                if (!isLoading && !endScroll) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == rowsArrayList.size - 1) {
                        //bottom of list!
                        loadMore()
                        isLoading = true
                    }
                }
            }
        })
    }

    private fun loadMore() {
        rowsArrayList.add(null)
        this.post {
            recyclerViewAdapter!!.notifyItemInserted(rowsArrayList.size - 1)
        }
        listener?.loadMoreListener()

        isLoading = false
    }

    fun setScrollPosition() {
        rowsArrayList.removeAt(rowsArrayList.size - 1)
        val scrollPosition = rowsArrayList.size
        this.post {
            recyclerViewAdapter!!.notifyItemRemoved(scrollPosition)
        }
    }

    fun setPeopleData(list: List<Person>) {
        list.forEach {
            val findPerson = rowsArrayList.find { list -> list?.id == it.id }
            if (findPerson == null) {
                rowsArrayList.add(it)
            } else {
                Log.d("list","same id: $findPerson")
            }
        }
        recyclerViewAdapter?.let { recyclerViewAdapter ->
            recyclerViewAdapter.notifyItemInserted(recyclerViewAdapter.itemCount - 1)
        }
    }

    fun initFirstData(list: List<Person>) {
        list.forEach {
            val findPerson = rowsArrayList.find { list -> list?.id == it.id }
            if (findPerson == null) {
                rowsArrayList.add(it)
            } else {
                Log.d("list","same id: $findPerson")
            }
        }
        isLoading = false
        endScroll = false
    }

    fun setPagingRecyclerViewListener(listener: PagingRecyclerViewListener) {
        this.listener = listener
    }

    fun setEndScroll(endScroll: Boolean) {
        this.endScroll = endScroll
    }

    fun setLoading(isLoading: Boolean) {
        this.isLoading = isLoading
    }

    interface PagingRecyclerViewListener {
        fun loadMoreListener()
    }
}