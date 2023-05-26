package kr.racto.milkyway.review

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import kr.racto.milkyway.databinding.FragmentSecondReviewBinding

class secondReviewFragment : Fragment() {

    private var _binding: FragmentSecondReviewBinding?=null

    private val binding get()=_binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val reviewViewModel= ViewModelProvider(this).get(ReviewViewModel::class.java)
        _binding= FragmentSecondReviewBinding.inflate(inflater,container,false)
        val root:View=binding.root

        init()
        return root
    }

    private fun init() {

    }

}