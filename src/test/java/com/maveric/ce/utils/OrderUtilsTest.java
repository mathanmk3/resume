package com.maveric.ce.utils;

import com.maveric.ce.dto.OrderDto;
import com.maveric.ce.exceptions.ErrorCodes;
import com.maveric.ce.exceptions.ServiceException;
import com.maveric.ce.repository.CurrencyExchangeOrdersRepo;
import com.maveric.ce.repository.IAccountRepository;
import org.json.JSONException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.json.JSONObject;


import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;



@ContextConfiguration(classes = {OrderUtils.class})
@ExtendWith(SpringExtension.class)
@TestPropertySource(properties = {
        "currencyApi=https://example.com/api/",
        "currencyJsonKey=rates",
        "currencyApiempty=",

})
class OrderUtilsTest {

    @Value("${currencyApi}")
    private String currencyApi;

    @Value("${currencyJsonKey}")
    private String rateKey;
    @Value("${currencyApiempty}")
    private String currencyApiempty;
    @MockBean
    private CurrencyExchangeOrdersRepo currencyExchangeOrdersRepo;

    @MockBean
    private IAccountRepository iAccountRepository;

    @Autowired
    private OrderUtils orderUtils;

    @MockBean
    private RestTemplate restTemplate;

    static MockedStatic<CommonUtils> utilities;

    @BeforeAll
    static void setup() {
        utilities = Mockito.mockStatic(CommonUtils.class);
    }

    @AfterAll
    static void shutdown(){
        utilities.clearInvocations();
        utilities.close();
    }

