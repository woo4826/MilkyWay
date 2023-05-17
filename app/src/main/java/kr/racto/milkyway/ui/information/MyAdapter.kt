package kr.racto.milkyway.ui.information

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.racto.milkyway.databinding.RowInformationBinding

class MyAdapter(var items:ArrayList<Info>):RecyclerView.Adapter<MyAdapter.ViewHolder>() {
    interface OnItemClickListener{
        fun OnItemClick(data: Info, position:Int)
    }
    var itemClickListener:OnItemClickListener?=null
    inner class ViewHolder(val binding: RowInformationBinding) : RecyclerView.ViewHolder(binding.root){
        init{
            binding.root.setOnClickListener{
                itemClickListener?.OnItemClick(items[adapterPosition],adapterPosition)
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
    }
    override fun getItemCount(): Int {
        return items.size
    }
}