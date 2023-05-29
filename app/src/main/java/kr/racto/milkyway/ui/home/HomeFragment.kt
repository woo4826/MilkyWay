package kr.racto.milkyway.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.UiThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.FusedLocationSource
import kr.racto.milkyway.R
import kr.racto.milkyway.RoomModel
import kr.racto.milkyway.databinding.FragmentHomeBinding


class HomeFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var naverMap: NaverMap
    private lateinit var mapView: MapView
    private lateinit var locationSource: FusedLocationSource
    private val LOCATION_PERMISSION_REQUEST_CODE = 1000

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

    fun dummyData(): ArrayList<RoomModel> {
        var roomList = ArrayList<RoomModel>()
        roomList.add(
            RoomModel(
                roomName = "1번 수유실",
                lat = 37.5670135,
                lon = 126.9783740,
                address = "서울시 광진구 아차산로",
                detailAddress = "3층",
                reviews = ArrayList<String>()
            )
        )
        return roomList

    }

    fun initMap() {
        var roomList = dummyData()
        for (i in roomList) {

            val marker = Marker(LatLng(i.lat!!, i.lon!!))
            marker.map = naverMap
//            marker.setOnClickListener {
//
//            }
        }


//            i.map = naverMap
//        val infoWindow = InfoWindow()
//        infoWindow.position = LatLng(37.5666102, 126.9783881)
//        infoWindow.map = naverMap
//       naverMap.setOnMapClickListener(
//
//       )
//// 마커를 클릭하면:
//        val listener = Overlay.OnClickListener { overlay ->
//            val marker = overlay as Marker
//
//            if (marker.infoWindow == null) {
//                // 현재 마커에 정보 창이 열려있지 않을 경우 엶
//                infoWindow.open(marker)
//            } else {
//                // 이미 현재 마커에 정보 창이 열려있을 경우 닫음
//                infoWindow.close()
//            }
//
//            true
//        }

//        marker.onClickListener = listener

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