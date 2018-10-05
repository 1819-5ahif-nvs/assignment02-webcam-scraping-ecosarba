package htl.at.linkwatcherandroid

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.VideoView



class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val uri = Uri.parse("");
        val videoView = findViewById(R.id.VideoView) as VideoView

        videoView.setVideoPath("http://172.18.3.152:8080/");

        videoView.start();
    }
}
