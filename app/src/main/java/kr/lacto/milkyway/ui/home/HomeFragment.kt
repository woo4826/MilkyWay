package kr.lacto.milkyway.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.UiThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay

import kr.lacto.milkyway.R
import kr.lacto.milkyway.databinding.FragmentHomeBinding

class HomeFragment : Fragment() , OnMapReadyCallback{

    private var _binding: FragmentHomeBinding? = null
    private lateinit var naverMap : NaverMap
    private lateinit var mapView: MapView

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

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        mapView = view.findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)

    }
    fun initMap(){
        val marker = Marker()
        marker.position = LatLng(37.5670135, 126.9783740)
        marker.map =  naverMap
        val infoWindow  = InfoWindow()
        infoWindow.position = LatLng(37.5666102, 126.9783881)
        infoWindow.map = naverMap
//       naverMap.setOnMapClickListener(
//
//       )
// 마커를 클릭하면:
        val listener = Overlay.OnClickListener { overlay ->
            val marker = overlay as Marker

            if (marker.infoWindow == null) {
                // 현재 마커에 정보 창이 열려있지 않을 경우 엶
                infoWindow.open(marker)
            } else {
                // 이미 현재 마커에 정보 창이 열려있을 경우 닫음
                infoWindow.close()
            }

            true
        }

        marker.onClickListener = listener

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    @UiThread
    override fun onMapReady(map: NaverMap) {
        naverMap = map
        // 확대 축소
        naverMap.maxZoom = 18.0
        naverMap.minZoom = 10.0

        // 지도 옵션 설정
        naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_TRANSIT, true)
        naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_BUILDING, true)
        naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_TRAFFIC, true)
        naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_TRANSIT, true)
        naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_TRANSIT, true)

        // 초기 위치 설정
        val cameraUpdate = CameraUpdate.scrollTo((LatLng(37.4979921,127.028046))).animate(CameraAnimation.Easing)
        naverMap.moveCamera(cameraUpdate)

        // 현위치 받아오기
        val uiSettings = naverMap.uiSettings
        uiSettings.isLocationButtonEnabled = false

        // 로케이션 버튼 재할당
//        locationButton.map = naverMap

        naverMap.locationTrackingMode = LocationTrackingMode.Face

        // 현재 위치 설정
//        locationSource = FusedLocationSource(this, CURRENT_LOCATION_CODE)
//        naverMap.locationSource = locationSource

        // Marker
        /*val marker = Marker()
        marker.position = LatLng(37.6154444,127.0341968)
        marker.map = naverMap
        marker.icon = MarkerIcons.BLACK
        marker.iconTintColor = Color.RED*/

        initMap()
    }
}