package kr.racto.milkyway.ui.detail

import android.util.Half.toFloat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.racto.milkyway.databinding.RowInformationBinding
import kr.racto.milkyway.databinding.RowReviewDetailBinding
import kr.racto.milkyway.ui.information.MyAdapter

class DetailAdapter(var items:ArrayList<DetailReview>):RecyclerView.Adapter<DetailAdapter.ViewHolder>() {
    interface OnItemClickListener{
        fun OnItemClick(data: DetailReview, position:Int)
    }
    var itemClickListener:OnItemClickListener?=null
    inner class ViewHolder(val binding: RowReviewDetailBinding) : RecyclerView.ViewHolder(binding.root){
        init{
            binding.root.setOnClickListener{
                itemClickListener?.OnItemClick(items[absoluteAdapterPosition],absoluteAdapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailAdapter.ViewHolder {
        val view = RowReviewDetailBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.userEmail.text = items[position].name
        holder.binding.rating.rating = items[position].rating.toFloat()
        holder.binding.date.text = items[position].date
        holder.binding.contents.text = items[position].contents
    }

    override fun getItemCount(): Int {
        return items.size
    }
}