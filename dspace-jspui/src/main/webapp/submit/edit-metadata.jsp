<%--

    The contents of this file are subject to the license and copyright
    detailed in the LICENSE and NOTICE files at the root of the source
    tree and available online at

    http://www.dspace.org/license/

--%>
<%--
  - Edit metadata form
  -
  - Attributes to pass in to this page:
  -    submission.info   - the SubmissionInfo object
  -    submission.inputs - the DCInputSet
  -    submission.page   - the step in submission
  --%>

<%@ page contentType="text/html;charset=UTF-8" %>

<%@ page import="javax.servlet.jsp.jstl.fmt.LocaleSupport" %>

<%@ page import="org.dspace.core.Context" %>
<%@ page import="org.dspace.app.webui.servlet.SubmissionController" %>
<%@ page import="org.dspace.submit.AbstractProcessingStep" %>
<%@ page import="org.dspace.app.util.SubmissionInfo" %>
<%@ page import="org.dspace.app.webui.util.UIUtil" %>
<%@ page import="org.dspace.content.Item" %>
<%@ page import="org.dspace.submit.inputForms.components.InputForm" %>
<%@ page import="org.dspace.submit.inputForms.components.InputFormPage" %>
<%@ page import="org.dspace.app.webui.submit.inputForms.components.InputFormJspuiMap" %>
<%@ page import="org.dspace.app.webui.submit.inputForms.components.InputFormField" %>

<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%
    request.setAttribute("LanguageSwitch", "hide");
%>

<%
    // Obtain DSpace context
    Context context = UIUtil.obtainContext(request);

    SubmissionInfo si = SubmissionController.getSubmissionInfo(context, request);

    Item item = si.getSubmissionItem().getItem();

    final int halfWidth = 23;
    final int fullWidth = 50;
    final int twothirdsWidth = 34;

    InputForm inputForm =
        (InputForm) request.getAttribute("submission.inputs");
    InputFormJspuiMap<String, InputFormField> inputFormJspuiMap =
            (InputFormJspuiMap<String, InputFormField>) request.getAttribute("submission.jspui.map");

    Integer pageNumStr =
        (Integer) request.getAttribute("submission.page");
    int pageNum = pageNumStr;

    // owning Collection ID for choice authority calls
    int collectionID = si.getSubmissionItem().getCollection().getID();
%>

<dspace:layout locbar="off" navbar="off" titlekey="jsp.submit.edit-metadata.title">

  <form action="<%= request.getContextPath() %>/submit#<%= si.getJumpToField()%>" method="post" name="edit_metadata" id="edit_metadata" onkeydown="return disableEnterKey(event);">

        <jsp:include page="/submit/progressbar.jsp"></jsp:include>

    <h1><fmt:message key="jsp.submit.edit-metadata.heading"/></h1>

<%
     //figure out which help page to display
     if (pageNum <= 1)
     {
%>
        <div><fmt:message key="jsp.submit.edit-metadata.info1"/>
        <dspace:popup page="<%= LocaleSupport.getLocalizedMessage(pageContext, \"help.index\") + \"#describe2\"%>"><fmt:message key="jsp.submit.edit-metadata.help"/></dspace:popup></div>
<%
     }
     else
     {
%>
        <div><fmt:message key="jsp.submit.edit-metadata.info2"/>
        <dspace:popup page="<%= LocaleSupport.getLocalizedMessage(pageContext, \"help.index\") + \"#describe3\"%>"><fmt:message key="jsp.submit.edit-metadata.help"/></dspace:popup></div>
    
<%
     }
%>

     <%-- HACK: a <center> tag seems to be the only way to convince certain --%>
     <%--       browsers to center the table. --%>
     <center>
     <table>
<%
         int pageIdx = pageNum - 1;

    InputFormPage inputFormPage = inputForm.getPages().get(pageIdx);
    java.util.List<org.dspace.submit.inputForms.components.InputFormField> formFields = inputFormPage.getMetadataFields(si);
    int fieldCountIncr;

     for (org.dspace.submit.inputForms.components.InputFormField formField : formFields)
     {
         InputFormField inputFormField = inputFormJspuiMap.get(formField);

       fieldCountIncr = 0;
       if (formField.isRepeatable() && !formField.isReadonly())
       {
         fieldCountIncr = 1;
         if (si.getMoreBoxesFor() != null && si.getMoreBoxesFor().equals(inputFormField.getFieldName(formField.getMetadataField())))
             {
           fieldCountIncr = 2;
         }
       }

       inputFormField.getRenderHintErrorHtmlString(si, formField, pageContext);
       inputFormField.getRenderFieldHtmlString(item, formField, fieldCountIncr, request, pageContext, collectionID);


       if (inputFormField.hasVocabulary(formField) &&  !formField.isReadonly())
       {
%>

                <tr>
                        <td>&nbsp;</td>
                        <td colspan="3" class="submitFormHelpControlledVocabularies">
                                <dspace:popup page="/help/index.html#controlledvocabulary"><fmt:message key="jsp.controlledvocabulary.controlledvocabulary.help-link"/></dspace:popup>
                        </td>
                </tr>

<%
                }
%>
<%-- HACK: Using this line to give the browser hints as to the widths of cells --%>
       <tr>
         <td width="40%">&nbsp;</td>
         <td colspan="2" width="5%">&nbsp;</td>
         <td width="40%">&nbsp;</td>
       </tr>

<%
     } // end of 'for rows'
%>
            </table>
        </center>
        
<%-- HACK:  Need a space - is there a nicer way to do this than <BR> or a --%>
<%--        blank <P>? --%>
        <p>&nbsp;</p>

<%-- Hidden fields needed for SubmissionController servlet to know which item to deal with --%>
        <%= SubmissionController.getSubmissionParameters(context, request) %>
        <center>
            <table border="0" width="80%">
                <tr>
                    <td width="100%">&nbsp;</td>
                <%  //if not first page & step, show "Previous" button
                                        if(!(SubmissionController.isFirstStep(request, si) && pageNum<=1))
                                        { %>
                    <td>
                                                <input type="submit" name="<%=AbstractProcessingStep.PREVIOUS_BUTTON%>" value="<fmt:message key="jsp.submit.edit-metadata.previous"/>" />
                    </td>
                                <%  } %>
                    <td>
                        <input type="submit" name="<%=AbstractProcessingStep.NEXT_BUTTON%>" value="<fmt:message key="jsp.submit.edit-metadata.next"/>"/>
                    </td>
                    <td>&nbsp;&nbsp;&nbsp;</td>
                    <td align="right">
                        <input type="submit" name="<%=AbstractProcessingStep.CANCEL_BUTTON%>" value="<fmt:message key="jsp.submit.edit-metadata.cancelsave"/>"/>
                    </td>
                </tr>
            </table>
        </center>
    </form>

</dspace:layout>
