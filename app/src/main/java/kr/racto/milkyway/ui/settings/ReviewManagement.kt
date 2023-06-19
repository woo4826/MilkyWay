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
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kr.racto.milkyway.R
import kr.racto.milkyway.databinding.FragmentReviewManagementBinding
import kr.racto.milkyway.login.App
import kr.racto.milkyway.model.Review
import kr.racto.milkyway.model.RoomData
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Call
import java.net.URLEncoder


class ReviewManagement : Fragment() {
    lateinit var binding:FragmentReviewManagementBinding
    lateinit var adapter:SettingAdapter

    val reviewList= ArrayList<SettingsReview>()
    val reviewIdList = ArrayList<Int>()
    val user: FirebaseUser? = FirebaseAuth.getInstance().getCurrentUser()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        binding= FragmentReviewManagementBinding.inflate(inflater,container,false)



        val userEmail = user?.email!!
        var call: Call<List<Review>>? = null

        call = App.apiService.getUserReviewData(userEmail)
        call.enqueue(object : Callback<List<Review>> {
            override fun onResponse(call: Call<List<Review>>, response: Response<List<Review>>) {
                if (response.isSuccessful) {
                    val userReviewList = response.body()

                    if (userReviewList?.size != 0) {
                        reviewList.clear()
                        reviewIdList.clear()
                        for(i in 0 until userReviewList!!.size){
                            reviewList.add(SettingsReview(userReviewList!![i].roomName,userReviewList[i].rating,userReviewList[i].title,userReviewList[i].description))
                            reviewIdList.add(userReviewList!![i].reviewId)
                        }
                        adapter.notifyDataSetChanged()
                        binding.noReview.visibility = View.GONE
                    } else{
                        Toast.makeText(activity, "리뷰 없음", Toast.LENGTH_SHORT).show()
                        binding.noReview.visibility = View.VISIBLE
                    }


                } else {
                    if (response.code() == 500) {
                        // 서버 내부 오류인 경우 처리
                        Toast.makeText(requireActivity(), "서버 내부 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
                    } else {
                        // 다른 상태 코드에 대한 처리
                        // 예: response.code() == 404 - 페이지를 찾을 수 없음
                        //     response.code() == 401 - 인증 실패
                        //     등등
                        Toast.makeText(requireActivity(), "요청에 실패했습니다."+response.code().toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }
            override fun onFailure(call: Call<List<Review>>, t: Throwable) {
                // 네트워크 요청이 실패한 경우
            }
        })



//        for(i in 1..5){
//            reviewList.add(SettingsReview("test",5.0,"2023-05-26","너무 좋다~"))
//        }

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

            val call = App.apiService.deleteReview(reviewIdList[viewHolder.absoluteAdapterPosition])
            call.enqueue(object : Callback<Boolean>{
                override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                    if (response.isSuccessful) {
                        // 삭제 성공
                        if (reviewList.isEmpty())
                            binding.noReview.visibility = View.VISIBLE

                    } else {
                        if (response.code() == 500) {
                            // 서버 내부 오류인 경우 처리
                            Toast.makeText(requireActivity(), "서버 내부 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
                        } else {
                            // 다른 상태 코드에 대한 처리
                            // 예: response.code() == 404 - 페이지를 찾을 수 없음
                            //     response.code() == 401 - 인증 실패
                            //     등등
                            Toast.makeText(requireActivity(), "요청에 실패했습니다."+response.code().toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                override fun onFailure(call: Call<Boolean>, t: Throwable) {

                }
            })
            reviewIdList.removeAt(position)
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