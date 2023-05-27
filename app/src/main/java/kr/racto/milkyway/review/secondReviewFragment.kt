package kr.racto.milkyway.review

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import kr.racto.milkyway.MainActivity
import kr.racto.milkyway.databinding.FragmentSecondReviewBinding

class secondReviewFragment : Fragment() {

    companion object {
        private const val REQUEST_IMAGE_PICK = 1000
    }

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
        val main=activity as MainReviewActivity
        binding.ratingbar.rating=main.Ratingvalue.toFloat()
        binding.ratingText.text=main.Ratingvalue

        binding.photoInsert.setOnClickListener {
            val pickImageIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(pickImageIntent, REQUEST_IMAGE_PICK)

        }

        binding.ratingbar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            val value=rating.toString()
            binding.ratingText.text=value
        }

        binding.completion.setOnClickListener {
            val newIntent= Intent(requireContext(), MainActivity::class.java)
            //review 작성한 내용 intent에 넘기기
            startActivity(newIntent)
        }
    }

}