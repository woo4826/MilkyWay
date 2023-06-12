package kr.racto.milkyway

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import kr.racto.milkyway.databinding.DialogLoadingBinding


class LoadingDialog(context:Context) : Dialog(context){

    private lateinit var binding: DialogLoadingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 만들어놓은 dialog_profile.xml 뷰를 띄운다.
        binding = DialogLoadingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
    }

    private fun initViews() = with(binding) {
        // 뒤로가기 버튼, 빈 화면 터치를 통해 dialog가 사라지지 않도록
        setCancelable(false)

        // background를 투명하게 만듦
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
}