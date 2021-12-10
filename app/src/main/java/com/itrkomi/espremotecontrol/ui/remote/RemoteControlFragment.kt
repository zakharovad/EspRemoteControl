package com.itrkomi.espremotecontrol.ui.remote

import RepeatListener
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.itrkomi.espremotecontrol.WebSocketService
import com.itrkomi.espremotecontrol.databinding.FragmentRemoteControlBinding
import com.itrkomi.espremotecontrol.models.LedModel
import com.itrkomi.espremotecontrol.repos.WSRepository
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance
import com.itrkomi.espremotecontrol.MainActivity
import com.itrkomi.espremotecontrol.models.BuzzerModel
import com.itrkomi.espremotecontrol.models.DriveModel


class RemoteControlFragment : Fragment(), KodeinAware {
    override val kodein: Kodein by lazy { (this.context as KodeinAware).kodein }
    private lateinit var viewModel: RemoteControlViewModel
    private var _binding: FragmentRemoteControlBinding? = null
    private val ledModel: LedModel by instance<LedModel>("LedModel");
    private val driveModel: DriveModel by instance<DriveModel>("DriveModel");
    private val buzzerModel: BuzzerModel by instance<BuzzerModel>("BuzzerModel");
    private val binding get() = _binding!!
    private var  wsService: WebSocketService? = null;
    private val connection  = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName?, service: IBinder) {
            val binder = service as WebSocketService.WebSocketBinder
            wsService = binder.getService()
            viewModel?.addWsService(wsService!!)
        }
        override fun onServiceDisconnected(className: ComponentName) {
            wsService = null
        }
    }
    fun doUnbindWsService() {
        this.requireActivity().unbindService(connection)
        super.onPause()
    }
    fun doBindWsService() {
        this.requireActivity().bindService(
            Intent(this.activity, WebSocketService::class.java),
            connection,
            Context.BIND_AUTO_CREATE
        )
    }
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        doBindWsService()
        viewModel = ViewModelProvider(this, RemoteControlViewModelFactory(ledModel,driveModel,buzzerModel))[RemoteControlViewModel::class.java]
        _binding = FragmentRemoteControlBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.viewModel = viewModel
        onButtonPress()
        return root
    }
    fun onButtonPress(){
        viewModel.bindDirectionButton(binding.buttonForward, RemoteControlViewModel.Direction.F);
        viewModel.bindDirectionButton(binding.buttonBack, RemoteControlViewModel.Direction.B);
        viewModel.bindDirectionButton(binding.buttonLeft, RemoteControlViewModel.Direction.L);
        viewModel.bindDirectionButton(binding.buttonRight, RemoteControlViewModel.Direction.R);
        viewModel.bindBuzzerButton(binding.buttonBuzzer);
    }
    override fun onDestroyView() {
        super.onDestroyView()
        doUnbindWsService()
        _binding = null
    }
}