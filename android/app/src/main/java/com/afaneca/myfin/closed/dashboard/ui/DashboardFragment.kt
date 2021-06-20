package com.afaneca.myfin.closed.dashboard.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.afaneca.myfin.R
import com.afaneca.myfin.base.BaseFragment
import com.afaneca.myfin.closed.dashboard.data.DashboardRepository
import com.afaneca.myfin.data.network.MyFinAPIServices
import com.afaneca.myfin.data.network.Resource
import com.afaneca.myfin.databinding.FragmentDashboardBinding
import com.afaneca.myfin.utils.ChartUtils
import com.afaneca.myfin.utils.visible
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import java.util.*


class DashboardFragment :
    BaseFragment<DashboardViewModel, FragmentDashboardBinding, DashboardRepository>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindObservers()
        bindListeners()
        getMonthlyIncomeExpensesDistributionDataForCurrentMonth()
        setupMonthlyOverviewChart()
    }

    private fun setupMonthlyOverviewChart() {
        /*val pieChart = ChartUtils.buildHalfPieChart(
            requireContext(),
            binding.monthlyOverviewPchart,
            "Overview Mensal",
            getDataset()
        )*/
    }

    override fun onResume() {
        super.onResume()
    }


    private fun getDataset(): PieData {
        val values: ArrayList<PieEntry> = ArrayList()
        values.add(PieEntry(75F, "Atual"))
        values.add(PieEntry(15F, "Restante"))

        val dataSet = PieDataSet(values, "!!!Overview Mensal!!!")
        dataSet.setColors(*ColorTemplate.MATERIAL_COLORS)
        dataSet.setSelectionShift(0f)
        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter())
        data.setValueTextSize(11f)
        data.setValueTextColor(Color.WHITE)

        return data
    }


    private fun getMonthlyIncomeExpensesDistributionDataForCurrentMonth() {
        val currentMonth = Calendar.getInstance().get(Calendar.MONTH);
        val currentYear = Calendar.getInstance().get(Calendar.YEAR);
        viewModel.requestMonthlyExpensesIncomeDistribution(currentMonth, currentYear)
    }

    private fun bindListeners() {
        viewModel.getMonthlyIncomeExpensesDistributionData().observe(viewLifecycleOwner, {
            binding.loadingPb.visible(it is Resource.Loading)
            when (it) {
                is Resource.Success -> {

                }
                is Resource.Failure -> {
                    Toast.makeText(requireContext(), it.errorMessage, Toast.LENGTH_LONG).show()
                }
            }

        })
    }

    private fun bindObservers() {
        // TODO("Not yet implemented")
    }

    override fun getViewModel(): Class<DashboardViewModel> = DashboardViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentDashboardBinding.inflate(inflater, container, false)

    override fun getFragmentRepository() = DashboardRepository(
        remoteDataSource.create(MyFinAPIServices::class.java), userData
    )

}