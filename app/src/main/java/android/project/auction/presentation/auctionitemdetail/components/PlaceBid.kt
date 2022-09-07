package android.project.auction.presentation.auctionitemdetail.components

import android.project.auction.data.local.entity.SellerOrBidder
import android.project.auction.domain.model.item.ItemDetail
import android.project.auction.presentation.Screen
import android.project.auction.presentation.auctionitemdetail.AuctionItemDetailEvent
import android.project.auction.presentation.auctionitemdetail.AuctionItemDetailViewModel
import android.project.auction.presentation.auth.sign_in.dpToSp
import android.util.Log
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController

@Composable
fun PlaceBid(
    savedStateHandle: SavedStateHandle,
    navController: NavController,
    itemDetail: ItemDetail
) {


    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher


    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = Color.White,
                title = {
                    Row(
                        modifier = Modifier.padding(15.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text("Place a Bid")

                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        onBackPressedDispatcher?.onBackPressed()
                    }) {
                        Icon(Icons.Default.Clear, "Menu")
                    }
                },
                actions = {

                }
            )
        },
        content = {
            PlaceBidContent(itemDetail, navController)
        }
    )
}


@Composable
fun PlaceBidContent(
    item: ItemDetail,
    navController: NavController,
    auctionItemDetailViewModel: AuctionItemDetailViewModel = hiltViewModel()
) {

    val state = auctionItemDetailViewModel.statePlaceBid

    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher



    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.TopCenter
    ) {

        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                text = "Place bid amount ",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = dpToSp(2.dp)
                ),
                fontSize = dpToSp(30.dp)
            )
            Log.d("STATEBIDAMOUNT", state.bidAmount)
            OutlinedTextField(
                value = state.bidAmount,
                onValueChange = {
                    auctionItemDetailViewModel.onEvent(
                        AuctionItemDetailEvent.BidAmountChanged(amount = it, itemId = item.id)
                    )
                },
                label = { Text(text = "Amount", color = Color.Black) },
                placeholder = { Text(text = "Bid Amount", color = Color.Black) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(0.9f),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Black,
                    unfocusedBorderColor = Color.Black
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )

            Spacer(modifier = Modifier.size(10.dp))

            Button(
                onClick = {

                    auctionItemDetailViewModel.onEvent(
                        AuctionItemDetailEvent.OnBidAmountPlaced(
                            item.id
                        )
                    )

                    auctionItemDetailViewModel.setSellerOrBidder(
                        SellerOrBidder(
                            item.description,
                            item.endTime,
                            0,
                            item.id,
                            item.minIncrease,
                            item.pictures,
                            item.startTime,
                            item.startingPrice,
                            item.subCategoryId,
                            item.categoryId,
                            item.title,
                            item.userFullName,
                            item.userId,
                            "item.m",
                            "bidder",
                        )
                    )

                    navController.navigate(
                        Screen.AuctionItemDetailScreen.route + "/${item.id}"
                    )


                }, modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Black,
                    contentColor = Color.White
                )
            ) {
                Text(text = "Place Bid", fontSize = dpToSp(20.dp))
            }
        }
    }
}