package com.digital4.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.digital4.context.AuthContext;
import com.digital4.schema.Address;
import com.digital4.schema.Auth;
import com.digital4.schema.Person;
import com.digital4.schema.Phone;
import com.digital4.sql.mapper.AddressMapper;
import com.digital4.sql.mapper.PersonMapper;
import com.digital4.sql.mapper.PhoneMapper;
import com.digital4.sql.vo.AddressVO;
import com.digital4.sql.vo.PartyAddressVO;
import com.digital4.sql.vo.PartyPhoneVO;
import com.digital4.sql.vo.PersonVO;
import com.digital4.sql.vo.PhoneVO;

@Component
public class PersonService {
	@Autowired
	PersonMapper personMapper;
	@Autowired
	AddressMapper addressMapper;	
	@Autowired
	PhoneMapper phoneMapper;
	@Autowired
	AuthContext authContext;

	
	//로그인->로그인 성공시 객체를 반환
	public Auth login(String loginId, String password) throws Exception{
		
		try {
			Person person = personSearch(loginId);
			if(person.getLoginId() == null) {
				throw new Exception("가입되지 않은 회원입니다.");
			}
			
			if(person.getPassword().equals(password)) {
				//Commerce4-Auth에 토큰 생성 호출
				//HttpUrlConnection-바운디드컨텍스트
				Auth auth = authContext.generateToken(person.getPersonId());
				//long tokenId = auth.getToken();
				
				return auth;	
			}
			else {
				throw new Exception("비밀번호 오류입니다.");
			}
		} catch(Exception e) {
			throw e;
		}
	}
	
	
	public Person personSearch(String loginId) throws Exception{
		PersonVO personVO = personMapper.getPerson(loginId);
		Person person = new Person();
		if(personVO != null) {
			person.setPersonId(personVO.getPersonId());
			person.setPersonName(personVO.getPersonName());
			person.setLoginId(personVO.getLoginId());
			person.setPassword(personVO.getPassword());
			person.setGender(personVO.getGender());
			
			List<Address> addressList = new ArrayList<>();
			for(AddressVO adv : personVO.getAddressList()) {
				addressList.add(new Address(adv.getAddressId(), adv.getAddressDetail()));
			}
			person.setAddressList(addressList);
			
			List<Phone> phoneList = new ArrayList<>();
			for(PhoneVO pv : personVO.getPhoneList()) {
				phoneList.add(new Phone(pv.getPhoneId(), pv.getPhoneNumber()));
			}
			person.setPhoneList(phoneList);
		}
		return person;
	}

	@Transactional
	public boolean signUp(Person person) throws Exception{
		try {
			if (personSearch(person.getLoginId()).getLoginId() != null) { //로그인아이디는 중복x
				throw new Exception("이미 가입된 회원정보 입니다.");
			}
			if(person.getLoginId().equals("") || person.getPassword().equals("") || person.getGender().equals("") ||
				person.getPersonName().equals("") || person.getAddressList().size() == 0 || person.getPhoneList().size() == 0) {
				throw new Exception("필수정보가 누락되었습니다.");
			}
			for(Phone tmpP : person.getPhoneList()){
				if(tmpP.getPhoneNumber().equals(""))
					throw new Exception("필수정보가 누락되었습니다.");
			}
			for(Address tmpA : person.getAddressList()){
				if(tmpA.getAddressDetail().equals(""))
					throw new Exception("필수정보가 누락되었습니다.");
			}
			person.setPersonId(System.currentTimeMillis());
			Thread.sleep(10);
			
			PersonVO personVO = new PersonVO(person.getPersonId(), person.getPersonName(), person.getGender(),
											 person.getLoginId(), person.getPassword());

			List<Address> addrList = person.getAddressList();
			List<Phone> phoneList = person.getPhoneList();
		
			for(Address addr : addrList) { //address테이블, party테이블 저장
				try {
					AddressVO existAddress = addressMapper.getAddress(addr.getAddressDetail());
					if(existAddress.getAddressDetail() != null) {
						PartyAddressVO pav = new PartyAddressVO(person.getPersonId(), existAddress.getAddressId());
						personMapper.insertPartyAddress(pav);
					}
				}
				catch(Exception e){
					try {
						long newAddressId = System.currentTimeMillis();
						Thread.sleep(10);
						AddressVO adv = new AddressVO(newAddressId, addr.getAddressDetail());
						PartyAddressVO pav = new PartyAddressVO(person.getPersonId(), newAddressId);
						addressMapper.insertAddress(adv);
						personMapper.insertPartyAddress(pav);
					} catch(Exception e1) {
						e1.printStackTrace();
					}
				}
		
			}
			
			for(Phone phone : phoneList) {
				try {
					PhoneVO existPhone = phoneMapper.getPhone(phone.getPhoneNumber());
					if(existPhone.getPhoneNumber() != null) {
						PartyPhoneVO pv = new PartyPhoneVO(person.getPersonId(), existPhone.getPhoneId());
						personMapper.insertPartyPhone(pv);
					}
				}
				catch(Exception e){
					try {
						long newPhoneId = System.currentTimeMillis();
						Thread.sleep(10);
						PhoneVO pv = new PhoneVO(newPhoneId, phone.getPhoneNumber());
						PartyPhoneVO ppv = new PartyPhoneVO(person.getPersonId(), newPhoneId);
						phoneMapper.insertPhone(pv);
						personMapper.insertPartyPhone(ppv);
					}catch(Exception e1) {
						e1.printStackTrace();
					}
				}
			}
			
			personMapper.signUp(personVO);
			return true;
		}
		catch (Exception e) {
			throw e;
		}
		
	}
}
