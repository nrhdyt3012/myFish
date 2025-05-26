package com.example.myfish

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.myfish.data.FishRepository
import com.example.myfish.ui.theme.MyFishTheme

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MyFishApp(
    modifier: Modifier = Modifier,
    viewModel: MyFishViewModel = viewModel(factory = ViewModelFactory(FishRepository()))
) {
    val navController = rememberNavController()
    val groupedFishes by viewModel.groupedFishes.collectAsState()
    val query by viewModel.query

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("My FishApp") },
                        actions = {
                            IconButton(onClick = { navController.navigate("about") }) {
                                Icon(
                                    imageVector = Icons.Default.AccountCircle, // Ikon About
                                    contentDescription = "about_page",
                                    modifier = Modifier.size(32.dp),
                                    tint = Color.White
                                )
                            }
                        },
                        colors = TopAppBarDefaults.mediumTopAppBarColors( // Sesuaikan dengan tema
                            containerColor = MaterialTheme.colorScheme.primary,
                            titleContentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                }
            ) { paddingValues ->
                LazyColumn(contentPadding = paddingValues) {
                    item {
                        SearchBar(
                            query = query,
                            onQueryChange = viewModel::search,
                            modifier = Modifier.background(MaterialTheme.colorScheme.primary)
                        )
                    }
                    groupedFishes.forEach { (initial, fishes) ->
                        stickyHeader { CharacterHeader(initial) }
                        items(fishes, key = { it.id }) { fish ->
                            FishListItem(
                                name = fish.name,
                                photoUrl = fish.photoUrl,
                                onClick = { navController.navigate("detail/${fish.id}") }
                            )
                        }
                    }
                }
            }
        }

        composable("detail/{fishId}") { backStackEntry ->
            val fishId = backStackEntry.arguments?.getString("fishId")?.toIntOrNull()
            val fish = viewModel.getFishById(fishId)
            fish?.let { DetailScreen(it) }
        }

        composable("about") { AboutScreen() }
    }
}



@Composable
fun CharacterHeader(
    char: Char,
    modifier: Modifier = Modifier
) {
    Surface(
        color = MaterialTheme.colorScheme.primary,
        modifier = modifier
    ) {
        Text(
            text = char.toString(),
            fontWeight = FontWeight.Black,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    SearchBar(
        inputField = {
            SearchBarDefaults.InputField(
                query = query,
                onQueryChange = onQueryChange,
                onSearch = {},
                expanded = false,
                onExpandedChange = {},
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                placeholder = {
                    Text(stringResource(R.string.search_hero))
                },
            )
        },
        expanded = false,
        onExpandedChange = {},
        shape = MaterialTheme.shapes.large,
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
            .heightIn(min = 48.dp),
    ) {
    }
}


@Composable
fun FishListItem(
    name: String,
    photoUrl: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.clickable {onClick()}
    ) {
        AsyncImage(
            model = photoUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(8.dp)
                .size(60.dp)
                .clip(CircleShape)
        )
        Text(
            text = name,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(start = 16.dp)
        )
    }
}


@Preview(showBackground = true)
@Composable
fun FishListItemPreview() {
    MyFishTheme {
        FishListItem(
            name = "Ikan Cupang",
            onClick = {},
            photoUrl = ""
        )
    }
}
    @Preview(showBackground = true)
    @Composable
    fun MyFishAppPreview() {
        MyFishTheme  {
            MyFishApp()
        }
    }
