package ...mvc.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import ...domain.Address;
import ...domain.Business;
import ...domain.Document;
import ...domain.Note;
import ...domain.WorkOrder;
import ...enums.WOStatus;
import ...mvc.service.interfaces.NoteService;
import ...mvc.service.interfaces.WorkOrderService;

@Controller
@RequestMapping("/workorders/*")
public class WorkOrderController {

	// Log
	protected Logger logger = Logger.getLogger(this.getClass()
			.getCanonicalName());

	@Autowired
	private WorkOrderService workOrderService;

	@Autowired
	private NoteService noteService;


	@Autowired
	private MessageSource messageSource;

	@RequestMapping
	public String list(Map<String, Object> map) {
		map.put("workOrderList", workOrderService.listWorkOrders());

		return "workorders/list";
	}

    // ... Other actions

	@RequestMapping(value = "/addNote", method = RequestMethod.POST)
	public String addNote(@ModelAttribute("note") Note note,
			HttpServletRequest request, Model model) {

		Long woId = Long.parseLong(request.getParameter("woId"));
		WorkOrder workOrder = workOrderService.getWorkOrderById(woId);

		boolean addNote = false;

		if (note.getId() == null) {
			workOrder.getNotes().add(note);

			addNote = true;
			// merge
			workOrderService.addWorkOrder(workOrder, true);

		} else {
		
			// In case of edit
			noteService.updateNote(note);

			addNote = false;
		}

		// In case of add
		if (addNote == true) {
			List<Note> notesList = workOrder.getNotes();
			Note lastNote = null;

			if (notesList.size() > 0)
				lastNote = notesList.get(notesList.size() - 1);

			if (lastNote != null) {
				model.addAttribute("note", lastNote);
				model.addAttribute("notesSize", notesList.size());
				model.addAttribute("added", "added");
			}
		}

		return "workorders/noteAdded";
	}

	// ... Other actions
	
	@RequestMapping(value = "/delete/{workOrderId}")
	public String deleteWorkOrder(@PathVariable("workOrderId") Long id,
			Locale locale) {

		// Before deleting work order make sure the files are deleted too.

		WorkOrder workOrder = workOrderService.getWorkOrderById(id);
		List<Document> documents = new ArrayList<Document>();

		String UPLOAD_DIR = messageSource.getMessage("app.uploadDir", null, "",
				locale);
		Subject currentUser = SecurityUtils.getSubject();
		UPLOAD_DIR = UPLOAD_DIR + currentUser.getPrincipal() + File.separator;

		if (workOrder != null) {

			// Cannot delete a directory with files.			

			documents = workOrder.getDocuments();

			for (Document document : documents) {
				File targetFile = new File(UPLOAD_DIR + document.getFinalName());

				if (targetFile.delete()) {
					logger.info("File successfully deleted.");
				} else {
					logger.error("Could not delete file.");
				}
			}

			// Now work order can be deleted
			workOrderService.removeWorkOrder(id);

		} else {
			logger.error("Could not delete workorder.");
		}

		return "redirect:/workorders/";
	}

	@RequestMapping(value = "/uploadDocument", method = RequestMethod.POST)
	public String uploadDocument(@ModelAttribute("document") Document document,
			HttpServletRequest request, Model model, Locale locale) {

		String UPLOAD_DIR = messageSource.getMessage("app.uploadDir", null, "",
				locale);
		Subject currentUser = SecurityUtils.getSubject();

		UPLOAD_DIR = UPLOAD_DIR + currentUser.getPrincipal() + File.separator;

		try {
			File dir = new File(UPLOAD_DIR);

			if (!dir.exists()) {
				boolean created = (dir).mkdirs();

				if (!created)
					logger.error("Could not create directory for user");
			}

			document.setOriginalName(document.getFileData()
					.getOriginalFilename());
			document.setFinalName(System.currentTimeMillis() + "");

			File myFile = new File(dir, document.getFinalName());
			document.getFileData().transferTo(myFile);

			logger.info("Successfully uploaded file");

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		Long woId = Long.parseLong(request.getParameter("dwoId"));
		WorkOrder workOrder = workOrderService.getWorkOrderById(woId);

		workOrder.getDocuments().add(document);
		workOrderService.addWorkOrder(workOrder, true);

		List<Document> documents = workOrder.getDocuments();
		Document lastDocument = null;

		if (documents.size() > 0)
			lastDocument = documents.get(documents.size() - 1);

		if (lastDocument != null) {
			model.addAttribute("document", lastDocument);
			model.addAttribute("documentsSize", documents.size());
		}

		return "workorders/uploadDocument";
	}

	// ... Other actions

}

