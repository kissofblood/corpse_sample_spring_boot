package com.september.fuelup.confuration;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Session;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class AppInitializer implements ApplicationRunner {

	@PersistenceContext private EntityManager entityManager;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		Session session = entityManager.unwrap(Session.class);
		
		session.createNativeQuery(
			"IF NOT EXISTS( " + 
				"SELECT * FROM INFORMATION_SCHEMA.COLUMNS " + 
					"WHERE TABLE_SCHEMA='dbo' AND " + 
						"TABLE_NAME='PROVIDERS' AND " + 
						"COLUMN_NAME='ContactName' " + 
			") " + 
			"BEGIN " + 
				"ALTER TABLE dbo.PROVIDERS ADD ContactName varchar(255) NULL " + 
			"END"
		).executeUpdate();
	}
}
