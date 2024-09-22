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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.room.Room
import com.example.parkingmateprac.R
import com.example.parkingmateprac.dao.AppDatabase
import com.example.parkingmateprac.model.dao.ParkingItemDao
import com.example.parkingmateprac.model.entity.ParkingItemEntity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.navigation.NavigationView
import com.kakao.vectormap.*
import com.kakao.vectormap.camera.CameraAnimation
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.*

class MainActivity : AppCompatActivity() {

    // 변수들 선언
    private lateinit var parkingItemDao: ParkingItemDao
    private var parkingItems: List<ParkingItemEntity> = emptyList()

    // 지도 관련 변수들
    private var mapView: MapView? = null
    private lateinit var kakaoMap: KakaoMap
    private lateinit var labelLayer: LabelLayer

    // UI 관련 변수들
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var errorLayout: RelativeLayout
    private lateinit var errorMessage: TextView
    private lateinit var errorDetails: TextView
    private lateinit var retryButton: ImageButton
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<FrameLayout>
    private lateinit var bottomSheetTitle: TextView
    private lateinit var bottomSheetAddress: TextView
    private lateinit var bottomSheetLayout: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 레이아웃 설정
        setContentView(R.layout.activity_main)

        // 데이터베이스 초기화 및 주차장 데이터 로드
        initializeDatabase()
        loadParkingItems()

        // 지도 초기화
        initializeMapView()

        // UI 컴포넌트 초기화
        initializeUIComponents()
    }

    private fun initializeDatabase() {
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "app_database"
        ).build()
        parkingItemDao = db.parkingItemDao()
    }

    private fun loadParkingItems() {
        // 비동기로 데이터 로드
        Thread {
            parkingItems = parkingItemDao.getAllParkingItems()
            runOnUiThread {
                if (::kakaoMap.isInitialized) {
                    displayMultiplePins()
                }
            }
        }.start()
    }

    private fun initializeMapView() {
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
                labelLayer = kakaoMap.labelManager.layer
                Log.d(TAG, "Map is ready")
                // 지도 준비 완료 시 핀 표시
                displayMultiplePins()
            }
        })
    }

    private fun initializeUIComponents() {
        // DrawerLayout 초기화
        drawerLayout = findViewById(R.id.drawer_layout)
        val menuButton: ImageButton = findViewById(R.id.menu_button)
        menuButton.setOnClickListener {
            drawerLayout.openDrawer(androidx.core.view.GravityCompat.START)
        }

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

        val customerService = findViewById<TextView>(R.id.nav_customer_service)
        customerService.setOnClickListener {
            Toast.makeText(this, "고객센터 선택", Toast.LENGTH_SHORT).show()
            // 고객센터 화면으로 이동하는 로직 추가
        }

        // 검색창 클릭 시 검색 페이지로 이동 (필요 시 구현)
        val searchEditText = findViewById<EditText>(R.id.search_edit_text)
        searchEditText.setOnClickListener {
            // 검색 액티비티로 이동하는 코드 구현
        }

        // 에러 화면 초기화
        initializeErrorScreen()

        // BottomSheet 초기화
        initializeBottomSheet()
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

        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        bottomSheetBehavior.isHideable = false

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                // 필요 시 추가 동작 구현
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // 필요 시 추가 동작 구현
            }
        })
    }

    private fun displayMultiplePins() {
        // 기존 마커 제거

        // 각 주차장 위치에 마커 추가
        parkingItems.forEach { parkingItem ->
            val position = LatLng.from(parkingItem.latitude, parkingItem.longitude)

            val labelOptions = LabelOptions.from(parkingItem.parkingName, position)
                .setStyles(createLabelStyles())
                .setTexts(parkingItem.parkingName)

            val label = labelLayer.addLabel(labelOptions)

            // Badge를 사용하여 아이콘 설정
            val badgeDrawable = ContextCompat.getDrawable(this, R.drawable.ic_pin)
            val badge = Badge.from(badgeDrawable)
            badge.setOffset(0f, 0f) // 필요에 따라 위치 조정
            label.addBadge(badge)

            // 마커 클릭 리스너 설정
            label.setOnClickListener {
                // 클릭 시 예약 화면으로 이동
                val intent = Intent(this, ReserveParkingLotActivity::class.java)
                // 좌표 정보를 인텐트에 추가
                intent.putExtra("latitude", parkingItem.latitude)
                intent.putExtra("longitude", parkingItem.longitude)
                intent.putExtra("parkingName", parkingItem.parkingName)
                intent.putExtra("address", parkingItem.category) // 필요에 따라 다른 정보 추가
                // 필요에 따라 추가 정보 전달
                startActivity(intent)
            }
        }

        // 지도의 중심과 줌 레벨 설정 (첫 번째 주차장 위치로 설정)
        if (parkingItems.isNotEmpty()) {
            val firstItem = parkingItems.first()
            val position = LatLng.from(firstItem.latitude, firstItem.longitude)
            moveCamera(position)
        }
    }

    private fun createLabelStyles(): LabelStyles {
        val labelStyle = LabelStyle.from(R.drawable.ic_pin)
            .setTextStyles(LabelTextStyle.from(this, R.style.labelTextStyle))
            .setZoomLevel(DEFAULT_ZOOM_LEVEL)

        return LabelStyles.from(labelStyle)
    }

    private fun moveCamera(position: LatLng) {
        kakaoMap.moveCamera(
            CameraUpdateFactory.newCenterPosition(position),
            CameraAnimation.from(CAMERA_ANIMATION_DURATION, false, false)
        )
    }

    private fun showErrorScreen(error: Exception) {
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
            override fun onMapReady(map: KakaoMap) {
                kakaoMap = map
                labelLayer = kakaoMap.labelManager.layer
                Log.d(TAG, "Map is ready on retry")
                // 지도 준비 완료 시 핀 표시
                displayMultiplePins()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        mapView?.resume()
        Log.d(TAG, "MapView resumed")
    }

    override fun onPause() {
        super.onPause()
        mapView?.pause()
        Log.d(TAG, "MapView paused")
    }
    companion object {
        private const val TAG = "MainActivity"
        private const val DEFAULT_ZOOM_LEVEL = 1
        private const val CAMERA_ANIMATION_DURATION = 10
    }
}
