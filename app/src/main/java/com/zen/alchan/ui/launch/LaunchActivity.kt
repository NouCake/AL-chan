package com.zen.alchan.ui.launch

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.zen.alchan.R
import com.zen.alchan.databinding.ActivityLaunchBinding
import com.zen.alchan.ui.base.BaseActivity
import com.zen.alchan.ui.root.RootActivity

class LaunchActivity : BaseActivity<ActivityLaunchBinding>() {

    override fun generateViewBinding(): ActivityLaunchBinding {
        return ActivityLaunchBinding.inflate(layoutInflater)
    }

    override fun setUpLayout() {
        val targetIntent = Intent(this, RootActivity::class.java)
        targetIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        targetIntent.data = intent.data
        targetIntent.putExtra("RESTART", intent.getBooleanExtra("RESTART", false))
        startActivity(targetIntent)
        overridePendingTransition(0, 0)
        finish()
    }

    override fun setUpObserver() {
        // do nothing
    }
}