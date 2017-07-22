package de.henninglanghorst.kotlinstuff.currying


fun <A, B, R, Func : (A, B) -> R> Func.curried(a: A) = fun(b: B) = this(a, b)
fun <A, B, C, R, Func : (A, B, C) -> R> Func.curried(a: A) = fun(b: B, c: C) = this(a, b, c)
fun <A, B, C, R, Func : (A, B, C) -> R> Func.curried(a: A, b: B) = fun(c: C) = this(a, b, c)

fun <A, B, C, D, R, Func : (A, B, C, D) -> R> Func.curried(a: A) = fun(b: B, c: C, d: D) = this(a, b, c, d)
fun <A, B, C, D, R, Func : (A, B, C, D) -> R> Func.curried(a: A, b: B) = fun(c: C, d: D) = this(a, b, c, d)
fun <A, B, C, D, R, Func : (A, B, C, D) -> R> Func.curried(a: A, b: B, c: C) = fun(d: D) = this(a, b, c, d)

