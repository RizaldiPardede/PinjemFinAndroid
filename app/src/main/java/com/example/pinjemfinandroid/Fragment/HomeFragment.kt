package com.example.pinjemfinandroid.Fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import com.example.pinjemfinandroid.Activity.DashboardActivity
import com.example.pinjemfinandroid.Activity.NotificationActivity
import com.example.pinjemfinandroid.Adapter.PlafonAdapter
import com.example.pinjemfinandroid.Adapter.PlafonHomeAdapter
import com.example.pinjemfinandroid.Animation.Animation
import com.example.pinjemfinandroid.Local.NotificationViewModel
import com.example.pinjemfinandroid.R
import com.example.pinjemfinandroid.Utils.PreferenceHelper
import com.example.pinjemfinandroid.Utils.RupiahFormatter

import com.example.pinjemfinandroid.ViewModel.HomeViewModel
import com.example.pinjemfinandroid.ViewModel.PlafonViewModel
import com.example.pinjemfinandroid.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by activityViewModels()
    private lateinit var preferenceHelper: PreferenceHelper
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val plafonViewModel: PlafonViewModel by activityViewModels()
    private lateinit var plafonAdapter: PlafonHomeAdapter
    private var currentPosition = 0
    private val autoScrollHandler = android.os.Handler()
    private lateinit var autoScrollRunnable: Runnable
    private val notificationviewModel: NotificationViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        preferenceHelper = PreferenceHelper(requireContext())
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        ViewCompat.setZ(binding.rvCardhome, 2f)
        ViewCompat.setZ(binding.shapeprofile, 1f)
        binding.btnSimulasi.backgroundTintList = null
        binding.rvCardhome.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
         plafonViewModel.getAllPlafon()
        plafonsetup()
        notificationviewModel.refreshUnreadCount()
        val nama = preferenceHelper.getString("username")


        binding.tvUsername.text = "Halo "+ nama


        binding.btnSimulasi.setOnClickListener {
            val amount = binding.etAmount.getCleanValue().toDouble()
            val tenor = binding.etTenor.text.toString().toIntOrNull()
            if (tenor != null) {
                val token = preferenceHelper.getString("token")
                if(token.isNullOrEmpty()){
                    binding.btnAjukan.visibility = View.GONE
                    viewModel.getSimulasiPengajuanNoAuth(amount, tenor)
                }
                else{

                    viewModel.getSimulasiPengajuanWithAuth(amount,tenor,token)
                }

            }

            binding.btnAjukan.setOnClickListener {
                sendLiveDatatsr(binding.tvPinjaman.text.toString()
                    ,binding.tvTenor.text.toString().toInt())

            }
        }

        // Observe hasil
        viewModel.simulasiResult.observe(viewLifecycleOwner) { result ->

            binding.linHasilSimulasi.visibility = View.VISIBLE
            val myAnimation = Animation()
            myAnimation.animationPopup(binding.linHasilSimulasi)
            binding.tvPinjaman.text = result.amount?.let { RupiahFormatter.format(it) }
            binding.tvAngsuran.text = result.angsuran?.let { RupiahFormatter.format(it) }
            binding.tvBunga.text = result.bunga.toString()
            binding.tvTenor.text = result.tenor.toString()
            binding.tvTotalPayment.text = result.total_payment?.let { RupiahFormatter.format(it) }
            val scrollView = binding.vScrollView
            val targetLayout = binding.btnAjukan
            scrollView.post {
                scrollView.smoothScrollTo(0, targetLayout.top)
                targetLayout.requestFocus()
            }
        }

        // Observe error
        viewModel.registerError.observe(viewLifecycleOwner) { error ->

        }

        binding.notificationContainer.setOnClickListener{
            val intent = Intent(requireContext(), NotificationActivity::class.java)
            startActivity(intent)
        }
        observeNotif()
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.sliderTenor.addOnChangeListener { slider, value, fromUser ->
            binding.etTenor.setText(value.toInt().toString())
        }
        var isEditing = false

        binding.etTenor.doAfterTextChanged { editable ->
            if (isEditing) return@doAfterTextChanged

            val editText = binding.etTenor
            val text = editable.toString()

            val input = text.toIntOrNull()
            if (input != null) {
                val clampedValue = input.coerceIn(1, 12)
                val clampedText = clampedValue.toString()

                if (text != clampedText) {
                    isEditing = true
                    val selectionStart = editText.selectionStart

                    editText.setText(clampedText)
                    val newSelection = selectionStart.coerceAtMost(clampedText.length)
                    editText.setSelection(newSelection)

                    isEditing = false
                }
            }
        }
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun sendLiveDatatsr(amount : String, tenor:Int) {
        val amount = RupiahFormatter.deformat(amount)
        val tenor = tenor

        sharedViewModel.setAmount(amount)
        sharedViewModel.setTenor(tenor)
        (activity as? DashboardActivity)?.moveToPage(2)
    }

    private fun plafonsetup(){
        plafonViewModel.getAllPlafonResult.observe(viewLifecycleOwner) { plafonList ->
            plafonAdapter = PlafonHomeAdapter(plafonList)

            val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            binding.rvCardhome.layoutManager = layoutManager
            binding.rvCardhome.adapter = plafonAdapter

            // Snap helper
            val snapHelper = LinearSnapHelper()
            snapHelper.attachToRecyclerView(binding.rvCardhome)

            // Auto-scroll tiap 3 detik
            autoScrollRunnable = object : Runnable {
                override fun run() {
                    if (plafonAdapter.itemCount == 0) return

                    currentPosition++
                    if (currentPosition >= plafonAdapter.itemCount) {
                        currentPosition = 0 // kembali ke awal
                    }

                    binding.rvCardhome.smoothScrollToPosition(currentPosition)
                    autoScrollHandler.postDelayed(this, 3000) // ulang setiap 3 detik
                }
            }
            autoScrollHandler.postDelayed(autoScrollRunnable, 3000)
        }

        plafonViewModel.getAllPlafonError.observe(viewLifecycleOwner) {
            Log.d("get all plafon : ", "$it")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        autoScrollHandler.removeCallbacks(autoScrollRunnable)
        _binding = null
    }
    override fun onResume() {
        super.onResume()
        if (::autoScrollRunnable.isInitialized) {
            autoScrollHandler.postDelayed(autoScrollRunnable, 3000)
        }
        notificationviewModel.refreshUnreadCount()
    }

    override fun onPause() {
        super.onPause()
        if (::autoScrollRunnable.isInitialized) {
            autoScrollHandler.removeCallbacks(autoScrollRunnable)
        }
    }

    fun observeNotif(){
        lifecycleScope.launchWhenStarted {
            notificationviewModel.unreadCount.collect { count ->
                if (count > 0) {
                    binding.notificationBadge.text = count.toString()
                    binding.notificationBadge.visibility = View.VISIBLE
                } else {
                    binding.notificationBadge.visibility = View.GONE
                }
            }
        }
    }

}