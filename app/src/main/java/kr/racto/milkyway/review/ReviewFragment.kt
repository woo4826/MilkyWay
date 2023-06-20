package kr.racto.milkyway.review

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import kr.racto.milkyway.databinding.FragmentReviewBinding


class ReviewFragment : Fragment() {

    private var _binding: FragmentReviewBinding? = null

    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val reviewViewModel = ViewModelProvider(this).get(ReviewViewModel::class.java)
        _binding = FragmentReviewBinding.inflate(inflater, container, false)
        val root: View = binding.root

        init()
        return root
    }

    private fun init() {

        val roomName=arguments?.getString("roomName")
        if(roomName!=null){
            binding.name.text=roomName
        }


        binding.ratingbar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            val value = rating.toString()
            val main = activity as MainReviewActivity
            binding.ratingText.text = value
            main.Ratingvalue = value
            binding.ratingbar.postDelayed({
                main.changeFrag()
            },500)
        }
    }
}