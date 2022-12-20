package com.digital4.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.digital4.schema.Address;
import com.digital4.schema.Phone;
import com.digital4.sql.mapper.PhoneMapper;
import com.digital4.sql.vo.AddressVO;
import com.digital4.sql.vo.PhoneVO;

@Component
public class PhoneService {
	@Autowired
	PhoneMapper phoneMapper;	
	
	public Phone phoneSearch(String phoneDetail) throws Exception {
		PhoneVO pv = phoneMapper.getPhone(phoneDetail);
		Phone phone = new Phone();
		if(pv != null) {
			phone.setPhoneId(pv.getPhoneId());
			phone.setPhoneNumber(pv.getPhoneNumber());
		}
		
		return phone;
	}
	
	public Phone phoneSearchById(long phoneId) throws Exception {
		PhoneVO pv = phoneMapper.getPhoneById(phoneId);
		Phone phone = new Phone();
		if(pv != null) {
			phone.setPhoneId(pv.getPhoneId());
			phone.setPhoneNumber(pv.getPhoneNumber());
		}
		
		return phone;
	}
	
	public boolean insertPhone(Phone phone) throws Exception{
		if (phoneSearch(phone.getPhoneNumber()).getPhoneNumber() != null) { 
			return false;
		}
		phone.setPhoneId(System.currentTimeMillis());
		PhoneVO pv = new PhoneVO(phone.getPhoneId(), phone.getPhoneNumber());
		phoneMapper.insertPhone(pv);
		return true;
	}

}
