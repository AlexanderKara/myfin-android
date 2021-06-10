package com.afaneca.myfin.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.afaneca.myfin.data.UserDataStore
import com.afaneca.myfin.data.network.BaseRepository
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import retrofit2.Retrofit

abstract class BaseFragment<VM : ViewModel, B : ViewBinding, R : BaseRepository> : Fragment() {
    protected lateinit var binding: B
    protected lateinit var viewModel: VM
    protected lateinit var userData: UserDataStore
    protected val remoteDataSource: Retrofit by inject() // = RetrofitClient()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userData = get()/*UserDataStore(requireContext())*/
        binding = getFragmentBinding(inflater, container)

        val factory = BaseViewModelFactory(getFragmentRepository())
        viewModel = ViewModelProvider(this, factory).get(getViewModel())

        return binding.root
    }

    abstract fun getViewModel(): Class<VM>

    abstract fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?): B

    abstract fun getFragmentRepository(): R
}