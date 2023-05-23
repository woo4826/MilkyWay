package kr.racto.milkyway.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kr.racto.milkyway.databinding.ActivityJoinBinding

class JoinActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    lateinit var binding: ActivityJoinBinding
    override fun onCreate(savedInstanceState: Bundle?)  {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        binding = ActivityJoinBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }
    fun init(){
        binding.joinbtn.setOnClickListener {
            var isGoToJoin = true
            val email = binding.editEmail.text.toString()
            val password = binding.editpw.text.toString()
            val passwordCheck = binding.editpw2.text.toString()
            if (email.isEmpty()) {
                Toast.makeText(this, "이메일을 입력해주세요.", Toast.LENGTH_LONG).show()
                isGoToJoin = false
            }
            if (password.isEmpty()) {
                Toast.makeText(this, "password1을 입력해주세요.", Toast.LENGTH_LONG).show()
                isGoToJoin = false
            }
            if (passwordCheck.isEmpty()) {
                Toast.makeText(this, "password2을 입력해주세요.", Toast.LENGTH_LONG).show()
                isGoToJoin = false
            }
            if (password != passwordCheck) {
                Toast.makeText(this, "비밀번호를 똑같이 입력해주세요.", Toast.LENGTH_LONG).show()
                isGoToJoin = false
            }
            if (password.length < 6) {
                Toast.makeText(this, "비밀번호를 6자리 이상으로 입략헤주세요.", Toast.LENGTH_LONG).show()
                isGoToJoin = false
            }
            if (isGoToJoin) {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(this, "화원가입 성공", Toast.LENGTH_LONG).show()
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(this, "회원가입 실패", Toast.LENGTH_LONG).show()
                        }
                    }
            }
        }
    }
}