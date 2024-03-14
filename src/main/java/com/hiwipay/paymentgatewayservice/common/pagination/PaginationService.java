package com.hiwipay.paymentgatewayservice.common.pagination;

import com.hiwipay.paymentgatewayservice.common.annotation.LogEntryExit;
import com.hiwipay.paymentgatewayservice.common.dto.response.ListResponseBody;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaginationService {

    public static Date addNDay(Date date, Integer n) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, n);
        return (c.getTime());
    }

    public static Date getStartDate(Date date) {
        if (date == null) {
            date = new Date(0);
        }
        return date;
    }

    public static Date getEndDate(Date date) {
        if (date==null) {
            date = PaginationService.addNDay(dayAtStart(LocalDate.now()),1);
        }
        return PaginationService.addNDay(date, 1);
    }

    public static ListResponseBody getPagination(boolean isPagination, Integer pageNumber, Integer numOfRecords, List<?> list) {
        int pageNumber1 = 0;
        int numOfRecords1 = 15;
        int totalRecords = list.size();

        if (isPagination) {
            pageNumber1 = pageNumber != null ? pageNumber : pageNumber1;
            numOfRecords1 = numOfRecords != null ? numOfRecords : numOfRecords1;
        }else{
            numOfRecords1=totalRecords;
        }
        int noOfPages ;
        if(numOfRecords1==0) {
            noOfPages=1;
        }else{
            noOfPages = list.size() / numOfRecords1;
             if (list.size() % numOfRecords1 > 0) {
                noOfPages++;
            }
        }
       

        if (list.size() - pageNumber1 * numOfRecords1 <= 0) {
            list = new ArrayList<>();
        }

        PagedListHolder page = new PagedListHolder(list);
        page.setPageSize(numOfRecords1);
        page.setPage(pageNumber1);
        list = page.getPageList();

         return ListResponseBody.builder()
                 .noOfRecord("" + list.size())
                 .noOfPages("" + noOfPages)
                 .pageSize("" + numOfRecords1)
                 .pageNo("" + pageNumber1)
                 .totalRecords("" + totalRecords)
                 .result(list)
                 .build();
    }

    public static ListResponseBody pageToListResponseBody(Page<?> page, List<?> list, boolean isPagination, Integer pageNumber, Integer numOfRecords) {
        return ListResponseBody.builder()
                .result(list)
                .noOfPages(page.getTotalPages()+"")
                .noOfRecord(page.getContent().size()+"")
                .pageNo(isPagination ? pageNumber+"" : "0")
                .pageSize(isPagination ? numOfRecords+"" : page.getContent().size()+"")
                .totalRecords(page.getTotalElements()+"")
                .build();
    }


    @LogEntryExit
    public static Date dayAtStart(LocalDate localDate){

        return Date.from(localDate.atStartOfDay().
                minus(330, ChronoUnit.MINUTES)
                .toInstant(ZoneOffset.UTC));
    }

    @LogEntryExit
    public static double roundOffTwoDecimal(Double no){
        if(no == null){
            return 0.00;
        }
        DecimalFormat decfor = new DecimalFormat("0.00");
        decfor.setRoundingMode(RoundingMode.HALF_UP);
        return Double.parseDouble(decfor.format(no));
    }
    
    public static double roundOffDecimal(Double no, int decimalPlaces) {
        if (no == null) {
            return 0.00;
        }

        StringBuilder patternBuilder = new StringBuilder("0.");
        for (int i = 0; i < decimalPlaces; i++) {
            patternBuilder.append("0");
        }
        DecimalFormat decfor = new DecimalFormat(patternBuilder.toString());
        decfor.setRoundingMode(RoundingMode.HALF_UP);

        return Double.parseDouble(decfor.format(no));
    }

    @LogEntryExit
    public static String roundOffTwoDecimalString(Double no){
        if(no == null){
            return "0.00";
        }
        DecimalFormat decfor = new DecimalFormat("0.00");
        decfor.setRoundingMode(RoundingMode.HALF_UP);
        return decfor.format(no);
    }

    @LogEntryExit
    public static String toNameCase(String string){
        if(StringUtils.isBlank(string)){
            return "User";
        }
        string = string.trim();
        StringBuilder result = new StringBuilder("");
        string = string.replace("_", "\\s");
        String[] array = string.split(" \\s+");
        for(String i: array){
            result
                .append(i.substring(0, 1).toUpperCase())
                .append(i.substring(1).toLowerCase())
                .append(" ");
        }
        return result.toString().trim();
    }
}
