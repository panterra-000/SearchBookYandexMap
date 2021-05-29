package uz.rdo.projects.searchbookyandexmap.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.findNavController
import uz.rdo.projects.searchbookyandexmap.MainActivity
import uz.rdo.projects.searchbookyandexmap.R
import java.util.concurrent.Executors
import kotlin.concurrent.thread

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Executors.newSingleThreadExecutor().execute {
            Thread.sleep(2000)
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}