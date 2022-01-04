package com.example.ddbc.project.ui.historyParcels

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ddbc.databinding.FragmentHistoryParcelsBinding
import com.example.ddbc.project.data.Parcel
import com.example.ddbc.project.model.MyViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class HistoryParcelsFragment : Fragment() {

    private lateinit var parcelRecyclerView: RecyclerView
    private val model: MyViewModel by activityViewModels()
    private var _binding: FragmentHistoryParcelsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHistoryParcelsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        parcelRecyclerView = binding.historyParcelsList
        parcelRecyclerView.layoutManager = LinearLayoutManager(activity)
        parcelRecyclerView.setHasFixedSize(true)

        return root
    }

    override fun onResume() {
        super.onResume()
        var parcels : List<Parcel>? = null

        GlobalScope.launch {
            parcels = model.getHistoryParcels()
            activity?.runOnUiThread(Runnable {
                if (parcels!!.isEmpty()) {
                    binding.msg.text = "אין חבילות בהיסטוריה"
                    binding.msg.visibility = View.VISIBLE
                } else {
                    binding.msg.visibility = View.GONE
                    parcelRecyclerView.adapter =
                        HistoryParcelsRecyclerViewAdapter(parcels as ArrayList<Parcel>)
                }
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}