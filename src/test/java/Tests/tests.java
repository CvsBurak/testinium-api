package Tests;

import BasePackage.BaseClass;
import org.testng.annotations.Test;

public class tests extends BaseClass {
	
	//Case variables
    String searchName = "Harry Potter";
    String exactTitleName = "Harry Potter and the Sorcerer\\'s Stone";

    //By Search - Get imdbID of the "Harry Potter and the Sorcerer's Stone" film
    @Test
    public void TestCase1() {
        getImdbIDByFilmName(searchName, exactTitleName);
    }

    //By ID or Title - Search film by imdbID which you get from the previous case
    @Test(dependsOnMethods = "TestCase1")
    public void TestCase2() {
        searchFilmByImdbID();
    }

    //By ID or Title - Verify status code of the Search film by IMDb ID case
    @Test(dependsOnMethods = "TestCase2")
    public void TestCase3() {
        verifyStatusCodeOfSearchByImdbID();
    }

    //By ID or Title - Verify existence of the "Title", "Year" and "Released" Areas
    @Test(dependsOnMethods = "TestCase3")
    public void TestCase4() {
        verifyExistenceOfTheAreas("Title");
        verifyExistenceOfTheAreas("Year");
        verifyExistenceOfTheAreas("Released");
    }
    
    //Post a movie
    @Test
    public void TestCase5() {
    	createMovie();
    }

}
