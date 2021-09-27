package ru.unit6.course.android.retrofit.ui.userdetails

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ru.unit6.course.android.retrofit.R
import ru.unit6.course.android.retrofit.data.model.User

//todo
class UserDetailsFragment: Fragment(R.layout.user_fragment) {

    private val userId by lazy { arguments?.getString(USER_ID) }
    private val viewModel: UserDetailsViewModel by lazy { ViewModelProvider(this).get(UserDetailsViewModel::class.java) }

    private val userObserver = Observer<User?>{
        it?: return@Observer
        requireView().findViewById<TextView>(R.id.id).text = it.name
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userId?.let { viewModel.loadUserInfo(it) }
        viewModel.user.observe(viewLifecycleOwner, userObserver)
    }

    companion object{
        const val TAG = "userFragment"
        const val USER_ID = "userId"
    }
}