package com.languageApp.wengu.data

sealed interface DataAction {
    data class Upsert(val dataObj: DataEntry) : DataAction
    data class Delete(val dataObj: DataEntry) : DataAction
    data class Sort(val type : SortType) : DataAction

    data object DeleteAllData : DataAction
}