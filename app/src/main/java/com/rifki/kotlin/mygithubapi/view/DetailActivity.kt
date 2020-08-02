package com.rifki.kotlin.mygithubapi.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.rifki.kotlin.mygithubapi.R
import com.rifki.kotlin.mygithubapi.model.GithubUser
import com.rifki.kotlin.mygithubapi.viewModel.DetailViewModel
import com.rifki.kotlin.mygithubapi.adapter.SectionsPagerAdapter
import com.rifki.kotlin.mygithubapi.view.fragment.FollowersFragment
import com.rifki.kotlin.mygithubapi.view.fragment.FollowingFragment
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {
    private lateinit var detailViewModel: DetailViewModel

    companion object {
        const val EXTRA_STATE = "extra_state"
    }

    private fun setActionBarTitle(username: String) {
        if (supportActionBar != null) {
            (supportActionBar as ActionBar).title = username
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val detailGithubUser = intent.getParcelableExtra(EXTRA_STATE) as GithubUser

        Glide.with(this).load(detailGithubUser.avatar).into(image_avatar)
        tv_username.text = detailGithubUser.username

        detailViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(DetailViewModel::class.java)

        detailViewModel.setDetailGithubUser(detailGithubUser.username)
        detailViewModel.getDetailGithubUsers().observe(this, Observer { githubUser ->
            if(githubUser != null) {
                tv_name.text = githubUser.name
                tv_repo.text = githubUser.repository.toString()
                tv_company.text = githubUser.company
                tv_bio.text = githubUser.bio
                tv_location.text = githubUser.location
            }
        })

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        sectionsPagerAdapter.setData(detailGithubUser.username)
        view_pager.adapter = sectionsPagerAdapter
        tabs_follow.setupWithViewPager(view_pager)
        supportActionBar?.elevation = 0f

        val bundle = Bundle()
        val followersFragment = FollowersFragment()
        bundle.putString(FollowersFragment.EXTRA_FOLLOWERS, detailGithubUser.username)
        followersFragment.arguments = bundle

        val followingFragment = FollowingFragment()
        bundle.putString(FollowingFragment.EXTRA_FOLLOWING, detailGithubUser.username)
        followingFragment.arguments = bundle

        setActionBarTitle(detailGithubUser.username)
    }
}
