package com.itrkomi.espremotecontrol.ui.remote

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.itrkomi.espremotecontrol.databinding.FragmentRemoteControlBinding
import com.itrkomi.espremotecontrol.repos.WSRepository
import com.itrkomi.espremotecontrol.ws.SocketAbortedException
import com.itrkomi.espremotecontrol.ws.WebSocketListener
import com.itrkomi.espremotecontrol.ws.models.SocketUpdate
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class RemoteControlFragment : Fragment(), KodeinAware {
    override val kodein: Kodein by lazy { (this.context as KodeinAware).kodein }
    private lateinit var viewModel: RemoteControlViewModel
    private var _binding: FragmentRemoteControlBinding? = null
    private val webSocketListener: WebSocketListener by instance<WebSocketListener>();
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        viewModel =
            ViewModelProvider(this, RemoteControlViewModelFactory(webSocketListener))[RemoteControlViewModel::class.java]

        _binding = FragmentRemoteControlBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.showReconnect = false
        viewModel.subscribeToSocketEvents();
        observeState(viewModel.state)
        return root
    }
    private fun observeState(state:LiveData<SocketUpdate?>){
        state.observe(viewLifecycleOwner, Observer {
            when( it){
                null->{
                    binding.showReconnect = false;
                }
                is SocketUpdate ->{
                    if(it.exception !== null)
                    binding.showReconnect = true
                }else ->{
                binding.showReconnect = false
                }

            }

        })
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}