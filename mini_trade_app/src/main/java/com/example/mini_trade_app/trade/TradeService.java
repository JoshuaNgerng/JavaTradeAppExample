package com.example.mini_trade_app.trade;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

// import com.example.mini_trade_app.order.OrderRepository;
import com.example.mini_trade_app.security.AuthUserInfo;
import com.example.mini_trade_app.shared.dto.Listing;
import com.example.mini_trade_app.shared.exception.BusinessException;
import com.example.mini_trade_app.trade.dto.TradeDetail;
import com.example.mini_trade_app.trade.dto.TradeLisitingParams;
import com.example.mini_trade_app.trade.dto.TradeLisitingResponse;
import com.example.mini_trade_app.trade.entity.Trade;
import com.example.mini_trade_app.trade.mapper.TradeMapper;
import com.example.mini_trade_app.user.entity.RoleType;
import com.example.mini_trade_app.user.entity.User;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TradeService {
    private final TradeRepository tradeRepo;
    // private final OrderRepository orderRepo;
    private final TradeMapper     tradeMapper;

    public TradeLisitingResponse getTradesListing(
        TradeLisitingParams params, AuthUserInfo authUser
    ) {
        User user = authUser.user();
        Page<Trade> data = search(user.getId(), authUser.role(), params);
        return new TradeLisitingResponse(
            Listing.from(data),
            data.getContent().stream().map(
                t -> tradeMapper.toData(t)
            ).toList()
        );
    }

    public TradeDetail getTradeDetail(
        Long tradeId, AuthUserInfo authUser
    ) {
        User user = authUser.user();
        Optional<TradeDetail> result = Optional.empty();
        if (authUser.role() == RoleType.ROLE_BUYER) {
            result = tradeRepo.findBuyerTradeDetails(tradeId, user.getId());
        } else {
            result = tradeRepo.findSellerTradeDetails(tradeId, user.getId());
        }
        return result.orElseThrow(
            () -> new BusinessException(TradeErrorCode.TRADE_NOT_FOUND)
        );
    }

    // private Trade getValidTrade(
    //     long tradeId, long userId, RoleType role
    // ) {
    //     Specification<Trade> spec = Specification
    //         .where(TradeSpecifications.hasUserId(userId, role))
    //         .and(TradeSpecifications.hasTradeId(tradeId));
    //     return tradeRepo.findOne(spec).orElseThrow(
    //         () -> new BusinessException(TradeErrorCode.TRADE_NOT_FOUND)
    //     );
    // }

    private Page<Trade> search(
        Long userId,
        RoleType role,
        TradeLisitingParams params
    ) {
        Specification<Trade> spec = Specification
            .where(TradeSpecifications.hasUserId(userId, role))
            .and(TradeSpecifications.quantityGte(params.lowerQuantityLimit().orElse(null)))
            .and(TradeSpecifications.quantityLte(params.upperQuantityLimit().orElse(null)))
            .and(TradeSpecifications.priceGte(params.lowerPricePerUnit().orElse(null)))
            .and(TradeSpecifications.priceLte(params.upperPricePerUnit().orElse(null)))
            .and(TradeSpecifications.createdAfter(params.afterDate().orElse(null)))
            .and(TradeSpecifications.createdBefore(params.beforeDate().orElse(null)));

        return tradeRepo.findAll(
            spec, params.pageParams().getPageable(Sort.by("createdAt").descending())
        );
    }
}
