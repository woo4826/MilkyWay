package kr.racto.milkyway.ui.settings

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.racto.milkyway.R
import kr.racto.milkyway.databinding.FragmentReviewManagementBinding

class ReviewManagement : Fragment() {
    lateinit var binding:FragmentReviewManagementBinding
    lateinit var adapter:SettingAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding= FragmentReviewManagementBinding.inflate(inflater,container,false)
        val data= ArrayList<SettingsReview>()

        for(i in 1..5){
            data.add(SettingsReview("test",5.0,"2023-05-26",R.drawable.testimg,"너무 좋다~"))
        }

        binding.recyclerview.layoutManager=LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false)
        adapter=SettingAdapter(data)
        binding.recyclerview.adapter=adapter

        val itemTouchHelper=ItemTouchHelper(SwipeToDeleteCallback())
        itemTouchHelper.attachToRecyclerView(binding.recyclerview)

        return binding.root
    }

    inner class SwipeToDeleteCallback : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        private val deleteIcon = ContextCompat.getDrawable(requireContext(), R.drawable.baseline_delete_forever_24) // 휴지통 아이콘
        private val backgroundColor = Color.parseColor("#E0E0E0") // 배경색
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }

        override fun onChildDraw(
            c: Canvas,
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            dX: Float,
            dY: Float,
            actionState: Int,
            isCurrentlyActive: Boolean
        ) {
            val itemView = viewHolder.itemView
            val iconMargin = (itemView.height - deleteIcon?.intrinsicHeight!!) /2

            val iconLeft = itemView.right - iconMargin - deleteIcon.intrinsicWidth
            val iconRight = itemView.right - iconMargin
            val iconTop = itemView.top + iconMargin
            val iconBottom = itemView.bottom - iconMargin

            deleteIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom)

            val background = RectF(
                itemView.right.toFloat() + dX,
                itemView.top.toFloat(),
                itemView.right.toFloat(),
                itemView.bottom.toFloat()-convertDpToPx(5f)
            )

            val paint = Paint()
            paint.color = backgroundColor
            c.drawRect(background, paint)

            deleteIcon.draw(c)

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.adapterPosition
            adapter.removeItem(position)
        }
    }

    private fun convertDpToPx(dp: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            resources.displayMetrics
        )
    }

}