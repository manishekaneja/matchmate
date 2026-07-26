package com.blahblah.matchmate.ui.recyclerview.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blahblah.matchmate.databinding.ItemPaginationLoaderBinding
import com.blahblah.matchmate.ui.recyclerview.viewholder.PaginationLoaderViewHolder

class PaginationLoaderAdapter : RecyclerView.Adapter<PaginationLoaderViewHolder>() {

    var isLoading: Boolean = false
        set(value) {
            if (field == value) return
            field = value
            if (value) notifyItemInserted(0) else notifyItemRemoved(0)
        }

    override fun getItemCount() = if (isLoading) 1 else 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        PaginationLoaderViewHolder(
            ItemPaginationLoaderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: PaginationLoaderViewHolder, position: Int) = Unit
}
