package com.ilkeruzer.recyclerviewpaging

import DataSource
import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener

class MainActivity : AppCompatActivity(), OnRefreshListener,
    PagingRecyclerView.PagingRecyclerViewListener {

    var swipeRefreshLayout: SwipeRefreshLayout? = null
    var pagingRecyclerView: PagingRecyclerView? = null

    private var nextKey: String? = null
    private val dataSource = DataSource()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        pagingRecyclerView = findViewById(R.id.recyclerView)
        swipeRefreshLayout = findViewById(R.id.swipeRefresh)
        swipeRefreshLayout?.setOnRefreshListener(this)
        pagingRecyclerView?.setPagingRecyclerViewListener(this)

        initPersonData()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initPersonData() {
        nextKey = null
        dataSource.fetch(next = nextKey, completionHandler = { fetchResponse, fetchError ->
            fetchResponse?.let { response ->
                if (response.next.isNullOrEmpty()) {
                    Toast.makeText(this, "Failed to fetch list, try again.", Toast.LENGTH_LONG)
                        .show()
                } else {
                    nextKey = response.next
                    pagingRecyclerView?.initFirstData(response.people)
                }

                pagingRecyclerView!!.adapter!!.notifyDataSetChanged()
            }

            fetchError?.let {
                Toast.makeText(this, it.errorDescription + ". Try Again!", Toast.LENGTH_LONG)
                    .show()
            }
        })
    }

    override fun onRefresh() {
        Handler(Looper.getMainLooper()).postDelayed({
            swipeRefreshLayout?.isRefreshing = false
            pagingRecyclerView?.clearAdapterData()
            initPersonData()
        }, 2000)
    }

    override fun loadMoreListener() {
        if (!nextKey.isNullOrEmpty()) {
            dataSource.fetch(next = nextKey, completionHandler = { fetchResponse, fetchError ->
                Log.d("data", fetchResponse.toString())
                pagingRecyclerView?.setScrollPosition()
                fetchResponse?.let { response ->
                    nextKey = response.next
                    pagingRecyclerView?.setPeopleData(response.people)
                    if (nextKey == null) {
                        Toast.makeText(this, "There is no one left to list.", Toast.LENGTH_LONG)
                            .show()
                        pagingRecyclerView?.setEndScroll(true)
                    }
                }

                fetchError?.let {
                    Toast.makeText(this, it.errorDescription + ". Try Again!", Toast.LENGTH_LONG)
                        .show()
                }
                pagingRecyclerView?.setLoading(false)
            })
        } else {
            pagingRecyclerView?.setScrollPosition()
        }
    }
}
