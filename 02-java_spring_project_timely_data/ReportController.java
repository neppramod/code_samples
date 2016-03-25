package ...mvc.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import ...domain.user.User;
import ...formatting.DataFormat;
import ...formatting.reporting.DailyReportJSONDataFormat;
import ...mvc.service.generic.interfaces.UsageService;
import ...mvc.service.generic.interfaces.UserService;
import ...mvc.service.reporting.DailyReport;
import ...util.customdatatype.Date;

/**
 * 
 * @author pramod Report generating controller
 */
 
@Controller
@RequestMapping("/reports")
public class ReportController {
	@Autowired
	private UserService userService;

	@Autowired
	private UsageService usageService;

	@Autowired
	DailyReport dailyReport;

	// Log
	protected static Logger logger = Logger.getLogger("reportcontroller");

	// Needs sid, year, month, day
	@RequestMapping(value = "/daily", method = RequestMethod.GET)
	public String getDailyReport(Model model, @RequestParam("sid") String sid,
			@RequestParam("year") Integer year,
			@RequestParam("month") Integer month,
			@RequestParam("day") Integer dayOfMonth) {

		// Log all the parameters
		logger.info("sid : " + sid);
		logger.info("year : " + year);
		logger.info("month : " + month);
		logger.info("day : " + dayOfMonth);

		logger.info("Finding user by sid");
		User user = userService.findUserBySid(sid);

		logger.info("Setting date");
		Date today = new Date(year, month, dayOfMonth);

		if (user != null) {
			logger.info("Setting daily report setters like day, user, format");

			dailyReport.setDay(today);
			dailyReport.setUser(user);
			dailyReport.setUsageDataFormat(usageService.getUsageDataFormat());

			DataFormat dataFormat = new DailyReportJSONDataFormat();

			String formattedData = dataFormat.getFormattedString(dailyReport);

			if (formattedData != null)
				model.addAttribute("report", formattedData);
		}

		return "reports/genericreport";
	}
}

