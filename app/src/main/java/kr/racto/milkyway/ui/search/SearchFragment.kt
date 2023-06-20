package kr.racto.milkyway.ui.search

import APIS
import NursingRoomDTO
import SearchReqDTO
import SearchResDTO
import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.webkit.WebView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kr.racto.milkyway.databinding.FragmentSearchBinding
import kr.racto.milkyway.ui.MyViewModel
import kr.racto.milkyway.ui.detail.RoomDetailActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URISyntaxException
import java.net.URLEncoder


class SearchFragment : Fragment() {

    var binding: FragmentSearchBinding? = null
    val model: MyViewModel by activityViewModels()
    lateinit var searchAdapter: SearchAdapter
    val api = APIS.create()
    var roomList: MutableList<NursingRoomDTO?> = mutableListOf()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    var slat: Double? = null
    var slng: Double? = null
    private var isLoading = false
    private var page = 1       // 현재 페이지

    var searchKeyword: String? = null
//    var searchKeyword: String? = "광진"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(layoutInflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
        init()
        val editTextSearch = binding!!.searchEditText
        editTextSearch.requestFocus()
        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editTextSearch, InputMethodManager.SHOW_IMPLICIT)
        editTextSearch.setOnKeyListener { v, keyCode, event ->
            when (keyCode) {
                KeyEvent.KEYCODE_ENTER -> search()
            }
            false
        }
    }

    override fun onDestroyView() {

        super.onDestroyView()
        binding = null
    }

    fun init() {
        binding!!.rvMainBottomSheet.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding!!.rvMainBottomSheet.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                LinearLayoutManager.VERTICAL
            )
        )
        searchAdapter = SearchAdapter(roomList)

        binding!!.searchButton.setOnClickListener {
            search()
        }

        binding!!.rvMainBottomSheet.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = binding!!.rvMainBottomSheet.layoutManager

                if (!isLoading) {
                    if((recyclerView.layoutManager as LinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition() == searchAdapter.items.size - 1){
                        page++
                        isLoading=true
                        searchLocation()
                    }
                }
            }
        })

        searchAdapter.itemClickListener = object : SearchAdapter.OnItemClickListener {
            override fun OnItemClick(position: Int) {
                /**
                 * 상세 페이지로 넘어가게 하기.
                 */
                val roomDictionary = HashMap<String, String>()
                val nursingRoomItem = searchAdapter.items[position]
                val id = nursingRoomItem!!.roomNo
                val name =nursingRoomItem!!.roomName
                val address = nursingRoomItem!!.address
                val callnumber = nursingRoomItem!!.managerTelNo
                if(id != null){
                    roomDictionary["roomId"] = id
                }
                if(name != null){
                    roomDictionary["roomName"] = name
                }
                if(address != null){
                    roomDictionary["address"] = address
                }
                if(callnumber != null){
                    roomDictionary["managerTelNo"] = callnumber
                }
                val i = Intent(activity, RoomDetailActivity::class.java)
                val bundle = Bundle()
                bundle.putSerializable("dictionary", roomDictionary)
                i.putExtras(bundle)
                startActivity(i)
            }

            override fun OnButtonClick(position: Int) {
                if (ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return
                }

                fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                    if (location != null) {

                        val searchData = searchAdapter.items[position]

                        openNaverMaps(
                            requireContext(),
                            "walk",
                            location.latitude,
                            location.longitude,
                            "내위치",
                            searchData!!.gpsLat!!,
                            searchData.gpsLong!!,
                            searchData.roomName!!
                        )
                    } else {
                        // 현재 위치를 가져올 수 없는 경우
                    }
                }
                // 목적지 데이터

            }
        }
        binding!!.rvMainBottomSheet.adapter = searchAdapter
    }

    fun searchLoadInit() {
        val req = SearchReqDTO(
            searchKeyword = searchKeyword!!,
            roomTypeCode = "",
            mylat = "",
            mylng = "",
            pageNo = page.toString()
        )
        val handler = android.os.Handler()
        handler.postDelayed({
            api.roomListByLatLon(req).enqueue(object : Callback<SearchResDTO> {
                override fun onResponse(
                    call: Call<SearchResDTO>,
                    response: Response<SearchResDTO>
                ) {
                    Log.d("log", response.toString())
                    Log.d("log", response.body().toString())
                    if (response.body() != null && response.body()!!.nursingRoomDTO.isNotEmpty()) {

                        searchAdapter.items.addAll(response.body()!!.nursingRoomDTO)
                        searchAdapter.notifyDataSetChanged()

                    }
                }

                override fun onFailure(call: Call<SearchResDTO>, t: Throwable) {
                    // 실패
                    Log.d("log", t.message.toString())
                    Log.d("log", "fail")
                }
            })
        }, 1000)
    }

    fun search() {
        searchKeyword = binding!!.searchEditText.text.toString()
        page=1
        searchAdapter.items.clear()
        isLoading = false
        searchLoadInit()

        if((binding!!.rvMainBottomSheet.layoutManager as LinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition() == searchAdapter.items.size - 1){
            isLoading=true
        }
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val currentFocusView = requireActivity().currentFocus
        if (currentFocusView != null) {
            imm.hideSoftInputFromWindow(currentFocusView.windowToken, 0)
        }
    }

    fun searchLocation() {
        val handler = android.os.Handler()
        if(isLoading){
            handler.postDelayed({
                searchAdapter.items.add(null)
                val itemsSize = searchAdapter.items.size
                searchAdapter.notifyItemInserted(itemsSize-1)
            },16)
        }

//        searchAdapter.items.add(null)
//        val itemsSize = searchAdapter.items.size
//        searchAdapter.notifyItemInserted(itemsSize-1)

        val req = SearchReqDTO(
            searchKeyword = searchKeyword!!,
            roomTypeCode = "",
            mylat = "",
            mylng = "",
            pageNo = page.toString()
        )
        handler.postDelayed({
            api.roomListByLatLon(req).enqueue(object : Callback<SearchResDTO> {
                override fun onResponse(
                    call: Call<SearchResDTO>,
                    response: Response<SearchResDTO>
                ) {
                    Log.d("log", response.toString())
                    Log.d("log", response.body().toString())
                    if (response.body() != null && response.body()!!.nursingRoomDTO.isNotEmpty()) {
                        var itemSize= searchAdapter.items.size
                        searchAdapter.items.removeAt(itemSize-1)
                        itemSize=searchAdapter.items.size
                        searchAdapter.notifyItemRemoved(itemSize)
                        searchAdapter.items.addAll(response.body()!!.nursingRoomDTO)
                        searchAdapter.notifyDataSetChanged()

                    }else{
                        var itemSize= searchAdapter.items.size
                        searchAdapter.items.removeAt(itemSize-1)
                        itemSize=searchAdapter.items.size
                        searchAdapter.notifyItemRemoved(itemSize)
                    }
                    isLoading=false
                }

                override fun onFailure(call: Call<SearchResDTO>, t: Throwable) {
                    // 실패
                    Log.d("log", t.message.toString())
                    Log.d("log", "fail")
                }
            })
        }, 2000)

    }

    fun openNaverMaps(
        context: Context,
        method: String = "bicycle",
        slat: Double,
        slng: Double,
        sname: String,
        dlat: String,
        dlng: String,
        dname: String
    ) {
        val packageName = "kr.racto.milkyway"
        val str_encode = URLEncoder.encode(dname, "UTF-8")
        var url ="nmap://route/$method?dlat=$dlat&dlng=$dlng&dname=$str_encode&appname=$packageName"
//        var url="nmap://route/$method?slat=$slat&slng=$slng&sname=$sname&dlat=$dlat&dlng=$dlng&dname=$str_encode&appname=$packageName"
        val intent = Intent(Intent.ACTION_VIEW,Uri.parse(url))

        val list: List<ResolveInfo> =
            context.packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
        if (list == null || list.isEmpty()) {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=com.nhn.android.nmap")
                )
            )
        } else {
            context.startActivity(intent)
        }
    }

    fun shouldOverrideUrlLoading(view: WebView?, url: String): Boolean {
        if (url.startsWith("intent:")) {
            val intent: Intent
            intent = try {
                Intent.parseUri(url, Intent.URI_INTENT_SCHEME)
            } catch (e: URISyntaxException) {
                return false
            }
            if (TextUtils.isEmpty(intent.getPackage())) {
                return false
            }
            val list: List<ResolveInfo> =
                context?.packageManager!!.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
            if (list == null || list.isEmpty()) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=" + intent.getPackage())
                    )
                )
            } else {
                startActivity(intent)
            }
            return true
        }
        return false
    }



}