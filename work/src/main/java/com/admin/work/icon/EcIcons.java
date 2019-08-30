package com.admin.work.icon;

import com.joanzapata.iconify.Icon;

/**
 * Copyright (C)
 * 文件名称: EcIcons
 * 创建人: 345
 * 创建时间: 2019/4/14 22:02
 * 描述: ${DESCRIPTION}
 */
public enum EcIcons implements Icon {
    /**
     *
     */
    music_list('\ue6e4'),
    music_library('\ue63e'),
    music_SMS('\ue502'),
    home_List('\u343a'),
    home_love('\ue630'),
    home_recently('\ue6d7'),
    home_native('\ue6ac'),
    home_bought('\ue64c'),
    home_Attention('\ue754'),
    home_omit('\ue65e'),
    home_tab_select('\ue688'),
    home_add('\ue54a'),
    home_tab_right('\ue61b'),
    discover_tab_comment('\ue623'),
    more_music('\ue680'),
    more_singer('\ue504'),
    more_Ranking('\ue623'),
    more_sort('\ue506'),
    more_radio('\ue623'),
    more_hear('\ue6ce'),
    music_more('\ue508'),
    music_break('\ue902'),
    music_sq('\ue501'),
    music_exclusive('\ue604'),
    music_video('\ue503'),
    music_hq('\ue73e'),
    music_sound_effect('\ue6fb'),
    music_mv('\ue74e'),
    music_line('\ue692'),
    play_random('\ue60b'),
    play_order('\ue505'),
    paly_repetition('\ue624'),
    paly_up('\ue64d'),
    paly_below('\ue51c'),
    paly_pause('\ue6fa'),
    paly_start('\ue65d'),
    paly_collect('\ue603'),
    paly_share('\ue5cd'),
    paly_comment('\ue623'),
    home_delete('\ue616'),
    music_paihang('\ue606'),
    music_classify('\ue509'),
    music_radio('\ue507'),
    dialog_play_below('\ue786'),
    dialog_dowload('\uee0f'),
    dialog_album('\ue603'),
    dialog_songMessage('\ue6aa');


    private char character;
    EcIcons(char character) {
        this.character = character;
    }

    //图标的键，例如'fa-ok'
    @Override
    public String key() {
        return name().replace('_', '-');
    }

    //与字体中的键匹配的字符
    @Override
    public char character() {
        return character;
    }
}
