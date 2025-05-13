package com.example.pinjemfinandroid.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import androidx.core.view.ViewCompat
import androidx.fragment.app.viewModels
import com.example.pinjemfinandroid.Animation.Animation
import com.example.pinjemfinandroid.R
import com.example.pinjemfinandroid.ViewModel.HomeViewModel
import com.example.pinjemfinandroid.databinding.FragmentHomeBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment(R.layout.fragment_home) {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()

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

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        ViewCompat.setZ(binding.ivPlafon, 2f)
        ViewCompat.setZ(binding.shapeprofile, 1f)
        binding.btnSimulasi.backgroundTintList = null
        binding.btnSimulasi.setOnClickListener {
            val amount = binding.etAmount.text.toString().toDoubleOrNull() ?: 0.0
            val tenor = binding.etTenor.text.toString().toIntOrNull() ?: 0
            viewModel.getSimulasiPengajuan(amount, tenor)
        }

        // Observe hasil
        viewModel.simulasiResult.observe(viewLifecycleOwner) { result ->

            binding.linHasilSimulasi.visibility = View.VISIBLE
            val myAnimation = Animation()
            myAnimation.animationPopup(binding.linHasilSimulasi)
            binding.tvPinjaman.text = result.amount.toString()
            binding.tvAngsuran.text = result.angsuran.toString()
            binding.tvBunga.text = result.bunga.toString()
            binding.tvTenor.text = result.tenor.toString()
            binding.tvTotalPayment.text = result.total_payment.toString()
            val scrollView = binding.vScrollView
            val targetLayout = binding.linHasilSimulasi
            scrollView.post {
                scrollView.smoothScrollTo(0, targetLayout.top)
                targetLayout.requestFocus()
            }
        }

        // Observe error
        viewModel.registerError.observe(viewLifecycleOwner) { error ->

        }
        return binding.root


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
}