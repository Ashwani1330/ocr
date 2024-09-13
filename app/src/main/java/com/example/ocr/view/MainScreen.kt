import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.ocr.view.CameraPreviewScreen
import com.example.ocr.viewModel.MainViewModel
import kotlinx.coroutines.launch

@Composable
fun MainScreen(viewModel: MainViewModel, navController: NavHostController) {
    val scope = rememberCoroutineScope()

    Scaffold(
        bottomBar = {
            BottomAppBar {
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Button(
                        onClick = {
                            scope.launch {
                                navController.navigate("cameraPreview")
                            }
                        },
                        modifier = Modifier.wrapContentSize()
                    ) {
                        Icon(imageVector = Icons.Default.Home, contentDescription = "Scan")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Scan")
                    }
                    Button(
                        onClick = {
                            scope.launch {
                                navController.navigate("scannedCards")
                            }
                        },
                        modifier = Modifier.wrapContentSize()
                    ) {
                        Icon(imageVector = Icons.Default.AccountBox, contentDescription = "Scan")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Scanned Cards")

                    }

                }
            }
        }
    ) { innerPadding ->
        CameraPreviewScreen(
            viewModel = viewModel,
            modifier = Modifier.padding(innerPadding)
        )
    }
}