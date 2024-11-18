<html>
    <head>
        <title>Get Parameter Example</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width,
                                       initial-scale=1.0">
    </head>
    <body>
        <!-- Here we specify that the from data will be sent to
             getparam.jsp page using the action attribute
        -->
        <form name="testForm" action="DrugRepresentation">
           <label><h1>Enter a text and click Submit</h1></label><br/>
           <input type="text" name="testParam"><br/>
           <input type="submit">
        </form>

        <form id="main" method="post" name="main" action="/indexPharma/index.jsp" >
            <button type="submit" class="btn btn-primary">Back</button>
        </form>

        <%@ page import = "com.example.app.representation.DrugRepresentation" %>
        <%@ page import = "com.example.app.resource.DrugResource" %>
        <%@ page import = "com.example.app.resource.DrugTest" %>
        <%@ page import =  "javax.ws.rs.core.Response" %>
        <%@ page import = "java.util.List" %>


        <%
        DrugRepresentation drug = new DrugRepresentation();
        DrugResource drugR = new DrugResource();
        DrugTest drugT = new DrugTe();est

        drug = drugT.find(); // how do you get username on button click and assign it to this variable?

        if (drug.id==6001)
        {
        %>
        alert("Username already exists");

        document.getElementById("id").value="";


        <%
        }
        %>

    </body>
</html>