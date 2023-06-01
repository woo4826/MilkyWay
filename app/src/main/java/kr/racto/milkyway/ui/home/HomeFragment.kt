package kr.racto.milkyway.ui.home

import APIS
import NursingRoomDTO
import SearchReqDTO
import SearchResDTO
import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.UiThread
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
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
    var markerList: MutableList<Marker> = mutableListOf()
    var roomList: MutableList<NursingRoomDTO> = mutableListOf()

    //    var selectedMarkerTag: String? = null
    val api = APIS.create()
    private lateinit var fusedLocationClient: FusedLocationProviderClient

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
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
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
            naverMap.locationTrackingMode = LocationTrackingMode.Follow

            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    fun setMarkers(lat: Double, lon: Double) {

        val req = SearchReqDTO(
            searchKeyword = "(내주변검색)",
            roomTypeCode = "",
            mylat = lat.toString(),
            mylng = lon.toString(),
            pageNo = "1"
        )
        api.roomListByLatLon(req).enqueue(object : Callback<SearchResDTO> {
            override fun onResponse(call: Call<SearchResDTO>, response: Response<SearchResDTO>) {
                Log.d("log", response.toString())
                Log.d("log", response.body().toString())
                if (response.body() != null && response.body()!!.nursingRoomDTO.isNotEmpty()) {
                    markerList.onEach { e ->
                        e.map = null
                    }
                    markerList.clear()
                    roomList.clear()
                    markerList.addAll(response.body()!!.nursingRoomDTO.map { item
                        ->
                        Marker(LatLng(item.gpsLat!!.toDouble(), item.gpsLong!!.toDouble()))

                    }.toList())
                    roomList.addAll(response.body()!!.nursingRoomDTO)
                    if (markerList.isEmpty())
                        return
                    for (i in 0 until markerList.size) {
                        markerList[i].apply {
                            icon = OverlayImage.fromResource(R.drawable.marker_icon)
                            tag = roomList[i].roomNo ?: ""
                            captionMinZoom = 15.0
                            captionMaxZoom = 21.0
                            captionText = roomList[i].roomName ?: ""
                            captionRequestedWidth = 150
                            if (roomList[i].roomName != roomList[i].location) {
                                subCaptionText = roomList[i].location ?: ""
                                subCaptionTextSize = 10f

                            }
                            map = naverMap
                        }

                    }
                    markerList.onEach { marker ->
                        marker.setOnClickListener { overlay ->
                            markerClickListener(marker)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<SearchResDTO>, t: Throwable) {
                // 실패
                Log.d("log", t.message.toString())
                Log.d("log", "fail")
            }
        })

    }

    fun markerClickListener(marker: Marker): Boolean {
        val cameraUpdate = CameraUpdate.scrollAndZoomTo(
            LatLng(
                marker.position.latitude,
                marker.position.longitude
            ), 15.0
        ).animate(CameraAnimation.Easing, 360)

        naverMap.moveCamera(cameraUpdate)
//        if (selectedMarkerTag != null && marker.tag == selectedMarkerTag) {
//            //open room detail fragment
//            Toast.makeText(requireContext(), "두번클릭", Toast.LENGTH_SHORT)
//        }

        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    @UiThread
    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap
        // 내장 위치 추적 기능 사용
        naverMap.locationSource = locationSource
        naverMap.addOnCameraChangeListener { reason, animated ->
//            Log.i("NaverMap", "카메라 변경 - reson: $reason, animated: $animated")

            // 주소 텍스트 세팅 및 확인 버튼 비활성화
//            binding.tvLocation.run {
//                text = "위치 이동 중"
//                setTextColor(Color.parseColor("#c4c4c4"))
//            }

        }
//        naverMap.setOnMapClickListener { _1, _2 -> this@HomeFragment.selectedMarkerTag = null }
        

        naverMap.addOnCameraIdleListener {
            setMarkers(
                naverMap.cameraPosition.target.latitude,
                naverMap.cameraPosition.target.longitude
            )
//            // 좌표 -> 주소 변환 텍스트 세팅, 버튼 활성화
//            binding.tvLocation.run {
//                text = getAddress(
//                    naverMap.cameraPosition.target.latitude,
//                    naverMap.cameraPosition.target.longitude
//                )
//                setTextColor(Color.parseColor("#2d2d2d"))
//            }
//            binding.btnConfirm.run {
//                setBackgroundResource(R.drawable.rect_round_ffd464_radius_8)
//                setTextColor(Color.parseColor("#FF000000"))
//                isEnabled = true
//            }
        }

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        // 사용자 현재 위치 받아오기
        var currentLocation: Location?
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                currentLocation = location
                // 위치 오버레이의 가시성은 기본적으로 false로 지정되어 있습니다. 가시성을 true로 변경하면 지도에 위치 오버레이가 나타납니다.
                // 파랑색 점, 현재 위치 표시
                naverMap.locationOverlay.run {
                    isVisible = true
                    position = LatLng(currentLocation!!.latitude, currentLocation!!.longitude)
                }

                // 카메라 현재위치로 이동
                val cameraUpdate = CameraUpdate.scrollTo(
                    LatLng(
                        currentLocation!!.latitude,
                        currentLocation!!.longitude
                    )
                )
                naverMap.moveCamera(cameraUpdate)


            }
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