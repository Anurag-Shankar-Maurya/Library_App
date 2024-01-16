package com.babumushai.library.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.babumushai.library.R
import com.babumushai.library.activity.DescriptionActivity
import com.babumushai.library.model.Book
import com.squareup.picasso.Picasso

class DashboardRecyclerAdapter(val context: Context, val itemList: ArrayList<Book>) :
    RecyclerView.Adapter<DashboardRecyclerAdapter.DashboardViewHolder>() {
    class DashboardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val bookName: TextView = view.findViewById(R.id.txtBookName)
        val bookAuthor: TextView = view.findViewById(R.id.txtBookAuthor)
        val bookCost: TextView = view.findViewById(R.id.txtBookCost)
        val bookRating: TextView = view.findViewById(R.id.txtBookRating)
        val bookImage: ImageView = view.findViewById(R.id.imgBookImage)
        val rlContent: RelativeLayout = view.findViewById(R.id.rlContent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_dashboard_single_row, parent, false)
        return DashboardViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        val book = itemList[position]

        holder.bookName.text = book.bookName
        holder.bookAuthor.text = book.bookAuthor
        holder.bookCost.text = book.bookPrice
        holder.bookRating.text = book.bookRating
//        holder.bookImage.setImageResource(book.bookImage)
        Picasso.get().load(book.bookImage).error(R.drawable.book_default_image)
            .into(holder.bookImage)

        holder.rlContent.setOnClickListener {
            val intent = Intent(context, DescriptionActivity::class.java)
            intent.putExtra("book_id", book.bookId)
            context.startActivity(intent)
        }
    }
}