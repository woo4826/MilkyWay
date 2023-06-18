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
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchFragment : Fragment() {

    var binding: FragmentSearchBinding? = null
    val model: MyViewModel by activityViewModels()
    lateinit var searchAdapter: SearchAdapter
    val api = APIS.create()
    var roomList: MutableList<NursingRoomDTO?> = mutableListOf()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    var slat: Double? =null
    var slng : Double? =null
    var searchKeyword : String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(layoutInflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    override fun onDestroyView() {

        super.onDestroyView()
        binding=null
    }

    fun init() {
        binding!!.rvMainBottomSheet.layoutManager=
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding!!.rvMainBottomSheet.addItemDecoration(
            DividerItemDecoration(requireContext(),
                LinearLayoutManager.VERTICAL)
        )
        searchAdapter = SearchAdapter(roomList)

        binding!!.searchButton.setOnClickListener {
            searchKeyword=binding!!.searchEditText.text.toString()
            searchLoadInit()
        }

        binding!!.rvMainBottomSheet.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = binding!!.rvMainBottomSheet.layoutManager

                if (searchKeyword !=null) {
                    val lastVisibleItem = (layoutManager as LinearLayoutManager)
                        .findLastCompletelyVisibleItemPosition()

                    // 마지막으로 보여진 아이템 position 이
                    // 전체 아이템 개수보다 5개 모자란 경우, 데이터를 loadMore 한다
                    if (layoutManager.itemCount <= lastVisibleItem + 5) {
                        page++
                        searchLocation()
                    }
                }
            }
        })

        searchAdapter.itemClickListener = object:SearchAdapter.OnItemClickListener{
            override fun OnItemClick(position: Int){
                /**
                 * 상세 페이지로 넘어가게 하기.
                 */
//                val intent = searchAdapter.items[position]
//                startActivity(intent)
            }

            override fun OnButtonClick(position: Int) {
                /**
                 * 길찾기로 인텐트.
                 */
                // 현재위치 데이터

                fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

                if (ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return
                }
                fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                    if(location!=null){
                        slat=location.latitude
                        slng=location.longitude
                    } else {
                        // 현재 위치를 가져올 수 없는 경우
                    }
                }.addOnFailureListener { exception: Exception ->
                        // 위치 정보를 가져오는 데 실패한 경우
                    }
                // 목적지 데이터
                val searchData = searchAdapter.items[position]

                openNaverMaps(requireContext(),"walk",slat!!,slng!!,"내위치",searchData!!.gpsLat!!,searchData!!.gpsLong!!,searchData!!.roomName!!)
            }
        }
        binding!!.rvMainBottomSheet.adapter=searchAdapter
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
                override fun onResponse(call: Call<SearchResDTO>, response: Response<SearchResDTO>) {
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
        },1000)
    }

    fun searchLocation() {
        searchAdapter.setLoadingView(true)

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
                override fun onResponse(call: Call<SearchResDTO>, response: Response<SearchResDTO>) {
                    Log.d("log", response.toString())
                    Log.d("log", response.body().toString())
                    if (response.body() != null && response.body()!!.nursingRoomDTO.isNotEmpty()) {
                        searchAdapter.setLoadingView(false)

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
        },1000)

    }

    fun openNaverMaps(context: Context, method : String ="walk", slat : Double, slng : Double, sname : String, dlat : String, dlng : String, dname : String) {
        val packageName="kr.racto.milkyway"
        val url = "nmap://route/$method?slat=$slat&slng=$slng&sname=$sname&dlat=$dlat&dlng=$dlng&dname=$dname&appname=$packageName"

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        intent.addCategory(Intent.CATEGORY_BROWSABLE)


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



    private var page = 1       // 현재 페이지


}