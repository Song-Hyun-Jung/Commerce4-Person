package com.digital4.controller;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.digital4.schema.Address;
import com.digital4.schema.ErrorMsg;
import com.digital4.schema.Phone;
import com.digital4.service.PhoneService;
import com.digital4.utils.ExceptionUtils;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "전화번호", description = "Phone Related API")
@RequestMapping(value = "/rest/phone")
public class PhoneController {
	@Resource
	PhoneService phoneSvc;

	@RequestMapping(value = "/search/{phoneNumber}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "전화번호 찾기 서비스", notes = "전화번호 검색하는 API.")
	@ApiResponses({ @ApiResponse(code = 200, message = "성공", response = Phone.class),
			@ApiResponse(code = 500, message = "실패", response = ErrorMsg.class) })
	public ResponseEntity<?> findPhone(@ApiParam(value = "전화번호") @PathVariable String phoneNumber) {
		MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();

		ErrorMsg errors = new ErrorMsg();
		
		try {
			Phone phone = phoneSvc.phoneSearch(phoneNumber);
			return new ResponseEntity<Phone>(phone, header, HttpStatus.valueOf(200));  // ResponseEntity를 활용한 응답 생성
		} catch (Exception e) {
			return ExceptionUtils.setException(errors, 500, e.getMessage(), header);
		}

	}
	
	@RequestMapping(value = "/searchById/{phoneId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "전화번호Id로 전화번호 찾기 서비스", notes = "전화번호 검색하는 API.")
	@ApiResponses({ @ApiResponse(code = 200, message = "성공", response = Phone.class),
			@ApiResponse(code = 500, message = "실패", response = ErrorMsg.class) })
	public ResponseEntity<?> findPhoneById(@ApiParam(value = "1", example="1") @PathVariable long phoneId) {
		MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();

		ErrorMsg errors = new ErrorMsg();
		
		try {
			Phone phone = phoneSvc.phoneSearchById(phoneId);
			return new ResponseEntity<Phone>(phone, header, HttpStatus.valueOf(200));  // ResponseEntity를 활용한 응답 생성
		} catch (Exception e) {
			return ExceptionUtils.setException(errors, 500, e.getMessage(), header);
		}

	}
	
	@RequestMapping(value = "/insertPhone", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "전화번호 등록", notes = "전화번호등록을 위한 API.")
	@ApiResponses({ @ApiResponse(code = 200, message = "성공", response = Phone.class),
			@ApiResponse(code = 500, message = "실패", response = ErrorMsg.class) })
	public ResponseEntity<?> signUp(
			@Parameter(name = "전화번호 등록을 위한 정보", description = "", required = true) @RequestBody Phone phone) {
		MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
		ErrorMsg errors = new ErrorMsg();
		Phone phone_res = new Phone();
		try {
			if(phoneSvc.insertPhone(phone)) {
				phone_res = phoneSvc.phoneSearch(phone.getPhoneNumber());
			}
			else {
				phone_res = phoneSvc.phoneSearch(phone.getPhoneNumber());
			}
			
		} catch (Exception e) {
			return ExceptionUtils.setException(errors, 500, e.getMessage(), header);
		}
		
		return new ResponseEntity<Phone>(phone_res, header, HttpStatus.valueOf(200)); // ResponseEntity를 활용한 응답 생성
	}
}
