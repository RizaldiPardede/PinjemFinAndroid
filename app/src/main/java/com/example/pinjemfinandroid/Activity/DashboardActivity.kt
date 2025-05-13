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
//        setSelectedNav(binding.navHome)
//        supportFragmentManager.beginTransaction()
//            .replace(R.id.fragment_container, HomeFragment())
//            .commit()
//
//        binding.navHome.setOnClickListener {
//            setSelectedNav(binding.navHome)
//            supportFragmentManager.beginTransaction()
//                .replace(R.id.fragment_container, HomeFragment())
//                .commit()
//
//        }
//        binding.navProfile.setOnClickListener {
//            setSelectedNav(binding.navProfile)
//            supportFragmentManager.beginTransaction()
//                .replace(R.id.fragment_container, ProfileFragment())
//                .commit()
//
//        }
//        binding.navTransaction.setOnClickListener {
//            setSelectedNav(binding.navTransaction)
//            supportFragmentManager.beginTransaction()
//                .replace(R.id.fragment_container, TransactionFragment())
//                .commit()
//        }

        binding.bottomNavigation.add(NafisBottomNavigation.Model(1, R.drawable.baseline_home_white))
        binding.bottomNavigation.add(NafisBottomNavigation.Model(2, R.drawable.baseline_account_circle_white))
        binding.bottomNavigation.add(NafisBottomNavigation.Model(3, R.drawable.baseline_account_balance_wallet))

        binding.bottomNavigation.show(1)

        binding.bottomNavigation.setOnShowListener {
            when (it.id) {
                1 -> openFragment(HomeFragment())
                2 -> openFragment(ProfileFragment())
                3 -> openFragment(TransactionFragment())
            }
        }

    }

//    private fun setSelectedNav(selected: ImageView) {
//        val navItems = listOf(
//            binding.navHome,
//            binding.navTransaction,
//            binding.navProfile
//        )
//        navItems.forEach { it.isSelected = (it == selected) }
//    }

    private fun openFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
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

