package com.taotao.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.ExceptionUtil;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemParamItem;
import com.taotao.rest.service.ItemService;

@Controller
@RequestMapping("/item")
public class ItemController {

	@Autowired
	private ItemService itemService;

	@RequestMapping("/base/{id}")
	@ResponseBody
	public TaotaoResult getItemById(@PathVariable Long id) {

		try {
			TbItem item = itemService.getItemById(id);
			return TaotaoResult.ok(item);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
		}

	}

	 @RequestMapping("/desc/{id}")
	 @ResponseBody
	 public TaotaoResult getItemDesc(@PathVariable Long id){
		 try {
			TbItemDesc desc=itemService.getItemDescById(id);
			return TaotaoResult.ok(desc);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
		}
	 }
	 
	 @RequestMapping("/param/{id}")
	 @ResponseBody
	 public TaotaoResult getItemParam(@PathVariable Long id){
		 try {
			TbItemParamItem paramItem=itemService.getItemParamItemByItemId(id);
			return TaotaoResult.ok(paramItem);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
		}
	 }

}
