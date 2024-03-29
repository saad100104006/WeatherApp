package com.app.weather.presentation

import android.Manifest
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.challange.weather.presentation.utils.LocationHelper
import com.challange.weather.presentation.utils.LocationHelper.Companion.REQUEST_CODE_RESOLVABLE_API
import com.google.android.gms.common.api.ResolvableApiException
import com.muddassir.connection_checker.ConnectionState
import com.muddassir.connection_checker.ConnectivityListener
import com.muddassir.connection_checker.checkConnection
import dagger.hilt.android.AndroidEntryPoint
import com.app.weather.R
import com.app.weather.domain.models.FavoriteLocationModel
import com.app.weather.domain.models.SearchResultModel
import com.app.weather.domain.models.WeatherData
import com.app.weather.infrastructure.util.observeAsAction
import com.app.weather.infrastructure.util.observeOnce
import com.app.weather.presentation.framework.theme.XWeatherTheme
import com.app.weather.presentation.screen.WeatherNavigationRoute
import com.app.weather.presentation.screen.home.HomeViewModel
import com.app.weather.presentation.screen.home.WeatherDetailScreen
import com.app.weather.presentation.screen.search.SearchScreen
import com.app.weather.presentation.screen.search.SearchViewModel
import com.app.weather.presentation.screen.weather_detail.WeatherDetailViewModel


