package kr.lacto.milkyway.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.UiThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import kr.lacto.milkyway.R
import kr.lacto.milkyway.databinding.FragmentHomeBinding


class HomeFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var naverMap: NaverMap
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
        mapView.getMapAsync(this)


    }

    fun initMap() {
        val marker = Marker()
        var markerList = ArrayList<Marker>()

        markerList.add(Marker(LatLng(37.5670135, 126.9783740)))
        markerList.add(Marker(LatLng(37.5671135, 126.9783740)))
        markerList.add(Marker(LatLng(37.5672135, 126.9783740)))
        markerList.add(Marker(LatLng(37.5673135, 126.9783740)))
        for (i in markerList)
            i.map = naverMap
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

    override fun onPause() {
        super.onPause()
//        mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
//        mapView.onStop()
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