package com.example.pinjemfinandroid.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pinjemfinandroid.Adapter.PlafonAdapter
import com.example.pinjemfinandroid.Model.ListPlafonResponseItem
import com.example.pinjemfinandroid.Utils.combineLoading
import com.example.pinjemfinandroid.ViewModel.PlafonViewModel

import com.example.pinjemfinandroid.databinding.ActivityPlafonBinding

class PlafonActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlafonBinding
    private lateinit var plafonAdapter: PlafonAdapter
    private val plafonViewModel:PlafonViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlafonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvListplafon.layoutManager = LinearLayoutManager(this)

        plafonViewModel.getAllPlafon()
        plafonViewModel.getAllPlafonResult.observe(this){
            plafonAdapter = PlafonAdapter(it)
            binding.rvListplafon.adapter = plafonAdapter
        }
        plafonViewModel.getAllPlafonError.observe(this){
            Log.d("get all plafon : ", "$it")
            Toast.makeText(this, "Error: $it", Toast.LENGTH_SHORT).show()
        }
        isLoading()


    }

    fun isLoading(){

        val isLoading = combineLoading(
            plafonViewModel.isLoading
        )

        isLoading.observe(this) { loading ->
            if (loading) {
                binding.loadingOverlay.visibility = View.VISIBLE
                binding.lottieView.playAnimation()
            } else {
                binding.lottieView.cancelAnimation()
                binding.loadingOverlay.visibility = View.GONE
            }
        }
    }
}