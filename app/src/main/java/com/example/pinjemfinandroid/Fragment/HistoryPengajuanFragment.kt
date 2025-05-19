package com.example.pinjemfinandroid.Fragment

import PengajuanResponseItem
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pinjemfinandroid.Adapter.PengajuanAdapter
import com.example.pinjemfinandroid.R
import com.example.pinjemfinandroid.Utils.PreferenceHelper
import com.example.pinjemfinandroid.ViewModel.PengajuanViewModel
import com.example.pinjemfinandroid.databinding.FragmentHistoryPengajuanBinding
import com.example.pinjemfinandroid.databinding.FragmentHomeBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HistoryPengajuanFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HistoryPengajuanFragment : Fragment(R.layout.fragment_history_pengajuan) {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentHistoryPengajuanBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: PengajuanAdapter
    private lateinit var pengajuanViewModel: PengajuanViewModel
    private lateinit var preferenceHelper: PreferenceHelper


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
        _binding = FragmentHistoryPengajuanBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        binding.rvPengajuan.layoutManager = LinearLayoutManager(requireContext())

        adapter = PengajuanAdapter(emptyList())
        binding.rvPengajuan.layoutManager = LinearLayoutManager(requireContext())
        binding.rvPengajuan.adapter = adapter

        preferenceHelper = PreferenceHelper(requireContext())
        pengajuanViewModel = ViewModelProvider(this).get(PengajuanViewModel::class.java)

        val token = preferenceHelper.getString("token")
        Log.d("HISTORY_FRAGMENT", "Token: $token")

        token?.let {
            pengajuanViewModel.getAllPengajuan(it)
        }

        pengajuanViewModel.ListpengajuanResult.observe(viewLifecycleOwner){
            adapter.updateData(it)
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
         * @return A new instance of fragment HistoryPengajuanFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HistoryPengajuanFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}