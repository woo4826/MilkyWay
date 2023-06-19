package kr.racto.milkyway.login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kr.racto.milkyway.LoadingDialog
import kr.racto.milkyway.MainActivity
import kr.racto.milkyway.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    lateinit var binding: ActivityLoginBinding
    private var dialog:LoadingDialog?=null
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    fun showLoadingDialog(){
        dialog=LoadingDialog(this@LoginActivity)
        dialog?.show()
    }

    fun dismissLoadingDialog(){
        dialog?.dismiss()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(0, 0);
    }
    fun init(){
        binding.root.setOnClickListener {
            // 키보드를 내리는 기능을 구현
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(binding.root.windowToken, 0)
        }
        binding.loginbtn.setOnClickListener {

            val email = binding.editEmail.text.toString()
            val password = binding.editPassword.text.toString()
            if(email.isEmpty())
                Toast.makeText(this,"E-mail을 입력하세요",Toast.LENGTH_SHORT).show()
            else if(password.isEmpty())
                Toast.makeText(this,"비밀번호를 입력하세요",Toast.LENGTH_SHORT).show()
            else {
                showLoadingDialog() // 에러나서 일단 주석처리---------
                coroutineScope.launch {
                    try {
                        auth.signInWithEmailAndPassword(email, password).await()
                        Toast.makeText(this@LoginActivity, "로그인 성공", Toast.LENGTH_LONG).show()
                        val next = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(next)
                        finish()
                    } catch (e: Exception) {
                        Toast.makeText(this@LoginActivity, "로그인 실패", Toast.LENGTH_LONG).show()
                        binding.editEmail.text.clear()
                        binding.editPassword.text.clear()
                    } finally {
                        dismissLoadingDialog()
                    }
                }
            }
        }
        binding.joinbtn.setOnClickListener {
            val intent = Intent(this, JoinActivity::class.java)
            startActivity(intent)
        }
        binding.nonmemberbtn.setOnClickListener {
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
            FirebaseAuth.getInstance().signOut()
            finish()
        }
    }
}