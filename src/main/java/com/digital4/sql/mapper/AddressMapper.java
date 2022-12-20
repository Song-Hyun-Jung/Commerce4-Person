package com.digital4.sql.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.digital4.sql.vo.AddressVO;

@Mapper
public interface AddressMapper {
	public AddressVO getAddress(String addressDetail);
	public AddressVO getAddressById(long addressId);
	public int insertAddress(AddressVO address);
}
