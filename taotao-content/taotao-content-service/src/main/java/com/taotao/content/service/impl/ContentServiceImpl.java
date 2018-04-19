package com.taotao.content.service.impl;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.JsonUtils;
import com.taotao.content.service.ContentService;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.TbContent;
import com.taotao.pojo.TbContentExample;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * Created by 14999 on 2018/4/19.
 */
public class ContentServiceImpl implements ContentService {
    @Autowired
    private TbContentMapper contentMapper;

    @Override
    public TaotaoResult addContent(TbContent content) {
        //补全pojo的属性
        content.setCreated( new Date());
        content.setUpdated(new Date());
        //插入到内容表
        contentMapper.insert(content);
        return TaotaoResult.ok();
    }

    @Override
    public List<TbContent> getContentByCid(Long ad1_category_id) {
        //缓存中没有命中，需要查询数据库
        TbContentExample example = new TbContentExample();
        TbContentExample.Criteria criteria = example.createCriteria();
        //设置查询条件
        criteria.andCategoryIdEqualTo(ad1_category_id);
        //执行查询
        List<TbContent> list = contentMapper.selectByExample(example);
        //返回结果
        return list;
    }
}
