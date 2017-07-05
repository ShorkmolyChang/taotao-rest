package com.taotao.rest.controller;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.ExceptionUtil;
import com.taotao.common.utils.JsonUtils;
import com.taotao.pojo.TbContent;
import com.taotao.rest.component.JedisClient;
import com.taotao.rest.service.ContentService;

@Controller
public class ContentController {

	@Autowired
	private ContentService contentService;
	@Autowired
	private JedisClient jedisClient;
	@Value("${REDIS_CONTENT_KEY}")
	private String REDIS_CONTENT_KEY;

	@RequestMapping("/content/{cid}")
	@ResponseBody
	public TaotaoResult getContentByCid(@PathVariable Long cid) {
		// 添加redis缓存查询,在查询数据库之前先查询缓存。如果有直接返回，没有则查询数据库并将返回结果存到redis缓存数据库中
		try {
			// 从redis中取缓存数据
			String json = jedisClient.hget(REDIS_CONTENT_KEY, cid + "");
			if (!StringUtils.isBlank(json)) {
				// 将json转化成TaoTaoResult类型
				List<TbContent> list = JsonUtils.jsonToList(json, TbContent.class);
				return TaotaoResult.ok(list);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		List<TbContent> result = contentService.getContentByCid(cid);

		// 将返回结果添加到redis缓存中
		try {
			// 为了规范redis数据库的key可以使用hash
			// 定义一个保存内容的key，hash中的每个项就是cid
			// value包含list，需要转化为json数据
			jedisClient.hset(REDIS_CONTENT_KEY, cid + "", JsonUtils.objectToJson(result));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return TaotaoResult.ok(result);
	}

//	供管理员端调用，当增添删除修改任何一条content的数据时，都要从redis中删除content对象的cid的所有数据
	@RequestMapping("/sync/content/{cid}")
	@ResponseBody
	public TaotaoResult syncRedisContent(@PathVariable Long cid) {
		try {
			TaotaoResult result = contentService.syncRedisContent(cid);
			return result;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
		}
	}

}
