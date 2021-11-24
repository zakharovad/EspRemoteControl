package com.itrkomi.espremotecontrol.ui.remote

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.itrkomi.espremotecontrol.databinding.FragmentRemoteControlBinding
import com.itrkomi.espremotecontrol.models.LedModel
import com.itrkomi.espremotecontrol.repos.WSRepository
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class RemoteControlFragment : Fragment(), KodeinAware {
    override val kodein: Kodein by lazy { (this.context as KodeinAware).kodein }
    private lateinit var viewModel: RemoteControlViewModel
    private var _binding: FragmentRemoteControlBinding? = null
    private val wSRepository: WSRepository by instance<WSRepository>();
    private val ledModel: LedModel by instance<LedModel>("LedModel");

    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this, RemoteControlViewModelFactory(wSRepository,ledModel))[RemoteControlViewModel::class.java]

        _binding = FragmentRemoteControlBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.ledModel = viewModel.ledModel
        return root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}