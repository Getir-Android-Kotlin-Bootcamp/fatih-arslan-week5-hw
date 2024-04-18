package org.example

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ticker
import kotlinx.coroutines.flow.*
import java.net.URL

fun page6() {
    // Starting a coroutine
    CoroutineScope(Dispatchers.IO).launch {
        delay(3000)
        println("Coroutine completed")
    }
}

fun page7() {
    // Async
    val deferredOne = GlobalScope.async {
        "Hello"
    }

    val deferredTwo = GlobalScope.async {
        " World"
    }

    runBlocking {
        val message = deferredOne.await() + deferredTwo.await()
        println(message)
    }
}

fun page9() {
    // UI update on Main Thread
    var text = ""
    CoroutineScope(Dispatchers.Main).launch {
        println("Main Thread: ${Thread.currentThread().name}")
        text = "Hello World"
    }
}

fun page10() {
    // IO Thread, fetch data from INTERNET
    CoroutineScope(Dispatchers.IO).launch {
        val url = "https://example.com"
        val response = URL(url).readText()
        println(response)
    }
}

fun page11() {
    // Default
    CoroutineScope(Dispatchers.Default).launch {
        var product = 1
        for (i in 1..1000000) {
            product *= i
        }
        println("Product: $product")
    }
}

fun page12() {
    // Coroutine with context
    CoroutineScope(Dispatchers.IO).launch {
        withContext(Dispatchers.Default) {
            var product = 1
            for (i in 1..1000000) {
                product *= i
            }
            println("Product: $product")
        }
    }

    val scope = CoroutineScope(Dispatchers.Default)
    fun myCoroutine(job: Job) {
        scope.launch(job) {
            var product = 1
            for (i in 1..1000000) {
                product *= i
            }
            println("Product: $product")
        }
    }

    val job = Job()
    myCoroutine(job)
}

fun page13() {
    // Job
    val job = Job()
    val scope = CoroutineScope(Dispatchers.Default + job)
    scope.launch {
        var product = 1
        for (i in 1..1000000) {
            product *= i
        }
        println("Product: $product")
    }
}

fun page15_16() {
    // List<Int> vs Flow<Int>
    val list = listOf(1, 2, 3, 4, 5)
    list.forEach {
        Thread.sleep(1000)
        println(it)
    }

    val flow = flow {
        emit(1)
        emit(2)
        emit(3)
        emit(4)
        emit(5)
    }

    CoroutineScope(Dispatchers.Default).launch {
        flow.collect {
            delay(1000)
            println(it)
        }
    }
}

fun page17() {
    // scope types
    val scope = CoroutineScope(Dispatchers.Default)
    scope.launch {
        println("Default")
    }
    val scope2 = CoroutineScope(Dispatchers.IO)
    scope2.launch {
        println("IO")
    }
    val scope3 = CoroutineScope(Dispatchers.Main)
    scope3.launch {
        println("Main")
    }
    val scope4 = CoroutineScope(Dispatchers.Unconfined)
    scope4.launch {
        println("Unconfined")
    }
    val scope5 = CoroutineScope(Dispatchers.Main.immediate)
    scope5.launch {
        println("Main Immediate")
    }
}

fun page18() {
    // SupervisorJob
    val parentJob = SupervisorJob()
    val scope = CoroutineScope(Dispatchers.Default + parentJob)
    scope.launch {
        delay(1000)
        println("Child 1")
    }
    scope.launch {
        delay(1000)
        println("Child 2")
    }
}

fun page19() {
    // Unsupervised Job
    val parentJob = Job()
    val scope = CoroutineScope(Dispatchers.Default + parentJob)
    scope.launch {
        delay(1000)
        println("Child 1")
    }
}

fun page20() {
    // Main scope
    val scope = MainScope()
    scope.launch {
        println("MainScope") // for UI updates
    }
}

suspend fun page21() {
    // SupervisorScope
    val scope = supervisorScope {
        launch {
            delay(1000)
            println("Child 1")
        }
        launch {
            delay(1000)
            println("Child 2")
        }
    }
}

suspend fun page23() {
    // flow kinds normal
    val flow = flow {
        emit(1)
        emit(2)
        emit(3)
        emit(4)
        emit(5)
    }

    flow.collect {
        println(it)
    }
}

suspend fun page24() {
    // asFlow
    val list = listOf(1, 2, 3, 4, 5)
    list.asFlow().collect {
        println(it)
    }
}

suspend fun page25_26() {
    // map and filter
    val flow = flow {
        emit(1)
        emit(2)
        emit(3)
        emit(4)
        emit(5)
    }

    flow.map {
        it * 2
    }.filter {
        it % 2 == 0
    }.collect {
        println(it)
    }
}

suspend fun page27() {
    // transform
    val flow = flow {
        emit(1)
        emit(2)
        emit(3)
        emit(4)
        emit(5)
    }

    flow.transform {
        emit(it * 2)
        emit(it * 3)
    }.collect {
        println(it)
    }
}

fun page31(): Job = runBlocking {
    // rendezvous channel
    val channel = Channel<Int>()
    val scope = CoroutineScope(Dispatchers.Default)
    scope.launch {
        repeat(5) {
            channel.send(it)
        }
    }

    scope.launch {
        repeat(5) {
            println(channel.receive())
        }
    }
}

fun page32() = runBlocking {
    // unlimited channel
    val channel = Channel<String>()
    launch {
        repeat(5) {
            channel.send("Message $it")
        }
    }

    launch {
        while (true) {
            println(channel.receive())
        }
    }
}

fun page33() = runBlocking {
    // Ticker
    val ticker = ticker(delayMillis = 1000, initialDelayMillis = 0)
    launch {
        println("Ticker started")
        while (true) {
            ticker.receive()
            println("Tick")
        }
    }

    delay(5000L)
}

suspend fun main() {
    page6()
    // page31().join()
    page33()
}
