<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<h3>
	<html:link page='/teacherServiceDistribution.do?method=prepareTeacherServiceDistribution'>
		<bean:message key="link.teacherServiceDistribution"/>
	</html:link>
	>
	<html:link page="/teacherServiceDistribution.do?method=prepareForTeacherServiceDistributionCreation">
		<bean:message key="link.teacherServiceDistribution.teacherServiceDistributionCreation"/>
	</html:link>
	>
	<bean:message key="link.teacherServiceDistribution.teacherEmptyServiceDistributionCreation"/>
</h3>

<html:form action="/teacherServiceDistribution">
<html:hidden property="method" value="createTeacherServiceDistribution"/>
<html:hidden property="page" value="0"/>

<table class='vtsbc'>
<tr>
	<th colspan="2">
		<b><bean:write name="departmentName"/></b>
	</th>
</tr>
<tr>
	<td align="left">
		<b><bean:message key="label.teacherServiceDistribution.executionYear"/>:</b>
		&nbsp;&nbsp;
		<html:select property="executionYear" onchange="this.form.method.value='prepareForEmptyTeacherServiceDistributionCreation'; this.form.submit();">
			<html:options collection="executionYearList" property="idInternal" labelProperty="year"/>
		</html:select>
	</td>
	<td>
		<b><bean:message key="label.teacherServiceDistribution.semester"/>:</b>
		&nbsp;&nbsp;
		<html:select property="executionPeriod">
			<html:option value="-1"><bean:message key="label.teacherServiceDistribution.both"/></html:option>
			<html:options collection="executionPeriodsList" property="idInternal" labelProperty="semester"/>
		</html:select>
	</td>
</tr>
<tr>
	<td align="left" colspan="2">
		<b><bean:message key="label.teacherServiceDistribution.name"/>:<b>
		&nbsp;&nbsp;
		<html:text property="name" size="40" maxlength="240" />
	</td>
</tr>
</table>
<html:button property="" onclick="this.form.method.value='createTeacherServiceDistribution'; this.form.page.value=1; this.form.submit()">
	<bean:message key="label.teacherServiceDistribution.create"/>
</html:button>

<br/>
<span class="error"><html:errors property="name"/></span>
<br/>
<br/>

<html:link page="/teacherServiceDistribution.do?method=prepareForTeacherServiceDistributionCreation">
	<bean:message key="link.back"/>
</html:link>
</html:form>