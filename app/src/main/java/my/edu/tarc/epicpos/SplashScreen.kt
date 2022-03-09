package my.edu.tarc.epicpos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView

class SplashScreen : AppCompatActivity() {
    private val handler = Handler()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

    }
    private val runnable = Runnable {
        if(!isFinishing){
            startActivity(Intent(applicationContext,MainActivity::class.java))
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        handler.postDelayed(runnable,1000)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable)

    }
}