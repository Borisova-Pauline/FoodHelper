package com.tomli.foodhelper.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tomli.foodhelper.ui.theme.ProgressBackground

@Composable
fun AdditionalScreen(){
    Column(modifier= Modifier.padding(10.dp).fillMaxSize()){
        Card(modifier= Modifier.fillMaxWidth().height(520.dp), colors = CardDefaults.cardColors(containerColor = ProgressBackground)){
            Column(modifier= Modifier.padding(10.dp)){
                Text(text="Меню на день")
            }
        }
        Spacer(modifier= Modifier.height(10.dp))
        Row(modifier= Modifier.weight(1f)){
            Card(modifier= Modifier.weight(1f).fillMaxHeight(), colors = CardDefaults.cardColors(containerColor = ProgressBackground)){
                Column(modifier= Modifier.padding(10.dp)){
                    Text(text="Время еды")
                }
            }
            Spacer(modifier= Modifier.width(10.dp))
            Card(modifier= Modifier.weight(1f).fillMaxHeight(), colors = CardDefaults.cardColors(containerColor = ProgressBackground)){
                Column(modifier= Modifier.padding(10.dp)){
                    Text(text="Общая статистика")
                }
            }
        }
    }
}