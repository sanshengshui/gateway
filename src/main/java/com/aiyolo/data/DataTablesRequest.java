package com.aiyolo.data;

/**
 * Created by xujun on 2017/3/15.
 */
public class DataTablesRequest extends AbstractTableRequest {

    private Integer draw = 0;
    private Integer start = 0;
    private Integer length = 20;

    public Integer getDraw() {
        return draw;
    }

    public void setDraw(Integer draw) {
        this.draw = draw;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    @Override
    public int getPage() {
        return length > 0 ? (start / length) : 0;
    }

    @Override
    public int getSize() {
        return length > 0 ? length : 20;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends AbstractTableResponse> T getResponse() {
        return (T) new DataTablesResponse();
    }

}
