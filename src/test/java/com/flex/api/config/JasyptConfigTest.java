package com.flex.api.config;

import static org.junit.jupiter.api.Assertions.*;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JasyptConfigTest {
	

	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	void encrypt() {
		final String password = "";
		final String target = "";
		
		StandardPBEStringEncryptor pbeEnc = new StandardPBEStringEncryptor();
		pbeEnc.setAlgorithm("PBEWithMD5AndDES");
		pbeEnc.setPassword(password); 
		
		String enc = pbeEnc.encrypt(target);
		System.out.println("enc = " + enc);
		
		//테스트용 복호화
		String des = pbeEnc.decrypt(enc);
		System.out.println("des = " + des);
	}

}
