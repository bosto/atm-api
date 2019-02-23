package com.bosto.atmapi.atm.controller;

import com.bosto.atmapi.atm.dao.ATMRepository;
import com.bosto.atmapi.atm.domain.ATM;
import com.bosto.atmapi.atm.domain.WithDraw;
import com.bosto.atmapi.atm.service.ATMService;
import com.bosto.atmapi.atm.view.RetrieveWithDrawRequest;
import com.bosto.atmapi.atm.view.UpdateWithDrawRequest;
import com.bosto.atmapi.atm.view.WithDrawRequest;
import com.bosto.atmapi.common.BaseController;
import com.bosto.atmapi.user.domain.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.bosto.atmapi.common.Maps.*;

@RestController
@RequestMapping("/atm")
public class ATMController extends BaseController {
    @Autowired
    ATMService atmService;

    @PostMapping("/create")
    public Map<String, Object> create(@RequestBody ATM atm) {
        return atmService.createATM(atm);
    }

    @GetMapping("/retrieve")
    public Map<String, Object> query() {
      return atmService.queryATM();
    }

    @GetMapping("/signature")
    public Map<String, Object> signATM(@RequestParam("atmExternalId") String atmExternalId) {
        return map("qrCodeString", atmService.generatorQRcodeString(atmExternalId));
    }

    @PostMapping("/auth")
    public Map<String, Object> authPass(@RequestBody UpdateWithDrawRequest request) {
        return atmService.requesterVerifyOTP(request.getAtmExternalId(), request.getOtp());
    }

    @PostMapping("/withdraw/scan")
    public Map<String, Object> scanATM(@ModelAttribute(CUSTOMER_CONTEXT) Customer customer,
                                       @RequestBody UpdateWithDrawRequest request) {
        return atmService.requesterScanQRcode(customer, request.getWithDrawId(), request.getQrCodeString());
    }

    @PostMapping("/withdraw/create")
    public Map<String, Object> withdraw(@ModelAttribute(CUSTOMER_CONTEXT) Customer customer,
                                        @RequestBody WithDrawRequest request) {
        return atmService.withDrawRequest(customer, request);
    }

    @PostMapping({"/withdraw/reject", "/withdraw/approve"})
    public Map<String, Object> updateWithDraw(@ModelAttribute(CUSTOMER_CONTEXT) Customer customer,
                                              @RequestBody UpdateWithDrawRequest request) {
        return atmService.updateWithDrawRequest(customer, request);
    }

    @GetMapping("/withdraw/requester")
    public Map<String, Object> getWithDrawForRequester(@ModelAttribute(CUSTOMER_CONTEXT) Customer customer) {
        return atmService.requesterRetrieveWithDraws(customer);
    }

    @GetMapping("/withdraw/approver")
    public Map<String, Object> getWithDrawForApprover(@ModelAttribute(CUSTOMER_CONTEXT) Customer customer) {
        return atmService.approveRetrieveWithDraws(customer);
    }

//    @PostMapping("/withdraw/{id}")
//    public Map<String, Object> signATM(@RequestParam("atmExternalId") String atmExternalId) {
//        ATM atm = atmRepository.findByExternalId(atmExternalId);
//        if (atm != null ) {
//
//        }
//        return map();
//    }
}
