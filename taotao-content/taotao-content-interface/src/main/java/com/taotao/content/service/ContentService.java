package com.taotao.content.service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbContent;

import java.util.List;

/**
 * Created by 14999 on 2018/4/19.
 */
public interface ContentService {
    TaotaoResult addContent(TbContent content);

    List<TbContent> getContentByCid(Long ad1_category_id);
}
