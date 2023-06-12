package kr.racto.milkyway.ui.settings

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide.init
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kr.racto.milkyway.FirstActivity
import kr.racto.milkyway.R
import kr.racto.milkyway.databinding.FragmentSettingsBinding
import kr.racto.milkyway.login.JoinActivity
import kr.racto.milkyway.login.LoginActivity

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    val scope = CoroutineScope(Dispatchers.IO)

    val toggleImg= listOf<Int>(R.drawable.toggle_off,R.drawable.toggle_on)
    var toggle_check=1 //toggle_check==1 자동로그인 활성화, toggle_check==0 자동로그인 비활성화

    val database = FirebaseDatabase.getInstance()
    val usersRef = database.getReference("users")
    val user: FirebaseUser? = FirebaseAuth.getInstance().getCurrentUser()

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
        scope.launch {
            withContext(Dispatchers.Main){
                if(user!=null){
                    val check=usersRef.child(user.uid).child("autoLogin").get().await().getValue(Boolean::class.java)!!
                    if(check){
                        toggle_check=1
                    }else{
                        toggle_check=0
                    }
                }
                binding.settingsAutoLogin.setImageResource(toggleImg[toggle_check])
            }
        }
    }

    private fun init() {
//        binding.settingsJoin.setOnClickListener {
//            val i= Intent(requireContext(),JoinActivity::class.java)
//            startActivity(i)
//        }
//        binding.settingsLogin.setOnClickListener {
//            val i= Intent(requireContext(),LoginActivity::class.java)
//            startActivity(i)
//        }
        binding.userID.text=user!!.email
        binding.settingsLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val i= Intent(requireContext(),LoginActivity::class.java)
            startActivity(i)
        }
        binding.settingsAutoLogin.setOnClickListener {
            if(toggle_check==0){ //자동로그인 기능 활성화
                toggle_check=1
                usersRef.child(user.uid).child("autoLogin").setValue(true)

            }else{ //자동로그인 기능 비활성화
                toggle_check=0
                usersRef.child(user.uid).child("autoLogin").setValue(false)
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
        binding.settingsWithdrawal.setOnClickListener {
            val uid=user.uid
            user.delete()
            usersRef.child(uid).removeValue()
            Toast.makeText(requireContext(),"회원탈퇴가 완료되었습니다.",Toast.LENGTH_SHORT).show()
            val next=Intent(requireContext(),FirstActivity::class.java)
            startActivity(next)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
