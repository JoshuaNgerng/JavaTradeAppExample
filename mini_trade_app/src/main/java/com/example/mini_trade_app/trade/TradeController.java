package com.example.mini_trade_app.trade;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mini_trade_app.security.AuthService;
import com.example.mini_trade_app.security.AuthUserInfo;
import com.example.mini_trade_app.security.CustomUserPrincipal;
import com.example.mini_trade_app.trade.dto.TradeDetail;
import com.example.mini_trade_app.trade.dto.TradeLisitingParams;
import com.example.mini_trade_app.trade.dto.TradeLisitingResponse;

@RestController
@RequestMapping("/trades")
public class TradeController {
    private final TradeService trade;
    private final AuthService  auth;

    public TradeController(
        TradeService trade,
        AuthService  auth
    ) {
        this.trade = trade;
        this.auth = auth;
    }

    @GetMapping("/")
    public TradeLisitingResponse getTradesListing(
        @AuthenticationPrincipal CustomUserPrincipal principal,
        @RequestBody TradeLisitingParams param
    ) {
        AuthUserInfo user = auth.verifyUserInfo(principal);
        return trade.getTradesListing(param, user);
    }

    @GetMapping("/{tradeId}")
    public TradeDetail getTradeDetails(
        @AuthenticationPrincipal CustomUserPrincipal principal,
        @PathVariable Long tradeId
    ) {
        AuthUserInfo user = auth.verifyUserInfo(principal);
        return trade.getTradeDetail(tradeId, user);
    }
}
