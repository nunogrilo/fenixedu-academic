package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.student.studentCurricularPlan;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.Branch;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class EditPosGradStudentCurricularPlanStateAndCredits extends Service {

    public void run(IUserView userView, Integer studentCurricularPlanId, String currentState, Double credits, String startDate,
	    List extraCurricularCourses, String observations, Integer branchId, String specialization)
	    throws FenixServiceException, ExcepcaoPersistencia {
	StudentCurricularPlan studentCurricularPlan = rootDomainObject.readStudentCurricularPlanByOID(studentCurricularPlanId);
	if (studentCurricularPlan == null) {
	    throw new InvalidArgumentsServiceException();
	}

	// StudentCurricularPlanState newState =
	// StudentCurricularPlanState.valueOf(currentState);

	Person person = Person.readPersonByUsername(userView.getUtilizador());
	if (person == null) {
	    throw new InvalidArgumentsServiceException();
	}

	Employee employee = person.getEmployee();
	if (employee == null) {
	    throw new InvalidArgumentsServiceException();
	}

	Branch branch = rootDomainObject.readBranchByOID(branchId);
	if (branch == null) {
	    throw new InvalidArgumentsServiceException();
	}

	studentCurricularPlan.setStartDate(stringDateToCalendar(startDate).getTime());
	// studentCurricularPlan.setCurrentState(newState);
	studentCurricularPlan.setEmployee(employee);
	studentCurricularPlan.setGivenCredits(credits);
	studentCurricularPlan.setObservations(observations);
	studentCurricularPlan.setBranch(branch);
	studentCurricularPlan.setSpecialization(Specialization.valueOf(specialization));

	List enrollments = studentCurricularPlan.getEnrolments();
	Iterator iterator = enrollments.iterator();

	while (iterator.hasNext()) {
	    Enrolment enrolment = (Enrolment) iterator.next();
	    if (extraCurricularCourses.contains(enrolment.getIdInternal())) {
		if (!enrolment.isExtraCurricular()) {
		    enrolment.markAsExtraCurricular();
		}
	    } else {
		if (enrolment.isExtraCurricular()) {
		    enrolment.setIsExtraCurricular(false);
		}
	    }
	}

    }

    private Calendar stringDateToCalendar(String startDate) throws NumberFormatException {
	Calendar calendar = Calendar.getInstance();
	String[] aux = startDate.split("/");
	calendar.set(Calendar.DAY_OF_MONTH, (new Integer(aux[0])).intValue());
	calendar.set(Calendar.MONTH, (new Integer(aux[1])).intValue() - 1);
	calendar.set(Calendar.YEAR, (new Integer(aux[2])).intValue());

	return calendar;
    }

}
