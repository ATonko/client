package tonko.com.client.view

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_repo_list.*
import tonko.com.client.App
import tonko.com.client.LOGIN
import tonko.com.client.PASSWORD
import tonko.com.client.PROJECT
import tonko.com.client.R
import tonko.com.client.USER
import tonko.com.client.adapters.RepoListAdapter
import tonko.com.client.adapters.RepoListener
import tonko.com.client.api.json_responses.Repos
import tonko.com.client.presenters.interfaces.IRepoListPresenter
import tonko.com.client.view.interfaces.BaseListView
import javax.inject.Inject

class RepoListActivity : AppCompatActivity(), RepoListener, BaseListView<Repos> {
    @Inject
    lateinit var presenter: IRepoListPresenter
    private var list: List<Repos> = ArrayList()

    override fun onClick(position: Int) {
        val intent = Intent(this, RepoActivity::class.java)
        intent.putExtra(USER, list[position].author!!.login)
        intent.putExtra(PROJECT, list[position].name)
        startActivity(intent)
    }

    override fun isSuccess(list: List<Repos>) {
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

    override fun isError(error: Int) {
        rv.visibility = View.GONE
        llNameWithAvatar.visibility = View.GONE
        llError.visibility = View.VISIBLE
        tvError.text = getString(error)
    }

    override fun isEmptyList() {
        llError.visibility = View.VISIBLE
        tvError.text = getString(R.string.no_repos_here)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repo_list)

        App.appComponent.plusRepoListComponent().inject(this)

        presenter.attachView(this)
        val sPref = App.appComponent.plusSharedPrefCompoment().getSharedPref()
        btnReload.setOnClickListener {
            if (intent.getStringExtra(LOGIN) != null) {
                presenter.getList(intent.getStringExtra(LOGIN))
            }
        }
        btnQuit.setOnClickListener {
            val editor = sPref.edit()
            editor.putString(LOGIN, "")
            editor.putString(PASSWORD, "")
            editor.apply()
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
        }

        rv.layoutManager = LinearLayoutManager(this)

        sPref.getString(LOGIN, "")?.let {
            if (it.isNotEmpty()) presenter.getList(it)
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }
}
