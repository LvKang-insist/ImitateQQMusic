package com.admin.work.main.player.details.page.lrc_song;

public class LrcBaen {
    private String lrc;
    private long start;
    private long end;

    public LrcBaen(){
    }


    public LrcBaen(String lrc, long start, long end) {
        this.lrc = lrc;
        this.start = start;
        this.end = end;
    }

    public String getLrc() {
        return lrc;
    }

    public void setLrc(String lrc) {
        this.lrc = lrc;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }
}
