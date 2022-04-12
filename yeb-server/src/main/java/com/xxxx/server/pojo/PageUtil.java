package com.xxxx.server.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageUtil<T> {

    private Long totalCount;  //总记录数
    private Long currPageNo;   //当前页码
    private List<T> list;

//    public PageUtil() {
//    }
//
//    public PageUtil(Long totalCount, Long currPageNo, List<T> list) {
//        this.totalCount = totalCount;
//        this.currPageNo = currPageNo;
//        this.list = list;
//    }
//
//    public Long getTotalCount() {
//        return totalCount;
//    }
//
//    public void setTotalCount(Long totalCount) {
//        this.totalCount = totalCount;
//    }
//
//    public Long getCurrPageNo() {
//        return currPageNo;
//    }
//
//    public void setCurrPageNo(Long currPageNo) {
//        this.currPageNo = currPageNo;
//    }
//
//    public List<T> getList() {
//        return list;
//    }
//
//    public void setList(List<T> list) {
//        this.list = list;
//    }
}
