package kr.racto.milkyway.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import kr.racto.milkyway.FirstActivity
import kr.racto.milkyway.R
import kr.racto.milkyway.databinding.FragmentSettingsBinding
import kr.racto.milkyway.login.App
import kr.racto.milkyway.login.JoinActivity
import kr.racto.milkyway.login.LoginActivity

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    val toggleImg= listOf<Int>(R.drawable.toggle_off,R.drawable.toggle_on)
    var toggle_check=1 //toggle_check==1 자동로그인 활성화, toggle_check==0 자동로그인 비활성화

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

        AutoLoginImg()
        init()
        return root
    }


    private fun AutoLoginImg(){
        val autoCheck= requireActivity().application as App
        synchronized(autoCheck){
            if(autoCheck.getSharedValue()){ //자동로그인 기능 활성화
                toggle_check=1
            }else{ //자동로그인 기능 비활성화
                toggle_check=0
            }
        }
        binding.settingsAutoLogin.setImageResource(toggleImg[toggle_check])
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
        binding.settingsLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val i= Intent(requireContext(),FirstActivity::class.java)
            startActivity(i)
        }
        binding.settingsAutoLogin.setOnClickListener {
            val autoCheck= requireActivity().application as App
            synchronized(autoCheck){
                if(toggle_check==0){ //자동로그인 기능 활성화
                    toggle_check=1
                    autoCheck.setSharedValue(true)
                }else{ //자동로그인 기능 비활성화
                    toggle_check=0
                    autoCheck.setSharedValue(false)
                }
            }
            binding.settingsAutoLogin.setImageResource(toggleImg[toggle_check])
        }
        val nextIntent=Intent(requireContext(),SettingBaseActivity::class.java)
        binding.settingsAgreement.setOnClickListener {
            nextIntent.putExtra("setting_base",0)
            startActivity(nextIntent)
        }
        binding.settingsPrivacy.setOnClickListener {
            nextIntent.putExtra("setting_base",1)
            startActivity(nextIntent)
        }
        binding.settingsDeveloper.setOnClickListener {
            nextIntent.putExtra("setting_base",2)
            startActivity(nextIntent)
        }
        binding.settingsOpensourceLicense.setOnClickListener {
            nextIntent.putExtra("setting_base",3)
            startActivity(nextIntent)
        }
        binding.settingsReviewList.setOnClickListener {
            //사용자 작성 리뷰 화면으로 넘어가는거 구성
            nextIntent.putExtra("setting_base",4)
            startActivity(nextIntent)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}