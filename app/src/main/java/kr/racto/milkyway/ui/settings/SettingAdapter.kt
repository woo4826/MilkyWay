package kr.racto.milkyway.ui.settings

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.racto.milkyway.databinding.RowReviewManagementBinding

class SettingAdapter(var items:ArrayList<SettingsReview>): RecyclerView.Adapter<SettingAdapter.ViewHolder>() {
    interface OnItemClickListener{
        fun OnItemClick(data: SettingsReview, position:Int)
    }
    var itemClickListener: SettingAdapter.OnItemClickListener?=null

    inner class ViewHolder(val binding:RowReviewManagementBinding):RecyclerView.ViewHolder(binding.root) {
        init{
            binding.root.setOnClickListener {
                itemClickListener?.OnItemClick(items[adapterPosition],adapterPosition)
            }
        }
    }

    fun removeItem(position: Int){
        items.removeAt(position)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettingAdapter.ViewHolder {
        val view=RowReviewManagementBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: SettingAdapter.ViewHolder, position: Int) {
        holder.binding.roomName.text=items[position].name
        holder.binding.rating.append(items[position].rating.toString())
        holder.binding.date.append(items[position].date)
        holder.binding.contents.text=items[position].date
        holder.binding.reviewImg.setImageResource(items[position].img)
    }

    override fun getItemCount(): Int {
        return items.size
    }
}