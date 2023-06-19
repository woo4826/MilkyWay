package kr.racto.milkyway.login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kr.racto.milkyway.FirstActivity
import kr.racto.milkyway.LoadingDialog
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

    fun showLoadingDialog(){
        val dialog=LoadingDialog(this)
        CoroutineScope(Main).launch {
            dialog.show()
            delay(2000)
            dialog.dismiss()
        }
    }

    fun init(){
        binding.root.setOnClickListener {
            // 키보드를 내리는 기능을 구현
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(binding.root.windowToken, 0)
        }
        binding.joinbtn.setOnClickListener {
            showLoadingDialog()
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
                            val database = FirebaseDatabase.getInstance()
                            val usersRef = database.getReference("users")
                            val currentUser = FirebaseAuth.getInstance().currentUser

                            usersRef.child(currentUser!!.uid).child("autoLogin").setValue(true)
                            finish()
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(this, "회원가입 실패", Toast.LENGTH_LONG).show()
                        }
                    }
            }
        }
    }
}