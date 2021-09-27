package ru.unit6.course.android.retrofit.ui.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import okhttp3.*
import ru.unit6.course.android.retrofit.R
import ru.unit6.course.android.retrofit.ui.userdetails.UserDetailsFragment

class MainFragment : Fragment(R.layout.main_fragment) {

    private val viewModel: MainViewModel by lazy { ViewModelProvider(this).get(MainViewModel::class.java) }
    private val mainAdapter: MainAdapter by lazy { MainAdapter(arrayListOf()) }
    private val recyclerView: RecyclerView by lazy { requireView().findViewById(R.id.recyclerView) }

    private val userCLickListener by lazy {
        MainAdapter.UserCLickListener { id ->
            val userFragment = UserDetailsFragment()
            val bundle = Bundle()
            bundle.putString(UserDetailsFragment.USER_ID, id)
            userFragment.arguments = bundle
            requireActivity().supportFragmentManager.beginTransaction()
                .add(R.id.container, userFragment, UserDetailsFragment.TAG).addToBackStack("").commit()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        setupObservers()

        //todo
//        runBlocking {
//            delay(7000)
//        }
    }

    private fun setupUI() {
        mainAdapter.userCLickListener = userCLickListener
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(
                DividerItemDecoration(
                    recyclerView.context,
                    (recyclerView.layoutManager as LinearLayoutManager).orientation
                )
            )
            adapter = mainAdapter
        }
    }

    private fun setupObservers() {
        viewModel.users.observe(viewLifecycleOwner) { users ->
            users ?: return@observe
            mainAdapter.addUsers(users)
        }
    }
}