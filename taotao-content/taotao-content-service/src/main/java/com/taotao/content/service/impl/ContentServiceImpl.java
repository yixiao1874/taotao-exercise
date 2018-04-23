package com.taotao.content.service.impl;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.JsonUtils;
import com.taotao.content.jedis.JedisClient;
import com.taotao.content.service.ContentService;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.TbContent;
import com.taotao.pojo.TbContentExample;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by 14999 on 2018/4/19.
 */
@Service
public class ContentServiceImpl implements ContentService {
    @Autowired
    private TbContentMapper contentMapper;
    @Autowired
    private JedisClient jedisClient;

    @Value("${INDEX_CONTENT}")
    private String INDEX_CONTENT;

    @Override
    public TaotaoResult addContent(TbContent content) {
        //补全pojo的属性
        content.setCreated( new Date());
        content.setUpdated(new Date());
        //插入到内容表
        contentMapper.insert(content);
        //同步缓存
        //删除对应的缓存信息
        jedisClient.hdel(INDEX_CONTENT, content.getCategoryId().toString());
        return TaotaoResult.ok();
    }

    @Override
    public List<TbContent> getContentByCid(Long ad1_category_id) {
        //先查询缓存
        //添加缓存不能影响正常业务逻辑
        try {
            //查询缓存
            String json = jedisClient.hget(INDEX_CONTENT, ad1_category_id + "");
            //查询到结果，把json转换成List返回
            if (StringUtils.isNotBlank(json)) {
                List<TbContent> list = JsonUtils.jsonToList(json, TbContent.class);
                return list;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //缓存中没有命中，需要查询数据库
        TbContentExample example = new TbContentExample();
        TbContentExample.Criteria criteria = example.createCriteria();
        //设置查询条件
        criteria.andCategoryIdEqualTo(ad1_category_id);
        //执行查询
        List<TbContent> list = contentMapper.selectByExample(example);
        try {
            jedisClient.hset(INDEX_CONTENT, ad1_category_id + "", JsonUtils.objectToJson(list));
        } catch (Exception e) {
            e.printStackTrace();
        }
        //返回结果
        return list;
    }
}
