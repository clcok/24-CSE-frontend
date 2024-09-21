package com.example.parkingmateprac.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.parkingmateprac.R
import com.example.parkingmateprac.model.Item
import com.example.parkingmateprac.repository.location.LocationSearcher
import com.example.parkingmateprac.viewmodel.OnKeywordItemClickListener
import com.example.parkingmateprac.viewmodel.OnSearchItemClickListener
import com.example.parkingmateprac.viewmodel.keyword.KeywordViewModel
import com.example.parkingmateprac.viewmodel.keyword.KeywordViewModelFactory
import com.example.parkingmateprac.viewmodel.main.MainViewModel
import com.example.parkingmateprac.viewmodel.main.MainViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.navigation.NavigationView
import com.kakao.vectormap.*
import com.kakao.vectormap.camera.CameraAnimation
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelLayer
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import com.kakao.vectormap.label.LabelTextStyle

class MainActivity : AppCompatActivity(), OnSearchItemClickListener, OnKeywordItemClickListener {

    private var mapView: MapView? = null
    private var kakaoMap: KakaoMap? = null
    private var labelLayer: LabelLayer? = null

    private lateinit var drawerLayout: DrawerLayout // 네비게이션 드로어
    private lateinit var errorLayout: RelativeLayout
    private lateinit var errorMessage: TextView
    private lateinit var errorDetails: TextView
    private lateinit var retryButton: ImageButton
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<FrameLayout>
    private lateinit var bottomSheetTitle: TextView
    private lateinit var bottomSheetAddress: TextView
    private lateinit var bottomSheetLayout: FrameLayout
    private lateinit var searchResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var locationSearcher: LocationSearcher
    private lateinit var keywordViewModel: KeywordViewModel
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        locationSearcher = LocationSearcher(this)

        // ViewModel 초기화
        keywordViewModel = ViewModelProvider(this, KeywordViewModelFactory(applicationContext)).get(KeywordViewModel::class.java)
        mainViewModel = ViewModelProvider(this, MainViewModelFactory(application)).get(MainViewModel::class.java)

        // DrawerLayout 초기화
        drawerLayout = findViewById(R.id.drawer_layout)
        val menuButton: ImageButton = findViewById(R.id.menu_button)
        menuButton.setOnClickListener {
            // 메뉴 버튼 클릭 시 네비게이션 드로어 열기
            drawerLayout.openDrawer(androidx.core.view.GravityCompat.START)
        }

