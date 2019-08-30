package com.admin.work.main.home;

import android.net.MacAddress;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.admin.core.app.AccountManager;
import com.admin.core.app.Latte;
import com.admin.core.app.MusicManager;
import com.admin.core.deleggate.LatteDelegate;
import com.admin.core.net.rx.RxRequest;
import com.admin.core.ui.recycler.MultipleItemEntity;
import com.admin.core.ui.recycler.MultipleRecyclerAdapter;
import com.admin.core.ui.recycler.MultipleViewHolder;
import com.admin.core.util.callback.CallBackType;
import com.admin.core.util.callback.CallbackManager;
import com.admin.core.util.callback.IGlobalCallback;
import com.admin.core.util.value.Resource;
import com.admin.work.R;
import com.admin.work.main.home.icon_love.LoveDelegate;
import com.admin.work.main.home.icon_love.LoveSong;
import com.admin.work.main.home.icon_native.NativeDelegate;
import com.admin.work.main.home.icon_recently.RecentlyDelegate;
import com.admin.work.main.home.tab.TabLayoutDelegate;
import com.admin.work.main.player.nativemusic.Song;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;

import org.litepal.LitePal;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import retrofit2.http.Url;

public class HomeRecyclerViewAdapter extends MultipleRecyclerAdapter implements View.OnClickListener {

    private HomeDelegate mHomeDelegate;
    private IGlobalCallback callBack;

    protected HomeRecyclerViewAdapter(List<MultipleItemEntity> data, LatteDelegate delegate) {
        super(data);
        this.mHomeDelegate = (HomeDelegate) delegate;
        addItemType(HomeItemType.HOME_ACCOUNT, R.layout.item_home_account);
        addItemType(HomeItemType.HOME_ICON, R.layout.item_home_icon);
        addItemType(HomeItemType.HOME_SORT, R.layout.item_home_sort);
        addItemType(HomeItemType.HOME_TABLAYOUT, R.layout.item_home_tab_delegate);
    }

    @Override
    protected void convert(MultipleViewHolder holder, MultipleItemEntity entity) {
        super.convert(holder, entity);
        switch (holder.getItemViewType()) {
            case HomeItemType.HOME_ACCOUNT:
                String url = Resource.getString(R.string.getLoadingImage);
                Map<String,String > m = new HashMap<>();
                m.put("name",AccountManager.getSignInNumber());
                String json =JSON.toJSONString(m);
                Log.e(TAG, "convert: "+json );
                RxRequest.onPostRx(mHomeDelegate.getContext(), url, json, (flag, result) -> {
                    if (flag){
                        JSONObject jsonObject = JSON.parseObject(result);
                        if (jsonObject.getString("result").equals("success")){
                            String pic = jsonObject.getString("pic");
                            if (!pic.equals("error")) {
                                Glide.with(Latte.getApplication())
                                        .load(pic)
                                        .into((ImageView) holder.getView(R.id.home_account_phone));
                                return;
                            }
                            Glide.with(Latte.getApplication())
                                    .load(R.drawable.logoko)
                                    .into((ImageView) holder.getView(R.id.home_account_phone));
                        }
                    }else {
                        Glide.with(Latte.getApplication())
                                .load(R.drawable.logoko)
                                .into((ImageView) holder.getView(R.id.home_account_phone));
                    }
                });
                holder.getView(R.id.home_account_phone).setOnClickListener(view -> {
                    mHomeDelegate.startCameraWithCheck();
                    CallbackManager.getInstance().addCallback(CallBackType.ON_CROP, new IGlobalCallback() {
                        @Override
                        public void executeCallBack(Object args) {
                            Uri uri = (Uri) args;
                            Glide.with(Latte.getApplication())
                                    .load(uri)
                                    .into((ImageView) holder.getView(R.id.home_account_phone));
                        }
                    });
                });
                holder.setText(R.id.home_account_text1, entity.getField(HomeItemFields.TEXT1));
                holder.setText(R.id.home_account_text2, entity.getField(HomeItemFields.TEXT2));
                holder.setText(R.id.home_account_name, entity.getField(HomeItemFields.NAME));
                break;
            case HomeItemType.HOME_ICON:
                LinearLayoutCompat layout = holder.getView(R.id.home_icon_layout);
                LinkedHashMap<String, String> map = entity.getField(HomeItemFields.MAP);
                int size = map.size();
                int count = 0;
                for (Map.Entry<String, String> icon : map.entrySet()) {
                    if (count < size) {
                        layout.getChildAt(count).setOnClickListener(this);
                        switch (count) {
                            case 0:
                                holder.setText(R.id.home_love_text1, icon.getKey());
                                holder.setText(R.id.home_love_text2,
                                        String.valueOf(LitePal.findAll(LoveSong.class).size()));
                                break;
                            case 1:
                                holder.setText(R.id.home_recently_text1, icon.getKey());
                                holder.setText(R.id.home_recently_text2,
                                        String.valueOf(MusicManager.getMusicSize(MusicManager.SourceCount.RECENTLY_COUNT)));
                                break;
                            case 2:
                                holder.setText(R.id.home_native_text1, icon.getKey());
                                holder.setText(R.id.home_native_text2,
                                        String.valueOf(MusicManager.getMusicSize(MusicManager.SourceCount.NATIVE_COUNT)));
                                break;
                            case 3:
                                holder.setText(R.id.home_bought_text1, icon.getKey());
                                holder.setText(R.id.home_bought_text2, icon.getValue());
                                break;
                            case 4:
                                holder.setText(R.id.home_Attention_text1, icon.getKey());
                                holder.setText(R.id.home_Attention_text2, icon.getValue());
                                break;
                        }
                    }
                    count++;
                }
                break;
            case HomeItemType.HOME_SORT:
//                LinearLayoutCompat sort = holder.getView(R.id.home_sort);
//                LinearLayoutCompat sort_layout = (LinearLayoutCompat) sort.getChildAt(1);
//                int sort_count = sort_layout.getChildCount();
                break;
            case HomeItemType.HOME_TABLAYOUT:
                mHomeDelegate.getSupportDelegate()
                        .loadRootFragment(R.id.item_home_tab_delegate, TabLayoutDelegate.create());
                break;
            default:
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.home_icon_love) {
            mHomeDelegate.extraTransaction()
                    .setCustomAnimations(R.anim.tran_start, R.anim.tran_end)
                    .start(new LoveDelegate());
            callBack = CallbackManager.getInstance().getCallBack(CallBackType.BOTTOM);
            if (callBack != null) {
                callBack.executeCallBack(null);
            }
        } else if (id == R.id.home_icon_recently) {
            mHomeDelegate.extraTransaction()
                    .setCustomAnimations(R.anim.tran_start, R.anim.tran_end)
                    .start(new RecentlyDelegate());
            //加载BOTTOM动画
            callBack = CallbackManager.getInstance().getCallBack(CallBackType.BOTTOM);
            if (callBack != null) {
                callBack.executeCallBack(null);
            }

        } else if (id == R.id.home_icon_native) {
            mHomeDelegate.extraTransaction()
                    .setCustomAnimations(R.anim.tran_start, R.anim.tran_end)
                    .start(new NativeDelegate());
            callBack = CallbackManager.getInstance().getCallBack(CallBackType.BOTTOM);
            if (callBack != null) {
                callBack.executeCallBack(null);
            }
        } else if (id == R.id.home_icon_bought) {
            Log.e(TAG, "onClick: 已购");
        } else if (id == R.id.home_icon_Attention) {
            Log.e(TAG, "onClick: 关注");
        }
    }
}
