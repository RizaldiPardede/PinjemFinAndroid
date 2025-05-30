package com.example.pinjemfinandroid.Fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.pinjemfinandroid.Activity.LoginActivity
import com.example.pinjemfinandroid.Model.PengajuanRequest
import com.example.pinjemfinandroid.R
import com.example.pinjemfinandroid.Utils.AlertEvent
import com.example.pinjemfinandroid.Utils.ConfirmationUtilsFlexible
import com.example.pinjemfinandroid.Utils.LottieAlertHelper
import com.example.pinjemfinandroid.Utils.PreferenceHelper
import com.example.pinjemfinandroid.Utils.RupiahFormatter
import com.example.pinjemfinandroid.ViewModel.PengajuanViewModel
import com.example.pinjemfinandroid.databinding.FragmentTransactionBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TransactionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TransactionFragment : Fragment(R.layout.fragment_transaction) {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentTransactionBinding? = null
    private lateinit var preferenceHelper: PreferenceHelper
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val binding get() = _binding!!
    private val pengajuanViewModel: PengajuanViewModel by activityViewModels()
    private var currentPengajuan: PengajuanRequest? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }


        preferenceHelper = PreferenceHelper(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTransactionBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        observeDatafromHome()
        ViewCompat.setZ(binding.linearLayoutCard, 2f)
        ViewCompat.setZ(binding.linearlayoutinformasi, 1f)
        val token = preferenceHelper.getString("token")
        if (token != null) {
            pengajuanViewModel.getInformasiPlafonCustomer(token)
        }

        ContextCompat.getDrawable(requireContext(), R.drawable.custom_thumb2)
            ?.let { binding.sliderTenor.setCustomThumbDrawable(it) }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        pengajuanViewModel = ViewModelProvider(this).get(PengajuanViewModel::class.java)
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



        preferenceHelper.getString("token")?.let { statisticPinjaman(it) }
        binding.btnAjukan.setOnClickListener {
            if (preferenceHelper.getString("token").isNullOrEmpty()){
                ConfirmationUtilsFlexible.showFlexibleDialog(
                    context = requireContext(),
                    title = "Belum Login",
                    message = "Anda Masih belum melakukan login",
                    positiveButtonText = "Login",
                    negativeButtonText = "Tidak",
                    onConfirmed = {
                        startActivity(Intent(requireContext(),LoginActivity::class.java))
                    },
                    onCancelled = {
                        // aksi ketika "Batal" ditekan
                    }
                )
            }
            else{
                currentPengajuan = PengajuanRequest(binding.etAmount.getCleanValue().toDouble(),binding.etTenor.text.toString().toInt())
                preferenceHelper.getString("token")?.let {
                    pengajuanViewModel.cekUpdateAkun(it)


                }


            }

        }
        observeCekUpdateResult()
        observealert()
        observePengajuanResult()


    }

    fun observeCekUpdateResult(){

        pengajuanViewModel.cekUpdate.observe(viewLifecycleOwner) { messageresponse ->
            currentPengajuan?.let { pengajuan ->
                preferenceHelper.getString("token")?.let {
                    pengajuanViewModel.postPengajuan(pengajuan, it)
                }
            }
            Toast.makeText(requireContext(), messageresponse.message, Toast.LENGTH_SHORT).show()
        }

        pengajuanViewModel.cekUpdateError.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }
    }

    fun observePengajuanResult(){
        pengajuanViewModel.pengajuanResponse.observe(viewLifecycleOwner,Observer{
            binding.linearLayoutKalkulasi.visibility = View.VISIBLE
//            binding.tvAngsuran.text = it.angsuran.toString()
            binding.tvAngsuran.text = RupiahFormatter.format(it.angsuran.toString().toDouble())

            binding.tvBunga.text = it.bunga.toString()
            binding.tvTotalPayment.text = RupiahFormatter.format(it.totalPayment.toString().toDouble())
            binding.tvTenor.text = it.tenor.toString()
            binding.tvPinjaman.text = it.tenor.toString()
            sharedViewModel.notifyRefresh()

        })

        pengajuanViewModel.pengajuanError.observe(viewLifecycleOwner,Observer{
            Toast.makeText(requireContext(),it,Toast.LENGTH_SHORT).show()
        })
    }
    fun observealert(){
        pengajuanViewModel.alertEvent.observe(viewLifecycleOwner) { event ->
            when (event) {
                is AlertEvent.ShowSuccess -> {
                    LottieAlertHelper.showSuccess(requireContext(),  event.message)

                }
                is AlertEvent.ShowError -> {
                    LottieAlertHelper.showError(requireContext(), event.message)

                }
                AlertEvent.Dismiss -> {
                    // Optional: bisa digunakan untuk dismiss manual
                }
            }
        }
    }

    fun statisticPinjaman(token:String){

        pengajuanViewModel.informasiPengajuan.observe(viewLifecycleOwner, Observer {
//            binding.tvJumlahPlafon.text = it.jumlahPlafon.toString()
            binding.tvJumlahPlafon.text = it.jumlahPlafon?.let { it1 -> RupiahFormatter.format(it1) }
            binding.tvSisaPlafon.text = it.sisaPlafon?.let { it1 -> RupiahFormatter.format(it1) }
//                it.sisaPlafon.toString()
            binding.tvPinjamanlunas.text = it.jumlahPinjamanLunas?.let { it1 ->RupiahFormatter.format(it1) }
//                it.jumlahPinjamanLunas.toString()
            binding.tvPinjamansekarang.text = it.jumlahPinjaman?.let { it1 -> RupiahFormatter.format(it1) }
//                it.jumlahPinjaman.toString()

            val progress = it.persentasilvup
            binding.progressBar.progress= progress.toString().toDouble().toInt()
            binding.pbPerentase.text = "$progress%"
            when(it.jenisPlafon){
                "Bronze"->{
                    binding.linearLayoutCard.background = ContextCompat.getDrawable(requireContext(), R.drawable.card_bronze)
                }
                "Silver"->{
                    binding.linearLayoutCard.background = ContextCompat.getDrawable(requireContext(), R.drawable.card_silver)
                }

                "Gold"->{
                    binding.linearLayoutCard.background = ContextCompat.getDrawable(requireContext(), R.drawable.card_gold)
                }
                "Platinum"->{
                    binding.linearLayoutCard.background = ContextCompat.getDrawable(requireContext(), R.drawable.card_platinum)
                }

            }

        })
        pengajuanViewModel.informasiPengajuanError.observe(viewLifecycleOwner, Observer {

        })

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TransactionFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TransactionFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    fun observeDatafromHome(){
        sharedViewModel.amount.observe(viewLifecycleOwner) { amount ->
            binding.etAmount.setCleanValue(amount)
            val scrollView = binding.vScrollView
            val targetLayout = binding.linearPengajuan
            scrollView.post {
                scrollView.smoothScrollTo(0, targetLayout.top)
                targetLayout.requestFocus()
            }
        }

        sharedViewModel.tenor.observe(viewLifecycleOwner) { tenor ->

            binding.etTenor.setText(tenor.toString())
        }
    }


}