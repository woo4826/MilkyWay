package kr.racto.milkyway.ui.information

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kr.racto.milkyway.databinding.FragmentInformationBinding

class InformationFragment : Fragment() {

    private var _binding: FragmentInformationBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    var InfoList:ArrayList<Info> = ArrayList()
    var trueIndex = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(InformationViewModel::class.java)

        _binding = FragmentInformationBinding.inflate(inflater, container, false)
        val root: View = binding.root

        InfoList.add(Info("자주묻는질문1", "답변1"))
        InfoList.add(Info("자주묻는질문2", "답변2"))
        InfoList.add(Info("자주묻는질문3", "답변3"))
        InfoList.add(Info("자주묻는질문4", "답변4"))
        InfoList.add(Info("자주묻는질문5", "답변5"))
        InfoList.add(Info("자주묻는질문6", "답변6"))
        InfoList.add(Info("자주묻는질문7", "답변7"))
        InfoList.add(Info("자주묻는질문8", "답변8"))
        InfoList.add(Info("자주묻는질문9", "답변9"))
        InfoList.add(Info("자주묻는질문10", "답변10"))

        // recyclerview가 어떤 형태로 관리될 것인지 설정
        binding.recyclerview.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        // RecyclerView.Adapter형식으로 커스텀된 MyAdapter 선언하고(인자로 배열) 달아주기
        val adapter = MyAdapter(InfoList)
        binding.recyclerview.adapter = adapter

        adapter.itemClickListener = object:MyAdapter.OnItemClickListener{
            override fun OnItemClick(data: Info, position: Int) {
                if(data.checked){
                    data.checked = false
                    trueIndex = -1
                }
                else{
                    if(trueIndex != -1) {
                        InfoList[trueIndex].checked = false
                    }
                    data.checked = true
                    trueIndex = position
                }
                adapter.notifyDataSetChanged()
            }

        }


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}