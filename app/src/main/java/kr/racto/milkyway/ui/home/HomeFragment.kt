package kr.racto.milkyway.ui.home

import APIS
import SearchReqDTO
import SearchResDTO
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.UiThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.util.FusedLocationSource
import kr.racto.milkyway.R
import kr.racto.milkyway.databinding.FragmentHomeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var naverMap: NaverMap
    private lateinit var mapView: MapView
    private lateinit var locationSource: FusedLocationSource
    private val LOCATION_PERMISSION_REQUEST_CODE = 1000
    val api = APIS.create()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView = view.findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (locationSource.onRequestPermissionsResult(
                requestCode, permissions,
                grantResults
            )
        ) {
            if (!locationSource.isActivated) { // 권한 거부됨
                naverMap.locationTrackingMode = LocationTrackingMode.None
            }
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    fun initMap() {
        val req = SearchReqDTO(
            searchKeyword = "광진",
            roomTypeCode = "",
            mylat = "",
            mylng = "",
            pageNo = "1"
        )
        api.roomListByLatLon(req).enqueue(object : Callback<SearchResDTO> {
            override fun onResponse(call: Call<SearchResDTO>, response: Response<SearchResDTO>) {
                Log.d("log", response.toString())
                Log.d("log", response.body().toString())
                response.body()
//                for (i in response.) {
//
//                    val marker = Marker(LatLng(i.lat!!, i.lon!!))
//                    marker.map = naverMap
//
//                }
            }

            override fun onFailure(call: Call<SearchResDTO>, t: Throwable) {
                // 실패
                Log.d("log", t.message.toString())
                Log.d("log", "fail")
            }
        })

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    @UiThread
    override fun onMapReady(map: NaverMap) {
        naverMap = map
        naverMap.locationSource = locationSource
        val uiSettings = naverMap.uiSettings
        uiSettings.isLocationButtonEnabled = true
        initMap()
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
}