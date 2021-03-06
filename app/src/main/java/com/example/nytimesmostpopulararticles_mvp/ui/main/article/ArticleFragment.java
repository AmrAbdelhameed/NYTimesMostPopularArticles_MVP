package com.example.nytimesmostpopulararticles_mvp.ui.main.article;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nytimesmostpopulararticles_mvp.R;
import com.example.nytimesmostpopulararticles_mvp.data.network.model.ArticlesResponse;
import com.example.nytimesmostpopulararticles_mvp.di.component.ActivityComponent;
import com.example.nytimesmostpopulararticles_mvp.ui.base.BaseFragment;
import com.example.nytimesmostpopulararticles_mvp.ui.main.MainActivity;
import com.example.nytimesmostpopulararticles_mvp.utils.AppConstants;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ArticleFragment extends BaseFragment implements
        ArticleMvpView, ArticleAdapter.Callback {

    @Inject
    ArticleMvpPresenter<ArticleMvpView> mPresenter;

    @Inject
    ArticleAdapter articleAdapter;

    @BindView(R.id.article_recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private NavController navController;

    public ArticleFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_article, container, false);

        ActivityComponent component = getActivityComponent();
        if (component != null) {
            component.inject(this);
            setUnBinder(ButterKnife.bind(this, view));
            mPresenter.onAttach(this);
            articleAdapter.setCallback(this);

            if (getActivity() != null) {
                ((MainActivity) getActivity()).setSupportActionBar(toolbar);
            }
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
    }

    @Override
    protected void setUp(View view) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(articleAdapter);

        mPresenter.onViewPrepared(7);
    }

    @Override
    public void onArticleEmptyViewRetryClick() {
        mPresenter.onViewPrepared(7);
    }

    @Override
    public void onItemClick(ArticlesResponse.Article article) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(AppConstants.ARTICLE, article);
        navController.navigate(R.id.action_articleFragment_to_articleDetailsFragment, bundle);
    }

    @Override
    public void updateArticle(List<ArticlesResponse.Article> articleList) {
        articleAdapter.addItems(articleList);
    }

    @Override
    public void onDestroyView() {
        mPresenter.onDetach();
        super.onDestroyView();
    }
}
