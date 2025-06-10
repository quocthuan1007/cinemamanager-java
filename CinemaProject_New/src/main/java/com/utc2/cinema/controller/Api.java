package com.utc2.cinema.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
public class Api {
    @RequestMapping(value ="/vnpay_return", method = RequestMethod.GET)
    @ResponseBody
    public String getBuilding(@RequestParam Map<String, String> apiContent) {
        System.out.println("All parameters: " + apiContent);

        String vnp_Amount = apiContent.get("vnp_Amount");
        String vnp_BankCode = apiContent.get("vnp_BankCode");
        String vnp_ResponseCode = apiContent.get("vnp_ResponseCode");
        String vnp_TransactionStatus = apiContent.get("vnp_TransactionStatus");
        String vnp_SecureHash = apiContent.get("vnp_SecureHash");

        System.out.println("Amount: " + vnp_Amount);
        System.out.println("Response Code: " + vnp_ResponseCode);
        return "Hello";
    }
}