    @Test
    @Disabled
    void testCurrencyRateFromApi() throws ServiceException, NullPointerException, RestClientException, JSONException {

        OrderDto orderDto = new OrderDto();
        orderDto.setCurrencyRate(BigDecimal.valueOf(7.5));
        orderDto.setOrderToCurrencyType("INR");
        ResponseEntity<String> responseEntity = mock(ResponseEntity.class);
        String currencyApi = "https://open.er-api.com/v6/latest/";
        String apiJson = "{\"result\":\"success\",\"provider\":\"https://www.exchangerate-api.com\",\"documentation\":\"https://www.exchangerate-api.com/docs/free\",\"terms_of_use\":\"https://www.exchangerate-api.com/terms\",\"time_last_update_unix\":1694563352,\"time_last_update_utc\":\"Wed, 13 Sep 2023 00:02:32 +0000\",\"time_next_update_unix\":1694650302,\"time_next_update_utc\":\"Thu, 14 Sep 2023 00:11:42 +0000\",\"time_eol_unix\":0,\"base_code\":\"INR\",\"rates\":{\"INR\":1,\"AED\":0.044294,\"AFN\":0.961205,\"ALL\":1.200459,\"AMD\":4.649761,\"ANG\":0.021589,\"AOA\":10.043243,\"ARS\":4.221121,\"AUD\":0.018773,\"AWG\":0.021589,\"AZN\":0.020492,\"BAM\":0.022008,\"BBD\":0.024122,\"BDT\":1.324046,\"BGN\":0.022009,\"BHD\":0.004535,\"BIF\":34.091743,\"BMD\":0.012061,\"BND\":0.016427,\"BOB\":0.083396,\"BRL\":0.059572,\"BSD\":0.012061,\"BTN\":1,\"BWP\":0.164675,\"BYN\":0.033599,\"BZD\":0.024122,\"CAD\":0.016366,\"CDF\":29.967742,\"CHF\":0.010758,\"CLP\":10.755717,\"CNY\":0.087993,\"COP\":48.222905,\"CRC\":6.485255,\"CUP\":0.289465,\"CVE\":1.24075,\"CZK\":0.276378,\"DJF\":2.143499,\"DKK\":0.083846,\"DOP\":0.684632,\"DZD\":1.653995,\"EGP\":0.372783,\"ERN\":0.180916,\"ETB\":0.666069,\"EUR\":0.011252,\"FJD\":0.027373,\"FKP\":0.009669,\"FOK\":0.083947,\"GBP\":0.009669,\"GEL\":0.031558,\"GGP\":0.009669,\"GHS\":0.138562,\"GIP\":0.009669,\"GMD\":0.775945,\"GNF\":103.222222,\"GTQ\":0.094922,\"GYD\":2.521031,\"HKD\":0.094444,\"HNL\":0.297559,\"HRK\":0.084781,\"HTG\":1.62697,\"HUF\":4.332174,\"IDR\":185.203766,\"ILS\":0.045931,\"IMP\":0.009669,\"IQD\":15.812766,\"IRR\":512.839798,\"ISK\":1.617855,\"JEP\":0.009669,\"JMD\":1.86141,\"JOD\":0.008551,\"JPY\":1.77409,\"KES\":1.765044,\"KGS\":1.061381,\"KHR\":49.546667,\"KID\":0.018775,\"KMF\":5.535837,\"KRW\":16.001853,\"KWD\":0.003723,\"KYD\":0.010051,\"KZT\":5.591761,\"LAK\":235.164671,\"LBP\":180.915512,\"LKR\":3.891208,\"LRD\":2.285181,\"LSL\":0.228555,\"LYD\":0.058435,\"MAD\":0.12237,\"MDL\":0.216588,\"MGA\":54.647059,\"MKD\":0.692422,\"MMK\":28.422872,\"MNT\":42.073263,\"MOP\":0.097278,\"MRU\":0.462995,\"MUR\":0.5456,\"MVR\":0.186148,\"MWK\":13.433166,\"MXN\":0.208427,\"MYR\":0.056412,\"MZN\":0.77016,\"NAD\":0.228555,\"NGN\":9.782875,\"NIO\":0.44116,\"NOK\":0.128862,\"NPR\":1.6,\"NZD\":0.020433,\"OMR\":0.004637,\"PAB\":0.012061,\"PEN\":0.044659,\"PGK\":0.043547,\"PHP\":0.683235,\"PKR\":3.616544,\"PLN\":0.052454,\"PYG\":88.051368,\"QAR\":0.043902,\"RON\":0.055946,\"RSD\":1.318782,\"RUB\":1.148014,\"RWF\":14.863993,\"SAR\":0.045229,\"SBD\":0.102705,\"SCR\":0.156347,\"SDG\":5.385507,\"SEK\":0.133876,\"SGD\":0.016427,\"SHP\":0.009669,\"SLE\":0.264114,\"SLL\":264.114062,\"SOS\":6.881481,\"SRD\":0.467421,\"SSP\":12.187025,\"STN\":0.275685,\"SYP\":155.877193,\"SZL\":0.228555,\"THB\":0.429808,\"TJS\":0.131955,\"TMT\":0.042208,\"TND\":0.037789,\"TOP\":0.02844,\"TRY\":0.324526,\"TTD\":0.081818,\"TVD\":0.018775,\"TWD\":0.385863,\"TZS\":30.220763,\"UAH\":0.444637,\"UGX\":44.859278,\"USD\":0.012061,\"UYU\":0.459219,\"UZS\":146.707615,\"VES\":0.40208,\"VND\":291.308281,\"VUV\":1.47124,\"WST\":0.03301,\"XAF\":7.381115,\"XCD\":0.032565,\"XDR\":0.009125,\"XOF\":7.381115,\"XPF\":1.342776,\"YER\":3.019032,\"ZAR\":0.228555,\"ZMW\":0.253636,\"ZWL\":56.620838}}";
        utilities.when(() -> CommonUtils.checkNullableAndEmpty(apiJson))
                .thenReturn(Optional.of(Boolean.FALSE));
        when(restTemplate.getForEntity(Mockito.<String>any(), Mockito.<Class<String>>any()))
                .thenReturn(responseEntity);
        when(responseEntity.getBody()).thenReturn(apiJson);
        JSONObject rateJson = mock(JSONObject.class);
        JSONObject ss = new JSONObject();
        JSONObject rateJsonObject = mock(JSONObject.class);
        when(rateJson.getJSONObject("rates")).thenReturn(rateJsonObject);
        when(rateJsonObject.has(orderDto.getOrderToCurrencyType())).thenReturn(true);
        //when(rateJson.getJSONObject(rateKey)
         //               .getBigDecimal(orderDto.getOrderToCurrencyType())).thenReturn(BigDecimal.valueOf(11L));
        OrderDto resp =orderUtils.currencyRateFromApi(orderDto);
        assertSame(orderDto.getCurrencyRate(), resp.getCurrencyRate());
        verify(restTemplate).getForEntity(Mockito.<String>any(), Mockito.<Class<String>>any(), isA(Object[].class));
        verify(responseEntity).getBody();
    }

    @Test
    public void testCurrencyRateFromApiWithInvalidCurrencyApi() {
        OrderDto orderDto = new OrderDto();
        orderDto.setOrderFromCurrencyType("USD");
        try {
            OrderDto result = orderUtils.currencyRateFromApi(orderDto);
        } catch (ServiceException e) {
            assertNotNull(e.getMessage());
        }
    }

