package ...formatting.reporting;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import ...datatype.reporting.EnergyDataUsage;
import ...datatype.reporting.ReportData;
import ...domain.reporting.HourlyReport;
import ...formatting.DataFormat;
import ...mvc.service.reporting.DailyReport;


/**
 * 
 * @author pramod JSON report formatting
 */
public class DailyReportJSONDataFormat implements DataFormat {

	// Log
	protected static Logger logger = Logger
			.getLogger("dailyreportjsondataformat");

	@Override
	public String getFormattedString(Object report) {

		DailyReport dailyReport = (DailyReport) report;

		/* Parent of all */
		JSONObject dailyReportObj = new JSONObject();

		/* Parent of usage, total, peak, least */
		JSONObject reportDataObj = new JSONObject();

		/* Report data */
		JSONObject usageDataObj = new JSONObject();

		/* Child energy object or usageDataObj */
		JSONObject usageEnergyUsageObj = new JSONObject();

        // .. Other business objects

		// For getReportData
		usageEnergyUsageObj.put("totalDuration",
				usageEnergyUsage.getTotalDuration());
		usageEnergyUsageObj.put("startDate", usageEnergyUsage.getStartDate());
		usageEnergyUsageObj.put("endDate", usageEnergyUsage.getEndDate());
        // .. Other values		

		// For hourly reports
		List<HourlyReport> hourlyReports = dailyReport.getHourlyReports();
		JSONArray hourlyReportArray = new JSONArray();
		JSONObject hourChild;

		if (!dailyReport.isEmpty()) {
			logger.info("getting daily report");

			for (HourlyReport hourlyReport : hourlyReports) {
				hourChild = new JSONObject();

				hourChild.put("id", hourlyReport.getId());
				hourChild.put("duration", hourlyReport.getDuration());

				Format formatter = new SimpleDateFormat("HH");

				Date date = new Date();
				date.setTime(hourlyReport.getStartTime() * 1000);

				hourChild.put("start", formatter.format(date));
				hourChild.put("consumption", hourlyReport.getConsumption());

				hourlyReportArray.add(hourChild);
			}
		} else {
			// logger.error("daily report empty");
			logger.warn("daily report empty");
		}

		// Parent json object
		dailyReportObj.put("daily report", reportDataObj);

		reportDataObj.put("peak energy usage", peakEnergyUsageObj);
		reportDataObj.put("report data", usageDataObj);
		// ..		
		reportDataObj.put("hourly reports", hourlyReportArray);

		return dailyReportObj.toJSONString();
	}

}