        // NavigationView에서 메뉴 항목 클릭 리스너 설정
        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_parking_ticket -> {
                    Toast.makeText(this, "주차권 선택", Toast.LENGTH_SHORT).show()
                }
                R.id.nav_coupon -> {
                    Toast.makeText(this, "쿠폰함 선택", Toast.LENGTH_SHORT).show()
                }
                R.id.nav_credit -> {
                    Toast.makeText(this, "충전금 선택", Toast.LENGTH_SHORT).show()
                }
                R.id.nav_points -> {
                    Toast.makeText(this, "적립금 선택", Toast.LENGTH_SHORT).show()
                }
            }
            drawerLayout.closeDrawer(androidx.core.view.GravityCompat.START)
            true
        }

        // ActivityResultLauncher 초기화
        searchResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            handleSearchResult(result.data)
        }

        // MapView 초기화 및 맵 라이프사이클 콜백 설정
        mapView = findViewById(R.id.map_view)
        mapView?.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {
                Log.d(TAG, "Map destroyed")
            }

            override fun onMapError(error: Exception) {
                Log.e(TAG, "Map error", error)
                showErrorScreen(error)
            }
        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(map: KakaoMap) {
                kakaoMap = map
                labelLayer = kakaoMap?.labelManager?.layer
                Log.d(TAG, "Map is ready")
            }
        })

        // 검색창 클릭 시 검색 페이지로 이동
        val searchEditText = findViewById<EditText>(R.id.search_edit_text)
        searchEditText.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            searchResultLauncher.launch(intent)
        }

        // 에러 화면 초기화
        initializeErrorScreen()

        // BottomSheet 초기화
        initializeBottomSheet()

        // Observe the last marker position
        mainViewModel.lastMarkerPosition.observe(this, Observer { item ->
            item?.let {
                Log.d(TAG, "Loaded last marker position: lat=${it.latitude}, lon=${it.longitude}, placeName=${it.place}, roadAddressName=${it.address}")
                addLabel(it)
                val position = LatLng.from(it.latitude, it.longitude)
                moveCamera(position)
                updateBottomSheet(it.place, it.address)
            }
        })
    }

    private fun initializeErrorScreen() {
        errorLayout = findViewById(R.id.error_layout)
        errorMessage = findViewById(R.id.error_message)
        errorDetails = findViewById(R.id.error_details)
        retryButton = findViewById(R.id.retry_button)
        retryButton.setOnClickListener { onRetryButtonClick() }
    }

    private fun initializeBottomSheet() {
        bottomSheetLayout = findViewById(R.id.bottomSheetLayout)
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout)
        bottomSheetTitle = findViewById(R.id.bottomSheetTitle)
        bottomSheetAddress = findViewById(R.id.bottomSheetAddress)

        // Bottom Sheet를 처음에 축소된 상태로 설정
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

        // Bottom Sheet가 완전히 숨겨지지 않도록 설정
        bottomSheetBehavior.isHideable = false

        // Bottom Sheet가 드래그로 확대되면 전체 화면으로 확장되도록 콜백 설정
        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    // Bottom Sheet가 확장되었을 때 동작 (전체 화면으로 확장)
                    val layoutParams = bottomSheetLayout.layoutParams
                    layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
                    bottomSheetLayout.layoutParams = layoutParams
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // 슬라이드 동작 시 처리할 동작 (필요한 경우 추가 가능)
            }
        })
    }

    private fun handleSearchResult(data: Intent?) {
        if (data == null) {
            showToast("검색 결과를 받아오지 못했습니다.")
            return
        }

        val placeName = data.getStringExtra("place_name")
        val roadAddressName = data.getStringExtra("road_address_name")
        val latitude = data.getDoubleExtra("latitude", 0.0)
        val longitude = data.getDoubleExtra("longitude", 0.0)

        if (placeName == null || roadAddressName == null) {
            showToast("검색 결과가 유효하지 않습니다.")
            return
        }

        Log.d(TAG, "Search result: $placeName, $roadAddressName, $latitude, $longitude")

        val item = Item(placeName, roadAddressName, "", latitude, longitude)
        addLabel(item)
        mainViewModel.saveLastMarkerPosition(item)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        mapView?.resume()  // MapView의 resume 호출
        Log.d(TAG, "MapView resumed")
    }

    override fun onPause() {
        super.onPause()
        mapView?.pause()  // MapView의 pause 호출
        Log.d(TAG, "MapView paused")
    }

    fun showErrorScreen(error: Exception) {
        errorLayout.visibility = View.VISIBLE
        errorMessage.text = getString(R.string.map_error_message)
        errorDetails.text = error.message
        mapView?.visibility = View.GONE
    }

    private fun onRetryButtonClick() {
        errorLayout.visibility = View.GONE
        mapView?.visibility = View.VISIBLE
        mapView?.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {
                Log.d(TAG, "Map destroyed on retry")
            }

            override fun onMapError(error: Exception) {
                Log.e(TAG, "Map error on retry", error)
                showErrorScreen(error)
            }
        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(kakaoMap: KakaoMap) {
                this@MainActivity.kakaoMap = kakaoMap
                labelLayer = kakaoMap.labelManager?.layer
                Log.d(TAG, "Map is ready on retry")
            }
        })
    }

    private fun addLabel(item: Item) {
        val placeName = item.place
        val roadAddressName = item.address
        val latitude = item.latitude
        val longitude = item.longitude

        val position = LatLng.from(latitude, longitude)
        val styles = kakaoMap?.labelManager?.addLabelStyles(
            LabelStyles.from(
                LabelStyle.from(R.drawable.ic_pin).setZoomLevel(DEFAULT_ZOOM_LEVEL),
                LabelStyle.from(R.drawable.ic_pin)
                    .setTextStyles(
                        LabelTextStyle.from(this, R.style.labelTextStyle)
                    )
                    .setZoomLevel(DEFAULT_ZOOM_LEVEL)
            )
        )

        labelLayer?.addLabel(
            LabelOptions.from(placeName, position).setStyles(styles).setTexts(placeName)
        )

        moveCamera(position)
        updateBottomSheet(placeName, roadAddressName)
    }

    private fun moveCamera(position: LatLng) {
        kakaoMap?.moveCamera(
            CameraUpdateFactory.newCenterPosition(position),
            CameraAnimation.from(CAMERA_ANIMATION_DURATION, false, false)
        )
    }

    private fun updateBottomSheet(placeName: String, roadAddressName: String) {
        bottomSheetTitle.text = placeName
        bottomSheetAddress.text = roadAddressName
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED  // 필요 시 확장 상태로 전환
    }


    override fun onKeywordItemClick(keyword: String) {
        // 아무 작업도 수행하지 않음
    }

    override fun onKeywordItemDeleteClick(keyword: String) {
        // 아무 작업도 수행하지 않음
    }

    override fun onSearchItemClick(item: Item) {
        // 검색 결과 목록에서 항목을 선택했을 때의 동작을 정의
        addLabel(item)
        mainViewModel.saveLastMarkerPosition(item)
    }

    companion object {
        private const val TAG = "MainActivity"
        private const val DEFAULT_ZOOM_LEVEL = 1
        private const val CAMERA_ANIMATION_DURATION = 10
    }
}