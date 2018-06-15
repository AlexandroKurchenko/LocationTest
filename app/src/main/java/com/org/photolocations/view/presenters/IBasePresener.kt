package com.org.photolocations.view.presenters

interface IBasePresenter<in V> {
    fun onViewAttached(view: V)
    fun onViewDetach()
}