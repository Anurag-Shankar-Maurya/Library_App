package com.babumushai.library.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.babumushai.library.R
import com.babumushai.library.adapter.DashboardRecyclerAdapter
import com.babumushai.library.model.Book
import com.babumushai.library.util.ConnectionManager
import org.json.JSONException
import java.util.Collections

class DashboardFragment : Fragment() {

    lateinit var recyclerDashboard: RecyclerView
    lateinit var layoutManager: LinearLayoutManager
    lateinit var recyclerAdapter: DashboardRecyclerAdapter
    lateinit var progressBar: ProgressBar
    lateinit var progressBarLayout: RelativeLayout

    val bookInfoList = arrayListOf<Book>()

    var ratingComparator = Comparator<Book> { book1, book2 ->

        if (book1.bookRating.compareTo(book2.bookRating, true) == 0) {
            //  sort according name if rating is same
            book1.bookName.compareTo(book2.bookName, true)
        } else
            book1.bookRating.compareTo(book2.bookRating, true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        // setting dashboard menu item
        setHasOptionsMenu(true)

        // as a fragment is created in 'onCreateView' thus it need a view to initialize
        recyclerDashboard = view.findViewById(R.id.recyclerDashboard)

        // giving context as activity as it is fragment
        layoutManager = LinearLayoutManager(activity)

        progressBar = view.findViewById(R.id.progressBar)

        progressBarLayout = view.findViewById(R.id.progressBarLayout)

        progressBarLayout.visibility = View.VISIBLE

        if (ConnectionManager().checkConnectivity(activity as Context)) {

            // creating API request queue
            val queue = Volley.newRequestQueue(activity as Context)
            val url = "http://13.235.250.119/v1/book/fetch_books/"
            val jsonObjectRequest = object : JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                Response.Listener {
                    // handle the responses
                    val success = it.getBoolean("success")
                    try {
                        progressBarLayout.visibility = View.GONE
                        if (success) {
                            val data = it.getJSONArray("data")

                            for (i in 0 until data.length()) {
                                val bookJsonObject = data.getJSONObject(i)
                                val bookObject = Book(
                                    bookJsonObject.getString("book_id"),
                                    bookJsonObject.getString("name"),
                                    bookJsonObject.getString("author"),
                                    bookJsonObject.getString("rating"),
                                    bookJsonObject.getString("price"),
                                    bookJsonObject.getString("image")
                                )
                                bookInfoList.add(bookObject)
                                recyclerAdapter =
                                    DashboardRecyclerAdapter(
                                        activity as Context,
                                        bookInfoList
                                    )    // typecasting using 'AS'

                                recyclerDashboard.adapter = recyclerAdapter
                                recyclerDashboard.layoutManager = layoutManager
                            }
                        } else {
                            Toast.makeText(
                                activity as Context,
                                "Oops...!\nSome error occurred",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }

                    } catch (e: JSONException) {
                        Toast.makeText(activity as Context, "Unexpected Error!", Toast.LENGTH_SHORT)
                            .show()
                    }
                },
                Response.ErrorListener {
                    // handle the error
                    if (activity != null) {
                        Toast.makeText(
                            activity as Context,
                            "Volley error occurred",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Content-type"] = "application/json"
                    headers["token"] = "072535544bd716"
                    return headers
                }
            }

            queue.add(jsonObjectRequest)
        } else {
            val dialog = AlertDialog.Builder(activity as Context)
            dialog.setTitle("Error")
            dialog.setMessage("No internet connection")
            dialog.setPositiveButton("Open Settings") { text, listner ->
                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                activity?.finish()
            }
            dialog.setNegativeButton("Exit") { text, Listner ->
                ActivityCompat.finishAffinity(activity as Activity)
            }
            dialog.create()
            dialog.show()
        }
        return view
    }

    // to add menu item to dashboard toolbar
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_dashboard, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item?.itemId

        if (id == R.id.action_sort) {
            Collections.sort(bookInfoList, ratingComparator)    // default sort 'lowest first'

            // reversing sort 'highest first'
            bookInfoList.reverse()
        }

        //  notifying adapter to update list
        recyclerAdapter.notifyDataSetChanged()

        return super.onOptionsItemSelected(item)
    }

}