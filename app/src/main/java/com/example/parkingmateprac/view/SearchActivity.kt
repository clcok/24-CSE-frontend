package com.example.parkingmateprac.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.parkingmateprac.viewmodel.adapter.KeywordAdapter
import com.example.parkingmateprac.viewmodel.adapter.SearchAdapter
import com.example.parkingmateprac.model.api.KakaoLocalApi
import com.example.parkingmateprac.databinding.ActivitySearchBinding
import com.example.parkingmateprac.model.dto.ItemDto
import com.example.parkingmateprac.viewmodel.OnKeywordItemClickListener
import com.example.parkingmateprac.viewmodel.OnSearchItemClickListener
import com.example.parkingmateprac.viewmodel.keyword.KeywordViewModel
import com.example.parkingmateprac.viewmodel.search.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchActivity : AppCompatActivity(), OnSearchItemClickListener, OnKeywordItemClickListener {

    // Hilt를 통해 KakaoLocalApi 주입
    @Inject
    lateinit var kakaoLocalApi: KakaoLocalApi

    private lateinit var binding: ActivitySearchBinding
    private lateinit var searchViewModel: SearchViewModel
    private lateinit var keywordViewModel: KeywordViewModel
    private lateinit var searchAdapter: SearchAdapter
    private lateinit var keywordAdapter: KeywordAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ViewModel 초기화 (Hilt로 주입받은 kakaoLocalApi 사용)
        searchViewModel = ViewModelProvider(this, SearchViewModelFactory(kakaoLocalApi))[SearchViewModel::class.java]
        keywordViewModel = ViewModelProvider(this, KeywordViewModelFactory(applicationContext))[KeywordViewModel::class.java]

        // 검색 결과 RecyclerView 설정
        searchAdapter = SearchAdapter(this)
        binding.searchResultView.apply {
            layoutManager = LinearLayoutManager(this@SearchActivity)
            adapter = searchAdapter
            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        }

        // 검색어 히스토리 RecyclerView 설정
        keywordAdapter = KeywordAdapter(this)
        binding.keywordHistoryView.apply {
            layoutManager = LinearLayoutManager(this@SearchActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = keywordAdapter
        }

        // 검색 입력 설정
        binding.searchTextInput.doAfterTextChanged {
            searchViewModel.searchLocationData(it.toString())
        }

        // 취소 버튼 설정
        binding.deleteTextInput.setOnClickListener {
            binding.searchTextInput.text.clear()
        }

        // 검색어 목록 관찰
        keywordViewModel.keywords.observe(this) {
            keywordAdapter.submitList(it)
        }

        // 검색 결과 관찰
        searchViewModel.items.observe(this) {
            searchAdapter.submitList(it)
            binding.searchResultView.visibility = if (it.isEmpty()) View.GONE else View.VISIBLE
            binding.emptyView.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    override fun onSearchItemClick(item: ItemDto) {
        // 검색 항목 클릭 시 선택된 데이터를 반환하고 검색어 저장
        keywordViewModel.saveKeyword(item.place)
        val resultIntent = Intent().apply {
            putExtra("place_name", item.place)
            putExtra("road_address_name", item.address)
            putExtra("latitude", item.latitude)
            putExtra("longitude", item.longitude)
        }
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }

    override fun onKeywordItemClick(keyword: String) {
        // 저장된 검색어 클릭
        binding.searchTextInput.setText(keyword)
        searchViewModel.searchLocationData(keyword)
    }

    override fun onKeywordItemDeleteClick(keyword: String) {
        // 저장된 검색어 삭제
        keywordViewModel.deleteKeyword(keyword)
    }
}
