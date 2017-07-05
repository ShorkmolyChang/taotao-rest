package com.taotao.rest.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.mapper.TbItemCatMapper;
import com.taotao.pojo.TbItemCat;
import com.taotao.pojo.TbItemCatExample;
import com.taotao.pojo.TbItemCatExample.Criteria;
import com.taotao.rest.poji.CatNode;
import com.taotao.rest.poji.ItemCatResult;
import com.taotao.rest.service.ItemCatService;

@Service
public class ItemCatServiceImpl implements ItemCatService{

	@Autowired
	private TbItemCatMapper itemCatMapper;
	
	@Override
	public ItemCatResult getItemCatList() {
		// TODO Auto-generated method stub
		
		ItemCatResult result=new ItemCatResult();
		result.setData(getItemCatList(0L));
		
		return result;
	}
	
	
	private List getItemCatList(Long parentId){
//		1、根据parentId查询列表
		TbItemCatExample example=new TbItemCatExample();
		Criteria criteria=example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		List<TbItemCat> list=itemCatMapper.selectByExample(example);
		
		List result=new ArrayList<>();
		int index=0;
		for(TbItemCat itemCat:list){
			
			if(index>=14)
				break;
			
//			如果该节点是一个父节点
			if(itemCat.getIsParent()){
				CatNode node=new CatNode();
				node.setUrl("/products/"+itemCat.getId()+".html");
//				该节点是第一级节点
				if(itemCat.getParentId()==0){
					
					node.setName("<a href='/products/"+itemCat.getId()+".html'>"+itemCat.getName()+"</a>");
					index++;
				}else{
					node.setName(itemCat.getName());
				}
//				递归查询的当前节点的子节点
				node.setList(getItemCatList(itemCat.getId()));
				result.add(node);
			}else{
				String item="/products/"+itemCat.getId()+".html|"+itemCat.getName();
				result.add(item);
			}
		}
		
		return result;
	}

}
