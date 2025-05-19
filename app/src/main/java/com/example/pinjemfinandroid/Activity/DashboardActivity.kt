package com.example.pinjemfinandroid.Activity

import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.pinjemfinandroid.Adapter.ViewPagerAdapter
import com.example.pinjemfinandroid.databinding.ActivityDashboardBinding
import com.example.pinjemfinandroid.Fragment.HomeFragment
import com.example.pinjemfinandroid.Fragment.ProfileFragment
import com.example.pinjemfinandroid.Fragment.TransactionFragment
import com.example.pinjemfinandroid.R
import com.example.pinjemfinandroid.Utils.PreferenceHelper
import com.nafis.bottomnavigation.NafisBottomNavigation

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding
    private lateinit var preferenceHelper: PreferenceHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        requestpermissionNotification()
        navbar()
        preferenceHelper = PreferenceHelper(this)




    }


    private fun navbar() {

        val adapter = ViewPagerAdapter(this)
        binding.fragmentContainer.adapter = adapter
        binding.fragmentContainer.isUserInputEnabled = true // aktifkan swipe
        binding.fragmentContainer.offscreenPageLimit = 3

        binding.bottomNavigation.add(NafisBottomNavigation.Model(1, R.drawable.baseline_home_white))
        binding.bottomNavigation.add(NafisBottomNavigation.Model(2, R.drawable.baseline_account_circle_white))
        binding.bottomNavigation.add(NafisBottomNavigation.Model(3, R.drawable.baseline_account_balance_wallet))
        binding.bottomNavigation.add(NafisBottomNavigation.Model(4, R.drawable.baseline_receipt_24))
        binding.bottomNavigation.show(1)

        binding.bottomNavigation.setOnShowListener {

            val index = when (it.id) {
                1 -> 0
                2 -> 1
                3 -> 2
                4 -> 3
                else -> 0
            }
            binding.fragmentContainer.setCurrentItem(index, true)
        }

        binding.fragmentContainer.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.bottomNavigation.show(position + 1)
            }
        })

    }




    private fun requestpermissionNotification(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    1001
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1001) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Log.d("FCM", "Permission granted")
            } else {
                Log.e("FCM", "Permission denied")
            }
        }
    }
}

