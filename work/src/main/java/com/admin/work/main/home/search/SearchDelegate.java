package com.admin.work.main.home.search;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.admin.core.app.MusicManager;
import com.admin.core.deleggate.LatteDelegate;
import com.admin.core.net.rx.RxRequest;
import com.admin.core.ui.recycler.MultipleItemEntity;
import com.admin.core.util.callback.CallBackType;
import com.admin.core.util.callback.CallbackManager;
import com.admin.core.util.callback.IGlobalCallback;
import com.admin.core.util.dimen.SetToolBar;
import com.admin.core.util.storage.LattePreference;
import com.admin.core.util.value.Resource;
import com.admin.work.R;
import com.admin.work.R2;
import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

import butterknife.BindView;
import butterknife.OnClick;

public class SearchDelegate extends LatteDelegate {
    @BindView(R2.id.rv_search)
    RecyclerView mRecyclerView = null;
    @BindView(R2.id.et_search_view)
    AppCompatEditText mSearchEdit = null;
    @BindView(R2.id.tb_main_page)
    Toolbar mToolbar = null;
    @BindView(R2.id.clear_history)
    AppCompatTextView mClear = null;
    private SearchAdapter adapter;


    @OnClick(R2.id.tv_top_search)
    void onClickSearch() {
        final String searchItemText = mSearchEdit.getText().toString();
        saveItem(searchItemText);
    }

    @OnClick(R2.id.icon_top_search_back)
    void onClickBack() {
        getSupportDelegate().pop();
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_search;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        SetToolBar.setToolBar(mToolbar);
        initEdit();
    }

    private void initEdit() {
        //获取光标
        mSearchEdit.requestFocus();
        //监听回车
        mSearchEdit.setOnEditorActionListener((textView, i, keyEvent) -> {
            if ((keyEvent != null && KeyEvent.KEYCODE_ENTER == keyEvent.getKeyCode() &&
                    KeyEvent.ACTION_DOWN == keyEvent.getAction())) {
                String name = mSearchEdit.getText().toString().trim();
                if (!name.isEmpty() && !name.equals("")) {
                    request(name, SearchDataConverter.MODE.SEARCH_SONG);
                    saveItem(name);
                }
                return true;
            }
            return true;
        });
        //监听输入变化
        mSearchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String name = charSequence.toString();
                if (!name.isEmpty() && !name.equals("")) {
                    request(name, SearchDataConverter.MODE.SEARCH_RESULT);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void request(String name,  SearchDataConverter.MODE searchResult) {
        WeakHashMap<String, Object> param = new MusicManager.MusicApi().
                getInstance().getParam(name, "qq");

        RxRequest.onGetRx( getContext(),Resource.getString(R.string.music_url), param, (flag, result) -> {
            if (flag) {
                SearchDataConverter converter = new SearchDataConverter();
                converter.setJsonData(result);
                adapter.setNewData(converter.convert(searchResult));
            }
        });
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(manager);

        final List<MultipleItemEntity> data =
                new SearchDataConverter().convert(SearchDataConverter.MODE.SEARCH_HISTORY);
        adapter = new SearchAdapter(data,this);
        mRecyclerView.setAdapter(adapter);

        mClear.setOnClickListener(v -> {
            LattePreference.setAppData(SearchDataConverter.TAG_SEARCH_HISTORY, null);
            adapter.getData().clear();
            adapter.notifyDataSetChanged();
        });
    }

    public void saveItem(String item) {
        if (!StringUtils.isEmpty(item) && !StringUtils.isSpace(item)) {
            List<String> history;
            //获取历史记录
            final String historyStr = LattePreference.getAppData(SearchDataConverter.TAG_SEARCH_HISTORY);
            if (StringUtils.isEmpty(historyStr)) {
                history = new ArrayList<>();
            } else {
                //如果历史记录不为空，则解析 存入集合中
                history = JSON.parseObject(historyStr, ArrayList.class);
                //如果搜索历史中有，则 return
                for (int i = 0; i < history.size(); i++) {
                    if (history.get(i).equals(item)){
                        return;
                    }
                }
            }
            //添加新的记录
            history.add(item);
            //将全部记录转换为 json 串保存
            final String json = JSON.toJSONString(history);
            LattePreference.setAppData(SearchDataConverter.TAG_SEARCH_HISTORY, json);
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        IGlobalCallback callBack = CallbackManager.getInstance().getCallBack(CallBackType.BOTTOM);
        if (callBack != null) {
            callBack.executeCallBack(null);
        }
        getSupportDelegate().pop();
        return true;
    }
}
