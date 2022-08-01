package android.project.auction.domain.use_case

import android.project.auction.domain.use_case.getbidhistory.GetBidHistory
import android.project.auction.domain.use_case.getcategories.GetCategories
import android.project.auction.domain.use_case.getitemdetail.GetItemDetail
import android.project.auction.domain.use_case.getitems.GetItems

data class AuctionProjectUseCase(
    val getCategories: GetCategories,
    val getItems: GetItems,
    val getItemDetail: GetItemDetail,
    val getBidHistory: GetBidHistory
)
