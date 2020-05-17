package org.zoop.personhandler.broker

interface AccountClients {
    fun requestAccounts()
    fun assignAccounts(message : Map<Long, Int>)
}