package ...

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ...domain.Business;
import ...enums.AccountType;
import ...mvc.service.interfaces.BusinessService;

@Controller
@RequestMapping("/businesses/*")
public class BusinessController {
	// Log
	protected static Logger logger = Logger.getLogger(BusinessController.class);

	@Autowired
	private BusinessService businessService;

	@RequestMapping
	public String list(Map<String, Object> map) {
		map.put("businessList", businessService.listBusiness());
		
		for (Business business : businessService.listBusiness())
			logger.info(business.getName());

		return "businesses/list";
	}

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String showAdd(Model model, HttpServletRequest request)
			throws Exception {
		logger.info("request for business addition form");

		model.addAttribute("business", new Business());

		return "businesses/add";
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String addBusiness(@ModelAttribute("business") Business business,
			BindingResult result) {
		logger.info("add business");

		businessService.addBusiness(business);

		return "redirect:/businesses/";
	}

	@RequestMapping(value = "/view/{businessId}", method = RequestMethod.GET)
	public String view(@PathVariable("businessId") Long id, Model model) {

		logger.info("getting business for id " + id);

		Business business = businessService.getBusinessById(id);
		model.addAttribute("business", business);

		return "businesses/view";
	}

	@RequestMapping(value = "/edit/{businessId}", method = RequestMethod.GET)
	public String showEdit(@PathVariable("businessId") Long id, Model model) {
		logger.info("request for business edit form");

		Business business = businessService.getBusinessById(id);
		model.addAttribute("business", business);

		return "businesses/edit";
	}

	@RequestMapping(value = "/edit/{businessId}", method = RequestMethod.POST)
	public String update(@PathVariable("businessId") Long id,
			@ModelAttribute("business") Business business, BindingResult result) {

		business.setId(id);
		
		logger.info("update business");
		logger.info(business.getAddress());
		logger.info(business.getId());

		businessService.updateBusiness(business);

		return "redirect:/businesses/";
	}

	@RequestMapping(value = "/delete/{businessId}")
	public String delete(@PathVariable("businessId") Long id) {
		businessService.removeBusiness(id);

		return "redirect:/businesses/";
	}

	@ModelAttribute("account_types")
	public Map<String, String> accountTypeReference(HttpServletRequest request)
			throws Exception {
		Map<String, String> accountTypes = new LinkedHashMap<String, String>();

		for (AccountType accountType : AccountType.values()) {
			accountTypes.put(accountType.name(), accountType.name());
		}

		return accountTypes;
	}
}

