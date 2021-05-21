package com.ndhzs.bee_task1.list

import java.util.*
import kotlin.Comparator

/**
 *@author 985892345
 *@email 2767465918@qq.com
 *@data 2021/5/18
 *@description 双向循环链表，与上次红岩作业写的有些不同，这次参考了 LinkedList 的源码
 */
class MyLinkedList<E>(c: Collection<E>? = null) : Iterable<E> {

    private var size = 0;
    private var first: Node<E>? = null
    private var last: Node<E>? = null

    init {
        c?.let {
            addAll(it)
        }
    }

    fun add(e: E) {
        if (size == 0) {
            val node = Node(e)
            node.prev = node
            node.next = node
            first = node
            last = node
        }else {
            val node = Node(e, last!!, first!!)
            first!!.prev = node
            last!!.next = node
            last = node
        }
        size++
    }

    fun add(index: Int, e: E) {
        checkPositionIndex(index)
        if (index == size || size == 0) {
            add(e)
        }else {
            val now = getNode(index)
            val prev = now.prev!!
            val node = Node(e, prev, now)
            prev.next = node
            now.prev = node
            if (index == 0) {
                first = node
            }
            size++
        }
    }

    fun addAll(c: Collection<E>) {
        addAll(size, c)
    }

    fun addAll(index: Int, c: Collection<E>) {
        checkPositionIndex(index)
        if (c.isEmpty()) {
            throw NoSuchElementException("Collection is empty.")
        }
        c.forEach {
            add(it)
        }
    }

    fun addFirst(e: E) {
        if (size == 0) {
            add(e)
        }else {
            val node = Node(e, last!!, first!!)
            first!!.prev = node
            last!!.next = node
            first = node
            size++
        }
    }

    fun addLast(e: E) {
        add(e)
    }

    fun get(index: Int): E {
        checkElementIndex(index)
        return getNode(index).item!!
    }

    fun getFirst(): E {
        checkElementIndex(0)
        return first!!.item!!
    }

    fun getLast(): E {
        checkElementIndex(0)
        return last!!.item!!
    }

    fun removeAt(index: Int): E {
        checkElementIndex(index)
        val now = getNode(index)
        val item: E
        if (size > 1) {
            val prev = now.prev!!
            val next = now.next!!
            prev.next = next
            next.prev = prev
            item = now.item!!
            now.item = null
            now.prev = null
            now.next = null
            size--
            if (index == 0) {
                first = next
            }else if (index == size - 1){
                last = prev
            }
        }else {
            item = now.item!!
            now.item = null
            now.prev = null
            now.next = null
            first = null
            last = null
            size--
        }
        return item
    }

    fun remove(e: E): Boolean {
        checkElementIndex(0)
        var index = 0
        var node = first!!
        if (size > 1) {
            while (index < size) {
                if (node.item == e) {
                    val prev = node.prev!!
                    val next = node.next!!
                    prev.next = next
                    next.prev = prev
                    node.item = null
                    node.prev = null
                    node.next = null
                    size--
                    if (index == 0) {
                        first = next
                    }else if(index < size - 1) {
                        last = prev
                    }
                    return true
                }
                node = node.next!!
                index++
            }
        }else {
            if (first!!.item == e) {
                node.item = null
                node.prev = null
                node.next = null
                first = null
                last = null
                size--
                return true
            }
        }
        return false
    }

    fun set(index: Int, e: E) {
        checkElementIndex(index)
        val now = getNode(index)
        now.item = e
    }

    fun size(): Int {
        return size
    }

    /**
     * 这个排序，官方的也是转换为Array再排序然后强转回来
     */
    fun sort(comparator: Comparator<E>) {
        val a = toArray()
        a.sortWith(comparator as Comparator<Any>)
        var i = 0
        var node = first!!
        while (i < size) {
            node.item = a[i] as E
            node = node.next!!
            i++
        }
    }

    fun toArray(): Array<Any> {
        var x = first
        return Array(size) {
            val item = x!!.item
            x = x!!.next
            item!!
        }
    }

    private fun checkElementIndex(index: Int) {
        if (index < 0 || index >= size) {
            throw IndexOutOfBoundsException("index = $index, Size = $size")
        }
    }

    private fun checkPositionIndex(index: Int) {
        if (index < 0 || index > size) {
            throw IndexOutOfBoundsException("index = $index, Size = $size")
        }
    }

    private fun getNode(index: Int): Node<E> {
        return if (index <= size/2) {
            var node = first;
            repeat(index) {
                node = node!!.next
            }
            node!!
        }else {
            var node = last
            repeat(size - 1 - index) {
                node = node!!.prev
            }
            node!!
        }
    }

    private class Node<E> (var item: E?) {

        var prev: Node<E>? = null
        var next: Node<E>? = null
        constructor(item: E?, prev: Node<E>, next: Node<E>) : this(item) {
            this.prev = prev
            this.next = next
        }
    }

    override fun iterator(): Iterator<E> {
        return MyIterator()
    }

    private inner class MyIterator : Iterator<E> {
        var index = 0
        var current = first
        override fun hasNext(): Boolean {
            return index < size
        }

        override fun next(): E {
            if (!hasNext()) throw NoSuchElementException()
            val currentItem = current!!.item!!
            current = current!!.next
            index++
            return currentItem
        }
    }
}