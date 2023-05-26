package kr.racto.milkyway.review

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import kr.racto.milkyway.databinding.FragmentReviewBinding


class ReviewFragment : Fragment() {

    private var _binding:FragmentReviewBinding?=null

    private val binding get()=_binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val reviewViewModel=ViewModelProvider(this).get(ReviewViewModel::class.java)
        _binding= FragmentReviewBinding.inflate(inflater,container,false)
        val root:View=binding.root

        init()
        return root
    }

    private fun init() {

    }

}