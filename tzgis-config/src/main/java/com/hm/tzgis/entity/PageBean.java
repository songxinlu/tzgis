package com.hm.tzgis.entity;

import lombok.Data;

@Data
public class PageBean {
  
    private Integer pageNo = 1;//页码
    private Integer pageSize = 10;//展示大小
}
