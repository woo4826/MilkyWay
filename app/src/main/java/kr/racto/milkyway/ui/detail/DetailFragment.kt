package kr.racto.milkyway.ui.detail

import android.app.Dialog
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kr.racto.milkyway.R
import kr.racto.milkyway.databinding.FragmentDetailBinding
import kr.racto.milkyway.ui.MyViewModel


class DetailFragment : BottomSheetDialogFragment() {

    var binding: FragmentDetailBinding?=null
    val model: MyViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDetailBinding.inflate(layoutInflater,container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val room = model.selectedRoom
        val roomDictionary = HashMap<String, String>()
        val id = room.value?.roomNo
        val name = room.value?.roomName
        val address = room.value?.address
        val callnumber = room.value?.managerTelNo
        if(id != null){
            roomDictionary["roomId"] = id
        }
        if(name != null){
            binding!!.roomName.text = name
            roomDictionary["roomName"] = name
        }
        if(address != null){
            binding!!.roomAddress.text = address
            roomDictionary["address"] = address
        }
        if(callnumber != null){
            roomDictionary["managerTelNo"] = callnumber
        }
        binding!!.btnMove.setOnClickListener {
            val intent = Intent(activity, RoomDetailActivity::class.java)
            val bundle = Bundle()
            bundle.putSerializable("dictionary", roomDictionary)
            intent.putExtras(bundle)
            startActivity(intent)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        setStyle(STYLE_NO_TITLE, R.style.AppBottomSheetDialogTheme)
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()

        val metrics = Resources.getSystem().displayMetrics //디바이스 크기 얻기
        val width = 1000
        //디바이스 width가 500(원하는 넓이)보다 작으면 디바이스 width 만큼
        //아니면, 500(원하는)크기 만큼으로 제한
        val height = -1 // MATCH_PARENT
        dialog!!.window!!.setLayout(width, height)
    }

}