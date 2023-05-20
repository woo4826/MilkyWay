package kr.racto.milkyway.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import kr.racto.milkyway.FirstActivity
import kr.racto.milkyway.MainActivity
import kr.racto.milkyway.R
import kr.racto.milkyway.databinding.FragmentSettingsBinding
import kr.racto.milkyway.login.JoinActivity
import kr.racto.milkyway.login.LoginActivity

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    val toggleImg= listOf<Int>(R.drawable.toggle_on,R.drawable.toggle_off)
    var toggle_check=0

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val settingsViewModel =
            ViewModelProvider(this).get(SettingsViewModel::class.java)

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        init()

        return root
    }


    private fun init() {
        binding.settingsJoin.setOnClickListener {
            val i= Intent(requireContext(),JoinActivity::class.java)
            startActivity(i)
        }
        binding.settingsLogin.setOnClickListener {
            val i= Intent(requireContext(),LoginActivity::class.java)
            startActivity(i)
        }
        binding.settingsReviewList.setOnClickListener {
            //사용자 작성 리뷰 화면으로 넘어가는거 구성

        }
        binding.settingsLogout.setOnClickListener {
            val i= Intent(requireContext(),FirstActivity::class.java)
            startActivity(i)
        }
        binding.settingsAutoLogin.setOnClickListener {
            if(toggle_check==0){
                toggle_check=1
            }else{
                toggle_check=0
            }
            binding.settingsAutoLogin.setImageResource(toggleImg[toggle_check])
        }
        binding.settingsAgreement.setOnClickListener {
            val i= Intent(requireContext(),AgreementActivity::class.java)
            startActivity(i)
        }
        binding.settingsPrivacy.setOnClickListener {
            val i= Intent(requireContext(),PrivacyActivity::class.java)
            startActivity(i)
        }
        binding.settingsDeveloper.setOnClickListener {
            val i= Intent(requireContext(),DeveloperActivity::class.java)
            startActivity(i)
        }
        binding.settingsOpensourceLicense.setOnClickListener {
            val i= Intent(requireContext(),OpensourceActivity::class.java)
            startActivity(i)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}