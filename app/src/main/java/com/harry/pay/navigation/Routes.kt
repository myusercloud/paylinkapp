package com.harry.pay.navigation

const val ROUT_HOME = "home"
const val ROUT_ABOUT = "about"
const val ROUT_CREATE_LINK = "create_link"
const val ROUT_EDIT_LINK = "edit_link/{linkId}"
const val ROUT_PROFILE = "profile"
const val ROUT_SPLASH = "splash"
const val ROUT_SCAFFOLD = "scaffold"
const val ROUT_REGISTER = "Register"
const val ROUT_EDIT = "edit"
const val ROUT_LOGIN = "Login"

fun routeToEditLink(linkId: Int) = "edit_link/$linkId"
