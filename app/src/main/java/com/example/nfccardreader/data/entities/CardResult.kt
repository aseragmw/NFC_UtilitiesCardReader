package com.example.nfccardreader.data.entities

class CardResult (
    val detected: Boolean,
    val serial:String,
    val type: CardType,
    val buffer:String
)