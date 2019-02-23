package com.bosto.atmapi.atm.view;

import com.bosto.atmapi.atm.domain.WithDraw;
import lombok.Data;

@Data
public class RetrieveWithDrawRequest {
    WithDraw.Status status;
    int page;
    int size;
}
