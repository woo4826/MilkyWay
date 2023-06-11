package kr.racto.milkyway.ui.home

import APIS
import NursingRoomDTO
import SearchReqDTO
import SearchResDTO
import android.Manifest
import android.content.Intent
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
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kr.racto.milkyway.MainActivity
import kr.racto.milkyway.R
import kr.racto.milkyway.databinding.FragmentHomeBinding
import kr.racto.milkyway.ui.MyViewModel
import kr.racto.milkyway.ui.RoomDetailActivity
import org.jsoup.Jsoup
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.*


class HomeFragment : Fragment(), OnMapReadyCallback {
    private val binding get() = _binding!!
    val scope = CoroutineScope(Dispatchers.IO)
    val model: MyViewModel by activityViewModels()

    private var _binding: FragmentHomeBinding? = null
    private lateinit var naverMap: NaverMap
    private lateinit var mapView: MapView
    private lateinit var locationSource: FusedLocationSource
    private val LOCATION_PERMISSION_REQUEST_CODE = 1000
    var markerList: MutableList<Marker> = mutableListOf()
    var roomList: MutableList<NursingRoomDTO> = mutableListOf()


    val api = APIS.create()
    private lateinit var fusedLocationClient: FusedLocationProviderClient

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
        setBottomInfo(View.INVISIBLE)
        binding.btnMove.run {
            setAllowClickWhenDisabled(false)
            isEnabled = false
        }
        binding.fabTracking.setOnClickListener {
            cameraMoveTo()
        }
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

    private val earthRadius = 6372.8 * 1000

    private var beforeLat: Double? = null
    private var beforeLon: Double? = null
    fun needToFetch(currentLat: Double, currentLon: Double): Boolean {
        if (beforeLat == null || beforeLon == null) return true

        return (earthRadius * getDistance(
            beforeLat!!,
            beforeLon!!,
            currentLat,
            currentLon
        )).toInt() > 1000
    }

    fun getDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Int {
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a =
            sin(dLat / 2).pow(2.0) + sin(dLon / 2).pow(2.0) * cos(Math.toRadians(lat2)) * cos(
                Math.toRadians(lat1)
            )
        val c = 2 * asin(sqrt(a))
        return (earthRadius * c).toInt()
    }

    private fun setMarkers(lat: Double, lon: Double) {
        if (!needToFetch(lat, lon)) return
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
                beforeLat = lat
                beforeLon = lon
            }

            override fun onFailure(call: Call<SearchResDTO>, t: Throwable) {
                // 실패
                Log.d("log", t.message.toString())
                Log.d("log", "fail")
            }
        })

    }

    var isImgLoading: Boolean = false
    fun getRoomMainImgUrl(roomId: String) {
        scope.launch {
            withContext(Dispatchers.Main) {
                isImgLoading = true

                binding.run {
//                    mainImg.set
                }
            }
            val imgParseUrl = "https://sooyusil.com/home/42.htm?roomNo=${roomId}"
            val doc = Jsoup.connect(imgParseUrl).get()
            val mainUrlSelector = "#displayImg"
            val urlList: MutableList<String> = mutableListOf()
            try {
                urlList.add("https://sooyusil.com" + doc.select(mainUrlSelector)[0].attributes()["src"])
            } catch (e: IndexOutOfBoundsException) {

            }
            val subUrlSelector = ".setImg"
            urlList.clear()
            try {
                urlList.addAll(
                    doc.select(subUrlSelector)
                        .map { item -> "https://sooyusil.com" + item.attributes()["src"] }.toList()
                )
            } catch (e: IndexOutOfBoundsException) {
            }
            withContext(Dispatchers.Main) {
                isImgLoading = false
                binding.run {
                    mainImg.load(urlList[0]) {
                        crossfade(true)
                        placeholder(R.drawable.testimg)
                    }
                    if (urlList.size > 1) {
                        subImg1.load(urlList[1]) {
                            crossfade(true)
                            placeholder(R.drawable.testimg)
                        }
                    }
                    if (urlList.size > 2) {
                        subImg2.load(urlList[2]) {
                            crossfade(true)
                            placeholder(R.drawable.testimg)
                        }
                    }
                    if (urlList.size > 3) {
                        subImg3.load(urlList[3]) {
                            crossfade(true)
                            placeholder(R.drawable.testimg)
                        }
                    }
                    detailRow1.visibility = View.VISIBLE

                }
            }
        }


    }

    fun markerClickListener(marker: Marker): Boolean {

        val cameraUpdate = CameraUpdate.scrollAndZoomTo(
            LatLng(
                marker.position.latitude,
                marker.position.longitude
            ), 15.0
        ).animate(CameraAnimation.Easing, 360)
        naverMap.moveCamera(cameraUpdate)
        val roomId = marker.tag.toString()
        val room = getRoomByRoomId(roomId)
        getRoomMainImgUrl(roomId)

        if (room != null) {
            model.setLiveData(room)
            (activity as MainActivity).showModal()
        }

        if (room != null) {
            setBottomInfo(View.VISIBLE, room, marker)
        } else {
            setBottomInfo(View.INVISIBLE)
        }
        return true
    }


    fun setBottomInfo(state: Int, room: NursingRoomDTO? = null, marker: Marker? = null) {
        if (room == null || state == View.INVISIBLE) {

            binding.run {
                btnMove.isEnabled = false
                binding.detailRow1.visibility = View.INVISIBLE
                binding.detailRow2.visibility = View.INVISIBLE
                binding.btnMove.visibility = View.INVISIBLE
                btnMove.setOnClickListener {
                }
//                roomInfo.text = ""
            }
        } else {
            if (marker != null)
                getCurrentLocation(marker)
            binding.run {
                roomInfo.text = room.roomName + "\n" + room.location
                btnMove.isEnabled = true
                binding.detailRow2.visibility = View.VISIBLE
                binding.btnMove.visibility = View.VISIBLE
                btnMove.setOnClickListener {
                    //move to detail page
                    val i  = Intent(activity, RoomDetailActivity::class.java)
                }
            }
        }
    }

    fun getCurrentLocation(marker: Marker) {
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
        var currentLocation: Location?

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                currentLocation = location
                // 파랑색 점, 현재 위치 표시
                naverMap.locationOverlay.run {
                    isVisible = true
                    position = LatLng(currentLocation!!.latitude, currentLocation!!.longitude)
                }
                binding.roomDistanceInfo.run {
                    text = "직선거리: ${
                        getDistance(
                            marker.position.longitude,
                            marker.position.latitude,
                            currentLocation!!.longitude,
                            currentLocation!!.latitude
                        )
                    }m"
                    visibility = View.VISIBLE
                }


            }
    }

    fun getRoomByRoomId(roomId: String): NursingRoomDTO? {
        val res = roomList.find { e -> e.roomNo === roomId }
        if (res != null) {
            return res
        }
        return null
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
            if (reason != 0) //아래의 cameraIdleListener 에서의 움직임을 비활성화
                setBottomInfo(View.INVISIBLE)


        }
        naverMap.setOnMapClickListener { _1, _2 ->
            setBottomInfo(View.INVISIBLE)
        }


        naverMap.addOnCameraIdleListener {
            setMarkers(
                naverMap.cameraPosition.target.latitude,
                naverMap.cameraPosition.target.longitude
            )
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

    fun cameraMoveTo() {

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
        var currentLocation: Location?
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                currentLocation = location
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