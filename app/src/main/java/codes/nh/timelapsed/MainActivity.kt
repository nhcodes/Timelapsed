package codes.nh.timelapsed

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import codes.nh.timelapsed.theme.MainTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainTheme {
                Main()
            }
        }
    }

}