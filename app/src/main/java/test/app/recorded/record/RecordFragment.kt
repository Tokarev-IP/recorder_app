package test.app.recorded.record

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.InternalCoroutinesApi
import test.app.recorded.MainActivity
import test.app.recorded.R
import test.app.recorded.database.RecordDatabase
import test.app.recorded.database.RecordDatabaseDao
import test.app.recorded.databinding.FragmentRecordBinding
import java.io.File
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.view.*


class RecordFragment : Fragment() {

    private lateinit var viewModel: RecordViewModel
    private lateinit var mainActivity: MainActivity
    private var count:Int? = null
    private var database: RecordDatabaseDao? = null
    private val MY_PERMISSIONS_RECORD_AUDIO = 123


    @InternalCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = DataBindingUtil.inflate<FragmentRecordBinding>(
            inflater,
            R.layout.fragment_record,
            container, false
        )

        database = context?.let { RecordDatabase.getInstance(it).recordDatabaseDao }

        mainActivity = activity as MainActivity

        viewModel = ViewModelProvider(this).get(RecordViewModel::class.java)

        binding.recordViewModel = viewModel
        binding.lifecycleOwner = this.viewLifecycleOwner

        if( RecordService.running){
            Toast.makeText(activity, R.string.recording_is_now, Toast.LENGTH_SHORT).show()
            binding.playButton.setImageResource(R.drawable.ic_baseline_stop_24)
        } else {
            viewModel.resetTimer()
        }

        binding.playButton.setOnClickListener {
            val intent: Intent =  Intent(activity, RecordService::class.java)

            if (ContextCompat.checkSelfPermission(requireContext(),
                            android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                        arrayOf(android.Manifest.permission.RECORD_AUDIO), MY_PERMISSIONS_RECORD_AUDIO)
            } else {
                if (RecordService.running) {

                    binding.playButton.setImageResource(R.drawable.ic_baseline_mic_24)
                    activity?.stopService(intent)
                    activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                    viewModel.stopTimer()
                    RecordService.running = false

                } else {
                    binding.playButton.setImageResource(R.drawable.ic_baseline_stop_24)
                    Toast.makeText(activity, R.string.toast_recording_start, Toast.LENGTH_SHORT).show()
                    RecordService.running = true

                    val folder = File(activity?.getExternalFilesDir(null)?.absolutePath.toString() + "/VoiceRecorder")
                    if (!folder.exists()) {
                        folder.mkdir()
                    }

                    activity?.startService(intent)
                    activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                    viewModel.startTimer()
                }
            }
        }


        createChannel(
                getString(R.string.notification_channel_id),
                getString(R.string.notification_channel_name)
        )

        return binding.root

    }

    override fun onResume() {
        if (RecordService.running) {
            Toast.makeText(activity, R.string.recording_is_now, Toast.LENGTH_SHORT).show()
        }
            super.onResume()
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode){
            MY_PERMISSIONS_RECORD_AUDIO ->{
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                } else {
                    Toast.makeText(activity, getString(R.string.toast_recording_permissions), Toast.LENGTH_LONG).show()
                }
                return
            }
        }
    }

    private fun createChannel(channelID: String, channelName: String){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel(
                    channelID,
                    channelName,
                    NotificationManager.IMPORTANCE_DEFAULT
            )
                    .apply {
                        setShowBadge(false)
                        setSound(null, null)
                    }
            val notificationManager = requireActivity().getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

}
