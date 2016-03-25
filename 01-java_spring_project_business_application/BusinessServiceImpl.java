package ...mvc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ...domain.Business;
import ...mvc.dao.interfaces.BusinessDAO;
import ...mvc.service.interfaces.BusinessService;

@Service
public class BusinessServiceImpl implements BusinessService {

	@Autowired
	private BusinessDAO businessDAO;

	@Transactional
	public void addBusiness(Business business) {
		businessDAO.create(business);
	}
	
	@Transactional
	public long addBusinessReturnId(Business business) {
		businessDAO.create(business);
		System.out.println("Created business with id "+business.getId());
		return business.getId();
	}
	

	@Transactional
	public void updateBusiness(Business business) {
		businessDAO.update(business);
	}
	
	public List<Business> listBusiness() {
		return businessDAO.findAll();
	}

	@Transactional
	public void removeBusiness(Long id) {
		businessDAO.delete(id);
	}

	@Override
	public Business getBusinessById(Long id) {
		return businessDAO.findById(id);
	}
}

