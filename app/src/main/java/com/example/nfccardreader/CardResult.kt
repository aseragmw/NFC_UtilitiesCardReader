package com.example.nfccardreader

class CardResult (
    val detected: Boolean,
    val serial:String,
    val type: CardType,
)