package com.taotao.rest.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.common.utils.JsonUtils;
import com.taotao.mapper.TbItemDescMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.mapper.TbItemParamItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemParamItem;
import com.taotao.pojo.TbItemParamItemExample;
import com.taotao.pojo.TbItemParamItemExample.Criteria;
import com.taotao.rest.component.JedisClient;
import com.taotao.rest.service.ItemService;

@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private TbItemMapper itemMapper;
	@Autowired
	private TbItemDescMapper itemDescMapper;
	@Autowired
	private TbItemParamItemMapper itemParamItemMapper;

	@Autowired
	private JedisClient jedisClient;

	@Value("${REDIS_ITEM_KEY}")
	private String REDIS_ITEM_KEY;
	@Value("${ITEM_BASE_INFO_KEY}")
	private String ITEM_BASE_INFO_KEY;
	@Value("${ITEM_DESC_INFO_KEY}")
	private String ITEM_DESC_INFO_KEY;
	@Value("${ITEM_PARAM_ITEM_INFO_KEY}")
	private String ITEM_PARAM_ITEM_INFO_KEY;
	@Value("${ITEM_EXPIRE_SECOND}")
	private int ITEM_EXPIRE_SECOND;

	@Override
	public TbItem getItemById(Long id) {
		// TODO Auto-generated method stub

		// 首先查询redis缓存数据库中是否有数据
		try {
			String json = jedisClient.get(REDIS_ITEM_KEY + ":" + id + ":" + ITEM_BASE_INFO_KEY);
			if (!StringUtils.isBlank(json)) {
				return JsonUtils.jsonToPojo(json, TbItem.class);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		// redis中没有数据就向数据库中查询
		TbItem item = itemMapper.selectByPrimaryKey(id);

		// 将从数据库中查询到的数据存到redis数据库中，同时需要设置过期时间（不能使用hset）
		// 在redis客户端中，除最后一个冒号后面的值是key之外，它前面的冒号都是文件夹，：分隔命名方式比较方便查看
		try {
			// 向redis中添加缓存
			jedisClient.set(REDIS_ITEM_KEY + ":" + id + ":" + ITEM_BASE_INFO_KEY, JsonUtils.objectToJson(item));
			// 设置过期时间
			// 过期时间只能设置在key上，为了便于分组，key使用:分隔的命名方式
			// REDIS_ITEM:BASE_INFO:{ITEM_ID},
			jedisClient.expire(REDIS_ITEM_KEY + ":" + id + ":" + ITEM_BASE_INFO_KEY, ITEM_EXPIRE_SECOND);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return item;
	}

	@Override
	public TbItemDesc getItemDescById(Long id) {
		// TODO Auto-generated method stub
		// 从redis缓存中查询
		try {
			String json = jedisClient.get(REDIS_ITEM_KEY + ":" + id + ":" + ITEM_DESC_INFO_KEY);
			if (StringUtils.isNoneBlank(json)) {
				return JsonUtils.jsonToPojo(json, TbItemDesc.class);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		// 从数据库中查询
		TbItemDesc itemDesc = itemDescMapper.selectByPrimaryKey(id);
		// 将结果存入redis数据库中，并设置过期时间为1天，单位为秒
		try {
			jedisClient.set(REDIS_ITEM_KEY + ":" + id + ":" + ITEM_DESC_INFO_KEY, JsonUtils.objectToJson(itemDesc));
			// 设置过期时间
			jedisClient.expire(REDIS_ITEM_KEY + ":" + id + ":" + ITEM_DESC_INFO_KEY, ITEM_EXPIRE_SECOND);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return itemDesc;
	}

	@Override
	public TbItemParamItem getItemParamItemByItemId(Long id) {
		// TODO Auto-generated method stub

		// 从redis中取数据
		try {
			String json = jedisClient.get(REDIS_ITEM_KEY + ":" + id + ":" + ITEM_PARAM_ITEM_INFO_KEY);
			if (StringUtils.isNoneBlank(json)) {
				return JsonUtils.jsonToPojo(json, TbItemParamItem.class);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		TbItemParamItemExample example = new TbItemParamItemExample();
		Criteria criteria = example.createCriteria();
		criteria.andItemIdEqualTo(id);
		List<TbItemParamItem> itemParamItem = itemParamItemMapper.selectByExampleWithBLOBs(example);
		if (itemParamItem == null || itemParamItem.size() <= 0)
			return null;

		// 存入redis数据库中
		try {
			jedisClient.set(REDIS_ITEM_KEY + ":" + id + ":" + ITEM_PARAM_ITEM_INFO_KEY,
					JsonUtils.objectToJson(itemParamItem));
			// 设置过期时间
			jedisClient.expire(REDIS_ITEM_KEY + ":" + id + ":" + ITEM_PARAM_ITEM_INFO_KEY, ITEM_EXPIRE_SECOND);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return itemParamItem.get(0);
	}

}
