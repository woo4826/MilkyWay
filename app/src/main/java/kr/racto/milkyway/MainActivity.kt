package kr.racto.milkyway

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kr.racto.milkyway.databinding.ActivityMainBinding
import kr.racto.milkyway.ui.detail.DetailFragment
import kr.racto.milkyway.ui.MyViewModel


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    val modalBottomSheet = DetailFragment()
    val myViewModel: MyViewModel by viewModels()
    val user: FirebaseUser? = FirebaseAuth.getInstance().getCurrentUser()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        setStatusBarTransparent()

        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

        // Keep the status bar always on
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)


        window.statusBarColor = resources.getColor(R.color.black, theme) // api 23 이상
        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        //비회원인 경우, 아예 설정 파트로들어갈 수 없게 구현
        navController.addOnDestinationChangedListener{_,destination,_->
            if(destination.id==R.id.navigation_settings && user==null){
                navController.navigateUp()
                ProfileDialog(this).show()
            }
        }
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_search,
                R.id.navigation_information,
                R.id.navigation_settings,
            )
        )
        navView.setupWithNavController(navController)
        settinginit()
    }

    fun showModal(){
        modalBottomSheet.show(supportFragmentManager, modalBottomSheet.tag)
    }

    private fun setStatusBarTransparent() {
        if (Build.VERSION.SDK_INT >= 19) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            if (Build.VERSION.SDK_INT < 21) {
                setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true)
            } else {
                setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
                window.statusBarColor = Color.TRANSPARENT
            }
        }
    }

    private fun setWindowFlag(bits: Int, on: Boolean) {
        val winAttr = window.attributes
        winAttr.flags = if (on) winAttr.flags or bits else winAttr.flags and bits.inv()
        window.attributes = winAttr
    }

    private fun settinginit() {
        val navController=findNavController(R.id.nav_host_fragment_activity_main)
        val check=intent.getIntExtra("settings",-1)
        if(check==0){
            navController.navigate(R.id.navigation_settings)
        }
    }

}