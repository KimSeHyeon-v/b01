package org.zerock.b01;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing // BaseEntity 의 AuditingEntityListener 를 실행하기 위함. (자동으로 엔터티객체의 시간을 상속시키고 데이터의 upsert 시 시간 반영)
public class B01Application {

	public static void main(String[] args) {
		SpringApplication.run(B01Application.class, args);
	}

}