@AndroidEntryPoint
class MainActivity : ComponentActivity(), LocationHelper.OnLocationCompleteListener,
    ConnectivityListener {

    private val mainViewModel: MainViewModel by viewModels()
    private val homeViewModel: HomeViewModel by viewModels()
    private var locationHelper: LocationHelper? = null
    private var cityAddressGlobal: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpLocationServices()
        checkConnection(this)
        setContent {
            CustomLoadingOverlay()
        }

        //observe on refresh location button click
        homeViewModel.isRefreshedCalledLiveData.observe(this){
            setContent {
                MainContent()
            }
        }

        //observe updates from location
        homeViewModel.isLocationUpdated.observeOnce(this) {
            if (it) {
                setContent {
                    MainContent()
                }
            }
        }

        homeViewModel.errorLiveData.observe(this){
            Toast.makeText(
                this,
                it,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun MainContent(){
        XWeatherTheme {
            // A surface container using the 'background' color from the theme
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = WeatherNavigationRoute.Home.getName()
            ) {
                composable(
                    WeatherNavigationRoute.Home.getName()
                ) {
                    Scaffold {
                        HomeScreenPage(
                            paddingValues = it,
                            onSearchIconTapped = mainViewModel::onNavigateToSearch,
                            weatherDataState = homeViewModel.forecastedWeatherData.observeAsState(),
                        )
                    }
                }
                composable(WeatherNavigationRoute.Search.getName()) {
                    val searchViewModel = hiltViewModel<SearchViewModel>()
                    Scaffold() {
                        SearchPage(
                            paddingValues = it,
                            onBackIconTapped = mainViewModel::onNavigateBack,
                            searchStringState = searchViewModel.searchString.observeAsState(),
                            onSearchStringChanged = searchViewModel::onSearchStringChanged,
                            isClearButtonVisible = searchViewModel.isClearSearchIconVisible.observeAsState(),
                            onRecommendationItemTapped = { searchResultModel ->
                                searchViewModel.onSearchRecommendationItemClicked(
                                    searchResultModel
                                )
                                mainViewModel.onShowWeatherDetail(searchResultModel.toFavoriteLocationModel())
                            },
                            onSearchFieldValueCleared = searchViewModel::onSearchFieldValueCleared,
                            recommendationResultState = searchViewModel.autocompleteResult.observeAsState()

                        )
                    }
                }
                composable(
                    route = WeatherNavigationRoute.SearchResult.getName(), arguments =
                    WeatherNavigationRoute.SearchResult.args
                ) {
                    val viewModel = hiltViewModel<WeatherDetailViewModel>()
                    Scaffold() {
                        SearchDetailPage(
                            paddingValues = it,
                            onNavigateBack = mainViewModel::onNavigateBack,
                            weatherDataState = viewModel.forecastedWeatherData.observeAsState(),
                            onSaveLocationTapped = viewModel::onSaveLocationTapped,
                        )
                    }
                }
                registerObservers(navController = navController)

            }

        }
    }

    private fun registerObservers(navController: NavController) {
        mainViewModel.onShowWeatherDetail.observeAsAction(
            lifecycleOwner = this,
            action = {
                navController.navigate(WeatherNavigationRoute.SearchResult.getNavigationRequest(it.toStringArg()))
            },
            onActionResolved = mainViewModel::onShowWeatherDetailResolved
        )
        mainViewModel.onNavigateToSearch.observeAsAction(
            lifecycleOwner = this,
            action = {
                navController.navigate(WeatherNavigationRoute.Search.getName())
            },
            onActionResolved = mainViewModel::onNavigateToSearchResolved
        )
        mainViewModel.onNavigateBack.observeAsAction(
            lifecycleOwner = this,
            action = {
                navController.popBackStack()
            },
            onActionResolved = mainViewModel::onNavigateBackResolved
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun HomeScreenPage(
        paddingValues: PaddingValues,
        onSearchIconTapped: () -> Unit,
        weatherDataState: State<List<WeatherData.Daily>?>
    ) {
        Scaffold(modifier = Modifier.padding(paddingValues),
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        homeViewModel.refreshFavoriteLocationData(cityAddressGlobal, isRefreshedCalled = true, isGetLocationUpdate = false)
                              },
                    shape = CircleShape,
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = null
                    )
                }
            }
        ) { it ->

            WeatherDetailScreen(
                weatherDataState = weatherDataState,
                paddingValues = it,
                onAppBarStartIconTapped = {},
                onAppBarEndIconTapped = onSearchIconTapped,
                appbarEndIcon = ImageVector.vectorResource(id = R.drawable.ic_search)
            )

        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun SearchDetailPage(
        paddingValues: PaddingValues,
        onNavigateBack: () -> Unit,
        weatherDataState: State<List<WeatherData.Daily>?>,
        onSaveLocationTapped: () -> Unit
    ) {

        Scaffold(modifier = Modifier.padding(paddingValues)) { it ->
            WeatherDetailScreen(
                weatherDataState = weatherDataState,
                paddingValues = it,
                onAppBarStartIconTapped = onNavigateBack,
                appbarStartIcon = ImageVector.vectorResource(id = R.drawable.ic_back),
                onAppBarEndIconTapped = onSaveLocationTapped
            )

        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun SearchPage(
        paddingValues: PaddingValues,
        onBackIconTapped: () -> Unit,
        searchStringState: State<String?>,
        onSearchStringChanged: (String) -> Unit,
        recommendationResultState: State<List<SearchResultModel>?>,
        onRecommendationItemTapped: (SearchResultModel) -> Unit,
        onSearchFieldValueCleared: () -> Unit,
        isClearButtonVisible: State<Boolean?>
    ) {
        Scaffold(modifier = Modifier.padding(paddingValues)) { paddingValues ->
            SearchScreen(
                searchFieldValue = searchStringState,
                onSearchFieldValueChanged = onSearchStringChanged,
                recommendationResult = recommendationResultState,
                onRecommendationClicked = onRecommendationItemTapped,
                paddingValues = paddingValues,
                onSearchFieldValueCleared = onSearchFieldValueCleared,
                isClearSearchQueryVisible = isClearButtonVisible,
                onBackIconTapped = onBackIconTapped
            )
        }
    }

    override fun getLocationUpdate(cityAddress: String?) {
            if (cityAddress != null) {
                    cityAddressGlobal = cityAddress
                    homeViewModel.refreshFavoriteLocationData(cityAddress, isGetLocationUpdate = true, isRefreshedCalled = false)
                } else {
                Toast.makeText(
                    this,
                    resources.getString(R.string.error_location),
                    Toast.LENGTH_LONG
                ).show()
            }
    }

    override fun onError(resolvableApiException: ResolvableApiException?, error: String?) {
        try {
            resolvableApiException?.startResolutionForResult(
                this, REQUEST_CODE_RESOLVABLE_API
            )
        } catch (e: IntentSender.SendIntentException) {
            Toast.makeText(
                this,
                e.message,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onResolvableApiResponseFailure() {

    }

    private fun setUpLocationServices(isRefreshed: Boolean = false) {
        val hasGetLocationPermission: Int = ActivityCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_FINE_LOCATION)
        if (hasGetLocationPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE_RESOLVABLE_API
            )
        } else {
            initializeLocationHelper(isRefreshed)
        }
    }

    /*Initialize the location helper*/
    private fun initializeLocationHelper(isRefreshed: Boolean = false) {
        locationHelper = LocationHelper(this, this)
        locationHelper?.startLocationUpdates()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_RESOLVABLE_API) {
            locationHelper?.onActivityResult(requestCode, resultCode)
        }
    }

    /*after approving location permission*/
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions!!, grantResults)
        when (requestCode) {
            REQUEST_CODE_RESOLVABLE_API -> {
                initializeLocationHelper()
            }
        }
    }


    /*Initial Loading Display*/
    @Composable
    fun CustomLoadingOverlay(modifier: Modifier = Modifier) {
        Box(
            modifier = modifier
                .fillMaxHeight()
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                color = Color.Blue
            )
        }
    }

    override fun onConnectionState(state: ConnectionState) {
        when (state) {
            ConnectionState.CONNECTED -> {
                /*start fetching location updates*/
                setUpLocationServices()
            }
            ConnectionState.DISCONNECTED -> {
                Toast.makeText(
                    this,
                    resources.getString(R.string.internet_disconnected),
                    Toast.LENGTH_LONG
                ).show()
            }
            else -> {

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        /*stop fetching location updates*/
        locationHelper?.stopLocationUpdates()

    }

}