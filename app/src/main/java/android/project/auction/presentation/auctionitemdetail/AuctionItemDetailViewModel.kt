package android.project.auction.presentation.auctionitemdetail

import android.project.auction.common.Resource
import android.project.auction.data.remote.dto.bids.HighestBid
import android.project.auction.domain.use_case.AuctionProjectUseCase
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuctionItemDetailViewModel @Inject constructor(
    private val useCase: AuctionProjectUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    var itemID: String = ""

    private val _state = mutableStateOf(AuctionItemDetailState())
    val state: State<AuctionItemDetailState> = _state

    var stateBidHistory by mutableStateOf(AuctionItemDetailState())

    var statePlaceBid by mutableStateOf(AuctionItemDetailState())

    var stateHighestBid by mutableStateOf(AuctionItemDetailState())

    private val resultChannel = Channel<Flow<Resource<Unit>>>()
    val bidResult = resultChannel.receiveAsFlow()

    init {
        savedStateHandle.get<String>("itemId")?.let { itemId ->
            getItemDetail(itemId)
            getBidHistory(itemId)
            getItemId(itemId)
            getHighestBid(itemId)
        }
    }

    private fun getItemId(itemId: String) {
        Log.d("GETTINGITEMID", itemId)
        itemID = itemId
    }

    fun onEvent(event: AuctionItemDetailEvent) {
        when (event) {
            is AuctionItemDetailEvent.BidAmountChanged -> {
                statePlaceBid = statePlaceBid.copy(
                    bidAmount = event.amount,
                    itemID = event.itemId
                )
            }
            is AuctionItemDetailEvent.OnBidAmountPlaced -> {
                placeBidAmount()
            }
        }
    }

    private fun getBidHistory(itemId: String) {
        useCase.getBidHistory.invoke(itemId = itemId).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    Log.d("RESOURCELOADING", result.data.toString())

                    stateBidHistory = stateBidHistory.copy(
                        isBidHistoryLoading = true
                    )
                }
                is Resource.Success -> {
                    Log.d("RESOURCESUCCES", result.data.toString())
                    result.data?.let { listings ->
                        stateBidHistory = stateBidHistory.copy(
                            bidHistory = listings
                        )
                    }
                }
                is Resource.Error -> {
                    Log.d("RESORCE ERROR", result.data.toString())

                    stateBidHistory = stateBidHistory.copy(
                        bidError = "Error Occured"
                    )
                }
            }
        }.launchIn(viewModelScope)
    }


    private fun getItemDetail(itemId: String) {
        useCase.getItemDetail.invoke(itemId).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    Log.d("TSTHİHESGTBID", result.data.toString())

                    _state.value = AuctionItemDetailState(
                        itemDetails = result.data
                    )
                }
                is Resource.Loading -> {
                    _state.value = AuctionItemDetailState(
                        isItemDetailLoading = true
                    )
                }
                is Resource.Error -> {
                    _state.value = AuctionItemDetailState(
                        error = result.message ?: "Cannot find such an item"
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun placeBidAmount() {
        viewModelScope.launch {
            stateBidHistory = stateBidHistory.copy(
                postingBidAmount = true
            )

            useCase.placeBidAmount.invoke(
                amount = statePlaceBid.bidAmount.toInt(),
                itemId = itemID
            )

            stateBidHistory = stateBidHistory.copy(
                postingBidAmount = false
            )
        }
    }

    private fun getHighestBid(itemId: String) {
        useCase.getHighestBid.invoke(itemId).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    stateHighestBid = stateHighestBid.copy(
                        highestBidLoading = true
                    )
                }
                is Resource.Success -> {
                    stateHighestBid = stateHighestBid.copy(
                        highestBid = result.data ?: HighestBid(
                            0.0,
                            "", "", "", ""
                        )
                    )
                }
                is Resource.Error -> {
                    stateHighestBid = stateHighestBid.copy(
                        error = result.message ?: "ERROR OCCURED"
                    )
                }
            }
        }.launchIn(viewModelScope)
    }
}