<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><%@page import="java.util.Enumeration"%>
<html:xhtml/><%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %><%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %><%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %><%@ page import="net.sourceforge.fenixedu.presentationTier.servlets.filters.SessionCounterFilter" %><h2><bean:message bundle="MANAGER_RESOURCES" key="manager.monitor.system.title"/></h2><br /><html:link module="/manager" page="/monitorSystem.do?method=monitor">	<bean:message bundle="MANAGER_RESOURCES" key="manager.monitor.system.refresh"/></html:link><br /><br /><br /><logic:present name="systemInfoApplicationServer">	<bean:message bundle="MANAGER_RESOURCES" key="manager.monitor.system.applicationServer"/>	<br /><br />
	<bean:message bundle="MANAGER_RESOURCES" key="label.server.name"/><%= request.getServerName() %>
	<br/>
	<bean:message bundle="MANAGER_RESOURCES" key="label.real.server.name"/><%= System.getenv("HOSTNAME") %>
	<br/>
	<strong><bean:message bundle="MANAGER_RESOURCES" key="label.note"/></strong>
	<bean:message bundle="MANAGER_RESOURCES" key="message.memory.units"/>	<table>		<tr>			<th class="listClasses-header">				<bean:message bundle="MANAGER_RESOURCES" key="manager.monitor.system.availableProcessors"/>			</th>			<th class="listClasses-header">				<bean:message bundle="MANAGER_RESOURCES" key="manager.monitor.system.freeMemory"/>			</th>			<th class="listClasses-header">				<bean:message bundle="MANAGER_RESOURCES" key="manager.monitor.system.totalMemory"/>			</th>			<th class="listClasses-header">				<bean:message bundle="MANAGER_RESOURCES" key="manager.monitor.system.maxMemory"/>			</th>		</tr>		<tr>			<td class="listClasses">							<bean:write name="systemInfoApplicationServer" property="availableProcessors"/>			</td>			<td class="listClasses">				<bean:write name="systemInfoApplicationServer" property="freeMemory"/>			</td>			<td class="listClasses">				<bean:write name="systemInfoApplicationServer" property="totalMemory"/>			</td>			<td class="listClasses">				<bean:write name="systemInfoApplicationServer" property="maxMemory"/>			</td>		</tr>	</table>
	<br/>
	<strong><bean:message bundle="MANAGER_RESOURCES" key="label.system.properties"/></strong>
	<br/>	<logic:iterate id="property" name="systemInfoApplicationServer" property="properties">		<bean:write name="property"/><br />	</logic:iterate></logic:present><logic:notPresent name="systemInfoApplicationServer">	<span class="error"><!-- Error messages go here -->		Error obtaining log information for application server.	</span></logic:notPresent><br /><br />
<strong><bean:message bundle="MANAGER_RESOURCES" key="label.system.env.properties"/></strong>
<br/><% request.setAttribute("systemEnv", System.getenv()); %>
<logic:iterate id="property" name="systemEnv">
	<bean:write name="property" property="key"/>=<bean:write name="property" property="value"/><br />
</logic:iterate>

<br/><br/>
<strong><bean:message bundle="MANAGER_RESOURCES" key="label.request.headers"/></strong>
<br/>
<%
	for (final Enumeration e = request.getHeaderNames(); e.hasMoreElements(); ) {
	    final Object o = e.nextElement();
	    %>
	    <strong>
	    <%= o.toString() %>
	    </strong>
	    =
	    <%= request.getHeader(o.toString()) %>
	    <br/>
	    <%
	}
%>