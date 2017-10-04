/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tkbautobooking;

import java.util.List;
import java.util.Map;
import org.apache.http.client.config.RequestConfig;

/**
 *
 * @author Jasper
 */
public class BookingParam {

    public static final String WEB_HOST = "http://bookseat.tkblearning.com.tw";
    public static final String WEB_LOGIN_URI = WEB_HOST + "/book-seat/student";
    public static final String WEB_LOGIN_POST_URI = WEB_HOST + "/book-seat/student/login/login";
    public static final String WEB_BOOKING_URI = WEB_HOST + "/book-seat/student/bookSeat/index";
    public static final String WEB_BOOKING_CLASSROOM_URI = WEB_HOST + "/book-seat/student/bookSeat/branch";
    public static final String WEB_BOOKING_DATE_URI = WEB_HOST + "/book-seat/student/bookSeat/canBookseatDate";
    public static final String WEB_BOOKING_TIME_URI = WEB_HOST + "/book-seat/student/bookSeat/sessionTime";
    public static final String WEB_POST_BOOKING_URI = WEB_HOST + "/book-seat/student/bookSeat/book";
    
    public static final RequestConfig REQUEST_PARAMS = RequestConfig.custom()
                                                        .setConnectTimeout(10000)
                                                        .setSocketTimeout(10000).build();
    
}
