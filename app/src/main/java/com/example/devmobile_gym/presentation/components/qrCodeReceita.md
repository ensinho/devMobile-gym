1. Adicionar permissões no AndroidManifest.xml
Adicionar aspermissões necessárias no AndroidManifest.xml:
<uses-permission android:name="android.permission.CAMERA" />
<uses-feature android:name="android.hardware.camera" />
<uses-feature android:name="android.hardware.camera.autofocus" />

2. Adicionar dependências no build.gradle (app)
Você precisa adicionar a biblioteca CameraX e uma biblioteca para leitura de QR code:
// CameraX
implementation "androidx.camera:camera-camera2:1.3.1"
implementation "androidx.camera:camera-lifecycle:1.3.1"
implementation "androidx.camera:camera-view:1.3.1"

// ML Kit para leitura de QR code (Google)
implementation "com.google.mlkit:barcode-scanning:17.2.0"

3. Criar a tela de leitura de QR code
Vamos criar um composable para a tela de leitura de QR Code:
------------------------------------------------------------
import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.Executors

@Composable
fun QRCodeScreen(
    navController: NavHostController,
    onQRCodeScanned: (String) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    
    var hasCameraPermission by remember { mutableStateOf(
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    )}
    
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasCameraPermission = isGranted
    }
    
    // Scanner options
    val options = remember {
        BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
            .build()
    }
    
    val scanner = remember {
        BarcodeScanning.getClient(options)
    }
    
    // Camera UI
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        if (hasCameraPermission) {
            Box(modifier = Modifier.weight(1f)) {
                CameraPreview(
                    modifier = Modifier.fillMaxSize(),
                    scanner = scanner,
                    onQRCodeScanned = onQRCodeScanned
                )
                
                // Overlay with instructions
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier
                            .align(Alignment.End)
                            .padding(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Fechar",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 24.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f)
                        )
                    ) {
                        Text(
                            text = "Posicione o QR code dentro da área de captura",
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        } else {
            // Permission request screen
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Precisamos de permissão para acessar a câmera",
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { permissionLauncher.launch(Manifest.permission.CAMERA) }
                ) {
                    Text("Permitir acesso à câmera")
                }
            }
        }
    }
}

@Composable
fun CameraPreview(
    modifier: Modifier = Modifier,
    scanner: BarcodeScanner,
    onQRCodeScanned: (String) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraExecutor = remember { Executors.newSingleThreadExecutor() }
    val previewView = remember { PreviewView(context) }
    
    DisposableEffect(Unit) {
        onDispose {
            cameraExecutor.shutdown()
        }
    }
    
    AndroidView(
        factory = { previewView },
        modifier = modifier
    ) { view ->
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(view.surfaceProvider)
            }
            
            val cameraSelector = CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build()
            
            val imageAnalysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also { analysis ->
                    analysis.setAnalyzer(cameraExecutor) { imageProxy ->
                        processImageProxy(imageProxy, scanner, onQRCodeScanned)
                    }
                }
            
            try {
                // Unbind all use cases before rebinding
                cameraProvider.unbindAll()
                
                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview,
                    imageAnalysis
                )
            } catch (exc: Exception) {
                Log.e("CameraPreview", "Use case binding failed", exc)
            }
        }, ContextCompat.getMainExecutor(context))
    }
}

@androidx.annotation.OptIn(androidx.camera.core.ExperimentalGetImage::class)
private fun processImageProxy(
    imageProxy: ImageProxy,
    scanner: BarcodeScanner,
    onQRCodeScanned: (String) -> Unit
) {
    val mediaImage = imageProxy.image
    if (mediaImage != null) {
        val image = InputImage.fromMediaImage(
            mediaImage,
            imageProxy.imageInfo.rotationDegrees
        )
        
        scanner.process(image)
            .addOnSuccessListener { barcodes ->
                for (barcode in barcodes) {
                    barcode.rawValue?.let { value ->
                        onQRCodeScanned(value)
                    }
                }
            }
            .addOnFailureListener {
                Log.e("BarcodeScanner", "Barcode scanning failed", it)
            }
            .addOnCompleteListener {
                imageProxy.close()
            }
    } else {
        imageProxy.close()
    }
}
------------------------------------------------------------


