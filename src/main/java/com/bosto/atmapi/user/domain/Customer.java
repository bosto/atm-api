package com.bosto.atmapi.user.domain;

import com.bosto.atmapi.account.domain.Account;
import lombok.Data;

import javax.persistence.*;

/**
 * Created by bosto on 2018/12/21
 **/

@Entity
@Data
public class Customer {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String username;

  private String referenceNumber;

  private String password;

  private String expire;

  private String role;

  @ManyToOne(cascade = CascadeType.REFRESH)
  private Account account;

  @ManyToOne(cascade = CascadeType.REFRESH)
  private Customer organization;

  public enum Role {
      APPROVER,
      REQUESTER,
      ORG
  }


}
