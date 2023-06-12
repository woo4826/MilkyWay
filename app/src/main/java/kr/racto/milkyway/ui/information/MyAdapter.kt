package kr.racto.milkyway.ui.information

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.allViews
import androidx.recyclerview.widget.RecyclerView
import kr.racto.milkyway.R
import kr.racto.milkyway.databinding.RowInformationBinding

class MyAdapter(var items:ArrayList<Info>):RecyclerView.Adapter<MyAdapter.ViewHolder>() {
    interface OnItemClickListener{
        fun OnItemClick(data: Info, position:Int)
    }
    var itemClickListener:OnItemClickListener?=null
    inner class ViewHolder(val binding: RowInformationBinding) : RecyclerView.ViewHolder(binding.root){
        init{
            binding.titleLayout.setOnClickListener{
                itemClickListener?.OnItemClick(items[absoluteAdapterPosition],absoluteAdapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdapter.ViewHolder {
        val view = RowInformationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: MyAdapter.ViewHolder, position: Int) {
        holder.binding.title.text = items[position].title
        holder.binding.content.text = items[position].content
        val purpleColor = ContextCompat.getColor(holder.itemView.context, R.color.purple)
        val whiteColor = ContextCompat.getColor(holder.itemView.context, R.color.white)
        val blackColor = ContextCompat.getColor(holder.itemView.context, R.color.black)
        if(items[position].checked) {
            holder.binding.content.visibility = View.VISIBLE

            holder.binding.titleLayout.setBackgroundColor(purpleColor)
            holder.binding.titleQ.setTextColor(whiteColor)
            holder.binding.title.setTextColor(whiteColor)
        }
        else {
            holder.binding.content.visibility = View.GONE

            holder.binding.titleLayout.setBackgroundColor(whiteColor)
            holder.binding.titleQ.setTextColor(purpleColor)
            holder.binding.title.setTextColor(blackColor)
        }
    }
    override fun getItemCount(): Int {
        return items.size
    }
}