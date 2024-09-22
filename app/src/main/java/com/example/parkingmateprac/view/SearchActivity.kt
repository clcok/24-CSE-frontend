package com.example.parkingmateprac.view

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.parkingmateprac.R
import com.example.parkingmateprac.dao.AppDatabase
import com.example.parkingmateprac.model.dao.KeywordDao
import com.example.parkingmateprac.model.entity.KeywordEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchActivity : AppCompatActivity() {

    private lateinit var keywordDao: KeywordDao
    private var keywords: List<KeywordEntity> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        // 데이터베이스 초기화
        initializeDatabase()

        // 키워드 데이터 로드
        loadKeywords()
    }

    // 데이터베이스 초기화 (팩토리 패턴 없이 직접 DAO 사용)
    private fun initializeDatabase() {
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "app_database"
        ).build()
        keywordDao = db.keywordDao()
    }

    // 키워드 데이터를 로드하고 UI에 반영하는 함수
    private fun loadKeywords() {
        lifecycleScope.launch(Dispatchers.IO) {
            keywords = keywordDao.getAllKeywords()
            withContext(Dispatchers.Main) {
                if (keywords.isNotEmpty()) {
                    Log.d("SearchActivity", "Loaded keywords: $keywords")
                    // 키워드를 UI에 반영하는 작업 (예: RecyclerView 업데이트)
                } else {
                    Toast.makeText(this@SearchActivity, "저장된 키워드가 없습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
