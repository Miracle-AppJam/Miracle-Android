package com.appjam.miracle

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.appjam.miracle.databinding.ActivityMainBinding
import com.appjam.miracle.local.MiracleDataBase
import com.appjam.miracle.remote.RetrofitBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        lifecycleScope.launch(Dispatchers.IO) {
//            val image = MiracleDataBase.getInstance(this@MainActivity)!!.drawDao().getMember(0).image
//            lifecycleScope.launch(Dispatchers.Main) {
//                binding.imageTest.setImageBitmap(image)
//            }
//        }
    }
}