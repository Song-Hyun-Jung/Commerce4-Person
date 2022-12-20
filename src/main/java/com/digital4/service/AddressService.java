package com.digital4.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.digital4.schema.Address;
import com.digital4.sql.mapper.AddressMapper;
import com.digital4.sql.vo.AddressVO;

@Component
public class AddressService {
	@Autowired
	AddressMapper addressMapper;	
	
	public Address addressSearch(String addressDetail) throws Exception {
		AddressVO adv = addressMapper.getAddress(addressDetail);
		Address addr = new Address();
		if(adv != null) {
			addr.setAddressId(adv.getAddressId());
			addr.setAddressDetail(adv.getAddressDetail());
		}
		
		return addr;
	}
	
	public Address addressSearchById(long addressId) throws Exception {
		AddressVO adv = addressMapper.getAddressById(addressId);
		Address addr = new Address();
		if(adv != null) {
			addr.setAddressId(adv.getAddressId());
			addr.setAddressDetail(adv.getAddressDetail());
		}
		
		return addr;
	}
	
	public boolean insertAddress(Address address) throws Exception{
		if (addressSearch(address.getAddressDetail()).getAddressDetail() != null) { 
			return false;
		}
		address.setAddressId(System.currentTimeMillis());
		AddressVO adv = new AddressVO(address.getAddressId(), address.getAddressDetail());
		addressMapper.insertAddress(adv);
		return true;
	}
	
}
