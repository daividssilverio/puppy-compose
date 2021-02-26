/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.navigate
import androidx.navigation.compose.rememberNavController
import com.example.androiddevchallenge.model.Pet
import com.example.androiddevchallenge.ui.theme.MyTheme
import dev.chrisbanes.accompanist.coil.CoilImage

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTheme {
                val navController = rememberNavController()
                val pets = (1..100).map { Pet("Cutie #$it") }.toList()

                NavHost(navController, startDestination = "pets") {
                    composable("pets") {
                        PetAdoption(
                            pets,
                            navigateToPetDetails = { pet ->
                                navController.navigate("details?petIndex=${pets.indexOf(pet)}")
                            }
                        )
                    }
                    composable(
                        "details?petIndex={petIndex}",
                        arguments = listOf(
                            navArgument("petIndex") {
                                defaultValue = -1
                            }
                        )
                    ) { backStackEntry ->
                        val petIndex = backStackEntry.arguments?.getInt("petIndex")
                        petIndex?.let {
                            PetDetails(pet = pets[petIndex])
                        } ?: navController.navigate("pets")
                    }
                }
            }
        }
    }
}

@Composable
fun PetDetails(pet: Pet) {
    Box(Modifier.fillMaxSize()) {
        PetCard(pet = pet) {}
    }
}

// Start building your app here!
@Composable
fun PetAdoption(pets: List<Pet>, navigateToPetDetails: (Pet) -> Unit) {
    Surface(color = MaterialTheme.colors.background) {
        Box {
            LazyColumn(contentPadding = PaddingValues(16.dp)) {
                items(pets) {
                    PetCard(pet = it, navigateToPetDetails)
                }
            }
        }
    }
}

@Composable
private fun PetCard(pet: Pet, onClick: (Pet) -> Unit) {
    Card(
        elevation = 4.dp,
        backgroundColor = MaterialTheme.colors.surface,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
            .height(280.dp)
            .clickable { onClick(pet) }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            PetImage(pet)
            PetInfoLabel(pet.name)
        }
    }
}

@Composable
private fun PetInfoLabel(label: String) {
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Bottom) {
        Box(
            Modifier
                .background(Color.White)
                .border(
                    width = 1.dp,
                    color = Color.Black.copy(alpha = 0.2f),
                    shape = MaterialTheme.shapes.medium.copy(
                        topEnd = CornerSize(0),
                        topStart = CornerSize(0)
                    )
                )
                .fillMaxWidth()
                .wrapContentSize()
        ) {
            Text(
                label,
                modifier = Modifier
                    .padding(16.dp)
            )
        }
    }
}

@Composable
private fun PetImage(it: Pet) {
    CoilImage(
        data = "https://robohash.org/${it.name.hashCode()}?set=set4&size=160x160&bgset=any",
        contentScale = ContentScale.FillWidth,
        contentDescription = "A cat",
        loading = {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        },
        error = {
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_baseline_error_outline_24),
                "Error indicator"
            )
        },
        modifier = Modifier.fillMaxSize(),
    )
}

@Preview("Light Theme", widthDp = 360, heightDp = 640)
@Composable
fun LightPreview() {
    MyTheme {
        PetAdoption(emptyList()) {}
    }
}

@Preview("Dark Theme", widthDp = 360, heightDp = 640)
@Composable
fun DarkPreview() {
    MyTheme(darkTheme = true) {
        PetAdoption(emptyList()) {}
    }
}