4. Modificar o arquivo de rotas
Agora, precisamos adicionar a rota do QR code no seu arquivo de rotas para direcionar corretamente ao abrir a câmera:
---------------------------------------------------------------------------------------------------------------------
object AlunoRoutes {
    const val Home = "home"
    const val Search = "search"
    const val QrCode = "qrcode"
    const val Chatbot = "chatbot"
    const val Profile = "profile"
}
---------------------------------------------------------------------------------------------------------------------


5. Configurar a navegação
Agora, vamos configurar a navegação para a tela de QR code:
-----------------------------------------------------------
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun AlunoNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    onQRCodeScanned: (String) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = AlunoRoutes.Home,
        modifier = modifier
    ) {
        composable(AlunoRoutes.Home) {
            HomeScreen(navController = navController)
        }
        
        composable(AlunoRoutes.Search) {
            SearchScreen(navController = navController)
        }
        
        composable(AlunoRoutes.QrCode) {
            QRCodeScreen(
                navController = navController,
                onQRCodeScanned = { result ->
                    // Aqui você processa o resultado do QR code e navega
                    // para a tela apropriada com o resultado
                    onQRCodeScanned(result)
                    navController.popBackStack() // Volta para a tela anterior após a leitura
                }
            )
        }
        
        composable(AlunoRoutes.Chatbot) {
            ChatbotScreen(navController = navController)
        }
        
        composable(AlunoRoutes.Profile) {
            ProfileScreen(navController = navController)
        }
    }
}

// Você precisará implementar essas telas ou usar as que já possui
@Composable
fun HomeScreen(navController: NavHostController) {
    // Implementação da tela Home
}

@Composable
fun SearchScreen(navController: NavHostController) {
    // Implementação da tela Search
}

@Composable
fun ChatbotScreen(navController: NavHostController) {
    // Implementação da tela Chatbot
}

@Composable
fun ProfileScreen(navController: NavHostController) {
    // Implementação da tela Profile
}
-----------------------------------------------------------


6. Implementar um modelo de dados para NavIcon
O seu código atual já tem uma implementação para os ícones da barra de navegação, mas para facilitar a referência, vamos criar o modelo de dados NavIcon:
------------------------------------------------------
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavIcon {
    data class VectorIcon(val icon: ImageVector) : NavIcon()
    data class DrawableIcon(val resId: Int) : NavIcon()
}

data class BottomNavigationItem(
    val title: String,
    val selectedIcon: NavIcon,
    val unselectedIcon: NavIcon,
    val hasNews: Boolean,
    val badgeCount: Int? = null
)
------------------------------------------------------


7. Modificar o MainActivity
Agora, vamos modificar sua MainActivity para incorporar a navegação e a leitura de QR code:
-------------------------------------------------------------------------------------------
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavGraph.Companion.findStartDestination

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            YourAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    
                    // Manipula o resultado do QR code escaneado
                    val handleQRCodeResult: (String) -> Unit = { result ->
                        // Aqui você pode processar o código QR 
                        // Por exemplo, exibir um Toast com o conteúdo
                        Toast.makeText(
                            this@MainActivity,
                            "QR Code lido: $result",
                            Toast.LENGTH_LONG
                        ).show()
                        
                        // Aqui você pode analisar o conteúdo e navegar para a tela apropriada
                        // Por exemplo, se for uma URL específica do seu app:
                        // if (result.startsWith("seuapp://")) {
                        //     val route = result.removePrefix("seuapp://")
                        //     navController.navigate(route)
                        // }
                    }
                    
                    AlunoNavHost(
                        navController = navController,
                        modifier = Modifier.fillMaxSize(),
                        onQRCodeScanned = handleQRCodeResult
                    )
                }
            }
        }
    }
}

// Placeholder para o seu tema - substitua pelo seu tema real
@Composable
fun YourAppTheme(content: @Composable () -> Unit) {
    MaterialTheme {
        content()
    }
}
-------------------------------------------------------------------------------------------


