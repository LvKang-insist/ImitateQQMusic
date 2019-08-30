package com.admin.work.main.home.search;

import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatTextView;

import com.admin.core.app.Latte;
import com.admin.core.app.MusicManager;
import com.admin.core.net.rx.RxRequest;
import com.admin.core.ui.recycler.MultipleFields;
import com.admin.core.ui.recycler.MultipleItemEntity;
import com.admin.core.ui.recycler.MultipleRecyclerAdapter;
import com.admin.core.ui.recycler.MultipleViewHolder;
import com.admin.core.util.value.Resource;
import com.admin.work.R;
import com.admin.work.main.home.HomeItemFields;
import com.admin.work.main.home.HomeItemType;
import com.admin.work.main.player.PlayerControl;
import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchAdapter extends MultipleRecyclerAdapter {
    private SearchDelegate delegate;

    protected SearchAdapter(List<MultipleItemEntity> data, SearchDelegate searchDelegate) {
        super(data);
        this.delegate = searchDelegate;
        addItemType(HomeItemType.HOME_SEARCH, R.layout.item_search);
        addItemType(HomeItemType.HOME_SEARCH_RESULT, R.layout.item_search_result);
        addItemType(HomeItemType.HOME_NETWORK_SONG, R.layout.item_search_song);
    }

    @Override
    protected void convert(MultipleViewHolder holder, MultipleItemEntity entity) {
        super.convert(holder, entity);
        switch (holder.getItemViewType()) {
            case HomeItemType.HOME_SEARCH:
                final AppCompatTextView tvSearchItem = holder.getView(R.id.tv_search_item);
                final String history = entity.getField(MultipleFields.TEXT);
                tvSearchItem.setText(history);
                tvSearchItem.setOnClickListener((view -> {
                    String url = Resource.getString(R.string.music_url);
                    RxRequest.onGetRx(delegate.getContext(), url,
                            MusicManager.MusicApi.instance.getParam(history, "kugou"), (flag, result) -> {
                                if (flag) {
                                    SearchDataConverter converter = new SearchDataConverter();
                                    converter.setJsonData(result);
                                    setNewData(converter.convert(SearchDataConverter.MODE.SEARCH_SONG));
                                }
                            });
                }));
                break;
            case HomeItemType.HOME_SEARCH_RESULT:
                final AppCompatTextView tvSearchResult = holder.getView(R.id.tv_search_result);
                final NetWorkQQSong song = entity.getField(HomeItemFields.NETWORK_SONG);
                final String name = song.getName() + "   - " + song.getAuthor();
                tvSearchResult.setText(name);
                holder.getView(R.id.item_search_result).setOnClickListener((view -> {
                    String url = Resource.getString(R.string.music_url);
                    //保存搜索的名字
                    delegate.saveItem(name);
                    RxRequest.onGetRx(delegate.getContext(), url,
                            MusicManager.MusicApi.instance.getParam(song.getName(), "kugou"), (flag, result) -> {
                                if (flag) {
                                    SearchDataConverter converter = new SearchDataConverter();
                                    converter.setJsonData(result);
                                    setNewData(converter.convert(SearchDataConverter.MODE.SEARCH_SONG));
                                }
                            });
                }));
                break;
            case HomeItemType.HOME_NETWORK_SONG:
                final NetWorkQQSong workSong = entity.getField(HomeItemFields.NETWORK_SONG);
                CircleImageView image = holder.getView(R.id.item_search_image);
                Glide.with(Latte.getApplication())
                        .load(workSong.getPic())
                        .into(image);
                holder.setText(R.id.item_search_song_name, workSong.getName());
                holder.setText(R.id.item_song_search_author, workSong.getAuthor());
                //播放
                holder.getView(R.id.item_search_song).setOnClickListener((view -> {
                    PlayerControl.getInstence().playNetWorkSong(workSong);
                    Toast.makeText(delegate.getContext(), "开始播放："+workSong.getName(), Toast.LENGTH_SHORT).show();
                }));
                //视频
                holder.getView(R.id.item_song_search_video).setOnClickListener((v -> {

                }));
                //更多
                holder.getView(R.id.item_song_search_more).setOnClickListener((v -> {

                }));
                break;
            default:
                break;
        }
    }
}