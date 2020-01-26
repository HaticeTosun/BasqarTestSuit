package Group1;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class TestCase1 {

    WebDriver driver;
    private WebDriverWait wait;

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

    @Parameters({"firstName", "lastName", "gender"})
    @Test
    public void createPreRegistration(String name, String lastName, String gender)  {
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
        wait.until( ExpectedConditions.elementToBeClickable( By.cssSelector(".cdk-overlay-pane mat-option:first-child") ) );
        driver.findElement(By.cssSelector(".cdk-overlay-pane mat-option:first-child")).click();

        String email = "tttttt@gmail.com";
        driver.findElement(By.cssSelector("input[placeholder='Email']")).sendKeys(email);

        // clicking relative info
        driver.findElement(By.cssSelector("app-registration-edit .mat-tab-label:nth-child(3)")).click();
        // click on reprresentative
        driver.findElement(By.cssSelector("mat-select[aria-label=\"Representative\"]")).click();
        wait.until( ExpectedConditions.elementToBeClickable( By.cssSelector(".cdk-overlay-pane mat-option:first-child") ) );
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

    @Parameters({"firstName", "lastName"})
    @Test(dependsOnMethods = "testingGender")
    public void verifying(String name, String lastName) {
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

    @Parameters({"firstName", "lastName"})
    @Test(dependsOnMethods = "verifying")
    public void deleting(String name, String lastName) {
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

    @Parameters({"gender"})
    @Test(dependsOnMethods = "createPreRegistration")
    public void testingGender(String myGender) {
        wait.until( ExpectedConditions.numberOfElementsToBeMoreThan( By.cssSelector( "tbody tr" ), 0 ) );
        List<WebElement> genders = driver.findElements(By.xpath("//tbody//tr//td[8]"));
        Assert.assertNotEquals( genders, null );

        boolean found = false;
        for (int i = 0; i < genders.size(); i++) {
            if (genders.get( i ).getText().trim().equals(myGender)) {
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
}
