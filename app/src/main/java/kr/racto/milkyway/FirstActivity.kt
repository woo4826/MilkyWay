package kr.racto.milkyway

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kr.racto.milkyway.databinding.ActivityFirstBinding
import kr.racto.milkyway.login.JoinActivity
import kr.racto.milkyway.login.LoginActivity


class FirstActivity : AppCompatActivity() {
    lateinit var binding: ActivityFirstBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFirstBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        check()
    }


    fun init(){
        binding.nonmemberbtn.setOnClickListener {
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
            FirebaseAuth.getInstance().signOut()
        }
        binding.loginbtn.setOnClickListener {
            val i = Intent(this, LoginActivity::class.java)
            startActivity(i)
        }
        binding.joinbtn.setOnClickListener {
            val i = Intent(this, JoinActivity::class.java)
            startActivity(i)
        }
    }

    fun check(){
        val database = FirebaseDatabase.getInstance()
        val usersRef = database.getReference("users")
        // Firebase 실시간 데이터베이스의 "users" 노드에서 자동 로그인 정보 가져오기
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.let {
            val userId = it.uid
            if(currentUser.uid==userId){
                usersRef.child(userId).child("autoLogin").addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val autoLoginEnabled = dataSnapshot.getValue(Boolean::class.java)
                        if (autoLoginEnabled == true) {
                            // 자동 로그인 설정이 활성화된 경우 처리 로직 추가
                            val homeMove_intent = Intent(applicationContext, MainActivity::class.java)
                            startActivity(homeMove_intent)
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                        // 데이터베이스 읽기 오류 처리
                    }
                })
            }
        }
    }
}