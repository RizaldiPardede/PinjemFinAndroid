package com.example.pinjemfinandroid

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.pinjemfinandroid.Model.SimulasiResponse
import com.example.pinjemfinandroid.ViewModel.HomeViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @ExperimentalCoroutinesApi
    class SimulasiViewModelTest {
        //InstantTaskExecutorRule
        @get:Rule
        val instantExecutorRule = InstantTaskExecutorRule()

        private lateinit var viewModel: HomeViewModel

        @Before
        fun setup() {
            viewModel = HomeViewModel()
        }

        @Test
        fun `getSimulasiPengajuanNoAuth returns expected result`() {
            // Data simulasi yang diharapkan
            val amount = 1000000.0
            val tenor = 6
            val expectedResponse = SimulasiResponse(
                amount = amount,
                tenor = tenor,
                angsuran = 175000.0,
                bunga = 5.0,
                total_payment = 1050000.0
            )

            // Amati LiveData
            val observer = Observer<SimulasiResponse> {}
            try {
                viewModel.simulasiResult.observeForever(observer)
                viewModel.getSimulasiPengajuanNoAuth(amount, tenor)


                Thread.sleep(2000)

                val result = viewModel.simulasiResult.value
                assertNotNull(result)
                assertEquals(expectedResponse.angsuran, result?.angsuran)
                assertEquals(expectedResponse.total_payment, result?.total_payment)
            } finally {
                viewModel.simulasiResult.removeObserver(observer)
            }
        }

    }
}