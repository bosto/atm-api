package com.bosto.atmapi.atm.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.bosto.atmapi.account.domain.Account;
import com.bosto.atmapi.account.service.AccountService;
import com.bosto.atmapi.atm.dao.ATMRepository;
import com.bosto.atmapi.atm.dao.WithDrawRepository;
import com.bosto.atmapi.atm.domain.ATM;
import com.bosto.atmapi.atm.domain.WithDraw;
import com.bosto.atmapi.atm.view.RetrieveWithDrawRequest;
import com.bosto.atmapi.atm.view.UpdateWithDrawRequest;
import com.bosto.atmapi.atm.view.WithDrawRequest;
import com.bosto.atmapi.exception.BaseException;

import com.bosto.atmapi.user.dao.UserRepository;
import com.bosto.atmapi.user.domain.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import static com.bosto.atmapi.common.Maps.*;
import static com.bosto.atmapi.atm.view.UpdateWithDrawRequest.Action.*;
import static com.bosto.atmapi.atm.domain.WithDraw.Status.*;

import static com.bosto.atmapi.security.JwtToken.*;
import static com.bosto.atmapi.common.RandomUtils.*;

@Service
@Slf4j
public class ATMService {
    @Autowired
    WithDrawRepository withDrawRepository;

    @Autowired
    ATMRepository atmRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AccountService accountService;

    private static final Map<String , String> atmMapOTP = new ConcurrentHashMap<>();


    public Map<String, Object> createATM(ATM atm) {
        String atmExternalId = atm();
        atm.setExternalId(atmExternalId);
        atmRepository.save(atm);
        return map("atm", atm);
    }

    public Map<String, Object> queryATM() {
      return map("atms", atmRepository.findAll());
    }

    public Map<String, Object> withDrawRequest(Customer customer, WithDrawRequest request) {
//        if (!accountService.checkBalance(accountNumber, request.getCurrency(), request.getAmount())) {
//            throw new BaseException("400","Account balance not enough to withdraw");
//        };
        WithDraw withDraw = new WithDraw();
        Customer requester = userRepository.findByUsername(customer.getUsername());
        Account account;
        if (requester.getOrganization() != null) {
            account = requester.getOrganization().getAccount();
        }  else {
            account = requester.getAccount();
        }
        withDraw.setRequester(requester);
        withDraw.setAmount(request.getAmount());
        withDraw.setCurrency(account.getCurrency());
        withDraw.setPurpose(request.getPurpose());
        withDraw.setStatus(WithDraw.Status.PENDING);
        long now = System.currentTimeMillis();
        withDraw.setLastModifiedDatetime(now);
        withDraw.setCreatedDatetime(now);
        withDraw.setAccountNumber(accountNumber());
        withDrawRepository.save(withDraw);
        return map("success", true);
    }

    public Map<String, Object> updateWithDrawRequest(Customer forCustomerTo, UpdateWithDrawRequest request) {
        Optional<WithDraw> withDrawOptional = withDrawRepository.findById(request.getWithDrawId());
        WithDraw withDraw;
        String withComment = request.getComment();
        if (!withDrawOptional.isPresent()) {
            throw new BaseException("400", "WithDraw record not found");
        } else {
            withDraw = withDrawOptional.get();
        }
        switch (request.getAction()) {
            case APPROVE:
                takeActionWith(APPROVED, forCustomerTo, withDraw, withComment);
                break;
            case REJECT:
                takeActionWith(REJECTED, forCustomerTo, withDraw, withComment);
                break;
//            case SCANATM:
//                requesterTakeActionWith(READY, forCustomerTo, withDraw, withComment);
//                break;
//            case AUTHPASS:
//                requesterTakeActionWith(TAKEN, forCustomerTo, withDraw, withComment);
//                break;
            default:

        }
        return map("success", true);
    }

