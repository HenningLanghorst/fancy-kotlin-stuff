package de.henninglanghorst.kotlinstuff.currying


import org.junit.jupiter.api.Test
import kotlin.test.assertEquals


class CurryingTest {

    @Test
    fun `function with 2 parameters - partial function with 1 parameter`() {
        // GIVEN
        val add = fun(a: Int, b: Int) = a + b
        //WHEN
        val increment = add.curried(1)
        // THEN
        assertEquals(add(1, 2), increment(2), "Should add 1 to 2")
    }


    @Test
    fun `function with 3 parameters - partial function with 1 parameter`() {
        // GIVEN
        val add = fun(a: Int, b: Int, c: Int) = a + b + c
        //WHEN
        val partialFunction1 = add.curried(1)
        // THEN
        assertEquals(add(1, 2, 3), partialFunction1(2, 3))
    }

    @Test
    fun `function with 3 parameters - partial function with 2 parameters`() {
        // GIVEN
        val add = fun(a: Int, b: Int, c: Int) = a + b + c
        //WHEN
        val partialFunction2 = add.curried(1, 2)
        // THEN
        assertEquals(add(1, 2, 3), partialFunction2(3))
    }

    @Test
    fun `function with 4 parameters - partial function with 1 parameter`() {
        // GIVEN
        val add = fun(a: Int, b: Int, c: Int, d: Int) = a + b + c + d
        //WHEN
        val partialFunction1 = add.curried(1)
        // THEN
        assertEquals(add(1, 2, 3, 4), partialFunction1(2, 3, 4))
    }

    @Test
    fun `function with 4 parameters - partial function with 2 parameters`() {
        // GIVEN
        val add = fun(a: Int, b: Int, c: Int, d: Int) = a + b + c + d
        //WHEN
        val partialFunction2 = add.curried(1, 2)
        // THEN
        assertEquals(add(1, 2, 3, 4), partialFunction2(3, 4))
    }

    @Test
    fun `function with 4 parameters - partial function with 3 parameters`() {
        // GIVEN
        val add = fun(a: Int, b: Int, c: Int, d: Int) = a + b + c + d
        //WHEN
        val partialFunction3 = add.curried(1, 2, 3)
        // THEN
        assertEquals(add(1, 2, 3, 4), partialFunction3(4))
    }
}