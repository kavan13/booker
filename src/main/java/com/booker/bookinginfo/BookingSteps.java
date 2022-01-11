package com.booker.bookinginfo;

import com.booker.constants.EndPoint;
import com.booker.model.AuthPojo;
import com.booker.model.BookingPojo;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Step;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BookingSteps {
    static String token;

    @Step("Creating new booking with firstname: {0}, lastname: {1}, totalprice: {2} , depositpaid: {3}, bookingdates: {4}, additionalneeds: {5}")
    public ValidatableResponse createBooking(String firstname, String lastname, int totalprice, boolean depositpaid,
                                             HashMap<String, Object> bookingdates, String additionalneeds) {
        BookingPojo bookingPojo = BookingPojo.getBookingPojo(firstname, lastname, totalprice, depositpaid, bookingdates, additionalneeds);
        return SerenityRest.given().log().all()
                .contentType(ContentType.JSON)
                .header("Accept", "application/json")
                .body(bookingPojo)
                .when()
                .post()
                .then();
    }

    @Step("Getting booking information with firstname: {0}")
    public ValidatableResponse findNewRecordById(int bookingid) {
        return SerenityRest.given().log().all()
                .header("Accept", "application/json")
                .pathParam("bookingId", bookingid)
                .when()
                .get(EndPoint.GET_SINGLE_BOOKING_BY_ID)
                .then();
    }

    @Step("Updating a booking record by Id with firstname: {0}, lastname: {1}, totalprice{2}, depositpaid: {3}, bookingdates: {4}, additionalneeds: {5}, bookingid: {6} ")
    public ValidatableResponse updateBookingRecordById(String firstname, String lastname, int totalprice, boolean depositpaid, HashMap<String, Object> bookingdates,
                                                       String additionalneeds, int bookingId) {
        token = "token=" + AuthPojo.getAuthToken();

        BookingPojo bookingPojo = BookingPojo.getBookingPojo(firstname, lastname, totalprice, depositpaid,
                bookingdates, additionalneeds);

        return SerenityRest.given().log().all()
                .contentType(ContentType.JSON)
                .header("Accept", "application/json")
                .header("cookie", token)
                .pathParam("bookingId", bookingId)
                .body(bookingPojo)
                .when()
                .put(EndPoint.UPDATE_SINGLE_BOOKING_BY_ID)
                .then()
                .statusCode(200)
                .log().all();
    }

    @Step("Getting a single record by updated firstname as the query parameter and extracting its id with firstname: {0}, bookingid: {1}")
    public List<Integer> findSingleBookingRecordByFirstName(String firstname, int bookingid) {

        return SerenityRest.given().log().all()
                .header("Accept", "application/json")
                .queryParams("firstname", firstname)
                .when()
                .get()
                .then()
                .extract()
                .path("bookingid");
    }

    @Step("Deleting the booking with bookingid: {0}")
    public ValidatableResponse deleteBooking(int bookingid) {
        return SerenityRest.given().log().all()
                .contentType(ContentType.JSON)
                .header("Cookie", token)
                .pathParam("bookingId", bookingid)
                .when()
                .delete(EndPoint.DELETE_SINGLE_BOOKING_BY_ID)
                .then();
    }
}