8. Implementação do componente CustomScreenScaffold
Finalmente, vamos implementar o seu CustomScreenScaffold com a navegação completa para o QR Code. 
O código que você já tem está bom, mas vamos garantir que a integração seja perfeita:
-------------------------------------------------------------------------------------------------
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomScreenScaffold(
    navController: NavHostController,
    needToGoBack: Boolean = false,
    onBackClick: () -> Unit,
    selectedItemIndex: Int,
    content: @Composable (Modifier) -> Unit,
    onMenuClick: () -> Unit = {}
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerContent = {
            Surface(color = Color(0xFF1E1E1E)) {
                DrawerContent(
                    navController = navController,
                    closeMenu = {
                        scope.launch { drawerState.close() }
                    },
                )
            }
        },
        drawerState = drawerState,
        gesturesEnabled = true,
    ) {
        val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

        val itemsUser = listOf(
            BottomNavigationItem("Home", NavIcon.DrawableIcon(R.drawable.home_icon), NavIcon.DrawableIcon(R.drawable.home_icon), false),
            BottomNavigationItem("Search", NavIcon.VectorIcon(Icons.Filled.Search), NavIcon.VectorIcon(Icons.Outlined.Search), false),
            BottomNavigationItem("QR Code", NavIcon.DrawableIcon(R.drawable.qr_code_icon), NavIcon.DrawableIcon(R.drawable.qr_code_icon), false),
            BottomNavigationItem("ChatBot", NavIcon.DrawableIcon(R.drawable.chat_icon_filled), NavIcon.DrawableIcon(R.drawable.chat_icon_outlined), false),
            BottomNavigationItem("Profile", NavIcon.VectorIcon(Icons.Filled.Person), NavIcon.VectorIcon(Icons.Outlined.Person), false)
        )

        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                if (needToGoBack) {
                    CenterAlignedTopAppBar(
                        title = { TitleScaffold() },
                        navigationIcon = {
                            IconButton(onClick = { onBackClick() }) {
                                Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "Voltar")
                            }
                        },
                        actions = {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(Icons.Default.Menu, contentDescription = "Menu")
                            }
                        },
                        scrollBehavior = scrollBehavior
                    )
                } else {
                    TopAppBar(
                        title = { TitleScaffold() },
                        actions = {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(Icons.Default.Menu, contentDescription = "Menu")
                            }
                        },
                        scrollBehavior = scrollBehavior
                    )
                }
            },
            bottomBar = {
                NavigationBar(
                    containerColor = Color(0xFF2B2B2B),
                    tonalElevation = 6.dp,
                    modifier = Modifier.clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
                ) {
                    itemsUser.forEachIndexed { index, item ->
                        if (index == 2) {
                            IconButton(
                                onClick = { 
                                    // Navega para a tela de QR Code
                                    navController.navigate(AlunoRoutes.QrCode) {
                                        // Opcional: popUpTo e launchSingleTop para controle de navegação
                                        launchSingleTop = true
                                    }
                                },
                                modifier = Modifier
                                    .padding(top = 0.dp)
                                    .background(Color.White, shape = CircleShape)
                                    .padding(3.dp)
                                    .background(Color(0xFF1E88E5), shape = CircleShape)
                                    .padding(6.dp)
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.qr_code_icon),
                                    contentDescription = item.title,
                                    tint = Color.White,
                                    modifier = Modifier.height(24.dp)
                                )
                            }
                        } else {
                            NavigationBarItem(
                                selected = selectedItemIndex == index,
                                onClick = {
                                    when (index) {
                                        0 -> navController.navigate(AlunoRoutes.Home) {
                                            popUpTo(navController.graph.findStartDestination().id) { 
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                        1 -> navController.navigate(AlunoRoutes.Search) {
                                            popUpTo(navController.graph.findStartDestination().id) { 
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                        3 -> navController.navigate(AlunoRoutes.Chatbot) {
                                            popUpTo(navController.graph.findStartDestination().id) { 
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                        4 -> navController.navigate(AlunoRoutes.Profile) {
                                            popUpTo(navController.graph.findStartDestination().id) { 
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                },
                                label = { Text(item.title, color = Color.White) },
                                alwaysShowLabel = true,
                                icon = {
                                    BadgedBox(
                                        badge = {
                                            when {
                                                item.badgeCount != null -> {
                                                    Badge { Text(text = item.badgeCount.toString()) }
                                                }
                                                item.hasNews -> {
                                                    Badge()
                                                }
                                            }
                                        }
                                    ) {
                                        val iconToShow = if (index == selectedItemIndex) item.selectedIcon else item.unselectedIcon
                                        when (iconToShow) {
                                            is NavIcon.VectorIcon -> {
                                                Icon(imageVector = iconToShow.icon, contentDescription = item.title)
                                            }
                                            is NavIcon.DrawableIcon -> {
                                                Icon(painter = painterResource(id = iconToShow.resId), contentDescription = item.title)
                                            }
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            content(Modifier.padding(innerPadding))
        }
    }
}

@Composable
fun TitleScaffold() {
    Text(text = "Academia App")
}

@Composable
fun DrawerContent(
    navController: NavHostController,
    closeMenu: () -> Unit
) {
    // Implementação do conteúdo do menu lateral
    // Você pode adicionar itens de menu, logo, etc.
    // Exemplo simples:
    Text(
        text = "Menu",
        color = Color.White,
        modifier = Modifier.padding(16.dp)
    )
}
-------------------------------------------------------------------------------------------------


9. Exemplo de uso do CustomScreenScaffold em uma tela
Aqui está um exemplo de como usar o CustomScreenScaffold em uma tela real:
-------------------------------------------------------------------------
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun ExampleScreen(navController: NavHostController) {
    // Defina o índice selecionado com base na tela atual
    // Por exemplo, para a tela Home:
    val selectedItemIndex = 0
    
    CustomScreenScaffold(
        navController = navController,
        needToGoBack = false, // Defina como true se precisar de botão voltar
        onBackClick = { /* Implemente lógica de voltar */ },
        selectedItemIndex = selectedItemIndex,
        content = { innerModifier ->
            val combinedModifier = innerModifier.padding(1.dp)
            LazyColumn(
                modifier = combinedModifier.fillMaxSize()
            ) {
                items(20) { index ->
                    Text(
                        text = "Item $index",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    )
}
-------------------------------------------------------------------------

Explicações:

Permissões e Dependências: Adicionamos as permissões necessárias para acessar a câmera e as dependências do CameraX e ML Kit para leitura de QR code.
QRCodeScreen: Esta é a tela principal que:

Solicita permissão de câmera (se necessário)
Mostra um preview da câmera
Analisa as imagens em busca de QR codes
Chama uma função de callback quando um QR code é encontrado


Navegação:

Configuramos a navegação para a tela de QR code
Definimos uma função de callback para processar o resultado do QR code
Na MainActivity, implementamos a lógica para lidar com o resultado do QR code


CustomScreenScaffold:

Mantivemos seu design atual
Melhoramos a navegação para a tela de QR code
Usamos o controle de navegação recomendado (popUpTo com saveState)


Implementação de tela de exemplo: Mostramos como usar o CustomScreenScaffold em uma tela real

Detalhes importantes de implementação:

Tratamento de permissões: A tela QRCodeScreen solicita permissão de câmera se necessário e exibe uma interface alternativa caso o usuário não conceda.
Análise de QR code em tempo real: O código configura um ImageAnalyzer que processa cada frame da câmera e detecta QR codes.
Navegação eficiente: Otimizamos a navegação para evitar criar múltiplas instâncias das telas principais quando o usuário troca entre elas.
Botão de QR code destacado: Mantivemos o design especial do botão de QR code na barra de navegação.
Callback de resultados: Implementamos um sistema de callback para processar os resultados do QR code de forma flexível.

Como testar e depurar (continuação):

Compile e execute o aplicativo no dispositivo físico (preferível, pois o emulador pode não ter suporte completo à câmera)
Teste a permissão de câmera - na primeira execução, o aplicativo deve solicitar permissão
Verifique se o botão QR code na navbar está destacado e funcionando corretamente
Confirme se a leitura de QR code funciona e se o callback é disparado com o conteúdo correto
Teste a navegação de retorno após a leitura do QR code para garantir que o usuário volte à tela anterior

Possíveis problemas e soluções:
1. Problemas de permissão
Se o usuário negar a permissão da câmera permanentemente, você pode precisar direcioná-lo para as configurações do aplicativo:

// Adicione essa função na QRCodeScreen
private fun openAppSettings(context: Context) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", context.packageName, null)
    }
    context.startActivity(intent)
}

2. Câmera não inicializa
Se a câmera não inicializar, verifique o LogCat para erros específicos. Problemas comuns incluem:

Conflito de uso (outro aplicativo usando a câmera)
Dispositivos sem câmera traseira
Permissão concedida mas com restrições

3. QR code não detectado
Algumas dicas para melhorar a detecção:

Certifique-se de que o QR code esteja bem iluminado
Adicione uma área guia na tela usando um overlay para ajudar o usuário
Ajuste o foco da câmera (você pode adicionar um modo de foco automático
