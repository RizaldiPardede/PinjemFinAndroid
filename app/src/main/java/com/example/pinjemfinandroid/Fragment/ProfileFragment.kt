package com.example.pinjemfinandroid.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pinjemfinandroid.Activity.AddDetailActivity
import com.example.pinjemfinandroid.Adapter.MenuAdapter
import com.example.pinjemfinandroid.Model.MenuModel
import com.example.pinjemfinandroid.R
import com.example.pinjemfinandroid.Utils.ConfirmationUtils
import com.example.pinjemfinandroid.Utils.ConfirmationUtils.showConfirmationDialog
import com.example.pinjemfinandroid.databinding.FragmentHomeBinding
import com.example.pinjemfinandroid.databinding.FragmentProfileBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment(R.layout.fragment_profile),MenuAdapter.OnItemClickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MenuAdapter
    private lateinit var dataList: List<MenuModel>


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
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        ViewCompat.setZ(binding.profileImage, 2f)
        ViewCompat.setZ(binding.linearLayout, 1f)
        binding.btnLogoutAccount.backgroundTintList = null
        binding.btnLogoutAccount.setOnClickListener{

            showConfirmationDialog(
                context = requireContext(),  // Ganti 'this' dengan konteks sesuai (Activity)
                message = "Apakah Anda yakin ingin mengaktifkan fitur ini?",
                onConfirmed = {
                    // Aksi jika user memilih Yes

                },
                onCancelled = {
                    // Aksi jika user memilih No

                }
            )

        }


        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        dataList = listOf(
            MenuModel("Profile Information", "Manage account details", R.drawable.baseline_account_circle_white),
            MenuModel("Plafon Information", "See Your Plafon here", R.drawable.baseline_credit_card_24),
            MenuModel("Payment History", "See your history paymeny", R.drawable.baseline_account_balance_wallet)
        )

        recyclerView = view.findViewById(R.id.recyclerMenu)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = MenuAdapter(dataList,this)
        recyclerView.adapter = adapter
    }




    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onItemClick(position: Int) {
        Toast.makeText(requireContext(), "Klik item $position", Toast.LENGTH_SHORT).show()
        when (position) {
            0 -> {
                startActivity(Intent(requireContext(),AddDetailActivity::class.java))
            }
            1 -> {

            }
            2 -> {

            }
            else -> {
                Toast.makeText(requireContext(), "Klik menu lainnya", Toast.LENGTH_SHORT).show()
            }
        }
    }
}