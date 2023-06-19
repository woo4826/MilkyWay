package kr.racto.milkyway.ui.search

import NursingRoomDTO
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import kr.racto.milkyway.databinding.RowSearchBinding
import kr.racto.milkyway.databinding.SearchRoadingBinding

class SearchAdapter (val items: MutableList<NursingRoomDTO?>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    interface OnItemClickListener{
        fun OnItemClick(position: Int)
        fun OnButtonClick(position: Int)
    }
    companion object {
        private const val TYPE_POST = 0
        private const val TYPE_LOADING = 1
    }
    var itemClickListener : OnItemClickListener?=null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    inner class ItemHolder(val binding: RowSearchBinding): RecyclerView.ViewHolder(binding.root){
        init{
            binding.name.setOnClickListener {
                itemClickListener?.OnItemClick(absoluteAdapterPosition)
            }

            binding.button.setOnClickListener {
                itemClickListener?.OnButtonClick(absoluteAdapterPosition)
            }
        }
    }
    inner class LoadingViewHolder(var binding: SearchRoadingBinding) : RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            TYPE_POST -> {
                val view = RowSearchBinding.inflate(
                    LayoutInflater.from(parent.context)
                    ,parent,false)
                return ItemHolder(view)
            }
            else -> {
                val view = SearchRoadingBinding.inflate(
                    LayoutInflater.from(parent.context)
                    ,parent,false)
                return LoadingViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            TYPE_POST -> {
                val itemHolder = holder as ItemHolder
                itemHolder.binding.name.text=items[position]!!.roomName
            }
        }
    }

    fun setLoadingView(b: Boolean) {
        if (b) {
            this.items.add(null)
            notifyItemInserted(items.size-1)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            null -> TYPE_LOADING
            else -> TYPE_POST
        }
    }

}