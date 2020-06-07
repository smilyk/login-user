package com.appsdeveloperblog.app.ws.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.appsdeveloperblog.app.ws.exceptions.UserServiceException;
import com.appsdeveloperblog.app.ws.io.entity.AddressEntity;
import com.appsdeveloperblog.app.ws.io.entity.UserEntity;
import com.appsdeveloperblog.app.ws.io.repository.UserRepository;
import com.appsdeveloperblog.app.ws.shared.AmazonSES;
import com.appsdeveloperblog.app.ws.shared.Utils;
import com.appsdeveloperblog.app.ws.shared.dto.AddressDTO;
import com.appsdeveloperblog.app.ws.shared.dto.UserDto;

/**
 * работаем только с логикой внутри метода...
 *
 */
class UserServiceImplTest {
//что бы могли использовать сервис. Без Mock потому что сервис не фиктивный = он реальный.
// Аннотация такая, потому что сервис внутри использует другие классы,
	@InjectMocks
	UserServiceImpl userService;

	@Mock
	UserRepository userRepository;
 
	@Mock
	Utils utils;
	
	@Mock
	AmazonSES amazonSES;

	@Mock
	BCryptPasswordEncoder bCryptPasswordEncoder;
 
	String userId = "hhty57ehfy";
	String encryptedPassword = "74hghd8474jf";
	
	UserEntity userEntity;
 
	@BeforeEach
	void setUp() throws Exception {
//		для @InjectMocks
		MockitoAnnotations.initMocks(this);
//		создание фиктивного пользоваытеля
		userEntity = new UserEntity();
		userEntity.setId(1L);
		userEntity.setFirstName("Sergey");
		userEntity.setLastName("Kargopolov");
		userEntity.setUserId(userId);
		userEntity.setEncryptedPassword(encryptedPassword);
		userEntity.setEmail("test@test.com");
		userEntity.setEmailVerificationToken("7htnfhr758");
		userEntity.setAddresses(getAddressesEntity());
	}

	@Test
	final void testGetUser() {
// если userRepository.findByEmail вызывается, то он возвращает userEntity независимо от того, какую почту ему передали
		when(userRepository.findByEmail(anyString())).thenReturn(userEntity);

		UserDto userDto = userService.getUser("test@test.com");

		assertNotNull(userDto);
		assertEquals("Sergey", userDto.getFirstName());

	}

//	тест для варианта, когда метод должен бросить Exception
	@Test
	final void testGetUser_UsernameNotFoundException() {
		when(userRepository.findByEmail(anyString())).thenReturn(null);

		assertThrows(UsernameNotFoundException.class,

				() -> {
					userService.getUser("test@test.com");
				}

		);
	}
	
	@Test
	final void testCreateUser_CreateUserServiceException()
	{
		when(userRepository.findByEmail(anyString())).thenReturn(userEntity);
		UserDto userDto = new UserDto();
		userDto.setAddresses(getAddressesDto());
		userDto.setFirstName("Sergey");
		userDto.setLastName("Kargopolov");
		userDto.setPassword("12345678");
		userDto.setEmail("test@test.com");
 	
		assertThrows(UserServiceException.class,

				() -> {
					userService.createUser(userDto);
				}

		);
	}
	
	@Test
	final void testCreateUser()
	{
		when(userRepository.findByEmail(anyString())).thenReturn(null);
//		геренируем adressId
		when(utils.generateAddressId(anyInt())).thenReturn("hgfnghtyrir884");
//   	проверка, что геренируем UserId
		when(utils.generateUserId(anyInt())).thenReturn(userId);
//   	проверка, что пароль не null
		when(bCryptPasswordEncoder.encode(anyString())).thenReturn(encryptedPassword);
//		сохраняем
		when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
//		не хотим отправлять письма... сильно долго проходим тест
		Mockito.doNothing().when(amazonSES).verifyEmail(any(UserDto.class));
 		
		UserDto userDto = new UserDto();
		userDto.setAddresses(getAddressesDto());
		userDto.setFirstName("Sergey");
		userDto.setLastName("Kargopolov");
		userDto.setPassword("12345678");
		userDto.setEmail("test@test.com");

		UserDto storedUserDetails = userService.createUser(userDto);
		assertNotNull(storedUserDetails);
		assertEquals(userEntity.getFirstName(), storedUserDetails.getFirstName());
		assertEquals(userEntity.getLastName(), storedUserDetails.getLastName());
		assertNotNull(storedUserDetails.getUserId());
		assertEquals(storedUserDetails.getAddresses().size(), userEntity.getAddresses().size());
		verify(utils,times(storedUserDetails.getAddresses().size())).generateAddressId(30);
		verify(bCryptPasswordEncoder, times(1)).encode("12345678");
		verify(userRepository,times(1)).save(any(UserEntity.class));
	}
	
	private List<AddressDTO> getAddressesDto() {
		AddressDTO addressDto = new AddressDTO();
		addressDto.setType("shipping");
		addressDto.setCity("Vancouver");
		addressDto.setCountry("Canada");
		addressDto.setPostalCode("ABC123");
		addressDto.setStreetName("123 Street name");

		AddressDTO billingAddressDto = new AddressDTO();
		billingAddressDto.setType("billling");
		billingAddressDto.setCity("Vancouver");
		billingAddressDto.setCountry("Canada");
		billingAddressDto.setPostalCode("ABC123");
		billingAddressDto.setStreetName("123 Street name");

		List<AddressDTO> addresses = new ArrayList<>();
		addresses.add(addressDto);
		addresses.add(billingAddressDto);

		return addresses;

	}
	
	private List<AddressEntity> getAddressesEntity()
	{
		List<AddressDTO> addresses = getAddressesDto();
		
	    Type listType = new TypeToken<List<AddressEntity>>() {}.getType();
	    
	    return new ModelMapper().map(addresses, listType);
	}


}
