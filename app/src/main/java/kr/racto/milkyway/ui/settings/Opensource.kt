package kr.racto.milkyway.ui.settings

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kr.racto.milkyway.R
import kr.racto.milkyway.databinding.FragmentOpensourceBinding

class Opensource : Fragment() {
    lateinit var binding:FragmentOpensourceBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding= FragmentOpensourceBinding.inflate(inflater,container,false)

        val main= activity as SettingBaseActivity
        binding.contents.text=main.readFile(R.raw.opensource)
        binding.contents.movementMethod= ScrollingMovementMethod()
        return binding.root
    }

}