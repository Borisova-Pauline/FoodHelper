package com.tomli.foodhelper.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.tomli.foodhelper.ui.theme.ChosenGreen
import com.tomli.foodhelper.ui.theme.OnChosenGreen

@Composable
fun NavigationBarButton(value: NavBarButtons, chosenVal: NavBarButtons,icon: Int, onClick:()->Unit){
    Box(contentAlignment = Alignment.Center, modifier= Modifier.clickable { onClick() }){
        Spacer(modifier= Modifier.size(55.dp, 45.dp).background(color= if(value==chosenVal) ChosenGreen else Color.Transparent, shape = RoundedCornerShape(7.dp)))
        Image(painter= painterResource(icon), contentDescription = null,
            modifier= Modifier.size(35.dp), colorFilter = ColorFilter.tint(if(value==chosenVal) OnChosenGreen else Color.White))
    }
}

enum class NavBarButtons{
    AdditionalSections, FoodDiary, FoodDB, Profile
}