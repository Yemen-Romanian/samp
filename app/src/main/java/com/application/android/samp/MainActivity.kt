package com.application.android.samp

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.application.android.samp.fragments.MainAudioTrackListFragment
import com.application.android.samp.viewmodels.MainViewModel


class MainActivity : AppCompatActivity() {
    private val mainViewModel: MainViewModel by lazy {
        ViewModelProviders.of(this)[MainViewModel::class.java]
    }

    private val requiredPermissions = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    private val PERMISSION_REQUEST_CODE = 1024

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (!checkAllPermissions()) {
            requestPermissions(requiredPermissions, PERMISSION_REQUEST_CODE)
        }

        val currentFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container)

        if (currentFragment == null) {
            val fragment = MainAudioTrackListFragment.newInstance()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }){
            mainViewModel.fetchAudioTracks()
        }
        else {
            Toast.makeText(
                this,
                "Not all the permission were granted",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun checkAllPermissions(): Boolean {
        return requiredPermissions.all {
            checkSelfPermission(it) == PackageManager.PERMISSION_GRANTED
        }
    }
}