package Group1;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.sql.*;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class TestCase1 {

    WebDriver driver;
    private WebDriverWait wait;
    private Statement statement;
    private Connection connection;

    @BeforeClass
    public void connect() throws SQLException {
        String url = "jdbc:mysql://database-techno.c771qxmldhez.us-east-2.rds.amazonaws.com:3306/daulet2030_studens_database";
        String user = "daulet2030";
        String password = "daulet2030@gmail.com";
        connection = DriverManager.getConnection( url, user, password );
        statement = connection.createStatement();
    }

    @AfterClass
    public void disconnect() throws SQLException {
        connection.close();
    }
    @BeforeClass
    @Parameters({"username", "password", "path"})
    public void setup(String username, String password,String path) {
        System.setProperty("webdriver.chrome.driver", path);
        driver = new ChromeDriver();
        driver.get("https://test-basqar.mersys.io");
        driver.manage().window().maximize();
        // login info
        driver.findElement(By.cssSelector("[formcontrolname=\"username\"]")).sendKeys(username);
        driver.findElement(By.cssSelector("[formcontrolname=\"password\"]")).sendKeys(password);
        driver.findElement(By.cssSelector("button[aria-label=\"LOGIN\"]")).click();
        wait = new WebDriverWait(driver, 7);
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver.findElement(By.xpath("//a[@class='cc-btn cc-dismiss']")).click();

        // Navigate to Approvement
        driver.findElement(By.cssSelector("fuse-navigation .group-items > .nav-item:nth-child(2)")).click();
        driver.findElement(By.cssSelector("fuse-navigation .group-items > .nav-item:nth-child(2) > .children > .nav-item:nth-child(2)")).click();

    }

    @Test(dataProvider = "students")
    public void createPreRegistration(String id) throws SQLException, InterruptedException {
        //TODO get name, lastname and gender from students table by id from dataprovider;
        PreparedStatement resultStatement = connection.prepareStatement( "select firstname, lastname, gender from preregistrations " +
                "where id = ?" );
        resultStatement.setString( 1, id );
        ResultSet rs = resultStatement.executeQuery();
        rs.first();
        String name = rs.getString( "firstname" );
        String lastName = rs.getString( "lastname" );
        String gender = rs.getString( "gender" );

        // Plus icon
        wait.until( ExpectedConditions.visibilityOfElementLocated( By.cssSelector("app-registration-approvement ms-add-button") ) );
        driver.findElement(By.cssSelector("app-registration-approvement ms-add-button")).click();

        boolean visible =false;
        WebDriverWait waitDropdownToBeVisible = new WebDriverWait( driver, 1 );

        // grade level of registration
        while(!visible){
            driver.findElement(By.cssSelector("registration-form-exam-info mat-select[aria-label='Grade Level of Registration']")).click();
            try {
                waitDropdownToBeVisible.until( ExpectedConditions.visibilityOfElementLocated( By.cssSelector(".cdk-overlay-pane mat-option:first-child") ) );
                visible = true;
            } catch(Exception e) {
                // cannot find it within one second, try again!
            }
        }
        // select grade level
        driver.findElement(By.cssSelector(".cdk-overlay-pane mat-option:first-child")).click();
        wait.until( ExpectedConditions.invisibilityOfElementLocated( By.cssSelector(".cdk-overlay-pane") ) );

       // choose exam
        visible = false;
        while(!visible){
            driver.findElement(By.cssSelector("registration-form-exam-info mat-select[aria-label='Choose Exam']")).click();
            try {
                waitDropdownToBeVisible.until( ExpectedConditions.visibilityOfElementLocated( By.cssSelector(".cdk-overlay-pane mat-option:first-child") ) );
                visible = true;
            } catch(Exception e) {
                // cannot find it within one second, try again!
            }
        }

        // select first exam
        driver.findElement(By.cssSelector(".cdk-overlay-pane mat-option:first-child")).click();

        // Clicking on information page
        driver.findElement(By.cssSelector("app-registration-edit .mat-tab-label:nth-child(2)")).click();
        // firstname
        driver.findElement(By.cssSelector("input[formcontrolname='firstName']")).sendKeys(name);
        // lastname
        driver.findElement(By.cssSelector("input[formcontrolname='lastName']")).sendKeys(lastName);
        // gender click
        driver.findElement(By.cssSelector("mat-select[formcontrolname='gender']")).click();
        if(gender.equals( "Male" )) {
            driver.findElement( By.cssSelector( ".cdk-overlay-pane mat-option:first-child" ) ).click();
        } else if(gender.equals( "Female" )) {
            driver.findElement( By.cssSelector( ".cdk-overlay-pane mat-option:last-child" ) ).click();
        }

        String birth = "09061988";
        driver.findElement(By.cssSelector("input[placeholder='Date of Birth']")).sendKeys(birth);
        driver.findElement(By.cssSelector("input[placeholder='Personal ID']")).sendKeys("12346");
        // click on citizenship
        driver.findElement(By.cssSelector("mat-select[aria-label=\"Citizenship\"]")).click();
        driver.findElement(By.cssSelector(".cdk-overlay-pane mat-option:first-child")).click();
        // click on nationality
        driver.findElement(By.cssSelector("mat-select[aria-label='Nationality']")).click();
        Thread.sleep(200);
//        wait.until( ExpectedConditions.elementToBeClickable( By.cssSelector(".cdk-overlay-pane mat-option:first-child") ) );
        driver.findElement(By.cssSelector(".cdk-overlay-pane mat-option:first-child")).click();

        String email = "tttttt@gmail.com";
        driver.findElement(By.cssSelector("input[placeholder='Email']")).sendKeys(email);

        // clicking relative info
        driver.findElement(By.cssSelector("app-registration-edit .mat-tab-label:nth-child(3)")).click();
        // click on reprresentative
        visible = false;
        while(!visible){
            driver.findElement(By.cssSelector("mat-select[aria-label=\"Representative\"]")).click();
            try {
                waitDropdownToBeVisible.until( ExpectedConditions.visibilityOfElementLocated( By.cssSelector(".cdk-overlay-pane mat-option:first-child") ) );
                visible = true;
            } catch(Exception e) {
                // cannot find it within one second, try again!
            }
        }
        driver.findElement(By.cssSelector(".cdk-overlay-pane mat-option:first-child")).click();

        // filling out the lastname and firstname
        driver.findElement(By.cssSelector("registration-form-relative-info input[placeholder='Last Name']")).sendKeys("Adams");
        driver.findElement(By.cssSelector("registration-form-relative-info input[placeholder='First Name']")).sendKeys("Mary");
        // mobile phone
        driver.findElement(By.cssSelector("input[placeholder='Mobile Phone']")).sendKeys("12345567");
        // click on Country
        driver.findElement(By.cssSelector("mat-select[aria-label='Country']")).click();
        driver.findElement(By.cssSelector(".cdk-overlay-pane mat-option:first-child")).click();
		// click on Save button
        driver.findElement(By.xpath("//ms-save-button[@caption='GENERAL.BUTTTON.SAVE']")).click();

        try {
            wait.until( ExpectedConditions.visibilityOfElementLocated( By.cssSelector("div[aria-label=\"Pre-registration successfully created\"]") ) );
        } catch (Exception e){
            Assert.fail( "Creation failure" );
        }
        driver.findElement(By.cssSelector("mat-toolbar [data-icon='arrow-left']")).click();
    }

    @Test(dependsOnMethods = "testingGender", dataProvider = "randomstudents")
    public void verifying(String id) throws SQLException {
        //TODO get name and lastname from students table by id from dataprovider;
        PreparedStatement resultStatement = connection.prepareStatement( "select firstname, lastname, gender from preregistrations " +
                "where id = ?" );
        resultStatement.setString( 1, id );
        ResultSet rs = resultStatement.executeQuery();
        rs.first();
        String name = rs.getString( "firstname" );
        String lastName = rs.getString( "lastname" );
        String gender = rs.getString( "gender" );
        wait.until( ExpectedConditions.numberOfElementsToBeMoreThan( By.cssSelector( "tbody tr" ), 0 ) );
        String expectedname = name + " " + lastName;

        List<WebElement> names = driver.findElements(By.xpath("//tbody//tr//td[3]"));
        Assert.assertNotEquals( names, null );

        boolean found = false;
        for (int i = 0; i < names.size(); i++) {
            if (names.get(i).getText().equals(expectedname)) {
                found = true;
                break;
            }
        }

        Assert.assertTrue( found );
    }

    @Test(dependsOnMethods = "verifying", dataProvider = "randomstudents")
    public void deleting(String id) throws SQLException {
        //TODO get name and lastname from students table by id from dataprovider;
        PreparedStatement resultStatement = connection.prepareStatement( "select firstname, lastname, gender from preregistrations " +
                "where id = ?" );
        resultStatement.setString( 1, id );
        ResultSet rs = resultStatement.executeQuery();
        rs.first();
        String name = rs.getString( "firstname" );
        String lastName = rs.getString( "lastname" );
        String gender = rs.getString( "gender" );
        wait.until( ExpectedConditions.numberOfElementsToBeMoreThan( By.cssSelector( "tbody tr" ), 0 ) );
        String expectedname = name + "  " + lastName;

        WebElement deleteButton = driver.findElement(By.xpath("//td[contains(text(), '"+expectedname+"')]/..//ms-delete-button"));
        Assert.assertNotEquals( deleteButton, null );
        deleteButton.click();
        driver.findElement(By.xpath(" //span[contains(text(),'Yes')]")).click();

        try {
            wait.until( ExpectedConditions.visibilityOfElementLocated( By.cssSelector("div[aria-label='"+expectedname+" successfully deleted']") ) );
        } catch (Exception e){
            Assert.fail( "Delete Failure!" );
        }
    }

    @Test(dependsOnMethods = "createPreRegistration", dataProvider = "randomstudents")
    public void testingGender(String id) throws SQLException {
        //TODO get gender from students table by id from dataprovider;
        PreparedStatement resultStatement = connection.prepareStatement( "select firstname, lastname, gender from preregistrations " +
                "where id = ?" );
        resultStatement.setString( 1, id );
        ResultSet rs = resultStatement.executeQuery();
        rs.first();
        String name = rs.getString( "firstname" );
        String lastName = rs.getString( "lastname" );
        String gender = rs.getString( "gender" );
        wait.until( ExpectedConditions.numberOfElementsToBeMoreThan( By.cssSelector( "tbody tr" ), 0 ) );
        List<WebElement> genders = driver.findElements(By.xpath("//tbody//tr//td[8]"));
        Assert.assertNotEquals( genders, null );

        boolean found = false;
        for (int i = 0; i < genders.size(); i++) {
            if (genders.get( i ).getText().trim().equals(gender)) {
                found = true;
                break;
            }
        }
        Assert.assertTrue( found );
    }
    @AfterClass
    public void quit(){
        driver.quit();
    }

    //TODO create a dataprovider that provides 3 ids from students table

    @DataProvider(name = "students")
    public Object[][] studentsData(){
        Object[][] result = new Object[3][1];
        for (int i = 0; i < 3; i++) {
            result[i][0] = String.valueOf(i+1);
        }
        return result;
    }
}
