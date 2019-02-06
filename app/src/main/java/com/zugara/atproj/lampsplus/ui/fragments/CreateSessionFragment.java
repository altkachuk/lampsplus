package com.zugara.atproj.lampsplus.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.Toast;

import com.zugara.atproj.lampsplus.R;
import com.zugara.atproj.lampsplus.model.singleton.SessionContext;
import com.zugara.atproj.lampsplus.presenters.CreateSessionPresenter;
import com.zugara.atproj.lampsplus.ui.fragments.base.BaseFragment;
import com.zugara.atproj.lampsplus.utils.ActivityHelper;
import com.zugara.atproj.lampsplus.views.CreateSessionView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnEditorAction;

/**
 * Created by andre on 21-Dec-18.
 */

public class CreateSessionFragment extends BaseFragment implements CreateSessionView {

    @BindView(R.id.sessionNameText)
    EditText sessionNameText;

    private CreateSessionPresenter createSessionPresenter;

    //-------------------------------------------------------------
    // Lifecycle override

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fargment_create_session, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        createSessionPresenter = new CreateSessionPresenter(getActivity().getApplicationContext(), this);
    }

    //-------------------------------------------------------------
    // Handlers

    @OnEditorAction(R.id.sessionNameText)
    public boolean onTextAction(int actionId) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            ActivityHelper.hideKeyboard(getActivity());
            createSessionPresenter.create(sessionNameText.getText().toString());
            return true;
        }

        return false;
    }

    @OnClick(R.id.createButton)
    public void onClickCreateButton() {
        ActivityHelper.hideKeyboard(getActivity());
        createSessionPresenter.create(sessionNameText.getText().toString());
    }

    //-------------------------------------------------------------
    // CreateSessionView methods

    @Override
    public void close() {
        sessionNameText.setText("");
        getView().setVisibility(View.GONE);
    }

    @Override
    public void showIllegalMessage() {
        Toast.makeText(getActivity().getApplicationContext(), getString(R.string.name_has_illegal_characters), Toast.LENGTH_SHORT).show();
    }
}
