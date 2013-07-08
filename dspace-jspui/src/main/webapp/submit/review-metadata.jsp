<%--

    The contents of this file are subject to the license and copyright
    detailed in the LICENSE and NOTICE files at the root of the source
    tree and available online at

    http://www.dspace.org/license/

--%>

<%--
  - Review metadata page(s)
  -
  - Parameters to pass in to this page (from review.jsp)
  -    submission.jump - the step and page number (e.g. stepNum.pageNum) to create a "jump-to" link
  --%>

<%@ page contentType="text/html;charset=UTF-8" %>

<%@ page import="java.io.IOException" %>

<%@ page import="org.dspace.submit.step.DescribeStep" %>
<%@ page import="org.dspace.app.webui.servlet.SubmissionController" %>
<%@ page import="org.dspace.app.util.SubmissionInfo" %>
<%@ page import="org.dspace.content.InProgressSubmission" %>
<%@ page import="org.dspace.app.webui.util.UIUtil" %>
<%@ page import="org.dspace.content.Collection" %>
<%@ page import="org.dspace.content.DCDate" %>
<%@ page import="org.dspace.content.DCValue" %>
<%@ page import="org.dspace.content.Item" %>
<%@ page import="org.dspace.core.Context" %>
<%@ page import="org.dspace.core.Utils" %>

<%@ page import="org.dspace.content.authority.MetadataAuthorityManager" %>

<%@ page import="javax.servlet.jsp.jstl.fmt.LocaleSupport" %>
<%@ page import="javax.servlet.jsp.PageContext" %>


<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%
    request.setAttribute("LanguageSwitch", "hide");

    // Obtain DSpace context
    Context context = UIUtil.obtainContext(request);

        //get submission information object
    SubmissionInfo subInfo = SubmissionController.getSubmissionInfo(context, request);

        //get the step number (for jump-to link and to determine page)
        String stepJump = (String) request.getParameter("submission.jump");

        //extract out the step & page numbers from the stepJump (format: stepNum.pageNum)
        //(since there are multiple pages, we need to know which page we are reviewing!)
    String[] fields = stepJump.split("\\.");  //split on period
        int pageNum = Integer.parseInt(fields[1]);

    Item item = subInfo.getSubmissionItem().getItem();
%>

<%!void layoutSection(HttpServletRequest request,
                       javax.servlet.jsp.JspWriter out,
                       SubmissionInfo subInfo,
                       Item item,
                       int pageNum,
                       PageContext pageContext)
        throws ServletException, IOException, SQLException {
        InputFormMap inputFormsMap = new DSpace().getServiceManager().getServiceByName(InputFormMap.class.getName(), InputFormMap.class);
        InProgressSubmission ip = subInfo.getSubmissionItem();
        InputForm inputForm = inputFormsMap.getInputForm(ip.getCollection());

        MetadataAuthorityManager mam = MetadataAuthorityManager.getManager();


        InputFormPage inputFormPage = inputForm.getPages().get(pageNum);
        for (int z = 0; z < inputFormPage.getAllMetadataFields().size(); z++)
       {
           InputFormField formField = inputFormPage.getAllMetadataFields().get(z);

          DCValue[] values;
          StringBuilder row = new StringBuilder();
          
          row.append("<tr>");
          row.append("<td width=\"40%\" class=\"metadataFieldLabel\">");
          row.append(LocaleSupport.getLocalizedMessage(pageContext, formField.getLabelMessageKey()));
          row.append("</td>");
          row.append("<td width=\"60%\" class=\"metadataFieldValue\">");

          values = formField.getMetadataValues(item);

          if (values.length == 0)
          {
             row.append(LocaleSupport.getLocalizedMessage(pageContext, "jsp.submit.review.no_md"));
          }
          else
          {
              MetadataField metadataField = formField.getMetadataField();
              boolean isAuthorityControlled = mam.isAuthorityControlled(metadataField.getSchema(), metadataField.getElement(),metadataField.getQualifier());

              for (DCValue value : values) {
                  row.append(Utils.addEntities(formField.getDisplayedValue(value)));
                  if (isAuthorityControlled) {
                      row.append("<span class=\"ds-authority-confidence cf-")
                              .append(value.confidence).append("\">")
                              .append(" </span>");
                  }
                  row.append("<br />");
              }
          }
          row.append("</td>");
          row.append("</tr>");
   
          out.write(row.toString());
       }
    }%>


<%-- ====================================================== --%>
<%--             DESCRIBE ITEM ELEMENTS                     --%>
<%-- ====================================================== --%>
            
<%@page import="org.dspace.workflow.WorkflowItem"%>
<%@ page import="org.dspace.kernel.ServiceManager" %>
<%@ page import="org.dspace.utils.DSpace" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="org.dspace.submit.inputForms.components.*" %>
<table width="100%">
               <tr>
                   <td width="100%">
                   <table width="700px">

<%
            layoutSection(request, out, subInfo, item, pageNum, pageContext);
%>
                                        </table>
                                    </td>
                                    <td valign="middle">
                                         <input type="submit" name="submit_jump_<%=stepJump%>" value="<fmt:message key="jsp.submit.review.button.correct"/>" />
                                    </td>
                                </tr>
                        </table>
