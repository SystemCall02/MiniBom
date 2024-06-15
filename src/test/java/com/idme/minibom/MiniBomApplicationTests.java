package com.idme.minibom;

import com.huawei.innovation.rdm.coresdk.basic.dto.ObjectReferenceParamDTO;
import com.huawei.innovation.rdm.coresdk.basic.vo.QueryCondition;
import com.huawei.innovation.rdm.coresdk.basic.vo.QueryRequestVo;
import com.huawei.innovation.rdm.coresdk.basic.vo.RDMPageVO;
import com.huawei.innovation.rdm.san2.delegator.PartDelegator;
import com.huawei.innovation.rdm.san2.dto.entity.PartBranchCreateDTO;
import com.huawei.innovation.rdm.san2.dto.entity.PartCreateDTO;
import com.huawei.innovation.rdm.san2.dto.entity.PartMasterCreateDTO;
import com.huawei.innovation.rdm.san2.dto.entity.PartViewDTO;
import com.idme.minibom.Result.Result;
import com.idme.minibom.pojo.DTO.PartQueryDTO;
import com.idme.minibom.pojo.DTO.PartVersionQueryDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class MiniBomApplicationTests {
	@Autowired
	PartDelegator partDelegator;
	@Autowired
	RestTemplate restTemplate;

	@Test
	void testPartQuery(){
		PartQueryDTO dto=new PartQueryDTO();
		dto.curPage=1;
		dto.pageSize=10;
		Result result=restTemplate.postForObject("http://localhost:8080/idme/part/query",dto,Result.class);
		System.out.println(result.get("data"));
	}

	//TO DO
	@Test
	void testPartCreate(){
		//创建part
		PartCreateDTO partCreateDTO=new PartCreateDTO();
		ObjectReferenceParamDTO objectReferenceParamDTO = new ObjectReferenceParamDTO();
		objectReferenceParamDTO.setId(642691063441534976L);

		partCreateDTO.setExtAttrs(new ArrayList<>());

		partCreateDTO.setTenant(objectReferenceParamDTO);
		partCreateDTO.setCreator("jomocool");
		partCreateDTO.setBranch(new PartBranchCreateDTO());

		PartMasterCreateDTO partMasterCreateDTO = new PartMasterCreateDTO();
		partMasterCreateDTO.setNumber("1");
		partMasterCreateDTO.setTenant(objectReferenceParamDTO);
		partCreateDTO.setMaster(partMasterCreateDTO);

		Result result=restTemplate.postForObject("http://localhost:8080/idme/part/create",partCreateDTO,Result.class);
		System.out.println(result.get("data"));

	}


	@Test
	void testVersion(){
		PartVersionQueryDTO dto=new PartVersionQueryDTO();
		dto.setMasterId(0L);
		dto.setVersion("1");
		dto.setCurPage(1);
		dto.setPageSize(10);
		dto.setIteration(1);
		Result result=restTemplate.postForObject("http://localhost:8080/idme/part/version",dto,Result.class);
		System.out.println(result.get("data"));
	}










	@Test
	void contextLoads() {
		PartQueryDTO dto = new PartQueryDTO();
		dto.id = 643010985585553408L;
		dto.name = "";
		dto.curPage=1;
		dto.pageSize=10;
		// 执行方法
		QueryRequestVo queryRequestVo = new QueryRequestVo();
		QueryCondition queryCondition = new QueryCondition();
    	queryRequestVo.setFilter(queryCondition);
		List<String> values = new ArrayList<>();
    	values.add("123");
    	queryCondition.setConditionValues(values);    // 设置查询条件：where uid like 123
    	queryCondition.setConditionName("uid");
    	queryCondition.setOperator("like");
		RDMPageVO rdmPageVO = new RDMPageVO();  // 分页查询
		rdmPageVO.setCurPage(1);
		rdmPageVO.setPageSize(20);
		queryRequestVo.setIsNeedTotal(true);
		// 验证结果
		List<PartViewDTO> partQueryDTOS= partDelegator.find(queryRequestVo,rdmPageVO);
		System.out.println(partQueryDTOS);
	}

}
