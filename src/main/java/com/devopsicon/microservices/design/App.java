package com.devopsicon.microservices.design;

import com.structurizr.Workspace;
import com.structurizr.api.StructurizrClient;
import com.structurizr.api.StructurizrClientException;
import com.structurizr.model.*;
import com.structurizr.view.*;

public class App {
    private static final String MICROSERVICE_TAG = "Microservice";
    private static final String MESSAGE_BUS_TAG = "Message Bus";
    private static final String DATASTORE_TAG = "Database";

    public static void main(String[] args) throws StructurizrClientException {

        Workspace workspace = new Workspace("DevOpsIcon", "DevOps Pipeline Learning Platform");
        Model model = workspace.getModel();

        SoftwareSystem mySoftwareSystem = model.addSoftwareSystem("Sales Information System", "Stores information ");
        Person customer = model.addPerson("Manager", "A manager");
        Container customerApp = mySoftwareSystem.addContainer("Sales Information Hybrid App", "Allows sales managers and sales people to view sales information", "Ionic");

        Container salesService = mySoftwareSystem.addContainer("Sales Service", "The point of access for sales information.", "Java and Spring Boot");
        salesService.addTags(MICROSERVICE_TAG);
        Container salesDatabase = mySoftwareSystem.addContainer("Sales Database", "Stores sales information.", "H2 In Mem");
        salesDatabase.addTags(DATASTORE_TAG);

        Container expensesService = mySoftwareSystem.addContainer("Expenses Service", "The point of access for expenses information.", "Java and Spring Boot");
        expensesService.addTags(MICROSERVICE_TAG);
        Container expensesDatabase = mySoftwareSystem.addContainer("Expenses Database", "Stores expenses information.", "H2 In Mem");
        expensesDatabase.addTags(DATASTORE_TAG);

        Container usersService = mySoftwareSystem.addContainer("Users Service", "Users information store", "NodeJS");
        usersService.addTags(MICROSERVICE_TAG);
        Container usersDatabase = mySoftwareSystem.addContainer("Users Database", "Stores user information ", "MongoDB In Mem");
        usersDatabase.addTags(DATASTORE_TAG);

        Container filesService = mySoftwareSystem.addContainer("Files Service", "Provides files storage capabilities.", "C# .NET");
        filesService.addTags(MICROSERVICE_TAG);
        Container filesStore = mySoftwareSystem.addContainer("Files Store", "Stores files as large binary blobl", "MySQL");
        filesStore.addTags(DATASTORE_TAG);

        Container messageBus = mySoftwareSystem.addContainer("Message Bus", "Transport for business events.", "RabbitMQ");
        messageBus.addTags(MESSAGE_BUS_TAG);

        customer.uses(customerApp, "Uses");
        customerApp.uses(salesService, "Access/Updates sales information using", "JSON/HTTPS", InteractionStyle.Synchronous);
        customerApp.uses(usersService,"Access/Update users information using", "JSON/HTTPS", InteractionStyle.Synchronous);
        customerApp.uses(expensesService, "Access/update expenses information using", "JSON/HTTPS", InteractionStyle.Synchronous);

        expensesService.uses(messageBus, "Send expense receipts for storage","AQMP", InteractionStyle.Asynchronous);
        expensesService.uses(expensesDatabase,"Stores data in", "JDBC", InteractionStyle.Synchronous);

        messageBus.uses(filesService,"Obtain expense receipts and store", "AQMP", InteractionStyle.Asynchronous);
        filesService.uses(filesStore,"Store files and references", "JDBC",InteractionStyle.Synchronous);

        salesService.uses(salesDatabase,"Stores data in","JDBC", InteractionStyle.Synchronous);
        usersService.uses(usersDatabase, "Stores data in", "JDBC", InteractionStyle.Synchronous);
        expensesService.uses(expensesDatabase,"", "");

        ViewSet views = workspace.getViews();

        ContainerView containerView = views.createContainerView(mySoftwareSystem, "Containers", null);
        containerView.addAllElements();

        Styles styles = views.getConfiguration().getStyles();
        styles.addElementStyle(Tags.ELEMENT).color("#000000");
        styles.addElementStyle(Tags.PERSON).background("#ffbf00").shape(Shape.Person);
        styles.addElementStyle(Tags.CONTAINER).background("#facc2E");
        styles.addElementStyle(MESSAGE_BUS_TAG).width(1600).shape(Shape.Pipe);
        styles.addElementStyle(MICROSERVICE_TAG).shape(Shape.Hexagon);
        styles.addElementStyle(DATASTORE_TAG).background("#f5da81").shape(Shape.Cylinder);
        styles.addRelationshipStyle(Tags.RELATIONSHIP).routing(Routing.Orthogonal);

        styles.addRelationshipStyle(Tags.ASYNCHRONOUS).dashed(true);
        styles.addRelationshipStyle(Tags.SYNCHRONOUS).dashed(false);

        StructurizrClient structurizrClient = new StructurizrClient("", "");
        structurizrClient.putWorkspace(38506, workspace);

    }
}
