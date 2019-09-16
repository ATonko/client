package tonko.com.client.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_repo_list.*
import tonko.com.client.*
import tonko.com.client.adapters.RepoListAdapter
import tonko.com.client.adapters.RepoListener
import tonko.com.client.api.json_responses.Repos
import tonko.com.client.presenters.RepoListPresenter
import tonko.com.client.view.interfaces.BaseListView

class RepoListActivity : AppCompatActivity(), RepoListener, BaseListView<Repos>
{
    private val presenter = RepoListPresenter()
    private var list: List<Repos> = ArrayList()
    private lateinit var sPref: SharedPreferences

    override fun onClick(position: Int)
    {
        val intent = Intent(this, RepoActivity::class.java)
        intent.putExtra(USER, list[position].author!!.login)
        intent.putExtra(PROJECT, list[position].name)
        startActivity(intent)
    }

    override fun isSuccess(list: List<Repos>)
    {
        rv.visibility = View.VISIBLE
        llNameWithAvatar.visibility = View.VISIBLE
        llError.visibility = View.GONE

        Glide
                .with(this)
                .load(list[0].author?.avatar_url)
                .into(ivAvatar)
        tvAuthorName.text = list[0].author?.login
        this.list = list
        rv.adapter = RepoListAdapter(this.list, this)
    }

    override fun isError(error: Int)
    {
        rv.visibility = View.GONE
        llNameWithAvatar.visibility = View.GONE
        llError.visibility = View.VISIBLE
        tvError.text = getString(error)
    }

    override fun isEmptyList()
    {
        llError.visibility = View.VISIBLE
        tvError.text = getString(R.string.no_repos_here)
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repo_list)

        presenter.attachView(this)
        sPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        btnReload.setOnClickListener {
            if (intent.getStringExtra(LOGIN) != null)
            {
                presenter.getList(intent.getStringExtra(LOGIN))
            }
        }
        btnQuit.setOnClickListener {
            val editor = sPref.edit()
            editor.putString(LOGIN, "")
            editor.putString(PASSWORD, "")
            editor.apply()
            startActivity(Intent(this, AuthActivity::class.java))
        }

        rv.layoutManager = LinearLayoutManager(this)

        sPref.getString(LOGIN, "")?.let {
            if (it.isNotEmpty()) presenter.getList(it)
        }


    }

    override fun onDestroy()
    {
        super.onDestroy()
        presenter.detachView()
    }
}
