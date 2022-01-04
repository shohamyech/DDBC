package com.example.ddbc.project.ui.friendsParcels

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ddbc.project.data.Parcel
import com.example.ddbc.databinding.FragmentFriendsParcelsBinding
import com.example.ddbc.project.model.MyViewModel

class FriendsParcelsFragment : Fragment() {

    private lateinit var parcelRecyclerView: RecyclerView
    private val model: MyViewModel by activityViewModels()
    private var _binding: FragmentFriendsParcelsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFriendsParcelsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        parcelRecyclerView = binding.friendsParcelsList
        parcelRecyclerView.layoutManager = LinearLayoutManager(activity)
        parcelRecyclerView.setHasFixedSize(true)

        return root
    }

    override fun onResume() {
        super.onResume()
        if (model.isSigned.value!!) {
            val parcels = model.getParcelsCloseToMyLocation()
            if (parcels.isEmpty()){
                binding.msg.text = "אין כרגע חבילות זמינות למשלוח"
                binding.msg.visibility = View.VISIBLE
            }else {
                binding.msg.visibility = View.GONE
                model.parcels.observe(viewLifecycleOwner, Observer {
                    parcelRecyclerView.adapter = FriendParcelRecyclerViewAdapter(parcels, model) })
            }
        }else{
            binding.msg.text = "התחבר כדי לראות חבילות למשלוח"
            binding.msg.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}