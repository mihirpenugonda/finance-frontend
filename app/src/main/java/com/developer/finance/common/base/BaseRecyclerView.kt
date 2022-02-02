package com.developer.finance.common.base
//
//import android.text.Layout
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.annotation.LayoutRes
//import androidx.recyclerview.widget.RecyclerView
//import androidx.room.Ignore
//import androidx.viewbinding.ViewBinding
//
//abstract class BaseRecyclerViewAdapter<T: Any, VB: ViewBinding>:
//    RecyclerView.Adapter<BaseRecyclerViewAdapter.Companion.BaseViewHolder<VB>>() {
//
//    var items = mutableListOf<T>()
//    private lateinit var layoutInflater: LayoutInflater
//
//    fun addItems(items: List<T>) {
//        this.items = items as MutableList<T>
//        notifyDataSetChanged()
//    }
//
//    @LayoutRes
//    abstract fun getLayout() : Int
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<VB> {
//        var view = layoutInflater.inflate(getLayout(), parent, false)
//
//        return BaseViewHolder<VB>()(view)
//    }
//
//    override fun getItemCount(): Int {
//        return items.size
//    }
//
//    companion object {
//        class BaseViewHolder<VB: ViewBinding>(val binding: VB) :
//                RecyclerView.ViewHolder(binding.root)
//    }
//
//
//}