    @Test
    void testCheckSufficientBalance() {
        when(iAccountRepository.checkSufficientAmmount(Mockito.anyLong(), Mockito.anyLong(), Mockito.any()))
                .thenReturn(Optional.of(Long.valueOf("11")));
        boolean expected = orderUtils.checkSufficientBalance(Mockito.any(BigDecimal.class), Mockito.anyLong(), Mockito.anyLong());
        assertThat(expected).isTrue();
    }

    @Test
    void testCheckInSufficientBalance() {
        when(iAccountRepository.checkSufficientAmmount(Mockito.anyLong(), Mockito.anyLong(), Mockito.any()))
                .thenReturn(Optional.empty());
        ServiceException ex = new ServiceException();
        ex.setHttpStatus(HttpStatus.BAD_REQUEST);
        ex.setMessage(ErrorCodes.INSUFFICIENT_BALANCE);
        try {
            boolean expected = orderUtils.checkSufficientBalance(Mockito.any(BigDecimal.class), Mockito.anyLong(), Mockito.anyLong());
        }catch (ServiceException e){
            assertEquals(ex.getMessage() ,e.getMessage());
            assertEquals(ex.getHttpStatus(),e.getHttpStatus());
        }
    }
    @Test
    void testCheckSufficientBalanceDBissue() {
        when(iAccountRepository.checkSufficientAmmount(Mockito.anyLong(), Mockito.anyLong(), Mockito.any()))
                .thenThrow(new DataAccessException(ErrorCodes.CONNECTION_ISSUE) {
                });
        try {
            orderUtils.checkSufficientBalance(Mockito.any(BigDecimal.class), Mockito.anyLong(), Mockito.anyLong());
        }catch (DataAccessException e) {
            assertEquals(ErrorCodes.CONNECTION_ISSUE, e.getMessage());
        }
    }

    @Test
    void updateCustomerAccountBalance() {
        OrderDto orderdetails = mock(OrderDto.class);
        when(iAccountRepository.getAccountBalance(Mockito.anyLong(), Mockito.anyLong())).thenReturn(Optional.of(BigDecimal.valueOf(1000)));
        when(iAccountRepository.getAccountBalance(Mockito.anyLong(), Mockito.anyLong())).thenReturn(Optional.of(BigDecimal.valueOf(1100)));
        when(iAccountRepository.updateCreditDetails(Mockito.anyLong(),Mockito.anyLong(),Mockito.any(BigDecimal.class), anyString())).thenReturn(Optional.of(Integer.valueOf(11)));
        when(iAccountRepository.updateDebitDetails(Mockito.anyLong(),Mockito.anyLong(),Mockito.any(BigDecimal.class), anyString())).thenReturn(Optional.of(Integer.valueOf(11)));
        when(orderdetails.getBuyingValue()).thenReturn(BigDecimal.valueOf(110));
        when(orderdetails.getSellingValue()).thenReturn(BigDecimal.valueOf(110));
        boolean expected = orderUtils.updateCustomerAccountBalance(orderdetails);
        assertThat(expected).isTrue();
    }

    @Test
    void updateCustomerAccountBalanceDBissue() {
        OrderDto orderdetails = mock(OrderDto.class);
        when(iAccountRepository.getAccountBalance(Mockito.anyLong(), Mockito.anyLong())).thenThrow(new DataAccessException(ErrorCodes.CONNECTION_ISSUE) {
        });
        try {
            orderUtils.updateCustomerAccountBalance(orderdetails);
        }catch (DataAccessException e) {
            assertEquals(ErrorCodes.CONNECTION_ISSUE, e.getMessage());
        }
    }

    @Test
    void testUpdateOrderExchangeTime() {
        Long orderId = 1L;
        when(currencyExchangeOrdersRepo.updateCurrencyExchangeDateTime(anyString(), Mockito.anyLong())).thenReturn(1);
        orderUtils.updateOrderExchangeTime(orderId);
        verify(currencyExchangeOrdersRepo).updateCurrencyExchangeDateTime(anyString(),  Mockito.anyLong());
    }

    @Test
    void testUpdateOrderExchangeTimeDBIssue() {
        Long orderId = 1L;
        when(currencyExchangeOrdersRepo.updateCurrencyExchangeDateTime(anyString(), Mockito.anyLong())).thenThrow(new DataAccessException(ErrorCodes.CONNECTION_ISSUE) {
        });
        try {
            orderUtils.updateOrderExchangeTime(orderId);
        }catch (DataAccessException e) {
            assertEquals(ErrorCodes.CONNECTION_ISSUE, e.getMessage());
        }
    }
}

