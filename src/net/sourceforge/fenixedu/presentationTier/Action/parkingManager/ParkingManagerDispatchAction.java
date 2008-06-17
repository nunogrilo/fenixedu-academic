package net.sourceforge.fenixedu.presentationTier.Action.parkingManager;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.parking.ParkingPartyBean;
import net.sourceforge.fenixedu.dataTransferObject.parking.SearchPartyBean;
import net.sourceforge.fenixedu.dataTransferObject.parking.VehicleBean;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.FileEntry;
import net.sourceforge.fenixedu.domain.PartyClassification;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.parking.ParkingGroup;
import net.sourceforge.fenixedu.domain.parking.ParkingParty;
import net.sourceforge.fenixedu.domain.parking.ParkingRequest;
import net.sourceforge.fenixedu.domain.parking.ParkingRequestSearch;
import net.sourceforge.fenixedu.domain.parking.ParkingRequestState;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.util.report.ReportsUtils;
import net.sourceforge.fenixedu.util.report.StyledExcelSpreadsheet;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.util.Region;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public class ParkingManagerDispatchAction extends FenixDispatchAction {
    private static final int MAX_NOTE_LENGTH = 250;

    public ActionForward showParkingRequests(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	// verificar autoriza��o
	ParkingRequestSearch parkingRequestSearch = (ParkingRequestSearch) getRenderedObject("parkingRequestSearch");

	if (parkingRequestSearch == null) {
	    parkingRequestSearch = new ParkingRequestSearch();
	    parkingRequestSearch.setParkingRequestState(ParkingRequestState.PENDING);
	    setSearchCriteria(request, parkingRequestSearch);
	}
	if (request.getParameter("dontSearch") == null) {
	    parkingRequestSearch.doSearch();
	}
	request.setAttribute("dontSearch", request.getParameter("dontSearch"));
	request.setAttribute("parkingRequestSearch", parkingRequestSearch);
	return mapping.findForward("showParkingRequests");
    }

    private void setSearchCriteria(HttpServletRequest request, ParkingRequestSearch parkingRequestSearch) {
	String parkingRequestState = request.getParameter("parkingRequestState");
	if (!StringUtils.isEmpty(parkingRequestState)) {
	    parkingRequestSearch
		    .setParkingRequestState(ParkingRequestState.valueOf(parkingRequestState));
	}
	String partyClassification = request.getParameter("partyClassification");
	if (!StringUtils.isEmpty(partyClassification)) {
	    parkingRequestSearch
		    .setPartyClassification(PartyClassification.valueOf(partyClassification));
	}
	String personName = request.getParameter("personName");
	if (!StringUtils.isEmpty(personName)) {
	    parkingRequestSearch.setPersonName(personName);
	}

	String carPlateNumber = request.getParameter("carPlateNumber");
	if (!StringUtils.isEmpty(carPlateNumber)) {
	    parkingRequestSearch.setCarPlateNumber(carPlateNumber);
	}
    }

    public ActionForward showRequest(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	// verificar autoriza��o
	final String codeString = request.getParameter("idInternal");
	Integer code = null;
	if (codeString == null) {
	    code = (Integer) request.getAttribute("idInternal");
	} else {
	    code = new Integer(codeString);
	}

	final ParkingRequest parkingRequest = rootDomainObject.readParkingRequestByOID(code);
	if (parkingRequest.getParkingRequestState() == ParkingRequestState.PENDING) {
	    request.setAttribute("groups", ParkingGroup.getAll());
	}
	request.setAttribute("parkingRequest", parkingRequest);
	request.setAttribute("parkingPartyBean", new ParkingPartyBean(parkingRequest.getParkingParty()));
	DynaActionForm dynaActionForm = (DynaActionForm) actionForm;
	if (!StringUtils.isEmpty(dynaActionForm.getString("cardAlwaysValid"))) { // in case of error validation
	    dynaActionForm.set("cardAlwaysValid", dynaActionForm.getString("cardAlwaysValid"));
	} else {
	    dynaActionForm.set("cardAlwaysValid", "no");
	}

	if (parkingRequest.getParkingParty().getParty().isPerson()) {
	    Person person = (Person) parkingRequest.getParkingParty().getParty();
	    if (person.getTeacher() != null
		    && person.getTeacher().isMonitor(ExecutionSemester.readActualExecutionSemester())) {
		request.setAttribute("monitor", "true");
	    }
	}

	String parkingRequestState = request.getParameter("parkingRequestState");
	if (parkingRequestState == null) {
	    parkingRequestState = "";
	}
	String partyClassification = request.getParameter("partyClassification");
	if (partyClassification == null) {
	    partyClassification = "";
	}
	String personName = request.getParameter("personName");
	if (personName == null) {
	    personName = "";
	}

	String carPlateNumber = request.getParameter("carPlateNumber");
	if (carPlateNumber == null) {
	    carPlateNumber = "";
	}

	request.setAttribute("parkingRequestState", parkingRequestState);
	request.setAttribute("partyClassification", partyClassification);
	request.setAttribute("personName", personName);
	request.setAttribute("carPlateNumber", carPlateNumber);
	return mapping.findForward("showParkingRequest");
    }

    public ActionForward showPhoto(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	Integer personID = new Integer(request.getParameter("personID"));
	Party party = rootDomainObject.readPartyByOID(personID);
	if (party.isPerson()) {
	    Person person = (Person) party;
	    FileEntry personalPhoto = person.getPersonalPhoto();
	    if (personalPhoto != null) {
		try {
		    response.setContentType(personalPhoto.getContentType().getMimeType());
		    DataOutputStream dos = new DataOutputStream(response.getOutputStream());
		    dos.write(personalPhoto.getContents());
		    dos.close();
		} catch (java.io.IOException e) {
		    throw new FenixActionException(e);
		}
	    }
	}
	return null;
    }

    public ActionForward editFirstTimeParkingParty(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	Integer parkingRequestID = new Integer(request.getParameter("code"));
	final ParkingRequest parkingRequest = rootDomainObject.readParkingRequestByOID(parkingRequestID);

	String note = request.getParameter("note");
	Object args[] = { parkingRequest, null, null, null, note, null, null };

	String parkingRequestState = request.getParameter("parkingRequestState");
	if (parkingRequestState == null) {
	    parkingRequestState = "";
	}
	String partyClassification = request.getParameter("partyClassification");
	if (partyClassification == null) {
	    partyClassification = "";
	}
	String personName = request.getParameter("personName");
	if (personName == null) {
	    personName = "";
	}
	String carPlateNumber = request.getParameter("carPlateNumber");
	if (carPlateNumber == null) {
	    carPlateNumber = "";
	}
	request.setAttribute("parkingRequestState", parkingRequestState);
	request.setAttribute("partyClassification", partyClassification);
	request.setAttribute("personName", personName);
	request.setAttribute("carPlateNumber", carPlateNumber);

	DynaActionForm dynaForm = (DynaActionForm) actionForm;
	if (!StringUtils.isEmpty((String) dynaForm.get("accepted"))
		|| request.getParameter("acceptPrint") != null) {
	    Long cardNumber = null;
	    Integer group = null;
	    try {
		cardNumber = new Long(request.getParameter("cardNumber"));
		if (cardNumber <= 0) {
		    saveErrorMessage(request, "cardNumber", "error.number.below.minimum");
		    request.setAttribute("idInternal", parkingRequestID);
		    return showRequest(mapping, actionForm, request, response);
		}
	    } catch (NullPointerException e) {
		saveErrorMessage(request, "cardNumber", "error.requiredCardNumber");
		request.setAttribute("idInternal", parkingRequestID);
		return showRequest(mapping, actionForm, request, response);
	    } catch (NumberFormatException e) {
		saveErrorMessage(request, "cardNumber", "error.invalidCardNumber");
		request.setAttribute("idInternal", parkingRequestID);
		return showRequest(mapping, actionForm, request, response);
	    }
	    try {
		group = new Integer(request.getParameter("groupID"));
	    } catch (NullPointerException e) {
		saveErrorMessage(request, "group", "error.requiredGroup");
		request.setAttribute("idInternal", parkingRequestID);
		return showRequest(mapping, actionForm, request, response);
	    } catch (NumberFormatException e) {
		saveErrorMessage(request, "group", "error.invalidGroup");
		request.setAttribute("idInternal", parkingRequestID);
		return showRequest(mapping, actionForm, request, response);
	    }

	    ParkingPartyBean parkingPartyBean = (ParkingPartyBean) getFactoryObject();
	    if (!isValidCardNumber(cardNumber, parkingPartyBean)) {
		saveErrorMessage(request, "cardNumber", "error.alreadyExistsCardNumber");
		request.setAttribute("idInternal", parkingRequestID);
		return showRequest(mapping, actionForm, request, response);
	    }
	    if (!isValidGroup(group)) {
		saveErrorMessage(request, "group", "error.invalidGroup");
		request.setAttribute("idInternal", parkingRequestID);
		return showRequest(mapping, actionForm, request, response);
	    }

	    String cardAlwaysValid = dynaForm.getString("cardAlwaysValid");
	    if (cardAlwaysValid.equalsIgnoreCase("no")
		    && (parkingPartyBean.getCardStartDate() == null || parkingPartyBean.getCardEndDate() == null)) {
		saveErrorMessage(request, "mustFillInDates", "error.card.mustFillInDates");
		request.setAttribute("idInternal", parkingRequestID);
		return showRequest(mapping, actionForm, request, response);
	    } else if (cardAlwaysValid.equalsIgnoreCase("yes")) {
		parkingPartyBean.setCardStartDate(null);
		parkingPartyBean.setCardEndDate(null);
	    }

	    if (!StringUtils.isEmpty(note) && note.length() > MAX_NOTE_LENGTH) {
		ActionMessages actionMessages = getMessages(request);
		actionMessages
			.add("note", new ActionMessage("error.maxLengthExceeded", MAX_NOTE_LENGTH));
		saveMessages(request, actionMessages);
		request.setAttribute("idInternal", parkingRequestID);
		return showRequest(mapping, actionForm, request, response);
	    }

	    if (parkingPartyBean.getCardStartDate() != null
		    && parkingPartyBean.getCardStartDate().isAfter(parkingPartyBean.getCardEndDate())) {
		saveErrorMessage(request, "invalidPeriod", "error.parkingParty.invalidPeriod");
		request.setAttribute("idInternal", parkingRequestID);
		return showRequest(mapping, actionForm, request, response);
	    }
	    args[1] = ParkingRequestState.ACCEPTED;
	    args[2] = cardNumber;
	    args[3] = group;
	    args[5] = parkingPartyBean.getCardStartDate();
	    args[6] = parkingPartyBean.getCardEndDate();
	} else if (request.getParameter("reject") != null) {
	    args[1] = ParkingRequestState.REJECTED;
	} else {
	    args[1] = ParkingRequestState.PENDING;
	}

	executeService("UpdateParkingParty", args);

	return showParkingRequests(mapping, actionForm, request, response);
    }

    public ActionForward exportToPDFParkingCard(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	String parkingPartyID = (String) request.getParameter("parkingPartyID");

	final ParkingParty parkingParty = rootDomainObject.readParkingPartyByOID(new Integer(
		parkingPartyID));
	Integer parkingGroupID = request.getParameter("groupID") != null ? Integer.valueOf(request
		.getParameter("groupID")) : null;
	ParkingGroup parkingGroup = null;
	if (parkingGroupID != null) {
	    parkingGroup = rootDomainObject.readParkingGroupByOID(parkingGroupID);
	} else {
	    parkingGroup = parkingParty.getParkingGroup();
	}

	Map<String, Object> parameters = new HashMap<String, Object>();
	parameters.put("imageUrl", getServlet().getServletContext().getRealPath("/").concat(
		"/images/Logo_IST_color.tiff"));

	Person person = (Person) parkingParty.getParty();
	parameters.put("number", getMostSignificantNumber(person, parkingGroup));

	List<Person> persons = new ArrayList<Person>();
	persons.add(person);

	byte[] data = ReportsUtils.exportToPdf("parkingManager.parkingCard", parameters, null, persons);
	response.setContentType("application/pdf");
	response.addHeader("Content-Disposition", "attachment; filename=cartao.pdf");
	response.setContentLength(data.length);
	ServletOutputStream writer = response.getOutputStream();
	writer.write(data);
	writer.flush();
	writer.close();
	response.flushBuffer();

	return mapping.findForward("");
    }

    private String getMostSignificantNumber(Person p, ParkingGroup parkingGroup) {
	if (p.getParkingParty().getPhdNumber() != null) {
	    return "N�: " + p.getParkingParty().getPhdNumber();
	}
	if (p.getTeacher() != null && p.getTeacher().getCurrentWorkingDepartment() != null
		&& !p.getTeacher().isMonitor(ExecutionSemester.readActualExecutionSemester())) {
	    return "N� Mec: " + p.getTeacher().getTeacherNumber();
	}
	if (p.getEmployee() != null && p.getEmployee().getCurrentWorkingContract() != null
		&& p.getPersonRole(RoleType.TEACHER) == null) {
	    return "N� Mec: " + p.getEmployee().getEmployeeNumber();
	}
	if (p.getStudent() != null && !parkingGroup.getGroupName().equalsIgnoreCase("Bolseiros")) {
	    DegreeType degreeType = p.getStudent().getMostSignificantDegreeType();
	    Collection<Registration> registrations = p.getStudent().getRegistrationsByDegreeType(
		    degreeType);
	    for (Registration registration : registrations) {
		StudentCurricularPlan scp = registration.getActiveStudentCurricularPlan();
		if (scp != null) {
		    return "N�: " + p.getStudent().getNumber();
		}
	    }
	}
	if (p.getGrantOwner() != null && p.getGrantOwner().hasCurrentContract()) {
	    return "N�: " + p.getGrantOwner().getNumber();
	}
	if (p.getTeacher() != null && p.getTeacher().getCurrentWorkingDepartment() != null
		&& p.getTeacher().isMonitor(ExecutionSemester.readActualExecutionSemester())) {
	    return "N� Mec: " + p.getTeacher().getTeacherNumber();
	}
	return "";
    }

    private boolean isValidGroup(Integer groupId) {
	for (ParkingGroup group : rootDomainObject.getParkingGroups()) {
	    if (group.getIdInternal().equals(groupId)) {
		return true;
	    }
	}
	return false;
    }

    private boolean isValidCardNumber(Long cardNumber, ParkingPartyBean parkingPartyBean) {
	for (ParkingParty parkingParty : rootDomainObject.getParkingParties()) {
	    if (parkingParty.getCardNumber() != null
		    && parkingPartyBean.getParkingParty() != parkingParty
		    && parkingParty.getCardNumber().equals(cardNumber)) {
		return false;
	    }
	}
	return true;
    }

    private void saveErrorMessage(HttpServletRequest request, String error, String errorMessage) {
	ActionMessages actionMessages = getMessages(request);
	actionMessages.add(error, new ActionMessage(errorMessage));
	saveMessages(request, actionMessages);
    }

    public ActionForward prepareSearchParty(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	request.setAttribute("searchPartyBean", new SearchPartyBean());
	return mapping.findForward("searchParty");
    }

    public ActionForward showParkingPartyRequests(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	IViewState viewState = RenderUtils.getViewState("searchPartyBean");
	SearchPartyBean searchPartyBean = null;
	if (viewState != null) {
	    searchPartyBean = (SearchPartyBean) viewState.getMetaObject().getObject();
	}

	if (searchPartyBean != null) {
	    searchPartyBean.setParty(null);
	    Object[] args = { searchPartyBean.getPartyName(), searchPartyBean.getCarPlateNumber() };
	    List<Party> partyList = (List<Party>) executeService("SearchPartyCarPlate", args);
	    request.setAttribute("searchPartyBean", searchPartyBean);
	    request.setAttribute("partyList", partyList);

	} else if (request.getParameter("partyID") != null || request.getAttribute("partyID") != null) {
	    final Integer idInternal = getPopertyID(request, "partyID");
	    final String carPlateNumber = request.getParameter("plateNumber");
	    Party party = rootDomainObject.readPartyByOID(idInternal);
	    setupParkingRequests(request, party, carPlateNumber);
	}

	return mapping.findForward("showParkingPartyRequests");
    }

    private int getPopertyID(HttpServletRequest request, String property) {
	if (request.getParameter(property) != null) {
	    return new Integer(request.getParameter(property));
	} else {
	    return (Integer) request.getAttribute(property);
	}
    }

    private void setupParkingRequests(HttpServletRequest request, Party party, String carPlateNumber)
	    throws FenixFilterException, FenixServiceException {
	if (party.getParkingParty() != null) {
	    request.setAttribute("parkingRequests", party.getParkingParty().getParkingRequests());
	}
	request.setAttribute("searchPartyBean", new SearchPartyBean(party, carPlateNumber));
    }

    public ActionForward prepareEditParkingParty(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	ParkingParty parkingParty = null;
	ParkingPartyBean parkingPartyBean = (ParkingPartyBean) getFactoryObject();
	if ((!StringUtils.isEmpty(request.getParameter("addVehicle")) && request.getParameter(
		"addVehicle").equals("yes"))
		&& parkingPartyBean != null) {
	    parkingPartyBean.addVehicle();
	    parkingParty = parkingPartyBean.getParkingParty();
	    request.setAttribute("parkingPartyBean", parkingPartyBean);
	    request.setAttribute("parkingPartyID", parkingParty.getIdInternal());
	    ((DynaActionForm) actionForm).set("addVehicle", "no");
	    RenderUtils.invalidateViewState();
	} else {
	    Integer parkingPartyID = getPopertyID(request, "parkingPartyID");
	    parkingParty = rootDomainObject.readParkingPartyByOID(parkingPartyID);
	    request.setAttribute("parkingPartyBean", new ParkingPartyBean(parkingParty));
	    request.setAttribute("parkingPartyID", parkingParty.getIdInternal());
	}

	DynaActionForm dynaActionForm = (DynaActionForm) actionForm;
	if (parkingParty.hasAnyVehicles()) {
	    if (!StringUtils.isEmpty(dynaActionForm.getString("cardAlwaysValid"))) {
		dynaActionForm.set("cardAlwaysValid", dynaActionForm.getString("cardAlwaysValid")); // in case of error validation
	    } else {
		if (parkingParty.getCardStartDate() == null) {
		    dynaActionForm.set("cardAlwaysValid", "yes");
		} else {
		    dynaActionForm.set("cardAlwaysValid", "no");
		}
	    }
	} else {
	    if (!StringUtils.isEmpty(dynaActionForm.getString("cardAlwaysValid"))) { // in case of error validation
		dynaActionForm.set("cardAlwaysValid", dynaActionForm.getString("cardAlwaysValid"));
	    } else {
		dynaActionForm.set("cardAlwaysValid", "no");
	    }
	}
	return mapping.findForward("editParkingParty");
    }

    public ActionForward editParkingParty(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	ParkingPartyBean parkingPartyBean = (ParkingPartyBean) getFactoryObject();
	request.setAttribute("parkingPartyID", parkingPartyBean.getParkingParty().getIdInternal());
	if (!isRepeatedCardNumber(parkingPartyBean.getCardNumber(), parkingPartyBean.getParkingParty())) {
	    saveErrorMessage(request, "cardNumber", "error.alreadyExistsCardNumber");
	    return prepareEditParkingParty(mapping, actionForm, request, response);
	}
	DynaActionForm dynaForm = (DynaActionForm) actionForm;
	String cardAlwaysValid = dynaForm.getString("cardAlwaysValid");
	parkingPartyBean.setCardAlwaysValid(false);
	if (cardAlwaysValid.equalsIgnoreCase("no")
		&& (parkingPartyBean.getCardStartDate() == null || parkingPartyBean.getCardEndDate() == null)) {
	    saveErrorMessage(request, "mustFillInDates", "error.card.mustFillInDates");
	    return prepareEditParkingParty(mapping, actionForm, request, response);
	} else if (cardAlwaysValid.equalsIgnoreCase("yes")) {
	    parkingPartyBean.setCardAlwaysValid(true);
	    parkingPartyBean.setCardStartDate(null);
	    parkingPartyBean.setCardEndDate(null);
	}
	boolean vehicleDataCorrect = true;
	boolean deleteAllVehicles = true;
	Set<String> vehiclePlates = new HashSet<String>();
	for (final Iterator<VehicleBean> iter = parkingPartyBean.getVehicles().iterator(); iter
		.hasNext();) {
	    VehicleBean vehicle = iter.next();
	    if (!vehicle.getDeleteVehicle()) {
		deleteAllVehicles = false;
		if (StringUtils.isEmpty(vehicle.getVehicleMake())
			|| StringUtils.isEmpty(vehicle.getVehiclePlateNumber())) {
		    vehicleDataCorrect = false;
		    break;
		}
	    } else if (vehicle.getVehicle() == null) {
		iter.remove();
		parkingPartyBean.getVehicles().remove(vehicle);
	    }
	    if (!vehicle.getDeleteVehicle() && !vehiclePlates.add(vehicle.getVehiclePlateNumber())) {
		saveErrorMessage(request, "repeatedPlates", "error.parkingParty.vehicle.repeatedPlates");
		return prepareEditParkingParty(mapping, actionForm, request, response);
	    }
	}
	if (!vehicleDataCorrect) {
	    saveErrorMessage(request, "vehicleMandatoryData", "error.parkingParty.vehicle.mandatoryData");
	    return prepareEditParkingParty(mapping, actionForm, request, response);
	}
	if (deleteAllVehicles) {
	    saveErrorMessage(request, "noVehicles", "error.parkingParty.no.vehicles");
	    return prepareEditParkingParty(mapping, actionForm, request, response);
	}
	executeService("ExecuteFactoryMethod", new Object[] { parkingPartyBean });
	request.setAttribute("partyID", parkingPartyBean.getParkingParty().getParty().getIdInternal());

	return showParkingPartyRequests(mapping, actionForm, request, response);
    }

    public ActionForward exportToExcel(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws IOException {
	ParkingRequestSearch parkingRequestSearch = new ParkingRequestSearch();
	setSearchCriteria(request, parkingRequestSearch);
	List<ParkingRequest> parkingRequestList = parkingRequestSearch.getSearchResult();
	StyledExcelSpreadsheet spreadsheet = new StyledExcelSpreadsheet("Pedidos_Parque", 15);
	spreadsheet.newHeaderRow();
	spreadsheet.addHeader("Categoria");
	spreadsheet.addHeader("N�mero");
	spreadsheet.addHeader("Nome", 9000);
	spreadsheet.addHeader("Estado");
	spreadsheet.addHeader("Data Pedido");
	spreadsheet.addHeader("Outras Informa��es", 6000);

	final ResourceBundle enumerationBundle = ResourceBundle.getBundle(
		"resources.EnumerationResources", Language.getLocale());
	for (ParkingRequest parkingRequest : parkingRequestList) {
	    if (parkingRequest.getParkingParty().getParty().isPerson()) {
		Person person = (Person) parkingRequest.getParkingParty().getParty();
		spreadsheet.newRow();
		int firstRow = spreadsheet.getRow().getRowNum();
		spreadsheet.addCell(enumerationBundle.getString(parkingRequestSearch
			.getPartyClassification().name()));
		spreadsheet.addCell(parkingRequest.getParkingParty().getMostSignificantNumber());
		spreadsheet.addCell(person.getName());
		spreadsheet.addCell(enumerationBundle.getString(parkingRequest.getParkingRequestState()
			.name()));
		spreadsheet.addDateTimeCell(parkingRequest.getCreationDate());
		if (!parkingRequest.getParkingParty().getDegreesInformation().isEmpty()) {
		    Iterator<String> iterator = parkingRequest.getParkingParty().getDegreesInformation()
			    .iterator();
		    String degreeInfo = (String) iterator.next();
		    spreadsheet.addCell(degreeInfo);
		    while (iterator.hasNext()) {
			spreadsheet.newRow();
			degreeInfo = (String) iterator.next();
			spreadsheet.addCell(degreeInfo, 5);
		    }
		    int lastRow = firstRow
			    + parkingRequest.getParkingParty().getDegreesInformation().size() - 1;
		    if (firstRow != lastRow) {
			for (int iter = 0; iter < 5; iter++) {
			    spreadsheet.getSheet().addMergedRegion(
				    new Region(firstRow, (short) iter, lastRow, (short) iter));
			}
		    }
		}
	    }
	}
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("Content-Disposition", "attachment; filename=pedidos_parque.xls");
	final ServletOutputStream writer = response.getOutputStream();
	spreadsheet.getWorkbook().write(writer);
	writer.flush();
	response.flushBuffer();
	return null;
    }

    private boolean isRepeatedCardNumber(Long cardNumber, ParkingParty parkingParty) {
	for (ParkingParty tempParkingParty : rootDomainObject.getParkingParties()) {
	    if (tempParkingParty.getCardNumber() != null
		    && tempParkingParty.getCardNumber().equals(cardNumber)
		    && tempParkingParty != parkingParty) {
		return false;
	    }
	}
	return true;
    }
}