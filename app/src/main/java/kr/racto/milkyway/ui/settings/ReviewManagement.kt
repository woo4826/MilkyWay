package kr.racto.milkyway.ui.settings

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Bundle
import android.util.Log
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
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Call



class ReviewManagement : Fragment() {
    lateinit var binding:FragmentReviewManagementBinding
    lateinit var adapter:SettingAdapter

    val reviewList= ArrayList<SettingsReview>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        binding= FragmentReviewManagementBinding.inflate(inflater,container,false)

        for(i in 1..5){
            reviewList.add(SettingsReview("test",5.0,"2023-05-26","너무 좋다~"))
        }

        binding.recyclerview.layoutManager=LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false)
        adapter=SettingAdapter(reviewList)
        binding.recyclerview.adapter=adapter

        val itemTouchHelper=ItemTouchHelper(SwipeToDeleteCallback())
        itemTouchHelper.attachToRecyclerView(binding.recyclerview)

        return binding.root
    }

    inner class SwipeToDeleteCallback : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        private val deleteIcon = ContextCompat.getDrawable(requireContext(), R.drawable.baseline_delete_forever_24) // 휴지통 아이콘
        private val backgroundColor = Color.parseColor("#80C7CDFF") // 배경색
        private var isSwipeActive = false
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
                itemView.top.toFloat()-convertDpToPx(5f),
                itemView.right.toFloat(),
                itemView.bottom.toFloat()-convertDpToPx(5f)
            )

            val paint = Paint()
            paint.color = backgroundColor
            c.drawRect(background, paint)

            if (isSwipeActive) {
                deleteIcon.draw(c)
            }
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }

        override fun onChildDrawOver(
            c: Canvas,
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder?,
            dX: Float,
            dY: Float,
            actionState: Int,
            isCurrentlyActive: Boolean
        ) {
            super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            isSwipeActive = isCurrentlyActive
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