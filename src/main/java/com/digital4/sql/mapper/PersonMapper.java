package com.digital4.sql.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.digital4.sql.vo.PartyAddressVO;
import com.digital4.sql.vo.PartyPhoneVO;
import com.digital4.sql.vo.PersonVO;

@Mapper
public interface PersonMapper {
	//public List<PersonVO> getPerson(String loginId);
	public PersonVO getPerson(String loginId);
	public void signUp(PersonVO person);
	public void insertPartyAddress(PartyAddressVO pav);
	public void insertPartyPhone(PartyPhoneVO ppv);
}
