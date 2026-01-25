package com.tomli.foodhelper.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tomli.foodhelper.R
import com.tomli.foodhelper.ui.theme.OnChosenGreen
import com.tomli.foodhelper.ui.theme.ProgressBackground

@Composable
fun AdditionalScreen(navController: NavController){
    val context= LocalContext.current
    Column(modifier= Modifier.padding(10.dp).fillMaxSize()){
        Card(modifier= Modifier.fillMaxWidth().height(520.dp), colors = CardDefaults.cardColors(containerColor = ProgressBackground),
            onClick = { Toast.makeText(context, "В разработке...", Toast.LENGTH_LONG).show()}){
            Column(modifier= Modifier.padding(10.dp)){
                Text(text="Меню на день")
                Spacer(modifier=Modifier.height(40.dp).padding(bottom = 5.dp).fillMaxWidth().background(color = Color(0xFFB8C4A6), shape = RoundedCornerShape(10.dp)))
                Spacer(modifier=Modifier.height(40.dp).padding(bottom = 5.dp).fillMaxWidth().background(color = Color(0xFFB8C4A6), shape = RoundedCornerShape(10.dp)))
                Spacer(modifier=Modifier.height(40.dp).padding(bottom = 5.dp).fillMaxWidth().background(color = Color(0xFFB8C4A6), shape = RoundedCornerShape(10.dp)))
                Spacer(modifier=Modifier.height(40.dp).padding(bottom = 5.dp).fillMaxWidth().background(color = Color(0xFFB8C4A6), shape = RoundedCornerShape(10.dp)))
                Spacer(modifier=Modifier.height(40.dp).fillMaxWidth().background(color = Color(0xFFB8C4A6), shape = RoundedCornerShape(10.dp)))
            }
        }
        Spacer(modifier= Modifier.height(10.dp))
        Row(modifier= Modifier.weight(1f)){
            Card(modifier= Modifier.weight(1f).fillMaxHeight(), colors = CardDefaults.cardColors(containerColor = ProgressBackground),
                onClick = { Toast.makeText(context, "В разработке...", Toast.LENGTH_LONG).show()}){
                Column(modifier= Modifier.padding(10.dp)){
                    Text(text="Время еды")
                    Image(painter = painterResource(R.drawable.clock_image), contentDescription = null,
                        colorFilter = ColorFilter.tint(OnChosenGreen), modifier=Modifier.fillMaxSize().align(Alignment.CenterHorizontally))
                }
            }
            Spacer(modifier= Modifier.width(10.dp))
            Card(modifier= Modifier.weight(1f).fillMaxHeight(), colors = CardDefaults.cardColors(containerColor = ProgressBackground),
                onClick = { navController.navigate("statistic")}){
                Column(modifier= Modifier.padding(10.dp)){
                    Text(text="Общая статистика")
                    Image(painter = painterResource(R.drawable.statistic_image), contentDescription = null,
                        colorFilter = ColorFilter.tint(OnChosenGreen), modifier=Modifier.fillMaxSize().align(Alignment.CenterHorizontally))
                }
            }
        }
    }
}