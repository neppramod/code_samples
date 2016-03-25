package com.zurelsoft.altitude.mvc.dao;

import org.springframework.stereotype.Repository;

import com.zurelsoft.altitude.domain.Business;
import com.zurelsoft.altitude.mvc.dao.interfaces.BusinessDAO;

@Repository
public class BusinessDAOImpl extends GenericDAOImpl<Business, Long> implements
		BusinessDAO {

}

