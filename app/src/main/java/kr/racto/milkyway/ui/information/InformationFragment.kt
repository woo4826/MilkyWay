package kr.racto.milkyway.ui.information

import android.R
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kr.racto.milkyway.databinding.FragmentInformationBinding
import org.jsoup.Jsoup

class InformationFragment : Fragment() {

    private var _binding: FragmentInformationBinding? = null
    val dataUrl = "https://sooyusil.com/home/27.htm#none"
    val scope = CoroutineScope(Dispatchers.IO)
    var qList = ArrayList<String>()
    var aList = ArrayList<String>()

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

        // recyclerview가 어떤 형태로 관리될 것인지 설정
        binding.recyclerview.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        // RecyclerView.Adapter형식으로 커스텀된 MyAdapter 선언하고(인자로 배열) 달아주기
        val adapter = MyAdapter(InfoList)
        binding.recyclerview.adapter = adapter

        scope.launch {
            val doc = Jsoup.connect(dataUrl).get()
            val elements1 = doc.select("a.faq_question")
            val elements2 = doc.select("div.faq_answer")

            for (element in elements1){
                qList.add(element.ownText().trim())
            }
            for (element in elements2) {
                val answerTextNodes = element.select("span").first()?.textNodes()

                val extractedText = StringBuilder()
                if (answerTextNodes != null) {
                    var i = 0
                    for (textNode in answerTextNodes) {
                        if(textNode.text() == " ")
                            i++
                        if(i <= 5)
                            continue
                        Log.i("text",textNode.text())
                        extractedText.append(textNode.text())
                    }
                }
                aList.add(extractedText.toString().trim())
            }

            for (i in 0 until  qList.size) {
                InfoList.add(Info(qList[i],aList[i]))
            }
            withContext(Dispatchers.Main) { //Main으로 변경
                adapter.notifyDataSetChanged()
            }
        }

//        InfoList.add(Info("자주묻는질문1", "답변1"))
//        InfoList.add(Info("자주묻는질문2", "답변2"))
//        InfoList.add(Info("자주묻는질문3", "답변3"))
//        InfoList.add(Info("자주묻는질문4", "답변4"))
//        InfoList.add(Info("자주묻는질문5", "답변5"))
//        InfoList.add(Info("자주묻는질문6", "답변6"))
//        InfoList.add(Info("자주묻는질문7", "답변7"))
//        InfoList.add(Info("자주묻는질문8", "답변8"))
//        InfoList.add(Info("자주묻는질문9", "답변9"))
//        InfoList.add(Info("자주묻는질문10", "답변10"))


        adapter.itemClickListener = object:MyAdapter.OnItemClickListener{
            override fun OnItemClick(data: Info, position: Int) {
                if(data.checked){
                    data.checked = false
                    trueIndex = -1
                }
                else{
                    if(trueIndex != -1) {
                        InfoList[trueIndex].checked = false
                        adapter.notifyItemChanged(trueIndex)
                    }
                    data.checked = true
                    trueIndex = position
                }
                adapter.notifyItemChanged(position)
            }

        }


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}