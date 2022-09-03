package com.example.lunchwallet.util


import org.junit.Assert

import org.junit.Test
import java.time.LocalDate

class UploadMealValidationTest {

    @Test
    fun `date is valid`() {
        Assert.assertNull(UploadMealValidation().verifyDate(LocalDate.now().plusDays(2)))
        Assert.assertNull(UploadMealValidation().verifyDate(LocalDate.now().plusDays(29)))
    }

    @Test
    fun `date is invalid`() {
        Assert.assertNotNull(UploadMealValidation().verifyDate(LocalDate.now().plusDays(35)))

    }

    @Test
    fun `name of meal field is not empty`() {
        Assert.assertNull(UploadMealValidation().verifyName("Fried rice & peppered chicken"))
    }


    @Test
    fun `valid serving time`() {
        Assert.assertNull(UploadMealValidation().verifyServingTime("Brunch"))
        Assert.assertNull(UploadMealValidation().verifyServingTime("Dinner"))
    }

    @Test
    fun `invalid serving time`() {
        Assert.assertNotNull(UploadMealValidation().verifyServingTime("Breakfast"))
        Assert.assertNotNull(UploadMealValidation().verifyServingTime(" "))
    }

    @Test
    fun `valid kitchen selection`() {
        Assert.assertNull(UploadMealValidation().verifyKitchen("Farah Park"))
        Assert.assertNull(UploadMealValidation().verifyKitchen("Uno"))
        Assert.assertNull(UploadMealValidation().verifyKitchen("Edo Tech Park"))
    }

    @Test
    fun `invalid kitchen selection`() {
        Assert.assertNotNull(UploadMealValidation().verifyServingTime("Select"))

    }
}