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

// Code available at https://developer.android.com/codelabs/jetpack-compose-theming#0

/* Changes for homework
1. Change cards color based on pokemon type
2. Change what the text on card displays
3. Change background color: set as default
4. Append 'Pokedex', 'Type' and 'Info' to be bold
5. Add pokemon images
6. Change background to a pokemon image
*/

package com.codelab.basics

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.codelab.basics.ui.theme.BasicsCodelabTheme
import com.codelab.basics.ui.theme.*

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get records from the DB
        val DBtest = DBClass(this@MainActivity)
        Log.d("CodeLab_DB", "onCreate: ")

        // Then the real data
        setContent {
            BasicsCodelabTheme {
                MyApp(modifier = Modifier.fillMaxSize()
                , names = DBtest.get2DRecords().toList())
            }
        }

    }
}

@Composable
fun MyApp(modifier: Modifier = Modifier,
          names: List<Array<String>>
) {
    var shouldShowOnboarding by rememberSaveable { mutableStateOf(true) }
    var invalidate by remember { mutableStateOf(names) }

    Surface(modifier, color = MaterialTheme.colorScheme.background) {
        if (shouldShowOnboarding) {
            OnboardingScreen(onContinueClicked = { shouldShowOnboarding = false })
        } else {
            Greetings(names=names)
        }
    }
}

@Composable
fun OnboardingScreen(
    onContinueClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
//        modifier = modifier.fillMaxSize(),
        modifier = with (Modifier) {
            fillMaxSize()
                .paint(
                    painterResource(id = R.drawable.cardbg),
                    contentScale = ContentScale.FillBounds
                )
        },
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            modifier = Modifier.padding(vertical = 48.dp),
            onClick = onContinueClicked
        ) {
            Text("View Pokedex")
        }
    }
}

@Composable
private fun Greetings(
    modifier: Modifier = Modifier,
    names: List<Array<String>>
) {
    LazyColumn(modifier = modifier.padding(vertical = 4.dp)) {
        items(items = names) { name ->
            Greeting(name = name)
            Log.d("CodeLab_DB", "name[0] = $name[0]")
            Log.d("CodeLab_DB", "name[1] = $name[1]")
        }
    }
}

@Composable
private fun Greeting(name: Array<String>) {
    // Need to get pokemonType to determine card color
    val pokemonType = name[1];

    fun getTypeColor(type: String): Color {
        return when (type.trim().lowercase()) {
            "fire" -> fire
            "water" -> water
            "grass" -> grass
            "fighting" -> fighting
            "bug" -> bug
            "flying" -> flying
            "poison" -> poison
            "electric" -> electric
            "ground" -> ground
            else -> cardBG
        }
    }

    val cardColor = getTypeColor(pokemonType)

    Card(
        colors = CardDefaults.cardColors(
            containerColor = cardColor
        ),
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        CardContent(name)
        Log.d("CodeLab_DB", "Access count: ${name[4]}")
    }
}

// WIll make changes to this
@Composable
private fun CardContent(name: Array<String>) {
    var expanded by remember { mutableStateOf(false) }

    val pokemonName = name[0]
    val type = name[1]
    val pokedexNum = name[2]
    val pokeInfo = name[3]
    val bold = SpanStyle(fontWeight = FontWeight.Bold)

    // Logging statements
    Log.d("CodeLab_DB", "pokedexNum = $pokedexNum ")
    Log.d("CodeLab_DB", "pokemonName = $pokemonName ")
    Log.d("CodeLab_DB", "type = $type ")
    Log.d("CodeLab_DB", "info = $pokeInfo ")

    // Images to load into cardview
    val pokeImg = when {
        pokemonName.equals("Bulbasaur", ignoreCase = true) -> R.drawable.bulbasaur
        pokemonName.equals("Charmander", ignoreCase = true) -> R.drawable.charmander
        pokemonName.equals("Squirtle", ignoreCase = true) -> R.drawable.squirtle
        pokemonName.equals("Caterpie", ignoreCase = true) -> R.drawable.caterpie
        pokemonName.equals("Pidgey", ignoreCase = true) -> R.drawable.pidgey
        pokemonName.equals("Ekans", ignoreCase = true) -> R.drawable.ekans
        pokemonName.equals("Pikachu", ignoreCase = true) -> R.drawable.pikachu
        pokemonName.equals("Diglett", ignoreCase = true) -> R.drawable.diglett
        pokemonName.equals("Machamp", ignoreCase = true) -> R.drawable.machamp
        else -> R.drawable.default_pokemon
    }

    Row(
        modifier = Modifier
            .padding(12.dp)
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(12.dp)
        ) {
            Image(painter = painterResource(id = pokeImg),
                contentDescription = null,
                modifier = Modifier
                    .size(72.dp)
                    .padding(8.dp)
            )
            Text(
                text = "$pokemonName", style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.ExtraBold
                )
            )
            if (expanded) {
                Text(buildAnnotatedString {
                    withStyle(style = bold) {
                        append("Pokedex: ")
                    }
                    append("$pokedexNum\n")
                    withStyle(style = bold) {
                        append("Type: ")
                    }
                    append("$type\n")
                    withStyle(style = bold) {
                        append("Info: ")
                    }
                    append("$pokeInfo")
                })
            }
        }

        IconButton(onClick = { expanded = !expanded }) {
            Icon(
                imageVector = if (expanded) Filled.ExpandLess else Filled.ExpandMore,
                contentDescription = if (expanded) {
                    stringResource(R.string.show_less)
                } else {
                    stringResource(R.string.show_more)
                }
            )
        }
    }
}

@Preview(
    showBackground = true,
    widthDp = 320,
    uiMode = UI_MODE_NIGHT_YES,
    name = "DefaultPreviewDark"
)

@Preview(showBackground = true, widthDp = 320, heightDp = 320)
@Composable
fun OnboardingPreview() {
    BasicsCodelabTheme {
        OnboardingScreen(onContinueClicked = {})
    }
}