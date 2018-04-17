package com.taotao.content.service;

import com.taotao.common.pojo.EasyUITreeNode;

import java.util.List;

/**
 * Created by 14999 on 2018/4/17.
 */
public interface ContentCategoryService {
    List<EasyUITreeNode> getContentCatList(long parentId);
}