    private Specification<WithDraw> requester(Customer user) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("requester"), user);
    }

    public Map<String, Object> requesterRetrieveWithDraws(Customer forCustomerTo) {
        return retrieveWithDraws(forCustomerTo, null, "REQUESTER");
    }

    public Map<String, Object> approveRetrieveWithDraws(Customer forCustomerTo) {
       return retrieveWithDraws(forCustomerTo, null, "ADMIN");
    }


    public Map<String, Object> retrieveWithDraws(Customer forCustomerTo, RetrieveWithDrawRequest request, String role) {
        Stream<WithDraw> withDrawStream;
        if (role.contains("ADMIN")) {
            withDrawStream = withDrawRepository.findAll().stream();
        } else {
            Specification<WithDraw> specification = requester(forCustomerTo);
            withDrawStream = withDrawRepository.findAll(specification).stream();
        }

        Map<String, Object> result = map("withDraws",
            withDrawStream.map(
              it -> {
                  Map<String, Object> item =
                      map(
                          pair("id", it.getId()),
                          pair("accountNumber", it.getAccountNumber()),
                          pair("amount", it.getAmount()),
                          pair("currency", it.getCurrency()),
                          pair("comment", it.getComment()),
                          pair("purpose", it.getPurpose()),
                          pair("lastModifiedDatetime", it.getLastModifiedDatetime()),
                          pair("createdDatetime", it.getCreatedDatetime()),
                          pair("status", it.getStatus()),
                          pair("requester", it.getRequester().getUsername()),
                          pair("requesterStaffId", it.getRequester().getReferenceNumber())
                      );
                  if (it.getLastOperator() != null) {
                      item.put("lastOperator", it.getLastOperator().getUsername());
                      item.put("lastOperatorStaffId", it.getLastOperator().getReferenceNumber());
                  }
                  return item;
              })
          );
        return result;
    }




  private void takeActionWith(WithDraw.Status status, Customer customer, WithDraw withDraw, String comment) {
        if (!withDraw.getStatus().equals(PENDING)) {
          throw new BaseException("400", "WithDraw is not pending status, unable to approve or reject");
        }
        withDraw.setLastModifiedDatetime(System.currentTimeMillis());
        withDraw.setLastOperator(customer);
        withDraw.setStatus(status);
        withDraw.setComment(comment);
        withDrawRepository.save(withDraw);
    }


    public String generatorQRcodeString(String atmId) {
        if (!atmRepository.existsATMByExternalId(atmId)) {
            throw new BaseException("400","ATM not found");
        }
        return createToken(atmId, "ATM", 30);
    }

    public Map<String, Object> requesterScanQRcode(Customer customer, Long withDrawId, String qrCodeString) {
        Optional<WithDraw> withDrawOptional = withDrawRepository.findById(withDrawId);
        if (!withDrawOptional.isPresent()) {
            throw new BaseException("400", "WithDraw record not found");
        }
        WithDraw withDraw = withDrawOptional.get();
        if (!withDraw.getStatus().equals(APPROVED)) {
            throw new BaseException("400", "WithDraw not approved yet, please approve first");
        }

        if (!withDraw.getRequester().equals(customer)) {
            throw new BaseException("400", "WithDraw not approved yet, please approve first");
        }
        String atmId = getSubject(qrCodeString);

        String otp = otp();

        JWTCreator.Builder builder = JWT.create();
        atmMapOTP.put(atmId, createToken(otp ,builder.withClaim("withDrawId", withDrawId), 30));
        return map("otp", otp);
    }

    public Map<String, Object> requesterVerifyOTP(String atmId, String otp) {

        log.info("requesterVerifyOTP-----------------1");
        String otpToken = atmMapOTP.getOrDefault(atmId, "");
        if (StringUtils.isEmpty(otpToken)) {
            throw new BaseException("400", "ATM didn't ready to withdraw");
        }
        log.info("requesterVerifyOTP-----------------2");
        DecodedJWT tokenBody = getTokenBody(otpToken);
        if (!tokenBody.getSubject().equals(otp)) {
            throw new BaseException("400", "OTP is wrong");
        }
        log.info("requesterVerifyOTP-----------------3");
        Long withDrawId = tokenBody.getClaim("withDrawId").asLong();
        if (!withDrawRepository.existsById(withDrawId)) {
            throw new BaseException("400", "Withdraw record not found");
        }
        log.info("requesterVerifyOTP-----------------4");
        WithDraw withDraw = withDrawRepository.findById(withDrawId).get();
        if (!withDraw.getStatus().equals(APPROVED)) {
            throw new BaseException("400", "WithDraw not approved yet, please approve first");
        }
        log.info("requesterVerifyOTP-----------------5");
        withDraw.setStatus(TAKEN);
        accountService.withDraw(withDraw.getRequester(), withDraw.getAmount());
        withDrawRepository.save(withDraw);
        return map("withDraw", map(
          pair("accountNumber", withDraw.getAccountNumber()),
          pair("amount", withDraw.getAmount()),
          pair("currency", withDraw.getCurrency()),
          pair("purpose", withDraw.getPurpose()),
          pair("lastModifiedDatetime", withDraw.getLastModifiedDatetime())
        ));
    }





}
